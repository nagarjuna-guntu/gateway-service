package com.polarbookshop.gateway_service.web;

import java.util.List;

public record Book(
		
		String isbn,
		
		String title,
		
		String author,
		
		double price,
		
		String publisher
		
		) {
	
	    public static final List<Book> BOOK_CACHE = List.of(
	    		 Book.of(
	    				 "1234567892", 
	    				 "Polar Journey",
	    			     "Iorek Polarson", 
	    			     12.90, Publisher.Polar.getName()),
	    		 Book.of(
	    				 "1491910771", 
	    				 "Head First Java: A Brain-Friendly Guide",
	    			     "Kathy Sierra", 
	    			     9.90, Publisher.O_Reilly.getName()),
	    		 Book.of(
	    				 "0134685997", 
	    				 "Effective Java 3rd Edition",
	    			     "Joshua Bloch", 
	    			     59.99, Publisher.Addison_Wesley.getName())
	    		
	    		);
	    public static Book of(String isbn, String title, String author, double price, String publisher) {
	    	return new Book (isbn, title, author, price, publisher);
	    }

}
