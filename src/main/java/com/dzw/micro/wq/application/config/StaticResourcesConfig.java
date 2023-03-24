package com.dzw.micro.wq.application.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 静态资源配置
 *
 * @author lyb
 * @date 2020/11/26
 */
@Configuration
public class StaticResourcesConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods("GET", "POST")
				.allowCredentials(false);   //不允许Cookie跨域
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//管理后台静态资源相对路径
		registry.addResourceHandler(this.genStaticPathPatterns("/admin")).addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/admin/");
		//文档静态资源相对路径
		registry.addResourceHandler(this.genStaticPathPatterns("/docs")).addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/docs/");
		//h5静态资源相对路径
		registry.addResourceHandler(this.genStaticPathPatterns("/h5")).addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/h5/");
		super.addResourceHandlers(registry);
	}

	private String[] genStaticPathPatterns(String prefix) {
		List<String> staticPathPatternsList = Lists.newArrayList();
		staticPathPatternsList.add(prefix + "/**/*.html");
		staticPathPatternsList.add(prefix + "/**/*.js");
		staticPathPatternsList.add(prefix + "/**/*.css");
		staticPathPatternsList.add(prefix + "/**/*.jpg");
		staticPathPatternsList.add(prefix + "/**/*.jpeg");
		staticPathPatternsList.add(prefix + "/**/*.gif");
		staticPathPatternsList.add(prefix + "/**/*.png");
		staticPathPatternsList.add(prefix + "/**/*.ttf");
		staticPathPatternsList.add(prefix + "/**/*.woff");
		staticPathPatternsList.add(prefix + "/**/*.svg");
		staticPathPatternsList.add(prefix + "/**/*.eot");
		return staticPathPatternsList.toArray(new String[staticPathPatternsList.size()]);
	}
}
