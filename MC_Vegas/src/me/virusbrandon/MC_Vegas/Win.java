package me.virusbrandon.MC_Vegas;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Win implements java.io.Serializable{
	private static final long serialVersionUID = 139959L;
	
	private int id; //Lining Up Triple Of These Will Win Something;
	private int required = 250; //We'll Default The The Required Spins To 250;
	private String prize;
	private int available;
	private int cmd;
	
	Win(int id, int required, String prize, int available, int cmd){
		this.id = id;
		this.required = required;
		this.prize = prize;
		this.available = available;
		this.cmd = cmd;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public int getID(){
		return id;
	}
	
	public String getPrize(){
		return prize;
	}
	
	public void setPrize(String prize){
		this.prize =prize;
	}
	
	public int getRequired(){
		return required;
	}
	
	public void setRequired(int i){
		this.required = i;
	}
	
	public int getAvailable(){
		return available;
	}
	
	public void setAvailable(int available){
		this.available = available;
	}
	
	public int getCmd(){
		return cmd;
	}
	
	public void runCmd(Player p){
		switch(cmd){
		case 1:
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "profile addtickets "+p.getName()+" 200");
		break;
		case 2:
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "profile addtickets "+p.getName()+" 500");
		break;
		case 3:
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "profile addtickets "+p.getName()+" 1000");
		break;
		case 4:
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "profile addtickets "+p.getName()+" 2500");
		break;
		}
	}
	
	public String toString(SlotManager m){
		String atRisk = "";
		if(m.getPlays()>= required){atRisk = ChatColor.RED+" "+ChatColor.BOLD+" < AT RISK!";}
		return "Prize: "+prize + " > Id: " + id + " > Req: " + required + atRisk;
	}
}
