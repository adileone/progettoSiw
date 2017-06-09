package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.UtenteRepository;

@Service
public class UtenteService {

	@Autowired
	private UtenteRepository userRepository; 

	public Iterable<Utente> findAll() {
		return this.userRepository.findAll();
	}

	@Transactional
	public void add(final Utente user) {
		this.userRepository.save(user);
	}

}
