package it.corso;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import it.corso.JWT.JWTTokenNeeded;
import it.corso.JWT.JWTTokenNeededFilter;
import jakarta.ws.rs.ApplicationPath;


@Component
@ApplicationPath("/api")
public class jerseyConfig extends ResourceConfig{

	
	public jerseyConfig() {
		register(JWTTokenNeededFilter.class);
		register(CorsFilter.class);
		packages("it.corso.controller");
	}
}
