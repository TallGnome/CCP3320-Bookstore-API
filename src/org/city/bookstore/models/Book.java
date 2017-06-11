package org.city.bookstore.models;

public class Book {
	private String title, author, ISBN, subject, language, publisher;
	private boolean inStock = false;
	private int id;
	
	public Book(){}
	public Book(String title, String author, String ISBN, String subject, String language, String publisher, boolean inStock){
		this.title = title;
		this.author = author;
		this.ISBN = ISBN;
		this.subject = subject;
		this.language = language;
		this.publisher = publisher;
		this.inStock = inStock;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		this.ISBN = iSBN;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public boolean isInStock() {
		return inStock;
	}
	public void addToStock(){
		this.inStock = true;
	}
	public void removeStock(){
		this.inStock = false;
	}
	public int getId() {
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	
	public String toString(){
		return "Title: " + this.title + ",\n" + "Author: " + this.author + "\n";
	}
	
}
