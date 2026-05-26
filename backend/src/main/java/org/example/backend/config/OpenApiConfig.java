//package org.example.backend.config;
//
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Configures the OpenAPI (Swagger) documentation for the E-commerce API.
// *
// * Registers a global "Bearer Auth" security scheme so that the Swagger UI
// * shows an "Authorize" button where users can paste their JWT token to
// * test protected endpoints.
// */
//@Configuration
//public class OpenApiConfig {
//
//    @Bean
//    public OpenAPI ecommerceOpenAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("E-Commerce API")
//                        .version("1.0")
//                        .description("E-commerce Backend API Documentation"))
//                .addSecurityItem(new SecurityRequirement().addList("Bearer Auth"))
//                .components(new Components()
//                        .addSecuritySchemes("Bearer Auth",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")));
//    }
//}
