package com.store.e_commerce_app.service;

import com.store.e_commerce_app.entities.Product;
import com.store.e_commerce_app.entities.RecentView;
import com.store.e_commerce_app.repositories.ProductRepository;
import com.store.e_commerce_app.repositories.RecentViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private RecentViewRepository recentViewRepository;

    @Autowired
    private ProductRepository productRepository;

    // Map itemId -> map of co-occurring itemId -> count
    private Map<Long, Map<Long, Integer>> coOccurrence = new HashMap<>();

    // popularity: itemId -> number of users who viewed
    private Map<Long, Integer> itemPopularity = new HashMap<>();

    @PostConstruct
    public void init() {
        rebuildModel();
    }

    // Build co-occurrence matrix from recent views (users -> items)
    public synchronized void rebuildModel() {
        List<RecentView> views = recentViewRepository.findAll();

        // group items by user
        Map<Long, Set<Long>> itemsByUser = new HashMap<>();
        for (RecentView v : views) {
            if (v.getUserDlts() == null || v.getProduct() == null) continue;
            long userId = v.getUserDlts().getUserId();
            long itemId = v.getProduct().getProductId();
            itemsByUser.computeIfAbsent(userId, k -> new HashSet<>()).add(itemId);
        }

        Map<Long, Map<Long, Integer>> co = new HashMap<>();
        Map<Long, Integer> popularity = new HashMap<>();

        for (Set<Long> items : itemsByUser.values()) {
            // update popularity (each user contributes 1 to each item)
            for (Long it : items) {
                popularity.merge(it, 1, Integer::sum);
            }
            List<Long> list = new ArrayList<>(items);
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (i == j) continue;
                    long a = list.get(i);
                    long b = list.get(j);
                    co.computeIfAbsent(a, k -> new HashMap<>())
                      .merge(b, 1, Integer::sum);
                }
            }
        }
        this.coOccurrence = co;
        this.itemPopularity = popularity;
    }

    // compute cosine similarity between two item vectors (co-occurrence rows)
    private double cosineSim(Map<Long, Integer> a, Map<Long, Integer> b) {
        if (a == null || b == null) return 0.0;
        double dot = 0.0;
        for (Map.Entry<Long, Integer> e : a.entrySet()) {
            Integer bv = b.get(e.getKey());
            if (bv != null) dot += e.getValue() * bv;
        }
        double na = 0.0, nb = 0.0;
        for (Integer v : a.values()) na += v * v;
        for (Integer v : b.values()) nb += v * v;
        if (na == 0 || nb == 0) return 0.0;
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    public List<Product> findSimilarProducts(Long productId, int limit) {
        try {
            if (!coOccurrence.containsKey(productId)) {
                // try rebuilding and retry
                rebuildModel();
                if (!coOccurrence.containsKey(productId)) return Collections.emptyList();
            }
            Map<Long, Integer> vec = coOccurrence.get(productId);
            Map<Long, Double> scores = new HashMap<>();
            for (Long other : coOccurrence.keySet()) {
                if (other.equals(productId)) continue;
                double sim = cosineSim(vec, coOccurrence.get(other));
                // small category/price boosts could be added here by fetching product details
                scores.put(other, sim);
            }
            return scores.entrySet().stream()
                    .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                    .limit(limit)
                    .map(e -> productRepository.findByProductId(e.getKey()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Hybrid recommendation for a user: uses recent views (recency-weighted) + popularity
    public List<Product> recommendForUser(Long userId, int limit) {
        try {
            // get recent views for user (most recent first)
            List<RecentView> recent = recentViewRepository.findByUserDltsUserIdOrderByViewedAtDesc(userId);
            if (recent == null || recent.isEmpty()) {
                // fallback: return top popular items
                return topPopular(limit);
            }

            // collect up to K unique recent itemIds preserving order
            int K = 5;
            LinkedHashSet<Long> recentItems = new LinkedHashSet<>();
            for (RecentView rv : recent) {
                if (rv.getProduct() != null) recentItems.add(rv.getProduct().getProductId());
                if (recentItems.size() >= K) break;
            }
            if (recentItems.isEmpty()) return topPopular(limit);

            // build recency weights: more recent -> higher weight
            List<Long> recentList = new ArrayList<>(recentItems);
            double[] weights = new double[recentList.size()];
            // simple weights: 1.0, 0.8, 0.6, ...
            for (int i = 0; i < recentList.size(); i++) {
                weights[i] = 1.0 - (i * 0.2);
                if (weights[i] < 0.1) weights[i] = 0.1;
            }

            // compute candidate scores
            Map<Long, Double> scores = new HashMap<>();
            // normalization factors
            double maxPop = itemPopularity.values().stream().mapToDouble(Integer::doubleValue).max().orElse(1.0);

            for (Long candidate : coOccurrence.keySet()) {
                if (recentItems.contains(candidate)) continue; // skip already viewed
                double score = 0.0;
                for (int i = 0; i < recentList.size(); i++) {
                    Long recentId = recentList.get(i);
                    Map<Long, Integer> candidateVec = coOccurrence.get(candidate);
                    Map<Long, Integer> recentVec = coOccurrence.get(recentId);
                    double sim = cosineSim(candidateVec, recentVec);
                    score += sim * weights[i];
                }
                // popularity normalized
                double pop = itemPopularity.getOrDefault(candidate, 0) / maxPop;
                // combine: 70% similarity, 30% popularity
                double finalScore = (score * 0.7) + (pop * 0.3);
                scores.put(candidate, finalScore);
            }

            // if scores empty (e.g., no co-occurrence), fallback to popular
            if (scores.isEmpty()) return topPopular(limit);

            return scores.entrySet().stream()
                    .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                    .limit(limit)
                    .map(e -> productRepository.findByProductId(e.getKey()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<Product> topPopular(int limit) {
        // return top items by itemPopularity
        return itemPopularity.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(e -> productRepository.findByProductId(e.getKey()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
