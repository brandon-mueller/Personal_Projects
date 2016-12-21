package me.virusbrandon.MC_Vegas;

import java.util.UUID;
import org.bukkit.entity.Player;

public class Profile implements java.io.Serializable{
	private static final long serialVersionUID = 1843829893L;
	
	private UUID id;
	private String name;
	private int balance = 0;
	private int tickets = 0;
	private GUI g;

	
	Profile(){ //default constructor
		this.name = "new";
	}
	
	Profile(Player p){
		this.id = p.getUniqueId();
		this.name = p.getName();
		this.g = new GUI();
	}
	
	Profile(Profile p){
		this.id = p.ggetUUID();
		this.name = p.getName();
		this.g = p.GUI();
		this.balance = p.getBalance();
	}
	
	public String getUUID(){
		return id.toString();
	}
	
	public UUID ggetUUID(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public GUI GUI(){
		return g;
	}
	
	public int setBalance(int bal){
		this.balance = bal;
		return balance;
	}
	
	public int getBalance(){
		return balance;
	}
	
	public int getTickets(){
		return tickets;
	}
	
	public void setTickets(int bal){
		this.tickets=bal;
	}
}
