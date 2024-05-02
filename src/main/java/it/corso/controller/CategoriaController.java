package it.corso.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import it.corso.dto.CategoriaDto;
import it.corso.dto.UtenteDto;
import it.corso.model.Categoria;
import it.corso.model.NomeCategoria;
import it.corso.service.CategoriaService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/categoria")
public class CategoriaController {
	
	@Autowired
	private CategoriaService categoriaService;
	
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creaCategoria(@Valid @RequestBody CategoriaDto categoriaDto) {
		try {
			categoriaService.categoriaCreazione(categoriaDto);
			
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		
		
		
	}
	
	
	@GET
	@Path("/getId/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findCategoryById(@PathParam("id") int id) {
		try {
			
			if (id>=0) {
				CategoriaDto categoriaDto = categoriaService.getCategoriaDtoById(id);
				
				return Response.status(Response.Status.OK).entity(categoriaDto).build();
			}
			
			return Response.status(Response.Status.NOT_FOUND).build();



		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}		
	}
	
	

	@GET
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		
		
		try {
			List<CategoriaDto> categoriaDto = categoriaService.getAllCategories();
			
			return Response.status(Response.Status.OK).entity(categoriaDto).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		

	}
	
	@GET
	@Path("/getAllCategories")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCategories(@QueryParam("filter")String filter) {
		
		
		try {
			List<CategoriaDto> categoriaDto = categoriaService.getAllCategoriesFiltrate(filter);
			
			return Response.status(Response.Status.OK).entity(categoriaDto).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		

	}
	
	

	/*
	@GET
	@Path("/getAllByCategory")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllByCategory(@QueryParam("tipoCategoria") String tipoCategoriaStr) {
	    if (tipoCategoriaStr != null && !tipoCategoriaStr.isEmpty()) {
	       
	            NomeCategoria tipoCategoria = NomeCategoria.valueOf(tipoCategoriaStr);
	            
	            List<Categoria> categoriaDto = categoriaService.getCategoriesByType(tipoCategoria);
	            return Response.status(Response.Status.OK).entity(categoriaDto).build();
	       
	    } else {
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}

	 */

}
