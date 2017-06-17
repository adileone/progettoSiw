package it.uniroma3.siw.model;

import org.bson.Document;

public class KayakRequest {
	
	private static final String CONTENT = "content";
	
	private Document content;
	
	public static KayakRequest fromDocument(Document doc) {
		KayakRequest newReq = new KayakRequest();
		newReq.setContent(doc.get("content", Document.class));
		return newReq;
	}
	
	public Document toDocument() {
		return new Document()
		           .append(CONTENT, this.content);
	}

	public Document getContent() {
		return content;
	}
	
	private void setContent(Document content) {
		this.content = content;
	}
}
