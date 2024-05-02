package it.corso.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.corso.dto.CategoriaDto;
import it.corso.model.Categoria;
import it.corso.model.NomeCategoria;

public interface CategoriaDao extends CrudRepository<Categoria, Integer> {


	//List<Categoria> findByCategoria(NomeCategoria valueOf);

}
