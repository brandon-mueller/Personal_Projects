package me.virusbrandon.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

import me.virusbrandon.powerblock.Main;
import me.virusbrandon.powerblock.TPlayer;
import me.virusbrandon.powerblock.Ticket;
import me.virusbrandon.threads.dlStThrd;

import org.bukkit.entity.Player;

public class StateManager {
	private Main main;
	
	public StateManager(Main main){
		this.main = main;
	}
	
	
	/**
	 * The Load States Function:
	 * 
	 * If The Server Did In Fact
	 * Stop Unexpectedly, There SaveStates
	 * Folder Will Contain Information About
	 * The Players Who Had Active Tickets At The
	 * Time Of The Server Crash.
	 * 
	 * These Players Will Be Issued New Tickets
	 * And Do No Have To Be Online To Get These
	 * Tickets.
	 * 
	 * Based On Information We Know About These
	 * Players, We Can Rebuild Their Profile And
	 * Add New Tickets Up To The Number That Were Lost
	 * In The Crash.
	 * 
	 */
	public void loadStates(File dir){
		try{
			for(File file:dir.listFiles()){
				if(file.isDirectory()){
					loadStates(file);
				} else {
					Scanner k = new Scanner(file);
					main.getPlayers().add(new TPlayer(k.nextLine(),k.nextLine(),main));
					int[]nums = new int[6];
					while(k.hasNext()){
						for(int x=0;x<6;x++){
							nums[x]=k.nextInt();
						}
						main.getPlayers().get(main.getPlayers().size()-1).addCustomTicket(nums);
						main.setTicketsPurchased(main.getTicketsPurchased()+1);
					}
					k.close();
				}
			}
		}catch(Exception e1){}
	}
	
	
	/**
	 * The Save State Function
	 * 
	 * Allows This Plugin To Re-Issue
	 * New Tickets To Powers Who Purchased
	 * Them Should The Server Go Down For Any
	 * Reason.
	 * 
	 * The Only Draw-Back Is That, We Will Not
	 * Be Able To Retain Exact Numbers, But
	 * Users Will Get The Exact Number Of Tickets
	 * That They Paid For.
	 * 
	 */
	public void saveState(Player p){
		try{
			File file = new File(main.getPaths().get(2)+p.getName()+"/state.txt");
			TPlayer t = main.findPlayer(p,0);
			if(!file.exists()){
				file = new File(main.getPaths().get(2)+p.getName());
				file.mkdirs();
				file = new File(main.getPaths().get(2)+p.getName()+"/state.txt");
				file.createNewFile();
			}
			Writer w= new OutputStreamWriter(new FileOutputStream(file));
			w.write(t.getName()+"\n");
			w.write(t.getUUID()+"\n");
			for(Ticket tt:t.getTickets(false)){
				String nums = "";
				for(int i:tt.getWhiteBlocks()){
					if(i<10){nums+="0";}
					nums+=i+" ";
				}
				w.write(nums+tt.getPowerBlock()+"\n");
			}
			w.close();	
		}catch(Exception e1){}
	}
	
	
	/**
	 * The Delete States Function:
	 * 
	 * Clears Out All Ticket Save States When
	 * The Winning Numbers Are Drawn.
	 * 
	 */
	public void clearStates(){
		dlStThrd dl = new dlStThrd(main);
		dl.start();
	}
}
