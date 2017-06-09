package it.uniroma3.siw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	public Task() {
	
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
