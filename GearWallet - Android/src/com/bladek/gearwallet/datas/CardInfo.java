package com.bladek.gearwallet.datas;

import java.io.Serializable;

public class CardInfo implements Serializable {
	// Serial Version UID
	private static final long serialVersionUID = 1L;
	
	// Member Variable
	private String name = "";
	private String number = "";
	private String color = "";
	private int freq = 0;
	
	/** Constructor **/
	public CardInfo(String name, String color) {
		this.name = name;
		this.color = color;
	}
	
	public CardInfo(String name, String number, String color, int freq){
		this.name = name;
		this.number = number;
		this.color = color;
		this.freq = freq;
	}
	
	/** Setter Methods **/
	public void setName(String name) {
		this.name = name;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public void setFreq(int freq) {
		this.freq = freq;
	}
	
	/** Getter Methods **/
	public String getName() {
		return name;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String getColor() {
		return color;
	}
	
	public int getFreq() {
		return freq;
	}
}

// End of InfoClass