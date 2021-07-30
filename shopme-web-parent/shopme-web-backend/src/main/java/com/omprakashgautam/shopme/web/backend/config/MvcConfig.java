package com.omprakashgautam.shopme.web.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author omprakash gautam
 * Created on 14-Jul-21 at 9:13 PM.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //Expose user photos path
        String userDir = "user-photos";
        Path userPhotosDir = Paths.get(userDir);
        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + userDir + "/**")
                .addResourceLocations("file:/" + userPhotosPath + "/");

        //Expose category image path
        String categoryDir = "category-images";
        Path categoryImageDir = Paths.get(categoryDir);
        String categoryImagePath = categoryImageDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + categoryDir + "/**")
                .addResourceLocations("file:/" + categoryImagePath + "/");

        //Expose brand logo path
        String brandLogo = "brand-logos";
        Path brandLogoDir = Paths.get(brandLogo);
        String brandLogoPath = brandLogoDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + brandLogo + "/**")
                .addResourceLocations("file:/" + brandLogoPath + "/");
    }
}
