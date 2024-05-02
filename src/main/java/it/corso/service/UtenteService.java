package it.corso.service;

import java.util.List;

import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteDtoAggiornamento;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteShowDto;
import it.corso.model.Utente;

public interface UtenteService {

	void utenteRegistrazione(UtenteDto uDto);
		
	boolean existsUtenteByEmail(String Email);

	Utente findByEmail(String email);
	
	void deleteUser(String email);	
	
	void updateUserData(UtenteDtoAggiornamento utente);

	UtenteShowDto getUserByEmail(String email);
	
	List<UtenteShowDto> getAllUser();

	boolean loginUtente(UtenteLoginRequestDto utenteLoginRequestDto);
	
	
}
