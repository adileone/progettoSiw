package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Pipeline;
import it.uniroma3.siw.model.Utente;



public interface PipelineRepository extends CrudRepository<Pipeline, Long> {

	List<Pipeline> findByName(String name);
	List<Pipeline> findByUser(Utente user);
	Iterable<Pipeline> findAllById(Long id);

}

