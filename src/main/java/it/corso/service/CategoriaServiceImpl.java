package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CategoriaDao;
import it.corso.dto.CategoriaDto;
import it.corso.model.Categoria;
import it.corso.model.NomeCategoria;

@Service
public class CategoriaServiceImpl implements CategoriaService {
	
	private ModelMapper modelmapper = new ModelMapper();

	
	@Autowired
	private CategoriaDao categoriaDao;

	@Override
	public void categoriaCreazione(CategoriaDto categoriaDto) {

		Categoria categoria =convertCategoria(categoriaDto);
		
		categoriaDao.save(categoria);
				
		
	}


	
	private Categoria convertCategoria(CategoriaDto categoriaDto) {
		Categoria categoria = new Categoria();
		categoria.setNomeCategoria(categoriaDto.getNomeCategoria());
		
		return categoria;
	}
	



	@Override
	public CategoriaDto getCategoriaDtoById(int id) {
		Optional<Categoria> categoria = categoriaDao.findById(id);
		
		CategoriaDto categoriaDto = modelmapper.map(categoria, CategoriaDto.class);
		
		return categoriaDto;
	}
	
	
	public List<CategoriaDto> getAllCategories(){
		List<Categoria> categoria = (List<Categoria>) categoriaDao.findAll();
		List<CategoriaDto> categoriaDto = new ArrayList<>();
		categoria.forEach(c -> categoriaDto.add(modelmapper.map(c,CategoriaDto.class)));
		
		
		return categoriaDto;
		
	}


	@Override
	public List<CategoriaDto> getAllCategoriesFiltrate(String filter){
	    if (filter == null || filter.isEmpty()) {
	        return getAllCategories();
	    } else {
	        Iterable<Categoria> categIterable = categoriaDao.findAll();
	        List<CategoriaDto> categoriaDto = new ArrayList<>();
	        for(Categoria categoria : categIterable) {
	            if (categoria.getNomeCategoria().name().equalsIgnoreCase(filter)) {
	                categoriaDto.add(modelmapper.map(categoria, CategoriaDto.class));
	            }
	        }
	        if (categoriaDto.isEmpty()) {
	            return getAllCategories();
	        }
	        return categoriaDto;
	    }
	}
	


	/*

	@Override
	public List<CategoriaDto> getCategoriesByType(String tipoCategoria) {

		List<Categoria> categoria = (List<Categoria>) categoriaDao.findAll();
		List<CategoriaDto> categoriaDto = new ArrayList<>();
		categoria.forEach(c -> categoriaDto.add(modelmapper.map(c,CategoriaDto.class)));
		
		List<CategoriaDto> categoriaFiltro = categoriaDto.stream().filter(categoria -> categoria.getNomeCategoria().toLowerCase().contains(filter.toLowerCase())).collect(Collectors.toList());
		
		return null;
	}


/*
	@Override
	public List<CategoriaDto> getCategoriesByType(String tipoCategoria) {
		List<Categoria> categoriaList = categoriaDao.findByCategoria(NomeCategoria.valueOf(tipoCategoria));
		
		List<CategoriaDto> categoriaDto = new ArrayList<>();
		
		return categoriaDto;
	}


*/
	
	
	
	
}



