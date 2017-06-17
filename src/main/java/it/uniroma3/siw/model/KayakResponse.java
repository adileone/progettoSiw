package it.uniroma3.siw.model;

import org.bson.Document;

public class KayakResponse {
	
	private static final String CONTENT = "content";
	
	private Document content;
	
	public static KayakResponse fromDocument(Document doc) {
		KayakResponse newRes = new KayakResponse();
		newRes.setContent(doc.get("content", Document.class));
		return newRes;
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
