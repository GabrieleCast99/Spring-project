package it.corso.service;

import java.util.List;
import java.util.Optional;

import it.corso.dto.CategoriaDto;
import it.corso.model.Categoria;
import it.corso.model.NomeCategoria;

public interface CategoriaService {

	void categoriaCreazione(CategoriaDto categoriaDto);

	CategoriaDto getCategoriaDtoById(int id);
	
	List<CategoriaDto> getAllCategories();

	List<CategoriaDto> getAllCategoriesFiltrate(String filter);
	

	//List<Categoria> getCategoriesByType(NomeCategoria tipoCategoria);

	//List<CategoriaDto> getCategoriesByType(String tipoCategoria);
	
}
