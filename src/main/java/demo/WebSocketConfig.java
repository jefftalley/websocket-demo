package demo;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import com.fasterxml.jackson.datatype.joda.ser.JacksonJodaFormat;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/requests").withSockJS();
	}
	
    @Bean
    public JodaModule jacksonJodaModule() {
        JodaModule module = new JodaModule();
        DateTimeFormatterFactory formatterFactory = new DateTimeFormatterFactory();
        formatterFactory.setIso(ISO.DATE_TIME);
        module.addSerializer(DateTime.class, new DateTimeSerializer(
            new JacksonJodaFormat(formatterFactory.createDateTimeFormatter().withZoneUTC())));
        return module;
    }
    
    
//	@Bean
//	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
//	    ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
//	    registration.addUrlMappings("/service/*");
//	    return registration;
//	}

}
