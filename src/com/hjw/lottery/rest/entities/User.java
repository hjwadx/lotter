package com.hjw.lottery.rest.entities;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 2955638011832610698L;

	public int id;
	public String name;

	public User() {
	}
	
	public User(String name) {
		this.name = name;
	}
	
	public User(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			if(((User)o).id == this.id){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
