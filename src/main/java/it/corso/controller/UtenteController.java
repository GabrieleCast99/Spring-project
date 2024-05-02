package it.corso.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import io.jsonwebtoken.*;
import java.security.Key;
import java.time.LocalDateTime;

import io.jsonwebtoken.security.Keys;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteDtoAggiornamento;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteLoginResponseDto;
import it.corso.dto.UtenteShowDto;
import it.corso.model.Ruolo;
import it.corso.model.Utente;
import it.corso.service.Blacklist;
import it.corso.service.UtenteService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/utente")
public class UtenteController {
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private Blacklist blacklist;
	
	@POST
	@Path("/reg")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creaUtente(@Valid @RequestBody UtenteDto uDto) {


		if(!Pattern.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}", uDto.getPassword())) {
			
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		if (utenteService.existsUtenteByEmail(uDto.getEmail())) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		utenteService.utenteRegistrazione(uDto);
					
		return Response.status(Response.Status.OK).build();
	}
	
	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUtente(@QueryParam(value = "email") String email) {
		
		try {
			UtenteShowDto UtenteShowDto = utenteService.getUserByEmail(email);
			
			return Response.status(Response.Status.OK).entity(UtenteShowDto).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}
	
	
	@GET
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		
		List<UtenteShowDto> utenteDto = utenteService.getAllUser();		
		
		return Response.status(Response.Status.OK).entity(utenteDto).build();

		
	}
	
	
	
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginUtente(@RequestBody UtenteLoginRequestDto utenteLoginRequestDto) {
		
		try {
			
			if(utenteService.loginUtente(utenteLoginRequestDto)) {
				return Response.ok(creaToken(utenteLoginRequestDto.getEmail())).build();
			}
			return Response.status(Response.Status.BAD_REQUEST).build();


		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
			
	}
	
	
	private UtenteLoginResponseDto creaToken(String email) {
		
		byte[] scretKey = "auevdsehkgjdvfeuskfbukfbouse12345667788453233345445453453242837478236".getBytes();
		
			
		Key key = Keys.hmacShaKeyFor(scretKey);
		
		Utente infoUtente = utenteService.findByEmail(email);
		
		Map<String,Object> map = new HashMap<>();
		
		map.put("email", email);
		map.put("nome", infoUtente.getNome());
		map.put("cognome", infoUtente.getCognome());
		List<String> ruoli = new ArrayList<>();
		for(Ruolo ruolo : infoUtente.getRuoli()) {
			//metodo name definito nella classe ename per restiruire il valore come stringa
			ruoli.add(ruolo.getTipologia().name());
		}
		
		map.put("ruoli", ruoli);
		
		//data di creazione e ttl espirazione
		Date creationDate = new Date();
		Date end = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(10L));
		
		//creazione del token firmato con la chiave segreta creata sopra
		String jwtToken = Jwts.builder()
			.setClaims(map)
			.setIssuer("http://localhost8080")
			.setIssuedAt(creationDate)
			.setExpiration(end)
			.signWith(key)
			.compact();
		
		UtenteLoginResponseDto token = new UtenteLoginResponseDto();
		token.setToken(jwtToken);
		token.setTokenCreationTime(creationDate);
		token.setTtl(end);
		
		return token;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@DELETE
	 @Path("delete/{email}")
	 public Response deleteUser(@PathParam("email") String email) {
	  
	  try {
	   utenteService.deleteUser(email);
	   
	   return Response.status(Response.Status.OK).build();
	  } catch (Exception e) {

	   return Response.status(Response.Status.BAD_REQUEST).build();
	  }
	  
	 }

	
	
	
	@PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
 public Response updateUser(@RequestBody UtenteDtoAggiornamento utente) {

  
  try {
   
   utenteService.updateUserData(utente);
   
   return Response.status(Response.Status.OK).build();
   
  } catch (Exception e) {
   
   return Response.status(Response.Status.BAD_REQUEST).build();
  }
  
 }
	
	
	@GET
	@Path("/logout")
	public Response logoututente(ContainerRequestContext containerRequestContext) {
		try {
			String authorizazionHeader	= containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
			 String token = authorizazionHeader.substring("Bearer".length()).trim();

			 blacklist.invalidateToken(token);
			 
			   return Response.status(Response.Status.OK).build();

		} catch (Exception e) {
			   return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}
