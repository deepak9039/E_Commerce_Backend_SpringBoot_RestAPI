package com.store.e_commerce_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Full absolute path of the upload directory
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        String location = "file:" + uploadPath + "/";

        registry.addResourceHandler("/image/category/**") // URL to access
                .addResourceLocations(location)            // Physical folder path
                .setCachePeriod(3600);
    }
}


//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Value("${app.upload.dir:static/image/category}")
//    private String uploadDir;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
//        String externalPath = uploadPath.toUri().toString();
//        // Map /uploads/** to the filesystem upload directory
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations(externalPath)
//                .setCachePeriod(3600);
//    }
//}

