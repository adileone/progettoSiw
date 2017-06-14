package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Link;
import it.uniroma3.siw.model.Pipeline;



public interface LinkRepository extends CrudRepository<Link, Long> {

	List<Link> findByPipeline(Pipeline pipe);

}

