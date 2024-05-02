package it.corso.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;

import it.corso.dao.RuoloDao;
import it.corso.dao.UtenteDao;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteDtoAggiornamento;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteShowDto;
import it.corso.model.Ruolo;
import it.corso.model.Utente;

@Service
public class UtenteServiceImpl implements UtenteService {
	

	private ModelMapper modelmapper = new ModelMapper();
	
	@Autowired
	private UtenteDao utenteDao;
	
	@Autowired
	private RuoloDao ruoloDao;

	
	//registrazione POST
	@Override
	public void utenteRegistrazione(UtenteDto uDto) {

        Utente utente = convertiDtoToUtente(uDto);

        utenteDao.save(utente);
        
	}


	
    private Utente convertiDtoToUtente(UtenteDto uDto) {
        Utente utente = new Utente();
        utente.setNome(uDto.getNome());
        utente.setCognome(uDto.getCognome());
        utente.setEmail(uDto.getEmail());
        
        String sha256hex = DigestUtils.sha256Hex(uDto.getPassword());
        utente.setPassword(sha256hex);
        

        return utente;
    }
    
    
    //verificare se l'email esiste per la registrazione
	@Override
	public boolean existsUtenteByEmail(String email) {
		return utenteDao.existsByEmail(email);
	}


	//ricerca tramite mail da riusare nel mapper
	@Override
	public Utente findByEmail(String email) {
	    
	        return utenteDao.findByEmail(email);	    
	}



	@Override
	 public void deleteUser(String email) {

	  Utente utente = utenteDao.findByEmail(email);
	  
	  int id = utente.getId();
	  Optional<Utente> userOptional = utenteDao.findById(id);
	  if (userOptional.isPresent()) {

	   utenteDao.delete(userOptional.get());
	  }
	}
	

	
	public void updateUserData(UtenteDtoAggiornamento utente) {
		
		try {
			Utente utenteDb= utenteDao.findByEmail(utente.getEmail());
			
			if(utenteDb != null) {
				utenteDb.setNome(utente.getNome());
				utenteDb.setCognome(utente.getCognome());
				utenteDb.setEmail(utente.getEmail());
				
				List<Ruolo> ruoliUtente = new ArrayList<>();
				Optional<Ruolo> ruoloDb = ruoloDao.findById(utente.getIdRuolo());

				if(ruoloDb.isPresent()) {
					Ruolo ruolo = ruoloDb.get();
					ruolo.setId(utente.getIdRuolo());
					
					ruoliUtente.add(ruolo);
					utenteDb.setRuoli(ruoliUtente);
			
				}
				utenteDao.save(utenteDb);
			}
			
			
			
		} catch (Exception e) {
		    
		    throw new RuntimeException("Si è verificato un errore durante l'aggiornamento dei dati dell'utente. Si prega di riprovare più tardi.");
		}
		
		
	}

	
	



	//ricerca per email con dto e mapper
	@Override
	public UtenteShowDto getUserByEmail(String email) {

		Utente user = findByEmail(email);
		
		UtenteShowDto uSDto = modelmapper.map(user, UtenteShowDto.class);

		return uSDto;
	}



	//recupero di tutti gli utenti
	@Override
	public List<UtenteShowDto> getAllUser() {

		List<Utente> listUtente = (List<Utente>) utenteDao.findAll();
		
		List<UtenteShowDto> utenteShowDtos = new ArrayList<>();
		
		listUtente.forEach(u -> utenteShowDtos.add(modelmapper.map(u, UtenteShowDto.class)));
		
		return utenteShowDtos;
	}



	//login
	@Override
	public boolean loginUtente(UtenteLoginRequestDto utenteLoginRequestDto) {

		Utente utente = new Utente();
		utente.setEmail(utenteLoginRequestDto.getEmail());
		utente.setPassword(utenteLoginRequestDto.getPassword());
		
		//tramite il get password di utente recupero la stringa e la passo al metodo che me la hash
		String passwordHash = DigestUtils.sha256Hex(utente.getPassword());
		
		Utente credenzialiUtente= utenteDao.findByEmailAndPassword(utente.getEmail(), passwordHash);
		
		
		//Operatore Ternario: dato dalla condizione  ? espressioneA se vero : espressione B se falso
		return credenzialiUtente != null ? true:false;
	}	
}
