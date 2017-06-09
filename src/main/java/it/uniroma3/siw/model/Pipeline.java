package it.uniroma3.siw.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Pipeline {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@OneToMany
	private List<Task> task;

	@ManyToOne
	private Utente user;


	public Pipeline() {
		this.task = new LinkedList<>();
	}

	public List<Task> getTask() {
		return task;
	}

	public void setTask(LinkedList<Task> task) {
		this.task = task;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Utente getUser() {
		return user;
	}

	public void setUser(Utente user) {
		this.user = user;
	}

}
