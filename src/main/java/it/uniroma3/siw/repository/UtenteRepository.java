package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long> {

	List<Utente> findByUsername(String username);

	List<Utente> findByEmail(String email);

}

