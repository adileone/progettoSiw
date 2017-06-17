package it.uniroma3.siw.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Edge {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Node sxItem;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Node dxItem;

	@JsonBackReference
	@ManyToOne
	private Pipeline pipeline; 


	public Edge() {

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

	public Node getSxItem() {
		return sxItem;
	}

	public void setSxItem(Node sxItem) {
		this.sxItem = sxItem;
	}

	public Node getDxItem() {
		return dxItem;
	}

	public void setDxItem(Node dxItem) {
		this.dxItem = dxItem;
	}

	@Override
	public String toString() {
		return "Edge [id=" + id + ", sxItem=" + sxItem + ", dxItem=" + dxItem + "]";
	}

	
}
