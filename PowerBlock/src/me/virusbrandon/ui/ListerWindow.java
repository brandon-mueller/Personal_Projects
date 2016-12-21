package me.virusbrandon.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.virusbrandon.powerblock.Main;
import me.virusbrandon.powerblock.TPlayer;
import me.virusbrandon.powerblock.Ticket;

public class ListerWindow {
	private TPlayer player;
	private int ticket = 0;
	private Main main;
	private Inventory lister;
	private String bl=ChatColor.BLACK+"",gr=ChatColor.GREEN+"",bo=ChatColor.BOLD+"",kk=ChatColor.MAGIC+"",wh=ChatColor.WHITE+"",re=ChatColor.RED+"",lp=ChatColor.LIGHT_PURPLE+"",ga=ChatColor.GRAY+"",ye=ChatColor.YELLOW+"",aq=ChatColor.AQUA+"",go=ChatColor.GOLD+"";
	private int[] sideUp = new int[]{0,9,8,17};
	private int[] sideMid = new int[]{18,27,26,35};
	private int[] sideDwn = new int[]{36,45,44,53};
	private int[] fstRow = new int[]{1,2,3,4,5,7};
	private int[] rows = new int[]{10,19,28,37,46};
	private int[] pb = new int[]{16,25,34,43,52};
	private boolean sBW = false;
	
	/**
	 * The Lister Winder Constructor:
	 * 
	 * @param player
	 */
	public ListerWindow(TPlayer player,Main main){
		this.player = player;
		this.main = main;
		lister = Bukkit.createInventory(null, 54,main.pr());
		refresh();
	}
	
	
	/**
	 * The Get Window Function:
	 * 
	 * Returns This Window
	 * For Viewing.
	 * 
	 */
	public Sound getWin(Player p){
		p.openInventory(lister);
		return main.getSounds()[0];
	}
	
	
	/**
	 * The Scroll Function (Recursive):
	 * 
	 * Scrolls The GUI Up Or Down.
	 */
	public Sound scroll(int dir){
		if((dir<0)&&(ticket>0)){
			ticket--;dir++;scroll(dir);
			refresh();
		} else if ((dir>0)&&((ticket+5)<player.getTickets(sBW).size())){
			ticket++;dir--;scroll(dir);
			refresh();
		}
		return main.getSounds()[0];
	}
	
	
	/**
	 * The Refresh Function:
	 * 
	 * Re-Draws The UI To Reflect
	 * The Change Of State Brought
	 * About By The Scroll Function
	 * Above.
	 * 
	 */
	@SuppressWarnings("deprecation")
	public Sound refresh(){
		for(int x=0;x<54;x++){
			main.gF().setUpItem(lister,x,new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLACK.getData()),wh+bo+"You "+(player.getNumTickets()>0?"Have "+player.getNumTickets()+" Ticket"+(player.getNumTickets()==1?"":"s"):"Don't Have Any ACTIVE Tickets"),"",gr+bo+"Did You Know?",gr+"That You Can Click Here, To",gr+"Sort Your Tickets By Either",gr+"Winning Tickets Only Or All",gr+"Available Tickets.","",lp+bo+"Current Sort Selection:",aq+"Show: "+(sBW?"Winners Only":"All Tickets"),bl+"#SORT",bl+"#UI");
		}
		for(int i:sideUp){
			if(ticket>0){
				main.gF().setUpItem(lister,i,new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.LIME.getData()),wh+kk+"|"+gr+bo+"  Scroll Up  "+wh+kk+"|","",tH(1),tH(2),tH(3),tH(4),"",tH2(1),tH2(2),bl+"#UP",bl+"#UI");
			} else {
				main.gF().setUpItem(lister,i,new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.RED.getData()),wh+kk+"|"+re+bo+"  Can't Scroll  "+wh+kk+"|","",tH(1),tH(2),tH(3),tH(4),"",tH2(1),tH2(2),bl+"#UI");
			}
		}
		for(int i:sideMid){
			main.gF().setUpItem(lister,i,new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.YELLOW.getData()),wh+kk+"|"+ye+bo+"  Back To Results Window  "+wh+kk+"|",bl+"#BACK",bl+"#UI");
		}
		for(int i:sideDwn){
			if((ticket+4)<(player.getTickets(sBW).size()-1)){
				main.gF().setUpItem(lister,i,new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.LIME.getData()),wh+kk+"|"+gr+bo+"  Scroll Down  "+wh+kk+"|","",tH(1),tH(2),tH(3),tH(4),"",tH2(1),tH2(2),bl+"#DOWN",bl+"#UI");
			} else {
				main.gF().setUpItem(lister,i,new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.RED.getData()),wh+kk+"|"+re+bo+"  Can't Scroll  "+wh+kk+"|","",tH(1),tH(2),tH(3),tH(4),"",tH2(1),tH2(2),bl+"#UI");
			}
		}
		for(int i:fstRow){
			if((main.getWTicket()!=null)&(!main.getRes().isDrawing())){
				if(i<7){
					main.gF().setUpItem(lister,i,new ItemStack(Material.WOOL,main.getWTicket().getWhiteBlocks().get(i-1),DyeColor.GRAY.getData()),wh+"White Block #"+wh+bo+main.getWTicket().getWhiteBlocks().get(i-1),ga+"Our Numbers","",aq+main.getWTicket().getDrawDate(),bl+"#UI");
				} else {
					main.gF().setUpItem(lister,7,new ItemStack(Material.WOOL,main.getWTicket().getPowerBlock(),DyeColor.PINK.getData()),re+"Power Block #"+re+bo+main.getWTicket().getPowerBlock(),ga+"Our Numbers","",aq+main.getWTicket().getDrawDate(),bl+"#UI");
				}
			} else {
				main.gF().setUpItem(lister,i,new ItemStack(Material.WOOL,0,DyeColor.GRAY.getData()),wh+bo+"???",re+"There Hasn't Been A",re+"Drawing Yet.","",lp+"Once A Drawing Is",lp+"Conducted, You'll Start",lp+"Seeing Those Results",lp+"Right Here",bl+"#UI");	
			}
		}
		try{
			int z = ticket;
			for(int i:rows){
				if((z+1)<=player.getTickets(sBW).size()){
					for(int x=i,y=0;x<(i+5);x++,y++){
						Ticket t = player.getTickets(sBW).get(z);
						main.gF().setUpItem(lister,x,new ItemStack(Material.WOOL,t.getWhiteBlocks().get(y),t.isOld()?DyeColor.BLACK.getData():DyeColor.WHITE.getData()),wh+"White Block #"+wh+bo+t.getWhiteBlocks().get(y),gr+"Your Numbers","",lp+"Ticket #"+(z+1)+" Of "+player.getTickets(sBW).size(),aq+t.getDrawDate(),"",re+bo+"Winnings: "+re+((t.isOld())?player.getTickets(sBW).get(z).getWinnings():"Unknown, Awaiting Next Drawing..."),go+"Ticket ID: "+player.getTickets(sBW).get(z).getID(),bl+"#UI");
					}
					z++;
				} else {
					break;
				}
			}
			z = ticket;
			for(int x=0;x<5;x++,z++){
				if((player.getTickets(sBW).size()-1)>=z){
					Ticket t = player.getTickets(sBW).get(z);
					main.gF().setUpItem(lister,pb[x],new ItemStack(Material.WOOL,t.getPowerBlock(),t.isOld()?DyeColor.BLACK.getData():DyeColor.RED.getData()),re+"Power Block #"+re+bo+t.getPowerBlock(),gr+"Your Numbers","",lp+"Ticket #"+(z+1)+" Of "+player.getTickets(sBW).size(),aq+t.getDrawDate(),"",re+bo+"Winnings: "+re+((player.getTickets(sBW).get(z).isOld())?t.getWinnings():"Unknown, Awaiting Next Drawing..."),go+"Ticket ID: "+t.getID(),bl+"#UI");
				} else {
					break;
				}
			}	
		}catch(Exception e1){}
		return Sound.UI_BUTTON_CLICK;
	}
	
	/**
	 * The Toggle Sort View Function:
	 * 
	 * Changes The Sort Option For These
	 * Tickets Between Show All To Show
	 * Winning Tickets Only.
	 * 
	 */
	public Sound toggleSortOption(){
		this.sBW=(sBW?false:true);
		return resetTicket().refresh();
	}
	
	/**
	 * The Reset Ticket Function:
	 * 
	 * Resets The Ticket Index
	 * To Zero When Called
	 * 
	 */
	public ListerWindow resetTicket(){
		this.ticket = 0;
		return this;
	}
	
	/**
	 * Private Ternary Helper Text Function:
	 * 
	 */
	private String tH(int i){
		return (i==1) ? lp+"Left-Click = Scroll 1 Ticket":(i==2) ? lp+"Right-Click = Scroll 5 Tickets":(i==3) ? lp+"Shift-Left-Click = Scroll 20 Tickets":(i==4) ? lp+"Shift-Right-Click = Scroll 100 Tickets":re+"seeing this? Check Your Code Genius";
	}
	
	/**
	 * Private Ternary Helper Text Function #2:
	 * 
	 */
	private String tH2(int ii){
		if(ii == 1){
			return aq+bo+"Showing"+(sBW?" Winning":"")+" Tickets:";
		} else {
			int i = player.getTickets(sBW).size();
			String s = aq+"("+(ticket+1)+" thru "+(ticket+5)+") Of ";
			return (i==0) ? s+"None":s+i+"";
		}
		
	}
}
