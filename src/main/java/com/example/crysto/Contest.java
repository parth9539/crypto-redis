package com.example.crysto;

public class Contest {

	private int id;
	private String name;
	public Contest() {}
	public Contest(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Contest [id=" + id + ", name=" + name + "]";
	}
}
