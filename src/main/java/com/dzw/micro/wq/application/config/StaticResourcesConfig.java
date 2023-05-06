package com.dzw.micro.wq.application.config;

import com.dzw.micro.wq.application.log.Log;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.util.List;

/**
 * 静态资源配置
 *
 * @author lyb
 * @date 2020/11/26
 */
@Configuration
public class StaticResourcesConfig extends WebMvcConfigurerAdapter {
	//从配置文件中加载静态资源相对路径
	@Value(value = "${file.resourceLocation}")
	private String resourceLocation;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods("GET", "POST")
				.allowCredentials(false);   //不允许Cookie跨域
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//放在工作目录
		File parentFile = new File(System.getProperty("user.dir"));
		String fileServicePath = resourceLocation;
		if (parentFile.exists()) {
			File file = new File(parentFile.getPath() + resourceLocation);
			if (!file.exists()) {
				file.mkdir();
				Log.info("创建文件服务文件夹:" + fileServicePath + "成功");
			}
			fileServicePath = file.getAbsolutePath() + File.separator;
		}
		//管理后台静态资源相对路径
		registry.addResourceHandler(this.genStaticPathPatterns("/admin")).addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/admin/");
		//文档静态资源相对路径
		registry.addResourceHandler(this.genStaticPathPatterns("/docs")).addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/docs/");
		//h5静态资源相对路径
		registry.addResourceHandler(this.genStaticPathPatterns("/h5")).addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/h5/");
		registry.addResourceHandler("/image/**").addResourceLocations(ResourceUtils.FILE_URL_PREFIX + fileServicePath);
		registry.addResourceHandler("/video/**").addResourceLocations(ResourceUtils.FILE_URL_PREFIX + fileServicePath);
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
