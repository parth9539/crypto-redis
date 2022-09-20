package com.example.crysto;

interface xyz{
	public void abc();
}

public class Something {

	public static void main(String args[]) {
		xyz ab = () -> System.out.println("Hello");
		ab.abc();
	}
}
