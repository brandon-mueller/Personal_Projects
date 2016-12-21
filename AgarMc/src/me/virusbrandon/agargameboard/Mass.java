package me.virusbrandon.agargameboard;

import me.virusbrandon.agarmc.Main;

import org.bukkit.*;
import org.bukkit.block.Block;

public class Mass{
	
	private Main main;
	private int mass;
	private Location loc;
	private int[] bType;
	
	public Mass(Main main){
		this.main = main;
		this.bType = main.gB().getBBlocks()[(int)(Math.random()*main.gB().getBBlockLen())];
		this.loc =  main.gB().rndLoc();
		this.mass = (int)(1+Math.random()*2);
		hlpr(bType);
	}
	
	public Mass(Main main, Location l, int[] bType, int mass){
		this.main = main;
		this.loc =  l;
		this.bType = bType;
		this.mass = mass;
		hlpr(bType);
	}
	
	@SuppressWarnings("deprecation")
	private void hlpr(int[] bType){
		Block b = loc.getWorld().getBlockAt(new Location(loc.getWorld(),loc.getX(),(loc.getY()-main.gB().getCellPadding()),loc.getZ()));
		b.setTypeId(bType[0]);
		b.setData((byte)bType[1]);
	}
	
	/**
	 * Returns The Mass Of This
	 * Food Object.
	 * 
	 */
	public int getMass(){
		return mass;
	}
	
	/**
	 * Sets The Mass Of This
	 * Food Object.
	 * 
	 */
	public void reMass(){
		this.mass = (int)(1+Math.random()*2);
	}
	
	/**
	 * Kills This Mass Particle.
	 * 
	 */
	@SuppressWarnings("deprecation")
	public Mass kill(){
		main.gB().getAllMass().remove(((int)loc.getBlockX())+""+((int)loc.getBlockZ()));
		Block b = loc.getWorld().getBlockAt(new Location(loc.getWorld(),loc.getX(),(loc.getY()-main.gB().getCellPadding()),loc.getZ()));
		b.setTypeId(main.gB().getBoardBlock()[0]);
		b.setData((byte)main.gB().getBoardBlock()[1]);
		return this;
	}
	
	@SuppressWarnings("deprecation")
	public Mass relocate(){
		Block b = loc.getWorld().getBlockAt(new Location(loc.getWorld(),loc.getX(),(loc.getY()-main.gB().getCellPadding()),loc.getZ()));
		b.setTypeId(main.gB().getBoardBlock()[0]);
		b.setData((byte)main.gB().getBoardBlock()[1]);
		if(main.gB().getAllMass().size()<=main.gB().getMaxMass()){
			this.loc =  main.gB().rndLoc();
			b = loc.getWorld().getBlockAt(new Location(loc.getWorld(),loc.getX(),(loc.getY()-main.gB().getCellPadding()),loc.getZ()));
			b.setTypeId(bType[0]);
			b.setData((byte)bType[1]);
		}
		return this;
	}
	
	/**
	 * Returns The Location Of
	 * This Food Object.
	 * 
	 */
	public Location getLocation(){
		return loc;
	}
	
	/**
	 * Returns The Type And Data
	 * 
	 */
	public String tND(){
		return bType[0]+" - "+bType[1];
	}
	
	/**
	 * The Mass toString Method
	 * 
	 */
	public String toString(){
		return "Mass: "+mass+" Loc: "+loc.getX()+", "+loc.getY()+", "+loc.getZ();
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */
