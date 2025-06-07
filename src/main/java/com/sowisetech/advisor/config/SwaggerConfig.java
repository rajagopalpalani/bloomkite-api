package com.sowisetech.advisor.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;


import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@SuppressWarnings("deprecation")
	private ApiInfo getApiInfo() {
		return new ApiInfo("Bloomkite", "", "", "", "", "", "");
	}

	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.sowisetech")).paths(PathSelectors.any()).build()
				.apiInfo(getApiInfo()).securitySchemes(Arrays.asList(apiKey()));
	}

	private ApiKey apiKey() {
		return new ApiKey("Bearer", "Authorization", "header");
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	// To view swagger documentation visit
	// "http://localhost:8080/advisor/swagger-ui.html#/"

}
