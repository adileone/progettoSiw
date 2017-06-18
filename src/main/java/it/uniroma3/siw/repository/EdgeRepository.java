package it.uniroma3.siw.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Edge;
import it.uniroma3.siw.model.Pipeline;


public interface EdgeRepository extends CrudRepository<Edge, Long> {

	List<Edge> findByPipeline(Pipeline pipe);
	ArrayList<Edge> findAllById(Long id);

}

