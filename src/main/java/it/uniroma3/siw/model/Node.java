package it.uniroma3.siw.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Inheritance
@DiscriminatorColumn(name="NODE_TYPE")
public abstract class Node {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@JsonBackReference
	@ManyToOne
	private Pipeline pipeline; 

	public Node() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Pipeline getPipeline() {
		return pipeline;
	}

	public void setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
	}
	
	
}
