package org.city.bookstore.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.city.bookstore.models.Book;

public class BookRepository {

	private Database dbInstance;
	private Statement stmt = null;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public BookRepository(){
		try {
			dbInstance = Database.getInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public List<Book> search(String query){
		try {
			conn = dbInstance.connect("SEARCH");
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
		String q = "%"+query+"%";
		q = q.toLowerCase();
		System.out.println("Creating statement for search query...");
		String sql;
		sql = "SELECT DISTINCT * FROM books WHERE LOWER(author) LIKE ? OR LOWER(title) LIKE ? OR LOWER(language) LIKE ? "
				+ " OR LOWER(subject) LIKE ? OR LOWER(language) LIKE ? OR LOWER(publisher) LIKE ?";

		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, q);
			pstmt.setString(2, q);
			pstmt.setString(3, q);
			pstmt.setString(4, q);
			pstmt.setString(5, q);
			pstmt.setString(6, q);

			ResultSet rs = pstmt.executeQuery();

			List<Book> booksList = new ArrayList<Book>();
			Book book;

			if (!rs.next()) {
				System.out.println(" *** No entities where retrieved on search ***");
			} else {
				System.out.println(" *** Entities where retrieved on search ***");
				do {
					book = new Book();
					book.setId(rs.getInt("id"));
					book.setAuthor(rs.getString("author"));
					book.setTitle(rs.getString("title"));
					book.setSubject(rs.getString("subject"));
					book.setLanguage(rs.getString("language"));
					book.setPublisher(rs.getString("publisher"));
					book.setISBN(rs.getString("isbn"));
					if(rs.getBoolean("in_stock"))
						book.addToStock();
					booksList.add(book);
				} while (rs.next());
			}
			return booksList;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			System.out.println("---- |*|Closing all Connections|*| ----");
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			} catch(SQLException e){
				System.out.println("---- |*|Failed to close connections|*| ----");
				e.printStackTrace();
			}
		}
	}

	public List<Book> all(){
		try {
			conn = dbInstance.connect("SELECT ALL");
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}

		System.out.println("Creating statement...");
		try {
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM books";

			rs = stmt.executeQuery(sql);

			List<Book> booksList = new ArrayList<Book>();
			Book book;

			if (!rs.next()) {
				System.out.println(" *** No entities where retrieved on select all ***");
			} else {
				System.out.println(" *** Entities where retrieved on select all ***");
				do {
					book = new Book();
					book.setId(rs.getInt("id"));
					book.setAuthor(rs.getString("author"));
					book.setTitle(rs.getString("title"));
					book.setSubject(rs.getString("subject"));
					book.setLanguage(rs.getString("language"));
					book.setPublisher(rs.getString("publisher"));
					book.setISBN(rs.getString("isbn"));
					if(rs.getBoolean("in_stock"))
						book.addToStock();
					booksList.add(book);
				} while (rs.next());
			}
			return booksList;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Select query exception: " + e.getMessage());
			return null;
		}finally{
			System.out.println("---- |*|Closing all Connections|*| ----");
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			} catch(SQLException e){
				System.out.println("---- |*|Failed to close connections|*| ----");
				e.printStackTrace();
			}
		}
	}


	public Book save(Book entity) {
		try {
			conn = dbInstance.connect("INSERT");
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}

		String title = entity.getTitle();
		String author = entity.getAuthor();
		String isbn = entity.getISBN();
		String lang = entity.getLanguage();
		String subj = entity.getSubject();
		String pub = entity.getPublisher();
		boolean stock = entity.isInStock();

		String sql = "INSERT INTO books(title, author, publisher, isbn, language, subject, in_stock) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, author);
			pstmt.setString(3, pub);
			pstmt.setString(4, isbn);
			pstmt.setString(5, lang);
			pstmt.setString(6, subj);
			pstmt.setBoolean(7, stock);

			ResultSet rs = pstmt.executeQuery();

			if (!rs.next()) {
				System.out.println(" *** Failed to create new entity... ***");
				return null;
			}
			int id = rs.getInt(1);

			Book book = new Book(title, author, isbn, subj, lang, pub, stock);
			book.setId(id);			

			return book;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			System.out.println("---- |*|Closing all Connections|*| ----");
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			} catch(SQLException e){
				System.out.println("---- |*|Failed to close connections|*| ----");
				e.printStackTrace();
			}
		}
	}


	public Book first(int id) {

		try {
			conn = dbInstance.connect("SELECT SINGLE");
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}

		String sql = "SELECT * FROM books WHERE id=?";
		Book book = new Book();
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id); 
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				System.out.println(" *** Select result empty... *** ");
				return null;
			} else {

				do {
					book = new Book();
					book.setId(rs.getInt("id"));
					book.setAuthor(rs.getString("author"));
					book.setTitle(rs.getString("title"));
					book.setSubject(rs.getString("subject"));
					book.setLanguage(rs.getString("language"));
					book.setPublisher(rs.getString("publisher"));
					book.setISBN(rs.getString("isbn"));
					if(rs.getBoolean("in_stock"))
						book.addToStock();
				} while (rs.next());

			}

			return book;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			System.out.println("---- |*|Closing all Connections|*| ----");
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			} catch(SQLException e){
				System.out.println("---- |*|Failed to close connections|*| ----");
				e.printStackTrace();
			}
		}
	}


	public Book update(Book entity) {
		if(this.first(entity.getId()) == null){
			System.out.println(" *** Entity to be updated does NOT exist... *** ");
			return null;
		}
		try {
			conn = dbInstance.connect("UPDATE");
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
		ResultSet rs = null;
		int id = entity.getId();
		String title = entity.getTitle();
		String author = entity.getAuthor();
		String isbn = entity.getISBN();
		String lang = entity.getLanguage();
		String subj = entity.getSubject();
		String pub = entity.getPublisher();
		boolean stock = entity.isInStock();
		String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, isbn = ? , language = ? , "
				+ "subject = ?, in_stock = ? WHERE id = ? RETURNING id";

		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, author);
			pstmt.setString(3, pub);
			pstmt.setString(4, isbn);
			pstmt.setString(5, lang);
			pstmt.setString(6, subj);
			pstmt.setBoolean(7, stock);
			pstmt.setInt(8, id);
			rs = pstmt.executeQuery();

			if(!rs.next()){
				System.out.println(" *** Entity was NOT updated... *** ");
				return null;
			}
			int upd_id = rs.getInt(1);
			if (upd_id <= 0)
				return null;

			Book book = new Book(title, author, isbn, subj, lang, pub, stock);
			book.setId(id);	
			return book;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			System.out.println("---- |*|Closing all Connections|*| ----");
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			} catch(SQLException e){
				System.out.println("---- |*|Failed to close connections|*| ----");
				e.printStackTrace();
			}
		}		
	}


	public boolean delete(int id) {
		try {
			conn = dbInstance.connect("DELETE");
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}

		String sql = "DELETE FROM books WHERE id = ?";

		try{

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);

			int row = pstmt.executeUpdate();

			System.out.println("Number of deleted rows---" + row +"----");

			if(row == 1)
				return true;
			return false;

		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			System.out.println("---- |*|Closing all Connections|*| ----");
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			} catch(SQLException e){
				System.out.println("---- |*|Failed to close connections|*| ----");
				e.printStackTrace();
			}
		}
	}


}
