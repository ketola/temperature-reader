package ketola.temperature.rest;

import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@org.springframework.context.annotation.Configuration
public class Configuration {

	@Bean
	public ServletRegistrationBean dispatcherRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet(), "/");

		// enable sse
		registration.setAsyncSupported(true);
		return registration;
	}

	@Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
	public DispatcherServlet dispatcherServlet() {
		return new DispatcherServlet();
	}

}
