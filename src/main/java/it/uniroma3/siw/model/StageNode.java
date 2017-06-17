package it.uniroma3.siw.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("STAGE")
public class StageNode extends Node {
	
	private String stageType;

	public StageNode() {

	}

	public String getStageType() {
		return stageType;
	}

	public void setStageType(String stageType) {
		this.stageType = stageType;
	}
	
	@Override
	public String toString() {
		return stageType;
	}
	
}
