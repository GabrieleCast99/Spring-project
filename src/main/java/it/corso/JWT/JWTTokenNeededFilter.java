package it.corso.JWT;

import java.io.IOException;
import java.security.Key;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.corso.service.Blacklist;
import jakarta.annotation.Resource;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

//classe che implementa l interfaccia
@Provider
@JWTTokenNeeded
public class JWTTokenNeededFilter implements ContainerRequestFilter {
	
	@Context
	private ResourceInfo resourceInfo;
	
	@Autowired
	private Blacklist blacklist;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
	Secured annotationRole =	resourceInfo.getResourceMethod().getAnnotation(Secured.class);
		
		if (annotationRole == null) {
			annotationRole = resourceInfo.getResourceClass().getAnnotation(Secured.class);
			//getResourceClass
			
		}
		String authorizazionHeader	= requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authorizazionHeader == null || !authorizazionHeader.startsWith("Bearer ")) {
			throw new NotAuthorizedException("Authorization header nmust be provided");
		}
		// estrae l token dalla http authorization header
		 String token = authorizazionHeader.substring("Bearer".length()).trim();
		 
		 if(blacklist.isTokenRevoked(token)) {
			 throw new NotAuthorizedException("BlacklistToken");
		 }
		 
		 try {
			
			 //si assicura che i byte siano gli stessi, il token viene generato su questo, la firma, 
			byte[] scretKey = "auevdsehkgjdvfeuskfbukfbouse12345667788453233345445453453242837478236".getBytes();
				
			Key key = Keys.hmacShaKeyFor(scretKey);
			 
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			Claims body = claims.getBody();
			List<String> rolesToken = body.get("ruoli", List.class);
			Boolean  hasRole = false;
			for(String role : rolesToken) {
				if (role.equals(annotationRole.role())) {
					hasRole=true;
				}
			}
			if(!hasRole) {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}
			
			
		} catch (Exception e) {

			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			
		}
	}

}
