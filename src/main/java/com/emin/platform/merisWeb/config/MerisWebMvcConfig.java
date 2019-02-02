package com.emin.platform.merisWeb.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.emin.platform.merisWeb.filter.UserFilter;
import com.emin.platform.merisWeb.interfaces.PersonApiFeign;

import freemarker.template.TemplateExceptionHandler;

@Configurable
@EnableWebMvc
public class MerisWebMvcConfig extends WebMvcConfigurerAdapter{
	@Autowired
	PersonApiFeign personApiFeign;
	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		
        configurer.favorPathExtension(false);
    }
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/js/**")
	            .addResourceLocations("classpath:/static/js/");
	    registry.addResourceHandler("/css/**")
	    		.addResourceLocations("classpath:/static/css/");
	    registry.addResourceHandler("/img/**")
				.addResourceLocations("classpath:/static/img/");
	    registry.addResourceHandler("/fonts/**")
				.addResourceLocations("classpath:/static/fonts/");
	    
	    
	}
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new UserFilter(personApiFeign)).addPathPatterns("/**").excludePathPatterns("/login","/getValidImg","/loginIn");
	}
	
	@Bean
	public CommandLineRunner customFreemarker(FreeMarkerViewResolver resolver) {
		return new CommandLineRunner() {
			@Autowired
			private freemarker.template.Configuration configuration;

			@Override
			public void run(String... strings) throws Exception {
				configuration.setLogTemplateExceptions(false);
				configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
				configuration.setNumberFormat("#");
				resolver.setViewClass(CustomFreeMarkerView.class);
			}
		};
	}
	
	/*@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(jacksonHttpMessageConverter());
		converters.add(stringHttpMessageConverter());	
	}
	
	@Bean
    public StringHttpMessageConverter stringHttpMessageConverter(){
    	StringHttpMessageConverter converter = new StringHttpMessageConverter();
    	
    	List<MediaType> supportedMediaTypes = new ArrayList<>();
    	supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
    	supportedMediaTypes.add(MediaType.APPLICATION_XML);
    	supportedMediaTypes.add(MediaType.TEXT_XML);
    	supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
    	supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
    	converter.setSupportedMediaTypes(supportedMediaTypes);
    	return converter;
    }
	
	@Bean
    public MappingJackson2HttpMessageConverter jacksonHttpMessageConverter(){
    	MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    	
    	List<MediaType> supportedMediaTypes = new ArrayList<>();
    	supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
    	supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        supportedMediaTypes.add(MediaType.ALL);
    	converter.setSupportedMediaTypes(supportedMediaTypes);
    	return converter;
    }*/
}
