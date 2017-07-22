package ru.com.m74.cw.spring;

import org.apache.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

/**
 * @author mixam
 * @since 10.05.17 11:23
 */
public class Starter implements WebApplicationInitializer {


    private static final Logger logger = Logger.getLogger(Starter.class);

    public void onStartup(ServletContext servletContext) throws ServletException {

        logger.info("Starting spring ...");
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("application.properties");
            if (properties.getProperty("spring.configLocation") == null)
                throw new ServletException("Property not found: spring.configLocation");

            dump(properties);

            FilterRegistration.Dynamic springSecurityFilterChain = servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy());
            springSecurityFilterChain.addMappingForUrlPatterns(null, false, "/*");
            springSecurityFilterChain.setAsyncSupported(true);

            AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
            context.setConfigLocations("ru.com.m74.cw.spring", properties.getProperty("spring.configLocation"));

            ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                    "dispatcher",
                    new DispatcherServlet(context));
            dispatcher.setLoadOnStartup(1);
            dispatcher.addMapping("/");

        } catch (IOException e) {
            throw new ServletException("application.properties not found!");
        }

    }


    private static void dump(Properties properties) {
        StringBuilder result = new StringBuilder();
        result.append("\n\nApplication properties\n\n");

        Set set = properties.keySet();
        String[] arr = (String[]) set.toArray(new String[set.size()]);
        Arrays.sort(arr);

        for (String name : arr) {
            String value = (String) properties.get(name);
            result.append(name).append(" = ").append(value).append("\n");
        }
        logger.info(result);
    }
}
