package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Pipeline;
import it.uniroma3.siw.repository.PipelineRepository;

@Service
public class PipelineService {

	@Autowired
	private PipelineRepository pipelineRepository; 

	public Iterable<Pipeline> findAll() {
		return this.pipelineRepository.findAll();
	}

	@Transactional
	public void add(final Pipeline pipe) {
		this.pipelineRepository.save(pipe);
	}

}
