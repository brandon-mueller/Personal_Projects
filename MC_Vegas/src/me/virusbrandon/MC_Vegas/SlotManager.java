package me.virusbrandon.MC_Vegas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SlotManager implements java.io.Serializable{
	private static final long serialVersionUID = 193928L;
	
	int lw,lx,ly,lz;
	float lyaw,lpitch;
	boolean lobbySet = false;
	int w,x,y,z; 										/*Origin Point, All Slot Machines Will Stack From This Point*/
	String wn;											/*World Name*/
	int xw,zw,yh;
	int sx,ex,sy,ey,sz,ez;
	int plays = 0; 										/*Total Plays From All Machines*/
	ArrayList<Win> wins = new ArrayList<>(); 			/*Possible Win Combinations*/
	ArrayList<Slot> slots = new ArrayList<>(); 			/*Active Slot Machines*/
	ArrayList<Block> blocks = new ArrayList<>(); 		/*Slot Machine Template*/
	String gr = ChatColor.GREEN +"",re=ChatColor.RED+"",ye=ChatColor.YELLOW+"",bo=ChatColor.BOLD+"",wh=ChatColor.WHITE+"",go=ChatColor.GOLD+"",aq=ChatColor.AQUA+"";
	SlotManager(Location l, ArrayList<Selection> s, Player p){
		if(s.size() == 2){
			this.x = l.getBlockX();
			this.y = l.getBlockY();
			this.z = l.getBlockZ();
			for(int a = 0;a<Bukkit.getWorlds().size();a++){
				if(Bukkit.getWorlds().get(a).getName().equalsIgnoreCase(l.getWorld().getName())){
					this.w = a;
					this.wn  = Bukkit.getWorlds().get(a).getName();
				}
			}
			initTemplate(s);
		} else {
			p.sendMessage(e()+"Template Selection Error!");
			blocks.get(0); /*We'll Purposely Cause An Exception Here >:)*/
		}
	}
	
	public String getWN(){
		return wn;
	}
	
	public void setW(int w){
		this.w = w;
	}
	
	public void addWin(int id,int required, String prize, int available,int cmd){
		wins.add(new Win(id, required, prize, available,cmd));
	}
	
	public void setPlays(int plays){
		this.plays = plays;
	}
	
	public int getPlays(){
		return plays;
	}
	
	public void addPlay(){
		plays++;
	}
	
	public void playerJoined(Player p){
		boolean foundOpenSlot = false;
		for(int a = 0;a<slots.size();a++){
			if(slots.get(a).getOpen() == true){
				slots.set(a,new Slot(this,new Location(Bukkit.getWorlds().get(w),(x+(a*15)),y,z),blocks,p));
				slots.get(a).buildMachine(xw,zw,yh);
				foundOpenSlot = true;
				break;
			}
		}
		if(foundOpenSlot == false){
			slots.add(new Slot(this,new Location(Bukkit.getWorlds().get(w),(x+(slots.size()*15)),y,z),blocks,p));
			slots.get(slots.size()-1).buildMachine(xw,zw,yh);
		}	
	}
	
	public void playerLeft(Player p){
		for(int x = 0;x<slots.size();x++){
			if(slots.get(x).getOwnerName().equalsIgnoreCase(p.getName())){
				slots.get(x).getOwner().getInventory().clear();
				slots.get(x).getOwner().teleport(getLobby());
				slots.get(x).destroyMachine(xw,zw,yh);
				slots.get(x).setOpen(true);
			}
		}
	}
	
	public void destroyAllMachines(){
		for(Slot s:slots){
			try{
				s.getOwner().teleport(getLobby());
				s.getOwner().getInventory().clear();
			}catch(Exception e1){}	
			s.destroyMachine(xw, zw, yh);
		}
		slots.clear();
	}
	
	@SuppressWarnings("deprecation")
	public void initTemplate(ArrayList<Selection> s){
		sx = Math.min(s.get(0).getX(), s.get(1).getX());
		sy = Math.min(s.get(0).getY(), s.get(1).getY());
		sz = Math.min(s.get(0).getZ(), s.get(1).getZ());
		ex = Math.max(s.get(0).getX(), s.get(1).getX());
		ey = Math.max(s.get(0).getY(), s.get(1).getY());
		ez = Math.max(s.get(0).getZ(), s.get(1).getZ());
		xw = (ex-sx);zw = (ez-sz);yh = (ey-sy);
		for(int x = sx;x<ex+1;x++){
			for(int z = sz;z < ez+1;z++){
				for(int y = sy;y<ey+1;y++){
					blocks.add(new Block(Bukkit.getWorlds().get(w).getBlockAt(x,y,z).getTypeId(),Bukkit.getWorlds().get(w).getBlockAt(x,y,z).getData()));
				}
			}
		}
	}
	
	public boolean lobbySet(){
		return lobbySet;
	}
	
	public void setLobby(Location l){
		for(int x = 0;x<Bukkit.getWorlds().size();x++){
			if(Bukkit.getWorlds().get(x).getName().equalsIgnoreCase(l.getWorld().getName())){
				this.lw = x;
			}
		}
		this.lx = l.getBlockX();
		this.ly = l.getBlockY();
		this.lz = l.getBlockZ();
		this.lyaw = l.getYaw();
		this.lpitch = l.getPitch();
		lobbySet = true;
	}
	
	public void announceWinner(Player p,Win w){
		for(Player pl:Bukkit.getOnlinePlayers()){
			pl.playSound(pl.getLocation(), Sound.ENDERDRAGON_DEATH, .3f, 2.0f);
		}
		Bukkit.broadcastMessage("_█_█_█_█_█_█_█_█_█_");
		Bukkit.broadcastMessage(wh+bo+"MC-Vegas :D!");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(go+bo+"JACKPOT HIT!!!!");
		Bukkit.broadcastMessage(aq+bo+"By Player: " + p.getName());
		Bukkit.broadcastMessage(gr+bo+"Prize Won: "+w.getPrize());
		Bukkit.broadcastMessage(re+bo+"CONGRATULATIONS!!!! WE HAVE A WINNER!");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("_█_█_█_█_█_█_█_█_█_");
	}
	
	public void recordWinner(Win prize, Player p, Date date) {
		DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
		try{		  
			File file = new File("plugins/MC_Vegas/Winners/winners.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append("Player Name: " + p.getName()+" -- Prize Won: " + prize.getPrize()+" -- Date: "+ df.format(date)+"\n");
			bw.close();
		}catch (IOException e) {
			File file = new File("plugins/MC_Vegas/Winners");
			file.mkdir();
			recordWinner(prize,p,date);
		}
	}
	
	public Location getLobby(){
		return new Location(Bukkit.getWorlds().get(lw), lx, ly, lz, (float)lyaw,(float)lpitch);
	}
	
	public ArrayList<Slot> getSlots(){
		return slots;
	}
	
	public ArrayList<Win> getWins(){
		return wins;
	}
	
	public String s(){ //A Success Icon
		return gr+bo+"[  " + wh+bo+"!" + gr+bo+"  ] ";
	}
	
	public String w(){ //A Warning Icon
		return ye+bo+"[  " + wh+bo+"!" + ye+bo+"  ] ";
	}
	
	public String e(){ //An Error Icon
		return re+bo+"[  " + wh+bo+"!" + re+bo+"  ] ";
	}
}
