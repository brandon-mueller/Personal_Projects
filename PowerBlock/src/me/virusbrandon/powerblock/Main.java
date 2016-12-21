package me.virusbrandon.powerblock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.virusbrandon.localapis.ActionBarAPI;
import me.virusbrandon.localapis.GUIFactory;
import me.virusbrandon.localapis.SimpleScoreboard;
import me.virusbrandon.localapis.TitleAPI;
import me.virusbrandon.threads.UIThread;
import me.virusbrandon.ui.ResultWindow;
import me.virusbrandon.util.Auth;
import me.virusbrandon.util.Bank;
import me.virusbrandon.util.Result;
import me.virusbrandon.util.Sorter;
import me.virusbrandon.util.StateManager;
import me.virusbrandon.util.UpdateDetector;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	/* Primitive Types */
	private boolean actionBarEnabled = true;
	private boolean autoRestart = true;
	private boolean recordDrawings = false;
	private float percent2Pot = 1.00F;
	private float pot = 0;
	private float startingPot = 0;
	private int hWhiteBlock = 25;
	private int hPowerBlock = 25;
	private int maxTickets = 25;
	private int secondsTilDraw = 300;
	private int drawingsTilRestart;
	private int temp = 0,temp2 = 0;
	private int ticketsPurchased = 0;
	private int timeLeft = 300;
	private long pricePerTicket = 25000L;
	private Sound[] sounds = new Sound[]{Sound.UI_BUTTON_CLICK};
	private String wh=ChatColor.WHITE+"",ye=ChatColor.YELLOW+"",re=ChatColor.RED+"",gr=ChatColor.GREEN+"",bo=ChatColor.BOLD+"",lp=ChatColor.LIGHT_PURPLE+"",bu=ChatColor.BLUE+"",bl=ChatColor.BLACK+"",aq=ChatColor.AQUA+"",ul=ChatColor.UNDERLINE+"",kk=ChatColor.MAGIC+"";
	private String website = " ";
	private String[] stf = new String[]{"w","d","b"};
	//TODO private dbManager db;
	
	/* Local Objects */
	private ActionBarAPI bar;
	private TitleAPI tit;
	private Auth auth;
	private Bank bank;
	private GUIFactory g;
	private ResultWindow res;
	private StateManager st;
	private Ticket wTicket = null;
	private SimpleScoreboard sss;
	private Sorter sort = new Sorter();
	private UpdateDetector det;
	
	/* Library Objects */
	private ArrayList<Double> prizes = new ArrayList<>();
	private ArrayList<String> paths = new ArrayList<>();
	private ArrayList<TPlayer> players = new ArrayList<>();
	private DecimalFormat df = new DecimalFormat("###,###,###.00");
	private YamlConfiguration Banks;
	private YamlConfiguration Config;

	
	/**
	 * The Player Join Event:
	 * 
	 * Alerts The Plugin Of A Newly Joined Player
	 * And Proceeds With Gathering Information
	 * About That Player So We Can Form A Profile
	 * For Them.
	 * 
	 * This Event Is Also Used To Direct Useful Information
	 * About The Server Towards That Player.
	 * 
	 * @param e
	 */
	@EventHandler
	public void join(PlayerJoinEvent e){
		e.setJoinMessage(null);
		Player p = e.getPlayer();
		initItems(p);
		p.sendMessage(pr()+aq+"We're Giving Away "+lp+bo+"$"+df.format(getPot()));
		p.sendMessage(pr()+aq+"Click Your Glass - Try To Win The Jackpot!");
		auth.checkAuth(p.getName());
		new JAnimate(this,p);
		sss = new SimpleScoreboard(re+"Just A Moment...");
		sss.add(re+".");
		sss.build();
		sss.send(p);
	}
	
	/**
	 * Quit Event
	 * 
	 */
	@EventHandler
	public void quit(PlayerQuitEvent e){
		findPlayer(e.getPlayer(),0).setSBReady(false);
		e.setQuitMessage(null);
	}
	
	/**
	 * The Player Death Event
	 * 
	 * Player Player Dies, And We Are Supposed
	 * To Feel Sorry For Them.
	 * 
	 */
	@EventHandler
	public void die(PlayerDeathEvent e){
		e.setDeathMessage(null);
	}
	
	
	/**
	 * The Player Respawn Event:
	 * 
	 * Just In Case A Players Wants To Try To Break
	 * Stuff And Get Struck By Lightning, We'll Be Nice
	 * And Give Them Their Menu Items Back.. Because We're
	 * Just Awesome Like That. :)
	 * 
	 * @param e
	 */
	@EventHandler
	public void respawn(PlayerRespawnEvent e){
		Player p = e.getPlayer();
		initItems(p);
	}
	
	/**
	 * The Inventory Click Event:
	 * 
	 * Detects In Interaction With One Of This
	 * Plugin's User Interfaces And Responds
	 * Accordingly Based On The Button That Was
	 * Clicked.
	 * 
	 * @param e
	 */
	@EventHandler
	public void click(InventoryClickEvent e){
		Player p = (Player)e.getWhoClicked();
		try{
			List<String> l = e.getCurrentItem().getItemMeta().getLore();
			e.setCancelled(checkForUI(e.getCurrentItem())); 
			String s = (l.get(l.size()-2));
			p.playSound(p.getLocation(),s.equalsIgnoreCase(bl+"#CLOSE")?clsInv(p):s.equalsIgnoreCase(bl+"#VIEW")?findPlayer(p,0).getWin().getWin(p):s.equalsIgnoreCase(bl+"#BACK")?res.getWin(p):s.equalsIgnoreCase(bl+"#UP")?findPlayer(p,0).getWin().scroll(clickVal(e)*-1):s.equalsIgnoreCase(bl+"#DOWN")?findPlayer(p,0).getWin().scroll(clickVal(e)):s.equalsIgnoreCase(bl+"#BUY")?buyTickets(clickVal(e),p,findPlayer(p,0),false):s.equalsIgnoreCase(bl+"#NUM")?findPlayer(p,0).getChooser().addNum(e.getCurrentItem().getAmount()+""):s.equalsIgnoreCase(bl+"#CLEAR")?findPlayer(p,0).getChooser().clearCurrent():s.equalsIgnoreCase(bl+"#NEXT")?findPlayer(p,0).getChooser().accept(p):s.equalsIgnoreCase(bl+"#PICK")?findPlayer(p,0).showChooser(p):(s.equalsIgnoreCase(bl+"#NOW")?(auth.checkAuth(p.getName())==true?advDraw():null):(s.equalsIgnoreCase(bl+"#SORT")?findPlayer(p,0).getWin().toggleSortOption():null)),.05f,2);
		}catch(Exception e1){}
	}
	
	/**
	 * The Server Ping Event
	 * 
	 */
	@EventHandler
	public void ping(ServerListPingEvent e){
		e.setMotd(randColor()+bo+"       The PowerBlock Network!\n"+pr()+pot());
		e.setMaxPlayers(1000);
	}
	
	
	/**
	 * The Block Place Event:
	 * 
	 * Prevents Players From Placing Menu Items
	 * On The Ground That Are Supposed To Be Used
	 * To Control Various Functions Of This Software.
	 * 
	 * @param e
	 */
	@EventHandler
	public void place(BlockPlaceEvent e){
		Player p = e.getPlayer();
		if(!auth.checkAuth(p.getName())){
			e.setCancelled(true);
		} else {
			e.setCancelled(checkForUI(e.getItemInHand()));
		}
	}
	
	/**
	 * The Block Break Event
	 * 
	 * Prevents Unauthorized Players From Breaking Blocks
	 * 
	 */
	@EventHandler
	public void blockBreak(BlockBreakEvent e){
		Player p = e.getPlayer();
		if(!auth.checkAuth(p.getName())){p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_IMPACT, 1, 1);p.setVelocity(p.getLocation().getDirection().multiply(-10));p.getLocation().getWorld().strikeLightning(p.getLocation());p.sendMessage(pr()+ye+bo+"BOOM! "+re+"Don't Break Stuff!");e.setCancelled(true);}
	}
	
	/**
	 * The Item Drop Event:
	 * 
	 * Prevents Players From Placing Menu Items
	 * On The Ground That Are Supposed To Be Used
	 * To Control Various Functions Of This Software.
	 * 
	 */
	@EventHandler
	public void drop(PlayerDropItemEvent e){
		Player p = e.getPlayer();
		if(!auth.checkAuth(p.getName())){
			e.setCancelled(true);
		} else {
			e.setCancelled(checkForUI(e.getItemDrop().getItemStack()));
		}	}
	
	
	/**
	 * The Player Interact Event:
	 * 
	 * Detects When Players Interact
	 * With An Item.
	 * 
	 */
	@EventHandler
	public void interact(PlayerInteractEvent e){
		if(checkForUI(e.getItem())){
			res.getWin(e.getPlayer());
		}
	}
	
	/**
	 * Command Preprocess Event
	 * 
	 */
	@EventHandler
	public void cmd(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		if(!auth.checkAuth(p.getName())){p.sendMessage(unk());e.setCancelled(true);}
	}
	
	
	/**
	 * The Enable Function:
	 * 
	 * This Is The Code That Is Run When The Software
	 * Is Started. Necessary To Initial Sections Of The
	 * Program Before Latter Code Can Be Used.
	 * 
	 */
	@Override
	public void onEnable(){
		loadSettings();
		bc(pr()+ye+"Has Been Started Successfully!");
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		new UIThread(this).start();
		for(Player p:Bukkit.getOnlinePlayers()){
			findPlayer(p,0).setSBReady(true);
		}
	}
	
	
	/**
	 * The Disable Function:
	 * 
	 * This Is The Code That Must Be Run Usually
	 * To Make Sure That Sensitive Data Is Preserved
	 * Properly For The Next Time This Program Runs.
	 * 
	 */
	@Override
	public void onDisable(){
		for(Player p:Bukkit.getOnlinePlayers()){
			String title = p.getOpenInventory().getTopInventory().getTitle();
			if(title.equalsIgnoreCase(pr())){
				p.closeInventory();
				p.sendMessage(pr()+ye+"Has Been Stopped");
			}
			findPlayer(p,0).remSB(p);
		}
		saveJackpot();
		det.cancel();
	}
	
	
	/**
	 * The On Command Function:
	 * 
	 * This Section Interprets The Commands Sent
	 * In By A User. Contains The Bank Command...
	 * Does Bank... Stuff, Yeah, Let's Go With That.
	 * 
	 */
	@Override
	public boolean onCommand(CommandSender se, Command c, String lbl, String[] args){
		try{
			if(se!=Bukkit.getConsoleSender()){if(!auth.checkAuth(se.getName())){se.sendMessage(unk());return true;}}
			if(lbl.equalsIgnoreCase("Bank")){
				Result res =(((args[0].equalsIgnoreCase(stf[0]))?(Double.parseDouble(args[2])<=0)?syntx(se):bank.process(args[1],(Double.parseDouble(args[2]))*-1,true):((args[0].equalsIgnoreCase(stf[1]))?(Double.parseDouble(args[2])<=0)?syntx(se):bank.process(args[1],Double.parseDouble(args[2]),true):(args[0].equalsIgnoreCase(stf[2]))?bank.process(args[1],0,false):null)));
				if(res!=null){
					if(res.balance()>=0){
						if(!se.getName().equalsIgnoreCase(res.name())){se.sendMessage(pr()+res.toString());}
						try{
							Bukkit.getPlayer(res.name()).sendMessage(pr()+res.toString());
							findPlayer(Bukkit.getPlayer(res.name()),0).updSB(Bukkit.getPlayer(res.name()));
						}catch(Exception e1){}
					} else {
						se.sendMessage(txt(0));
					}
				} else {
					syntx(se);
				}
			}else {
				se.sendMessage(unk());
			}
		} catch(Exception e1){
			syntx(se);
		}
		return true;
	}
	
	
	/**
	 * The Get HWhite Block Function:
	 * 
	 * Returns The Highest Number That
	 * A Random White Block Number Can Be.
	 * 
	 * @return
	 * 
	 */
	public int getHWhiteBlock(){
		return hWhiteBlock;
	}
	
	
	/**
	 * The Get HPower Block Function:
	 * 
	 * Returns The Highest Number That
	 * A Random Power Block Number Can Be.
	 * 
	 * @return
	 * 
	 */
	public int getHPowerBlock(){
		return hPowerBlock;
	}
	
	
	/**
	 * The Get Max Tickets Function:
	 * 
	 * Returns The Maximum Number Of Tickets
	 * That Any Player May Purchase For A
	 * Single Drawing.
	 * 
	 * @return
	 * 
	 */
	public int getMaxTickets(){
		return maxTickets;
	}
	
	
	/**
	 * The Get Tickets Purchased Function:
	 * 
	 * Returns The Number Of Tickets Purchased
	 * 
	 */
	public int getTicketsPurchased(){
		return ticketsPurchased;
	}
	
	
	/**
	 * Set Tickets Purchased Function
	 * 
	 */
	public void setTicketsPurchased(int tickets){
		this.ticketsPurchased = tickets;
	}
	
	
	/**
	 * The Get Winning Ticket Function:
	 * 
	 * Returns Information About The Winning
	 * Ticket That Players Tickets Must Match
	 * To Win The Jackpot.
	 * 
	 */
	public Ticket getWTicket(){
		return wTicket;
	}
	
	
	/**
	 * The Get Pot Function:
	 * 
	 * Returns The Currently Value Of
	 * The Jackpot
	 * 
	 */
	public float getPot(){
		return Float.parseFloat(new DecimalFormat("########.##").format(pot));
	}
	
	
	/**
	 * The Get Price Per Ticket Function:
	 * 
	 * Returns The Price For Each Ticket
	 * That A Player Buyers
	 * 
	 */
	public long getPricePerTicket(){
		return pricePerTicket;
	}
	
	
	/**
	 * The Get Res Function:
	 * 
	 * Returns The Results Window
	 * For Viewing.
	 * 
	 */
	public ResultWindow getRes(){
		return res;
	}
	
	
	/**
	 * Plugin Prefix
	 * @return
	 * 
	 */
	public String pr(){
		return bu+"["+lp+"Power Block"+bu+"] "+wh;
	}
	
	
	/**
	 * Next Draw Message
	 * 
	 */
	public String nd(){
		if((timeLeft>0)&(!res.isDrawing())){
			return lp+"Next Draw In: "+time((timeLeft<=60)?re:gr);
		} else {
			return lp+"Next Draw: "+"NOW!";
		}
	}
	
	
	/**
	 * Message BroadCast Function:
	 * 
	 */
	private void bc(String message){
		Bukkit.broadcastMessage(message);
	}
	
	
	/**
	 * Get Decimal Format Function:
	 * 
	 * Returns The Decimal Format Used
	 * Instead Of Creating Another Indentical
	 * Object In Another Class.
	 * 
	 */
	public DecimalFormat getDf(){
		return df;
	}
	
	/**
	 * The Time Function:
	 * 
	 * Returns A Formatted Representation
	 * Of The Time Remaining Before The Next
	 * Drawing.
	 * 
	 */
	public String time(String c){
		String time = "";
		if(timeLeft/3600<10){time+=c+bo+"0";}
		time+=c+bo+(timeLeft/3600)+c+"h ";
		if(((timeLeft%3600)/60)<10){time+=c+bo+"0";}
		time+=c+bo+((timeLeft%3600)/60)+c+"m ";
		if((timeLeft%60)<10){time+=c+bo+"0";}
		time+=c+bo+(timeLeft%60)+c+"s ";
		return time;
	}
	
	/**
	 * The Time Left Function:
	 * 
	 * Returns The Numeric Amount
	 * Of Time Left Til The Next Drawing.
	 * 
	 */
	public int getTimeLeft(){
		return timeLeft;
	}
	
	
	/**
	 * The Tick Function:
	 * 
	 * Advances The Countdown Until It's
	 * Time To Do Another Drawing.
	 * 
	 */
	protected int tick(){
		if(!res.isDrawing()){
			timeLeft--;
			ann();
			temp++;temp=temp%2;
			if(autoRestart){
				if((secondsTilDraw-timeLeft>29)&(drawingsTilRestart<=0)){
					if((secondsTilDraw-timeLeft>30)){
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Restart");
					} else {
						for(Player p:Bukkit.getOnlinePlayers()){
							p.kickPlayer(bu+bo+paths.get(4));
						}
					}
				}
			}
			if(timeLeft%60 == 0){bc(pr()+nd());bc(pr()+pot());}
			if(timeLeft<0){
				draw();
				timeLeft=secondsTilDraw;
			} else if(timeLeft<6){
				if(timeLeft == 5){
					tit.sendTitle(Bukkit.getOnlinePlayers(),200,200,200,re+bo+"Next Drawing In:",null);
				}
				tit.sendTitle(Bukkit.getOnlinePlayers(),0,20,10,null,ye+bo+timeLeft+"");
			}
			tit.sendTabTitle(Bukkit.getOnlinePlayers(),randColor()+bo+"The PowerBlock Network!",pot());
		}
		return 0;
	}
	
	/**
	 * Advance Draw Function:
	 * 
	 */
	private Sound advDraw(){
		timeLeft = 6;
		return null;
	}
	
	
	/**
	 * Get Temp Var
	 * 
	 */
	public int getT(){
		return temp;
	}
	
	/**
	 * Get Bank Function:
	 * 
	 */
	public Bank getBank(){
		return bank;
	}
	
	
	/**
	 * The Get Config Function
	 * 
	 * Returns The Open Config So We Can
	 * Grab Various Information.
	 * 
	 */
	public YamlConfiguration getConfig(){
		return Config;
	}
	
	
	/**
	 * The Pot Function:
	 * 
	 * Returns A String Representation Of
	 * The Current Jackpot!
	 * 
	 */
	private String pot(){
		return aq+"Current Jackpot: "+gr+bo+"$"+df.format(getPot());
	}
	
	
	/**
	 * Get Factory Function:
	 * 
	 * Returns The GUIFactory
	 * Instance.
	 * 
	 */
	public GUIFactory gF(){
		return g;
	}
	
	
	/**
	 * Get Prizes Function:
	 * 
	 */
	public ArrayList<Double> getPrizes(){
		return prizes;
	}
	
	/**
	 * Actionbar Announcer Function:
	 * 
	 */
	private void ann(){
		if(actionBarEnabled){
			if(timeLeft>=0){
				for(Player p:Bukkit.getOnlinePlayers()){
					bar.sendActionBar(p,acbMsg());
					findPlayer(p,0).updSB(p);
				}	
			}
		}
	}
	
	private String acbMsg(){
		temp2=(temp2%20);temp2++;
		return(((temp2%10)<6)?aq+bo+"( Open The "+re+bo+"Power Block"+aq+bo+" Menu, Click Your Glass )":randColor()+bo+"Purchase Credits At: "+website);
	}
	
	/**
	 * The Buy Tickets Function
	 * ...Wonder What This Thing Does :/
	 * 
	 */
	public Sound buyTickets(int amt,Player p,TPlayer t,boolean custom){
		if(amt<1){return Sound.ENTITY_VILLAGER_NO;}
		if((amt+t.getTickets(false).size())<=maxTickets){
			if((pricePerTicket*amt)<=bank.process(p.getName(),0,false).balance()){
				if(t.getTickets(false).size()>0){if(t.getTickets(false).get(0).isOld()){t.clearTickets();}}
				bank.process(p.getName(),((pricePerTicket*amt)*-1),true);
				t.getWin().resetTicket();
				if(!custom){t.addTicket(amt);}else{t.addCustomTicket(t.getChooser().getNumbers());}
				pot+=((pricePerTicket*amt)*percent2Pot);
				p.sendMessage(pr()+gr+"Ticket Purchase: "+re+bo+"-$"+(pricePerTicket*amt));
				p.sendMessage(pr()+gr+"New Balance: "+gr+bo+"$"+(long)bank.process(p.getName(),0,false).balance());
				p.sendMessage(pr()+gr+"You Now Have "+t.getTickets(false).size()+" Ticket"+(t.getTickets(false).size()!=1?"s":"")+"!");
				p.sendMessage(pr()+gr+"You Can Purchase Up To "+(maxTickets-t.getTickets(false).size())+" More!");
				st.saveState(p);
				ticketsPurchased+=amt;
				findPlayer(p,0).updSB(p);
			} else {
				p.sendMessage(pr()+re+bo+"✖ "+re+"Insufficient Funds");
				p.sendMessage(pr()+gr+bo+"✔ "+gr+"Purchase Credits And WIN: "+website);
			}
		} else {
			if(!t.getTickets(false).get(0).isOld()){
				if(t.getTickets(false).size()<maxTickets){
					p.sendMessage(pr()+re+"You Can't Purchase That Many More!");
					p.sendMessage(pr()+gr+"You Can Purchase Up To "+(maxTickets-t.getTickets(false).size())+" More!");
				} else {
					p.sendMessage(pr()+re+"You Can't Purchase Anymore Tickets Right Now!");
				}
			} else {
				t.clearTickets();
				buyTickets(amt,p,t,custom);
			}
		}
		try{
			Banks.save(paths.get(1));
		}catch(Exception e1){}
		return sounds[0];
	}
	
	
	/**
	 * The Draw Function:
	 * 
	 * Draws The Numbers For The Winning Ticket
	 * And Checks All Players' Tickets And Broadcasts
	 * A Winner If There Is One.
	 * 
	 * If There Is No Winner, The Jackpot Will Continue
	 * To Grow Until A Winner Is Chosen.
	 * 
	 */
	private Sound draw(){
		if(ticketsPurchased>0){
			this.wTicket = new Ticket(this,null,0);
			res.setIsDrawing(true);
			for(Player p:Bukkit.getOnlinePlayers()){
				try{
					return((p.getOpenInventory().getTopInventory().getItem(0).getItemMeta().getDisplayName().equalsIgnoreCase(wh+kk+"|"+re+bo+"  Back To Results Window  "+wh+kk+"|"))?res.getWin(p):null);
				} catch(Exception e1){}
			}
		} else {
			bc(pr()+re+"No One Bought Any Tickets This Time ;_;");
		}
		return null;
	}
	
	public Sound drawFinish(){
		wTicket.setDrawDate(new Date());
		/*TODO COMMENT UNTIL WEBSITE IS READY!! db.updateNumbers(wTicket);*/
		ArrayList<TPlayer> winners = new ArrayList<>();
		winners = drawHelper(winners,0);	
		if(winners.size()>0){
			String names="";
			for(int x=0;x<winners.size();x++){
				if((winners.size()>1)&(x==(winners.size()-1))){
					names+="and "+winners.get(winners.size()-1).getName();
				} else {
					if(winners.size()>1){
						names+=winners.get(x).getName()+", ";
					} else {
						names+=winners.get(x).getName();
					}
				}
			}
			bc(pr()+gr+"A Big Congratulations To: "+names);
			bc(pr()+gr+"For Hitting The Jackpot For: "+gr+bo+"$"+pot);
			if(winners.size()>1){
				bc(pr()+re+"Since There Is More Than One Winner");
				bc(pr()+re+"The Pot Is Being Split Evenly Across "+winners.size()+re+" Ways");
			}
			for(TPlayer win:winners){
				bank.process(win.getName(),(pot/winners.size()),true);
				try{Bukkit.getPlayer(win.getName()).sendMessage(pr()+gr+bo+"$"+df.format((pot/winners.size()))+gr+" Has Been Deposited To Your Account");/*man.showScoreboard(Bukkit.getPlayer(win.getName()));*/}catch(Exception e1){}
			}
			this.pot = ((pot%winners.size())+startingPot);
		} else {
			bc(pr()+re+"No Jackpot This Round, Darn.");
			bc(pr()+gr+"The Jackpot Will Continue To Increase.");
		}
		this.ticketsPurchased=0;
		st.clearStates();	
		bc(pr()+lp+"Click The Glass - View Results");
		bASB();
		if(autoRestart){
			drawingsTilRestart--;
			if(drawingsTilRestart<=0){
				bc(pr()+ye+bo+"Is Restarting In 30 Seconds!");
			}
		}
		return null;
	}
	
	
	/**
	 * Draw Helper Function:
	 * 
	 * Recursively Moves Through All Players With
	 * Active Tickets And Check Their Tickets For
	 * Numbers That Match Our Winning Numbers.
	 * 
	 */
	public ArrayList<TPlayer> drawHelper(ArrayList<TPlayer> winners,int i){
		if(i>=players.size()){
			return winners;
		} else {
			for(Ticket tt:players.get(i).getTickets(false)){
				if(!players.get(i).getTickets(false).get(0).isOld()){
					if(wTicket.checkTicket(tt).getPrizeLvl()==6){
						winners.add(players.get(i));
						tt.setWinnings("JACKPOT!");
					} else if(tt.getPrizeLvl()>0){
						bank.process(players.get(i).getName(), prizes.get(tt.getPrizeLvl()-1), true);
						tt.setWinnings(prizes.get(tt.getPrizeLvl()-1)+"");
					}
					tt.setStatus();
				}
			}
			players.get(i).setWinningTickets(sort.sort(players.get(i).getTickets(true)));
			if(recordDrawings){if(players.get(i).getTickets(false).size()>0){if(!players.get(i).getTickets(false).get(0).isOld()){recordDrawing(players.get(i),wTicket);}}}
			players.get(i).setAllTicketsOld(wTicket);
			Player p = Bukkit.getPlayer(players.get(i).getName());
			if(p!=null){
				p.sendMessage(pr()+ye+"Your Non-Jackpot Winnings: "+gr+bo+"$"+players.get(i).getTotWinnings());
			}
			return drawHelper(winners, (i+1));
		}
	}
	
	
	/**
	 * The Find Player Function (Recursive):
	 * 
	 * Determines If A Player Is Registered Into
	 * This Plugin And Sends Back That Player If They
	 * Are Registered. If They Are Not, A New TPlayer
	 * Is Created And Then That Object Is Returned.
	 * 
	 * @param p
	 * @param qty
	 * @return
	 */
	public TPlayer findPlayer(Player p,int i){
		if(players.size()>0){
			if(p.getUniqueId().toString().equalsIgnoreCase(players.get(i).getUUID())){
				return players.get(i);
			} else {
				if((i+1)<players.size()){
					return findPlayer(p,i+1);
				} else {
					players.add(new TPlayer(p,this));
					return players.get(players.size()-1);
				}
			}
		} else {
			players.add(new TPlayer(p,this));
			return players.get(players.size()-1);
		}	
	}
	
	
	/**
	 * The Get Players Function:
	 * 
	 * Returns The List Of All Action
	 * Players And Those Whose Profiles
	 * Were Built Due To Active Tickets
	 * 
	 */
	public ArrayList<TPlayer> getPlayers(){
		return players;
	}
	
	
	/**
	 * The Get Paths Function:
	 * 
	 */
	public ArrayList<String> getPaths(){
		return paths;
	}
	
	
	/**
	 * The Click Value Function:
	 * 
	 * Takes In A Click Event And
	 * Returns A Value Based On That
	 * Click. Used For Code Shortening
	 * Purposes
	 * 
	 */
	private int clickVal(InventoryClickEvent e){
		return ((e.isShiftClick())&&(e.isRightClick()))?100:((e.isShiftClick())&&(e.isLeftClick()))?20:(e.isRightClick())?5:1;
	}
	
	
	/**
	 * Close Inventory Function:
	 * 
	 */
	private Sound clsInv(Player p){
		p.closeInventory();
		return sounds[0];
	}
	
	
	/**
	 * The Record Drawing Function:
	 * 
	 * Writes Out Information About The Drawing
	 * To A File For A Provided Player.
	 * 
	 */
	private void recordDrawing(TPlayer t,Ticket wT){
		File file = new File(paths.get(3)+t.getName()+"/"+wT.getDay());
		file.mkdirs();
		file = new File(paths.get(3)+t.getName()+"/"+wT.getDay()+"/"+wT.getTime()+".txt");
		if(!file.exists()){try {file.createNewFile();}catch(IOException e){}}
		try{
			FileOutputStream outputStream = new FileOutputStream(file);
			Writer writer = new OutputStreamWriter(outputStream);
			writer.write("Power Block Drawing Record For "+t.getName()+"\n");
			writer.write("UUID For "+t.getName()+": "+t.getUUID()+"\n");
			writer.write("Tickets Purchased This Round: "+t.getTickets(false).size()+"\n\n");
			for(Ticket tt:t.getTickets(false)){
				writer.write(tt.simpleToString()+"   WT:  "+wT.simpleToString()+tt.getStatus()+"\n");
			}
			writer.close();
		}catch(Exception e1){}
	}
	
	
	/**
	 * The Load Settings Function:
	 * 
	 * Load In Any Pre-Defined Settings From This
	 * Plugin's Config.Yml.
	 * 
	 * Keep Calm, If The File Isn't There, After You
	 * Started The Plugin, Just Refresh The Document
	 * Window. Then Change The Settings To Your Liking
	 * And Then Reload The Plugin For Your New Settings
	 * To Take Effect.
	 * 
	 */
	private void loadSettings(){
		try{
			File file = new File(getDataFolder(),"Config.yml");
			if(!file.exists()){
				file = new File("plugins/PowerBlock");
				file.mkdir();
				file = new File("plugins/PowerBlock/Config.yml");
				file.createNewFile();
				YamlConfiguration Config = YamlConfiguration.loadConfiguration(file);
				Config.set("WhiteBlocks",64);
				Config.set("PowerBlocks",64);
				Config.set("MaxTickets",10000);
				Config.set("PricePerTicket",1.00);
				Config.set("SecondsBetweenDraws",300); /* Seconds */
				Config.set("LastTimeLeftTilDraw",300);
				Config.set("StartingJackpot",2500); /* Currency */
				Config.set("CurrentJackpot",2500); /* Currency */
				Config.set("PercentageOfSalesIntoJackpot",0.25);
				Config.set("EnableActionBar", true);
				Config.set("RecordDrawings", false);
				Config.set("AutoRestartAfterXDrawings",false);
				Config.set("DrawingsTilRestart",2);
				Config.set("AuthFileSavePath","Plugins/PowerBlock/Auth.yml");
				Config.set("BanksFileSavePath","Plugins/PowerBlock/Banks.yml");
				Config.set("SaveStatesFileSavePath","Plugins/PowerBlock/SaveStates/");
				Config.set("RecordsFileSavePath","Plugins/PowerBlock/Drawing Records/");
				Config.set("ServerRestartKickMessage","Thanks For Playing PowerBlock! Please Log Back On In A Few Seconds. The Server Is Restarting...");
				Config.set("Website", "Insert Website");
				Config.set("DATABASE_ADDRESS", "none");
				Config.set("DATABASE_USER", "none");
				Config.set("DATABASE_PASS", "none");
				for(int x=1;x<6;x++){
					Config.set("Match"+x+"Prize",0.00);
				}
				Config.save(file);
			}
			Config = YamlConfiguration.loadConfiguration(file);
			this.hWhiteBlock = Config.getInt("WhiteBlocks");
			if((hWhiteBlock < 5)|(hWhiteBlock>64)){this.hWhiteBlock = 25;}
			this.hPowerBlock = Config.getInt("PowerBlocks");
			if((hPowerBlock < 1)|(hPowerBlock>64)){this.hPowerBlock = 25;}
			this.maxTickets = Config.getInt("MaxTickets");
			if(maxTickets < 0){this.maxTickets = 25;}
			this.pricePerTicket = Config.getLong("PricePerTicket");
			if(pricePerTicket<1){this.pricePerTicket = 25000L;}
			this.secondsTilDraw = Config.getInt("SecondsBetweenDraws");
			if((secondsTilDraw<30)|(secondsTilDraw>86400)){this.secondsTilDraw = 300;}
			this.timeLeft = Config.getInt("LastTimeLeftTilDraw");
			if((timeLeft<0)|(timeLeft>100000)){this.timeLeft = secondsTilDraw;}
			this.startingPot = Config.getLong("StartingJackpot");
			if(startingPot<0){this.startingPot = 0;}
			this.pot = (float) Config.getDouble("CurrentJackpot");
			this.percent2Pot = Float.parseFloat((String)Config.getString("PercentageOfSalesIntoJackpot"));
			if(percent2Pot<0){this.percent2Pot=1.00f;}
			this.actionBarEnabled = Config.getBoolean("EnableActionBar");
			this.recordDrawings = Config.getBoolean("RecordDrawings");
			this.autoRestart = Config.getBoolean("AutoRestartAfterXDrawings");
			this.drawingsTilRestart = Config.getInt("DrawingsTilRestart");
			if(drawingsTilRestart<=0){this.drawingsTilRestart = 2;}
			this.website = Config.getString("Website");
			paths.add(Config.getString("AuthFileSavePath"));
			paths.add(Config.getString("BanksFileSavePath"));
			paths.add(Config.getString("SaveStatesFileSavePath"));
			paths.add(Config.getString("RecordsFileSavePath"));
			paths.add(Config.getString("ServerRestartKickMessage"));
			for(int x=1;x<6;x++){
				prizes.add(Config.getDouble("Match"+x+"Prize"));
			}
			init();
		}catch(Exception e1){
			e1.printStackTrace();
			File config = new File("plugins/PowerBlock/config.yml");
			config.mkdir();
		}
	}
	
	/**
	 * The Init Function:
	 * 
	 */
	private void init(){
		auth = new Auth(this);
		bank = new Bank(this);
		bar = new ActionBarAPI();
		bank.init();
		g = new GUIFactory();
		res = new ResultWindow(this);
		st = new StateManager(this);
		st.loadStates(new File(paths.get(2)));
		tit = new TitleAPI();
		det = new UpdateDetector(this);
		//TODO db = new dbManager(this);
	}
	
	private void bASB(){
		for(Player p:Bukkit.getOnlinePlayers()){
			findPlayer(p,0).updSB(p);
		}
	}
	
	
	/**
	 * Make Power Block Dir Function:
	 * makes the directory needed for this plugins's files
	 * 
	 */
	public void mkPbDir(){
		File file = new File("plugins/PowerBlock");
		file.mkdir();
	}
	
	
	/**
	 * The Check For UI Function:
	 * 
	 * A Helper Function To The Events
	 * At The Top Of This Code Section.
	 * 
	 */
	private boolean checkForUI(ItemStack st){
		try{
			List<String>l=new ArrayList<>();
			l = st.getItemMeta().getLore();
			if(l.get(l.size()-1).equalsIgnoreCase(bl+"#UI")){
				return true;
			}  
		}catch(Exception e1){}
		return false;
	}
	
	/**
	 * The Save JackPot Function:
	 * Saves The Jackpot So It May
	 * Persist Into The Next Run Of
	 * The Plugin.
	 * 
	 */
	private void saveJackpot(){
		try{
			File file = new File(getDataFolder(),"Config.yml");
			YamlConfiguration Config = YamlConfiguration.loadConfiguration(file);
			Config.set("CurrentJackpot",pot);
			Config.set("LastTimeLeftTilDraw", timeLeft);
			Config.save(file);
		}catch(Exception e1){}
	}
	
	/**
	 * InitItems Function:
	 * 
	 * Gives A Players Their Initial Menu Items So
	 * They Can Play PowerBlock.
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void initItems(Player p){
		ItemStack st = new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.SILVER.getData());
		ItemMeta met = st.getItemMeta();
		met.setDisplayName(gr+bo+"Welcome To Power Block!");
		List<String>l=new ArrayList<>();
		l.add(" ");l.add(lp+bo+ul+"Getting Started:");l.add(lp+"First Off, The Money Amount You");l.add(lp+"See In The Jackpot Is Real Money.");l.add(lp+"Match Your Ticket's Numbers To");l.add(lp+"Our Winning Numbers And Walk");l.add(lp+"Away With A Whole Lot Of Cash!");l.add(" ");l.add(aq+bo+ul+"Playing The Game:");l.add(aq+"Visit The Server Website To Purchase");l.add(aq+"Usable Credit Here On The Server. Then");l.add(aq+"Click On The Green Buttons Above To Use");l.add(aq+"Those Credits To Buy Tickets. The More");l.add(aq+"Tickets You Buy The Better Your Jackpot");l.add(aq+"Chances Are!");l.add(bl+"#UI");
		met.setLore(l);
		st.setItemMeta(met);
		for(int x=0;x<36;x++){
			p.getInventory().setItem(x, st);
		}
	}
	
	/**
	 * Syntx Function:
	 * Just A Message For Checking Your Syntax.
	 * 
	 */
	private Result syntx(CommandSender se){
		se.sendMessage(pr()+re+bo+"✖ "+re+"Check Your Command Syntax");
		se.sendMessage(pr()+gr+bo+"✔ "+gr+"Should Be: /Bank [D,W,B] [Name] [Amt > 0]");
		return null;
	}
	
	/**
	 * The Unknown Function:
	 * 
	 * The purpose is to act just like
	 * the user entered an unknown command
	 * when they do not have permission to
	 * perform operations.
	 * 
	 * @return 
	 * 
	 */
	private String unk(){
		return wh+"Unknown command. Type \"/help\" for help.";
	}
	
	/**
	 * The Test Function:
	 * 
	 */
	private String txt(int i){
		return(i==0?pr()+re+bo+"✖ "+re+"Insufficient Funds":pr()+re+bo+"✖ "+re+"Player Not Found!");
	}
	
	
	/**
	 * The Get Sounds Function:
	 * 
	 * Looks like we'll be tossing around MORE
	 * Stuff, Yay!
	 * 
	 */
	public Sound[] getSounds(){
		return sounds;
	}
	
	/**
	 * The Get Sorter Function:
	 * 
	 */
	public Sorter getSorter(){
		return sort;
	}
	
	/**
	 * The Get Title Function:
	 * 
	 */
	public TitleAPI getTitle(){
		return tit;
	}
	/**
	 * Rand Color Function:
	 * 
	 */
	public String randColor(){
		String[]s=new String[]{wh,aq,re,lp,bu,gr};
		return s[((int)(Math.random()*s.length))];
	}
}