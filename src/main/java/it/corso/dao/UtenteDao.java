package it.corso.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.corso.model.Utente;

public interface UtenteDao extends CrudRepository<Utente, Integer> {

	boolean existsByEmail(String email);

	Utente findByEmail(String email);
	
	Utente findByEmailAndPassword(String email, String Password);
	
	void deleteByEmail(String email);

}
