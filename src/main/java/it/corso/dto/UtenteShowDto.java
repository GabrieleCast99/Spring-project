package it.corso.dto;

import java.util.List;

public class UtenteShowDto {
	
	private int id;
	private String nome;
	private String email;
	private List<UtenteRuoloDto> ruoli;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<UtenteRuoloDto> getRuoli() {
		return ruoli;
	}
	public void setRuoli(List<UtenteRuoloDto> ruoli) {
		this.ruoli = ruoli;
	}
	
	

}
