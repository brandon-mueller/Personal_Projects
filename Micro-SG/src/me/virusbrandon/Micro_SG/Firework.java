package me.virusbrandon.Micro_SG;

import java.util.ArrayList;

import org.bukkit.entity.FallingBlock;

public class Firework {
	private ArrayList<FallingBlock> fb = new ArrayList<>();
	private int lifeSpan = 0;
	
	Firework(){}
	
	public void tick(){
		lifeSpan++;
	}
	
	public void addBlock(FallingBlock b){
		fb.add(b);
	}
	
	public int getLife(){
		return lifeSpan;
	}
	
	public void destroy(){
		for(FallingBlock b:fb){
			b.remove();
		}
	}
}
