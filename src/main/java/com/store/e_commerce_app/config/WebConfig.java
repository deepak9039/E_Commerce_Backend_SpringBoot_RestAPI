package com.store.e_commerce_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // read both properties (fallbacks point to the locations you use when saving files)
    @Value("${app.upload.dir:src/main/resources/static/category_img}")
    private String categoryUploadDir;

    @Value("${app.upload.product:src/main/resources/static/product_img}")
    private String productUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // map category folder
        Path catPath = Paths.get(categoryUploadDir).toAbsolutePath().normalize();
        String catLocation = "file:" + catPath.toString() + "/";
        registry.addResourceHandler("/image/category/**")
                .addResourceLocations(catLocation)
                .setCachePeriod(3600);

        // map product folder
        Path prodPath = Paths.get(productUploadDir).toAbsolutePath().normalize();
        String prodLocation = "file:" + prodPath.toString() + "/";
        registry.addResourceHandler("/image/product/**")
                .addResourceLocations(prodLocation)
                .setCachePeriod(3600);
    }
}
