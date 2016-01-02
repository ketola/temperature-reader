package ketola.temperature.rest;

import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

public class Configuration {
	
	@Bean
	public ServletRegistrationBean dispatcherServlet() {
	    ServletRegistrationBean registration = new ServletRegistrationBean(
	            new DispatcherServlet(), "/");
	    
	    // enable sse
	    registration.setAsyncSupported(true);
	    return registration;
	}

}
