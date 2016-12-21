package me.virusbrandon.BuildManager;

import java.util.ArrayList;

public class Build implements java.io.Serializable{
	private static final long serialVersionUID = 184820909090L;
	
	ArrayList<Integer> blocks;
	ArrayList<Byte> data;
	String name;
	
	Build(ArrayList<Integer> blocks,ArrayList<Byte> data, String name){
		this.blocks = blocks;
		this.data = data;
		this.name = name;
	}
	
	public ArrayList<Integer> getBlocks(){
		return blocks;
	}

	public ArrayList<Byte> getData(){
		return data;
	}
	
	public String getName(){
		return name;
	}
	
	public void setOverWritable(){
		this.name = "nothing";
		this.blocks = null;
	}
}
