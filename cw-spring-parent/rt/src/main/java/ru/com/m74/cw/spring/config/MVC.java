package ru.com.m74.cw.spring.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author mixam
 * @since 10.05.17 10:52
 */
@Configuration
@EnableWebMvc
@PropertySource("file:${db.properties}")
//@PropertySource("classpath:application.properties")
//@PropertySource(value = "file:${user.dir}/application.properties", ignoreResourceNotFound = true)
//@PropertySource(value = "file:${application.properties}", ignoreResourceNotFound = true)
public class MVC extends WebMvcConfigurerAdapter {
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // не сериализировать если значение поля равно null
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // обшепринятый в проекте формат даты
        mapper.setDateFormat(new SimpleDateFormat(TIMESTAMP_FORMAT));
        // Не падать при десериализации если readonly fields (если есть только геттер, а сеттера нет)
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter.getClass().isAssignableFrom(MappingJackson2HttpMessageConverter.class)) {
                MappingJackson2HttpMessageConverter jackson2HttpMessageConverter =
                        (MappingJackson2HttpMessageConverter) converter;
                jackson2HttpMessageConverter.setObjectMapper(objectMapper());
            }
        }
        super.extendMessageConverters(converters);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/");
        super.addResourceHandlers(registry);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize((long) 10 * 1024 * 1024); // max 10 Mb
        return resolver;
    }
}
