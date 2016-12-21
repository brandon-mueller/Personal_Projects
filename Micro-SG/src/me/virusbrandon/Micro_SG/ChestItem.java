package me.virusbrandon.Micro_SG;

public class ChestItem implements java.io.Serializable{
	private static final long serialVersionUID = 12889834L;
	private String name;
	private int frequency;
	
	ChestItem(String name, int frequency){
		this.name = name;
		this.frequency = frequency;
	}
	
	public String getName(){
		return name;
	}
	
	public int getFreq(){
		return frequency;
	}
	
	public String getItem(){
		return "Name: "+name+" Freq: "+frequency;
	}
}
