package it.uniroma3.siw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Skill {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	private String name;
	private double value;

	@ManyToOne
	private Utente user;

	public Skill() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Utente getUser() {
		return user;
	}

	public void setUser(Utente user) {
		this.user = user;
	}

}
