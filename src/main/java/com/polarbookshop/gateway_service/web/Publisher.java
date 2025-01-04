package com.polarbookshop.gateway_service.web;

public enum Publisher {
	
	Polar("Polar Publications"),
	O_Reilly("O'Reilly Media"),
	Manning("Manning Publications"),
	Addison_Wesley("Addison-Wesley Professional");
	
	private final String name;
	private Publisher(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
