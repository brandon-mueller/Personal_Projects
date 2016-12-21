package me.virusbrandon.maps;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

public class Template implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Piece> pieces = new ArrayList<>();
	private String name = "";
	private int SIZE_LIMIT = 50;
	private String wn;
	private int spawnX,spawnY,spawnZ;
	private float spawnP,spawnYa;
	private boolean spawnset = false;
	
	/**
	 * SmashVerse Arena Templates are to be
	 * limited in size
	 * 
	 * @param loc
	 * @param stuff
	 */
	public Template(Location loc, int limit, TemplateManager tm, String ... stuff){
		this.SIZE_LIMIT = limit;
		for(String s:stuff){
			this.name+=s+" ";
		}
		for(int y = 0;y<(SIZE_LIMIT+1);y++){ /* I can't stand O(n^3) >:( */
			for(int z = 0;z<(SIZE_LIMIT+1);z++){
				for(int x = 0;x<(SIZE_LIMIT+1);x++){
					Location l = new Location(loc.getWorld(),loc.getX()+x,loc.getY()+y,loc.getZ()+z);
					checkForSpawn(l,x,y,z,l.getPitch(),l.getYaw());
					pieces.add(new Piece(l.getWorld().getBlockAt(l),x,y,z));
				}
			}
		}
	}
	
	/**
	 * If A Value Is Set Here For Spawn, The Values Are
	 * Only Relative To A Location, Not The Actual Spawn Point.
	 * 
	 * @param l
	 * @param x
	 * @param y
	 * @param z
	 * @param p
	 * @param ya
	 */
	public void checkForSpawn(Location l,int x, int y, int z, float p, float ya){
		if(l.getWorld().getBlockAt(l).getType().equals(Material.SIGN_POST)|l.getWorld().getBlockAt(l).getType().equals(Material.WALL_SIGN)){
			Sign sign = (Sign) l.getWorld().getBlockAt(l).getState();
			if(sign.getLine(0).equalsIgnoreCase("SPAWN")){
				l.getWorld().getBlockAt(l).setType(Material.AIR);
				this.wn = l.getWorld().getName();
				this.spawnX = x;
				this.spawnY = y;
				this.spawnZ = z;
				this.spawnP = p;
				this.spawnYa= ya;
				this.spawnset = true;
			}
		}
	}
	
	public ArrayList<Piece> getPieces(){
		return pieces;
	}
	
	public boolean isSpawnSet(){
		return spawnset;
	}
	
	public String getName(){
		return name;
	}
	
	public String WN(){
		return wn;
	}
	
	public int X(){
		return spawnX;
	}
	
	public int Y(){
		return spawnY;
	}
	
	public int Z(){
		return spawnZ;
	}
	
	public float P(){
		return spawnP;
	}
	
	public float YA(){
		return spawnYa;
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}