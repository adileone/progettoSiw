package it.uniroma3.siw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Link {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String sxItem;
	private String dxItem;
	
	private String stage;
	
	
	public Link() {
	
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSxItem() {
		return sxItem;
	}

	public void setSxItem(String sxItem) {
		this.sxItem = sxItem;
	}

	public String getDxItem() {
		return dxItem;
	}

	public void setDxItem(String dxItem) {
		this.dxItem = dxItem;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	@Override
	public String toString() {
		return "Link [id=" + id + ", sxItem=" + sxItem + ", dxItem=" + dxItem + ", stage=" + stage + "]";
	}
	
}
