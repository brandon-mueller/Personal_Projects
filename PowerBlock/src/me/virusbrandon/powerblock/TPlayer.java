package me.virusbrandon.powerblock;

import java.util.ArrayList;

import me.virusbrandon.localapis.SimpleScoreboard;
import me.virusbrandon.ui.ListerWindow;
import me.virusbrandon.ui.NumberChooser;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TPlayer {
	private Main main;
	private String name;
	private String uuid;
	private ArrayList<Ticket> tickets = new ArrayList<>();
	private ArrayList<Ticket> winningTickets = new ArrayList<>();
	private ListerWindow lis;
	private NumberChooser chooser;
	private SimpleScoreboard sss;
	private String bo=ChatColor.BOLD+"",re=ChatColor.RED+"",gr=ChatColor.GREEN+"",go=ChatColor.GOLD+"",st=ChatColor.STRIKETHROUGH+"",wh=ChatColor.WHITE+"",ye=ChatColor.YELLOW+"";
	private boolean ready = false;
	private double totWinnings = 0.0;
	
	/**
	 * The TPlayer Constructor:
	 * 
	 * Well.. Couldn't Use Player So
	 * This Will Suffice :P
	 * 
	 * @param p
	 * @param main
	 * 
	 */
	public TPlayer(Player p, Main main){
		this.name = p.getName();
		this.uuid = p.getUniqueId().toString();
		this.main = main;
		lis = new ListerWindow(this,main);
		sss = new SimpleScoreboard(main.pr());
	}
	
	/**
	 * The TPlayer Constructor
	 * 
	 * Alternate Constructor
	 * Used By Save States
	 * 
	 */
	public TPlayer(String name, String uuid, Main main){
		this.name = name;
		this.uuid = uuid;
		this.main = main;
		lis = new ListerWindow(this,main);
		sss = new SimpleScoreboard(main.pr());
	}
	

	
	/**
	 * The Add Ticket Function (Recursive):
	 * 
	 * Instead of using a loop a recursive
	 * solution was used which is faster.
	 * 
	 * @param qty
	 * 
	 */
	public void addTicket(int qty){
		tickets.add(new Ticket(main,this,tickets.size()));
		qty--;
		if(qty>0){
			addTicket(qty);
		}
		lis.refresh();
	}
	
	
	/**
	 * The Add Winning Ticket Function:
	 * 
	 * Called By The Ticket Class So The User
	 * Is Able To Sort Their Tickets By Winning
	 * Tickets Instead Of Being Forced To Sift
	 * Through All Of Their Tickets To Find The
	 * Winning Tickets
	 * 
	 */
	public void addWinningTicket(Ticket ticket){
		winningTickets.add(ticket);
		totWinnings+=ticket.getWinningsAmt();
	}
	
	
	/**
	 * The Get Total Winnings Function:
	 * 
	 */
	public double getTotWinnings(){
		return totWinnings;
	}
	
	
	/**
	 * The Add Custom Ticket Function:
	 * 
	 * Takes Input The Numbers Chosen
	 * By The Player And Crates A New
	 * Ticket For Them.
	 * 
	 */
	public void addCustomTicket(int[] numbers){
		tickets.add(new Ticket(main,this,numbers,tickets.size()));
		lis.refresh();
	}
	
	
	/**
	 * The Show Chooser Function:
	 * 
	 * Returns The Number Chooser
	 * Winder And Starts The Process
	 * Where The Player Picks Their
	 * Own Number For Their Ticket.
	 * 
	 */
	public Sound showChooser(Player p){
		chooser = new NumberChooser(main,this);
		p.openInventory(chooser.getWin());
		return main.getSounds()[0];
	}
	
	
	/**
	 * The Get Chooser Function:
	 * 
	 * Returns The Whole Chooser Object
	 * So We Have Access To The Chosen
	 * Numbers Later.
	 * 
	 */
	public NumberChooser getChooser(){
		return chooser;
	}
	
	public void setSBReady(boolean ready){
		this.ready = ready;
	}
	
	public void updSB(Player p){
		if(ready){
			sss.reset();
			sss = new SimpleScoreboard(main.pr());
			sss.add(wh+bo+st+"=======================");
			sss.blankLine();
			sss.add(re+bo+"Hi There, "+re+p.getName());
			sss.add(go+bo+"Jackpot: "+gr+bo+"$"+re+main.getPot());
			sss.blankLine();
			sss.add(ye+bo+"Credits: "+gr+bo+"$"+re+main.getBank().process(p.getName(), 0, false).balance());
			sss.add(gr+bo+"Tickets: "+gr+bo+"T"+re+main.findPlayer(p,0).getNumTickets());
			sss.blankLine();
			sss.add(wh+bo+"Next Drawing In:");
			sss.add(main.time(main.getTimeLeft()<=60?re:gr));
			sss.blankLine();
			sss.add(wh+bo+st+"=======================");
			sss.build();
			sss.send(p);
		}
	}
	
	public void remSB(Player p){
		sss.reset();
		sss = new SimpleScoreboard(main.pr());
		sss.send(p);
	}
	
	
	
	/**
	 * The Get Tickets Function:
	 * 
	 * We'll Use This To Retrieve All Tickets
	 * Purchased By This Player So We Can Check
	 * To See If They Are A Winner.
	 * 
	 * @return
	 * 
	 */
	public ArrayList<Ticket> getTickets(boolean sortByWinners){
		return ((sortByWinners)?winningTickets:tickets);
	}
	
	
	/**
	 * Set Winning Tickets Function:
	 * 
	 */
	public void setWinningTickets(ArrayList<Ticket> winners){
		this.winningTickets = winners;
	}
	
	
	/**
	 * Set All Tickets Old Function:
	 * 
	 */
	public void setAllTicketsOld(Ticket wTicket){
		for(Ticket tt:tickets){
			tt.setOld();
			tt.setDrawDate(wTicket.getDate());
			getWin().refresh();
		}
	}
	
	
	
	/**
	 * The Get Ticket Amount Function:
	 * 
	 * Returns The Number Of Non-Drawn
	 * Tickets.
	 * 
	 */
	public int getNumTickets(){
		try{
			if(tickets.get(0).isOld()){
				return 0;
			} else {
				return tickets.size();
			}
		}catch(Exception e1){
			return 0;
		}
		
	}
	
	
	/**
	 * The Clear Tickets Function:
	 * 
	 * Once a Drawing Has Finished
	 * A Players' Tickets Are No Longer Any
	 * Good. To Participate In The Next Drawing,
	 * Players Will Have To Purchase More.
	 * 
	 */
	public void clearTickets(){
		tickets.clear();
		winningTickets.clear();
		this.totWinnings = 0.0;
	}
	
	
	/**
	 * The Get Name Function:
	 * 
	 * Retrieves The Name Of The
	 * Owner Of This Ticket
	 * 
	 */
	public String getName(){
		return name;
	}
	
	
	/**
	 * The Get UUID Function:
	 * 
	 * Retrieves The Unique ID Of
	 * The Owner Of This Ticket
	 * 
	 */
	public String getUUID(){
		return uuid;
	}
	
	
	/**
	 * The Get Win Function:
	 * 
	 * ..
	 */
	public ListerWindow getWin(){
		return lis;
	}
}
