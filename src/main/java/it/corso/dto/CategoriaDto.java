package it.corso.dto;

import it.corso.model.NomeCategoria;

public class CategoriaDto {
	
	private int id;

	private NomeCategoria nomeCategoria;

	public NomeCategoria getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(NomeCategoria nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
