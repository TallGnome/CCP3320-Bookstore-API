package org.city.bookstore.endpoints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.city.bookstore.data.BookRepository;
import org.city.bookstore.models.Book;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/books")
public class Bookstore {

	private Gson gson;
	public Bookstore(){
		System.out.println("Endpoint call...");
		GsonBuilder builder = new GsonBuilder();
		this.gson = builder.create();
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchBooks(@QueryParam("q") String query){
		if(query == null || query == ""){
			BookRepository b = new BookRepository();
			List<Book> books = b.all();
			String json = this.gson.toJson(books);
			if(books == null || books.isEmpty())
				return Response.status(404).build();
			else
				return Response.status(200).entity(json).build();
		}
		else{
			BookRepository b = new BookRepository();
			List<Book> books = b.search(query);
			String json = this.gson.toJson(books);
			if(books == null || books.isEmpty())
				return Response.status(404).build();
			else
				return Response.status(200).entity(json).build();
		}
	}
	
//	@GET
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getBooks(){
//		BookRepository b = new BookRepository();
//		List<Book> books = b.all();
//		String json = this.gson.toJson(books);
//		if(books == null || books.isEmpty())
//			return Response.status(404).build();
//		else
//			return Response.status(200).entity(json).build();
//	}

	@GET
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBook(@PathParam("id") int id){
		BookRepository b = new BookRepository();
		System.out.println("Books Repository created on single get...");
		Book book = b.first(id);
		String json = this.gson.toJson(book);
		if(book != null)
			return Response.status(200).entity(json).build();
		else
			return Response.status(404).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addBook(InputStream body){
		BufferedReader reader = new BufferedReader(new InputStreamReader(body));
	    Book newbook = gson.fromJson(reader, Book.class);
	    try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    if(newbook == null){
	    	System.out.println("***ERROR*** == Data Null");
	    	return Response.status(404).build();
	    }
	    System.out.println(newbook.toString());
		BookRepository b = new BookRepository();
	    System.out.println("Books Repository created on add...");
	    
	    Book saved = b.save(newbook);
	    if(saved != null){
	    	String json = gson.toJson(saved);
			return Response.status(200).entity(json).build();
	    }
	    else
	    	return Response.status(404).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateBook(InputStream body){
		BufferedReader reader = new BufferedReader(new InputStreamReader(body));
	    Book newbook = gson.fromJson(reader, Book.class);
	    try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BookRepository b = new BookRepository();
		System.out.println("Books Repository created on update...");
		
		Book book = b.update(newbook);
		if(book == null){
			return Response.status(404).build();
		}
		String json = gson.toJson(newbook);
		return Response.status(200).entity(json).build();
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response deleteBook(@PathParam("id") int id){
		BookRepository b = new BookRepository();
		System.out.println("Books Repository created on delete...");
		if(b.delete(id)){
			return Response.status(204).build();
		}
		return Response.status(418).build();
	}
	
}
