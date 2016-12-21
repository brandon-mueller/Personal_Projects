package me.virusbrandon.maps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.virusbrandon.game.Arena;
import me.virusbrandon.game.ArenaManager;
import me.virusbrandon.sv_utils.ArenaStatus.Status;

public class TemplateManager {
	private ArrayList<Template> templates = new ArrayList<>();
	private ArrayList<Arena> buildQueue = new ArrayList<>();
	private ArenaManager man;
	private TemplateBuilder builder;
	private int limit;
	private String wh=ChatColor.WHITE+"";
	
	public TemplateManager(ArenaManager man, int limit){
		this.man = man;
		this.limit = limit;
		this.builder = new TemplateBuilder(this);
	}
	
	/**
	 * Returns A Random Map Template To
	 * The Arena Manager
	 * 
	 * @return
	 */
	public Template chooseMap(){
		if(templates.size()>0){
			int i = (int)(Math.random()*templates.size());
			Template temp = templates.get(i);
			return temp;
		}
		/* NullPointer Access! No Map Templates Available! */
		return null;
	}
	
	/**
	 * Shows The Arena Build Space
	 * That Is Available To You.
	 * 
	 */
	public void showBuildSpace(Player p){
		if(man.getOrigin()!=null){
			Location or = man.getOrigin();
			int i = man.getMaxArenaSize();
			int[][] spots = new int[][]{{0,0,0},{i,0,0},{0,0,i},{i,0,i},{0,i,0},{i,i,0},{0,i,i},{i,i,i}};
			for(int[] s:spots){
				Location l = new Location(or.getWorld(),or.getX()+(-s[0]),or.getY()+s[1],or.getZ()+(-s[2]));
				l.getWorld().getBlockAt(l).setType(Material.REDSTONE_BLOCK);
			}
			p.sendMessage("Build Region Is Now Visible At Your Set Origin Location...");
			p.teleport(new Location(or.getWorld(),or.getX(),(or.getY()+1),or.getZ()));
		}
	}
	
	/**
	 * Add A Template To The Template Manager
	 * So We Can Use It Later To Construct A
	 * Saved Arena
	 * 
	 * @param template
	 */
	public void saveTemplate(String ... name){
		String[] n = new String[name.length-2];
		for(int x=2;x<name.length;x++){
			n[x-2]=name[x];
		}
		if(man.getOrigin()!=null){
			Location or = man.getOrigin();
			Location loc = new Location(or.getWorld(),or.getX()+(-1*50),or.getY(),or.getZ()+(-1*50));
			Template template = new Template(loc,limit,this,n);
			if(template.isSpawnSet()){
				templates.add(template);
				Bukkit.broadcastMessage("Template Save - Successful!");
			} else {
				Bukkit.broadcastMessage("Template Save - Failed - Spawn NOT Detected!");
			}
			if(man.getArenaCount()<1){
				man.start();
			}
		}
	}
	
	/**
	 * Returns The Arena Size Limit
	 * 
	 */
	public int getLimit(){
		return limit;
	}
	
	/**
	 * Returns All Templates
	 * 
	 */
	public ArrayList<Template> getTemplates(){
		return templates;
	}
	
	/**
	 * Constructs A Saved Template Relative
	 * To The Set Origin Point Into The Given Slot.
	 * 
	 * @param slot
	 */
	public boolean buildTemplate(Arena arena){
		if(arena!=null){
			if(man.getOrigin()!=null){
				if(((!builder.isBusy())&(arena!=null))){
					arena.setStatus(Status.PREPARING);
					builder.beginBuilding(arena, man.getOrigin(), arena.getSlot());
					man.checkArenas();
				} else {
					arena.setStatus(Status.QUEUED);
					buildQueue.add(arena);
				}
				return true;
			} else {
				return false; /* Build Failed Because We Need An Origin Location */
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Returns The Next Arena In The Build Queue
	 * 
	 */
	public Arena getNextQueuedArena(){
		man.checkArenas();
		if(buildQueue.size()>0){
			Arena next = buildQueue.remove(0);
			return next;
		} else {
			return null; /* Handle This By Checking For Null Where Called */
		}
	}
	
	/**
	 * Removes A Template
	 * 
	 */
	public void removeTemplate(Template template){
		templates.remove(template);
	}
	
	/**
	 * Returns The Names On All
	 * Current Templates
	 * 
	 */
	public String[] getTNameList(){
		String[] s = new String[templates.size()];
		if(templates.size()>0){
			for(int x=0;x<s.length;x++){
				s[x]=wh+x+". "+templates.get(x).getName();
			}
		} else {
			return new String[]{wh+"No Templates Found!"};
		}
		return s;
	}
	
	/**
	 * Returns Available Arena Templates
	 * 
	 */
	public int getTemplateCount(){
		return templates.size();
	}
	
	public void loadTemplates(){
		ObjectInputStream read;
		File file = new File("plugins/SmashVerse/Templates");
		if(!file.exists()){
			file.mkdir();
		}
		try{
			int i = file.listFiles().length;
			if(i>0){
				man.getConsole().sendMessage(man.getMain().getPfx()+"- We Found "+i+" Template"+((i==1)?"":"s")+" - Loading "+((i>1)?"Them":"It")+"...");
				for(File f:file.listFiles()){
					read = new ObjectInputStream(new FileInputStream(f));
					templates.add((Template)read.readObject());
					String t = templates.get(templates.size()-1).getName();
					man.getConsole().sendMessage(man.getMain().getPfx()+"- Map Template \""+t.substring(0,(t.length()-1))+"\" Loaded In!");
					read.close();
				}
			} else {
				man.getConsole().sendMessage(man.getMain().getPfx()+"- We Couldn't Find Any Map Templates... Sorry :(");
			}
		}catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	public void saveTemplates(){
		ObjectOutputStream write;
		File file;
		try{
			for(File f : new File("plugins/SmashVerse/Templates").listFiles()) {
			    f.delete();
			}
			if(templates.size()>0){	
				int i = templates.size();
				man.getConsole().sendMessage(man.getMain().getPfx()+"- We Have "+i+" Template"+((i==1)?"":"s")+" To Save - Writing "+((i==1)?"It":"Them")+"...");
				for(Template template:templates){
					String n = template.getName().replaceAll(" ","");
					file = new File("plugins/SmashVerse/Templates/"+n+".template");
					write = new ObjectOutputStream(new FileOutputStream(file));
					write.writeObject(template);
					man.getConsole().sendMessage(man.getMain().getPfx()+"- Template File \""+n+"\" Written Out!");
					write.close();
				}
				templates.clear();
			} else {
				man.getConsole().sendMessage(man.getMain().getPfx()+"- We Don't Have Any Templates To Save D:");
			}
		}catch(Exception e1){
			e1.printStackTrace();
		}	
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}