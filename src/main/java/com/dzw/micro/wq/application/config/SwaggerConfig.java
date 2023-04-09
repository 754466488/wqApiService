package com.dzw.micro.wq.application.config;

import com.dzw.micro.wq.Application;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SpringFox-swagger2生成api文档,减少团队之间的沟通成本
 * 先利用 SpringFox 库生成实时文档 http://host:port/swagger-ui.html
 *
 * @author lyb
 * @date 2020/11/26
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	Docket createMiniApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("Mobile移动端"))
				.enable(Application.isDev() || Application.isProd())
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				.paths(PathSelectors.ant("/api/**"))
				.build()
				.groupName("miniApi");
	}

	@Bean
	Docket createAdminApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo("Web管理后台"))
				.enable(Application.isDev() || Application.isProd())
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				.paths(PathSelectors.ant("/admin/**"))
				.build()
				.groupName("adminApi");
	}

	private ApiInfo apiInfo(String name) {
		return new ApiInfoBuilder()
				.title(Application.NAME + name + "API文档")
				.version("1.0")
				.build();
	}
}
