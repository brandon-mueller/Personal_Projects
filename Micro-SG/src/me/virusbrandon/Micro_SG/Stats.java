package me.virusbrandon.Micro_SG;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Stats implements java.io.Serializable {
	private static final long serialVersionUID = 133982989L;
	
	private String name;										//Player Name
	private UUID uuid;											//Player Unique ID
	private int wins;											//Wins
	private int kills;											//Kills
	private int deaths;											//Deaths
	private int elixer;											//Revival Elixer, These Get Purchased From The WebSite Store
	private int points;											//Points, Earned In Game -- Are Spendable On Revival Elixers
	private int LTpoints;										//LifeTime Points -- Not Spendable
	private int revives;										//LifeTime Number Of Revives 
	private GUI gui;											//Every Player Has Their Own GUI
	private ArrayList<Integer> startItems = new ArrayList<>();	//Items The Player Will Start With
	
	Stats(Player p,ArenaManager m){
		this.name = p.getName();
		this.uuid = p.getUniqueId();
		gui = new GUI(0,m);
	}
	
	Stats(String name, UUID uuid, ArenaManager m){
		this.name = name;
		this.uuid = uuid;
		gui = new GUI(0,m);
	}
	
	public String getName(){
		return name;
	}
	
	public UUID getUUID(){
		return uuid;
	}
	
	public void setWins(int start, int toChange,int mult){
		this.wins=(start+toChange);
		setPoints(getPoints(),100,mult,false);
	}
	
	public int getWins(){
		return wins;
	}
	
	public void setKills(int start,int toChange,int mult){
		this.kills=(start+toChange);
		setPoints(getPoints(),5,mult,false);
	}
	
	public int getKills(){
		return kills;
	}
	
	public void setDeaths(int start,int toChange){
		this.deaths=(start+toChange);
		setPoints(getPoints(),-5,1,false);
	}
	
	public int getDeaths(){
		return deaths;
	}
	
	public int setElixer(int start,int toChange){
		if(start+toChange<0){
			return -1;
		}
		this.elixer=(start+toChange);
		return 0;
	}
	
	public int getElixers(){
		return elixer;
	}
	
	public int setPoints(int start,int toChange,int mult,boolean cmd){
		if(start+toChange<0){
			this.points=0;
			return 0;
		}else{
			this.points=start+(toChange*mult);
			if((toChange>0)&(cmd==false)){
				setLTPoints(getLTPoints(),toChange);
			}
			return 0;
		}
	}
	
	public int getPoints(){
		return points;
	}
	
	public GUI GUI(){
		return gui;
	}
	
	public void initGUI(ArenaManager m){
		this.gui = new GUI(0,m);
	}
	
	public void initItems(){
		this.startItems=new ArrayList<>();
	}
	
	public void killGUI(){
		this.gui = null;
	}
	
	public int getRevives(){
		return revives;
	}
	
	public void setRevives(int start,int toChange){
		this.revives=(start+toChange);
		setPoints(getPoints(),-2,1,false);
	}
	
	public void setLTPoints(int start,int toChange){
		this.LTpoints=(start+toChange);
	}
	
	public int getLTPoints(){
		return LTpoints;
	}
	
	public void addStartItem(int i){
		this.startItems.add(i);
	}
	
	public ArrayList<Integer> getItems(){
		return startItems;
	}
	
	public void clearStartItems(){
		startItems.clear();
	}
	
	public String toString(){
		return "\n- Kills: "+getKills()+"\n- Deaths: "+getDeaths()+"\n- Revives: "+getRevives()+"\n- Wins: "+getWins()+"\n- Points: "+getPoints()+"\n- LifeTime Points: "+getLTPoints()+"\n- Revival Elixers: "+getElixers();
	}
}
