package me.virusbrandon.agarmc;

import java.util.*;

import me.virusbrandon.agargameboard.*;
import me.virusbrandon.agarlobbysigns.LobbySign;
import me.virusbrandon.agarlocalapis.*;
import me.virusbrandon.agarutils.*;
import me.virusbrandon.commands.AICommand;
import me.virusbrandon.commands.HelpCommand;
import me.virusbrandon.commands.SetLobbyCommand;
import me.virusbrandon.commands.SetOriginCommand;
import me.virusbrandon.commands.TeleportCommand;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin implements Listener{
	private HoloManager hm = new HoloManager();
	private GameDriver gd;
	private ActionBarAPI bar;
	private TitleAPI ti;
	private GameBoard gb;
	private GUIFactory gf;
	private Sorter sr;
	private HashMap<UUID,AgarPlayer> players = new HashMap<>();
	private ArrayList<AgarPlayer> AIs = new ArrayList<>();
	private ArrayList<String> names = new ArrayList<>();
	private String re = ChatColor.RED+"",ye=ChatColor.YELLOW+"",gr=ChatColor.GREEN+"",bl=ChatColor.BLACK+"",bo=ChatColor.BOLD+"",wh=ChatColor.WHITE+"";
	private String ver = "1.11";
	
	/*
	 * SOFTWARE PERMISSIONS:
	 * 
	 * Agarmc.ADMIN
	 * Agarmc.cells.color.
	 * 		BLACK
	 * 		DARK_BLUE
	 * 		DARK_GREEN
	 * 		DARK_AQUA
	 * 		DARK_RED
	 * 		DARK_PURPLE
	 * 		GOLD
	 * 		GRAY
	 * 		DARK_GRAY
	 * 		BLUE
	 * 		GREEN
	 * 		AQUA
	 * 		RED
	 * 		LIGHT_PURPLE
	 * 		YELLOW
	 * 		WHITE
	 * 		RANDOM
	 * 
	 * Agarmc.cells.nametag
	 * 
	 * And That's Everything!
	 * 
	 */
	
	/**
	 * Fired When A Player Joins The Server
	 * 
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if(gb != null){
			if(gb.getLobby() != null){
				World w = Bukkit.getWorld(gb.getOrigin().getWorld().getName());
				if(w!=null){
					if(e.getPlayer().getWorld().getName().equalsIgnoreCase(w.getName())){
						Location l = gb.getLobby();
						e.getPlayer().teleport(new Location(l.getWorld(),l.getX(),(l.getY()+1),l.getZ()));
					}
				}
			}
		}
	}
	
	/**
	 * Fired When A Player Leaves The Server.
	 * 
	 * @param e
	 */
	@EventHandler
	public void leave(PlayerQuitEvent e){
		lvhlpr(e.getPlayer());
	}
	
	/**
	 * Fired When A Player Runs A Command.
	 * 
	 * @param e
	 */
	@EventHandler
	public void cmd(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		AgarPlayer ag = findPlayer(p);
		if(ag!=null){
			if(e.getMessage().equalsIgnoreCase("/Leave")){
				if(!lvhlpr(p)){
					e.getPlayer().sendMessage(re+"You Are Not In The Game!");
					e.setCancelled(true);
				} else {
					e.getPlayer().sendMessage(gr+"You Have Quit The Game!");
					e.setCancelled(true);
				}
			} else {
				p.sendMessage(pfx()+"Only /Leave Is Available While Playing!");
				e.setCancelled(true);
			}
		}
	}
	
	/**
	 * Fired When A Player Clicks On An Object
	 * Within An Inventory User Interface.
	 * 
	 * @param e
	 */
	@EventHandler
	public void click(InventoryClickEvent e){
		try{
			List<String> l = new ArrayList<>();
			l = e.getCurrentItem().getItemMeta().getLore();
			if(l.contains(bl+"#AGAR")){
				e.setCancelled(true);
			}
		}catch(Exception e1){}
	}
	
	/**
	 * Blocks Player Block Break
	 * When In Game.
	 * 
	 * @param e
	 */
	@EventHandler
	public void dest(BlockBreakEvent e){
		Player p = e.getPlayer();
		if(findPlayer(p)!=null){
			e.setCancelled(true);
		}
	}
	
	/**
	 * Prevents The Destruction
	 * Of A Lobby Sign.
	 * 
	 * @param e
	 */
	@EventHandler
	public void bbreak(BlockBreakEvent e){
		Block b = e.getBlock();
		try{
			for(LobbySign lo:gb.getAllLobbySigns()){
				if(b.getLocation().distance(lo.getLoc())<2){
					e.setCancelled(true);
				}
			}
		}catch(Exception e1){}
	}
	
	/**
	 * Prevents The Unloading Of Chunks
	 * 
	 */
	@EventHandler
	public void unload(ChunkUnloadEvent e){
		World w = e.getChunk().getWorld();
		try{
			if((w.getName().equalsIgnoreCase(gB().getOrigin().getWorld().getName()) | w.getName().equalsIgnoreCase(gB().getLobby().getWorld().getName()))){
				e.setCancelled(true);
			}
		}catch(Exception e1){}
	}
	
	/**
	 * Prevents The Placement Of
	 * Blocks Anywhere.
	 * 
	 * @param e
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void place(BlockPlaceEvent e){
		Player p = e.getPlayer();
		try{
			List<String> l = new ArrayList<>();
			l = p.getItemInHand().getItemMeta().getLore();
			if(l.contains(bl+"#AGAR")){
				e.setCancelled(true);
			}
		}catch(Exception e1){}
	}
	
	/**
	 * Prevents The Dropping Of
	 * Items Associated With Agar.MC
	 * 
	 * @param e
	 */
	@EventHandler
	public void drop(PlayerDropItemEvent e){
		try{
			List<String> l = new ArrayList<>();
			l = e.getItemDrop().getItemStack().getItemMeta().getLore();
			if(l.contains(bl+"#AGAR")){
				e.setCancelled(true);
			}
		}catch(Exception e1){}	
	}
	
	/**
	 * Handles The Interaction Between
	 * Players And Lobby Join Signs.
	 * 
	 * @param e
	 */
	@EventHandler
	public void inter(PlayerInteractEvent e){
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		try{
			BlockState state = b.getState();
			if(state instanceof Sign){
				Sign s = ((Sign)state);
				if(s.getLine(2).equalsIgnoreCase(gr+bo+"Join Now!")){
					new Pref(this,p);
				}
			}
		}catch(Exception e1){}
	}
	
	/**
	 * Fires When A Falling Block
	 * Touches The Ground And Forms To
	 * A Solid Block.
	 * 
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void form(EntityChangeBlockEvent e){
		if(e.getEntity() instanceof FallingBlock){
			Location l = e.getBlock().getLocation();
			if(l.getWorld().getName().equalsIgnoreCase("AGAR.MC-GAMEBOARD")){
				if(Material.getMaterial(e.getTo().getId()) != Material.GRASS){
					if(!germHlpr(l,e.getEntity().getVelocity())){
						Mass m = new Mass(this,new Location(l.getWorld(),l.getX(),l.getY(),l.getZ()),new int[]{e.getTo().getId(),e.getData()},5);
						gB().addMass(m);
					}
				} else {
					new Germ(this,l);
				}
				e.setCancelled(true);
			}
		}
	}
	
	/**
	 * Helper Function Which Shows To The Users
	 * That A Germ Is Growing After Receiving Mass
	 * From A Nearly Cell.
	 * 
	 * @param l
	 * @param v
	 * @return
	 */
	private boolean germHlpr(Location l,Vector v){
		Germ g = gB().getAllGerms().get(((int)l.getX())+""+((int)l.getZ()));
		if(g != null){
			return g.grow(v);
		}
		return false;
	}
	
	/**
	 * Helper Method For Leaving The Game.
	 * 
	 * @param p
	 * @return
	 */
	private boolean lvhlpr(Player p){
		AgarPlayer plr = findPlayer(p);
		if(plr!=null){
			plr.done(false);
			return true;
		}
		return false;
	}
	
	/**
	 * Called When The Software Starts.
	 * 
	 */
	@Override
	public void onEnable(){
		init();
	}
	
	/**
	 * Called When The Software Is Stopped.
	 * 
	 */
	@Override
	public void onDisable(){
		endAll();
		hm.remAllHolos();
		ArrayList<Player> pl = new ArrayList<Player>(){
		private static final long serialVersionUID = 1L;
		{addAll(Bukkit.getOnlinePlayers());}};
		for(Player p:pl){
			p.setScoreboard(new SimpleScoreboard("").getBlankScoreboard());
		}
		try{
			gb.saveConfig();
		}catch(Exception e1){}
	}
	
	@Override
	public boolean onCommand(CommandSender se, Command c, String lbl, String[] args){
		return true;
	}
	
	/**
	 * Essential Function For Software
	 * Initialization.
	 * 
	 */
	private void init(){
		bar = new ActionBarAPI();
		if(bar.getNMSVer().equalsIgnoreCase(ver)){
			gd = new GameDriver(this);
			ti = new TitleAPI();
			gf = new GUIFactory();
			gb = new GameBoard(this);
			sr = new Sorter();
			Bukkit.getServer().getPluginManager().registerEvents(this, this);
		    Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPS(), 100L, 1L);
		    setUpNames();
		    registerCommands();
		} else {
			errorFatal("Unsupported Server Version: "+bar.getNMSVer());
		}
	}
	
	/**
	 * Returns The Gameboard Instance.
	 * 
	 * @return
	 */
	public GameBoard gB(){
		return gb;
	}
	
	/**
	 * Returns The Gamedriver Instance.
	 * 
	 * @return
	 */
	public GameDriver gD(){
		return gd;
	}
	
	/**
	 * Returns The HoloManager Instance.
	 * 
	 * @return
	 */
	public HoloManager hM(){
		return hm;
	}
	
	/**
	 * Returns The ActionBarAPI Instance.
	 * 
	 * @return
	 */
	public ActionBarAPI getBar(){
		return bar;
	}
	
	/**
	 * Returns Reference To Our TitleAPI
	 * 
	 * @return
	 */
	public TitleAPI gT(){
		return ti;
	}
	
	/**
	 * Returns Reference To Our GUI Factory
	 * 
	 * @return
	 */
	public GUIFactory gF(){
		return gf;
	}
	
	/**
	 * Returns The Software Chat Prefix
	 * 
	 * @return
	 */
	public String pfx(){
		return re+bo+"Agar.mc"+wh+" >> ";
	}

	/**
	 * Selects And Returns A Random ChatColor
	 * 
	 * @return
	 */
	public ChatColor chooseColor(){
		return ChatColor.values()[(int)(Math.random()*15)];
	}
	
	/**
	 * Creates An AI
	 * 
	 */
	public void createAI(){
		AgarPlayer pl = new AgarPlayer(this,null,new Pref(this,null).setCellColor("ANY"));
		AIs.add(pl);
	}
	
	/**
	 * Locates An AI
	 * 
	 */
	public boolean locateAI(int i, Player p){
		try{
			p.teleport(AIs.get(i).getCells().get(0).getEntity().getLocation());
		}catch(Exception e1){return false;}
		return true;
	}
	
	/**
	 * Splits An AI's Cell(s)
	 * 
	 */
	public boolean splitAI(int i){
		try{
			AIs.get(i).split(-1);
		} catch(Exception e1){return false;}
		return true;
	}
	
	/**
	 * Ejects AI Mass
	 * 
	 */
	public boolean ejectAIMass(int i){
		try{
			AIs.get(i).eject();
		}catch(Exception e1){return false;}
		return true;
	}
	
	/**
	 * Merge AI Cells
	 * 
	 */
	public boolean mergeAI(int i){
		try{
			AIs.get(i).setSplitTime(0);
		}catch(Exception e1){return false;}
		return true;
	}
	
	/**
	 * Kills AN AI
	 * 
	 */
	public boolean killAI(int i){
		try{
			AIs.get(i).done(false);
		}catch(Exception e1){return false;}
		return true;
	}
		
	/**
	 * Returns All AIs
	 * 
	 */
	public ArrayList<AgarPlayer> getAllAIs(){
		return AIs;
	}
	
	/**
	 * Joins A Player Into The Game
	 * 
	 * @param p
	 * @param pr
	 */
	public void join(Player p,Pref pr){
		if(findPlayer(p) == null){
			if(gb.isReady()){
				players.put(p.getUniqueId(),new AgarPlayer(this,p,pr));
			} else {
				p.sendMessage(re+"The Game Is Not Ready Yet!");
			}
		}
	}
	
	/**
	 * Ends The Games Of All Currently Active
	 * Players In Case Of Software Shutdown, Or
	 * Because The Server Operators Are Changing
	 * A Few Things.
	 * 
	 */
	public void endAll(){
		for(Player p:Bukkit.getOnlinePlayers()){
			if(p.getOpenInventory().getTopInventory().getTitle().contains("[AG]")){
				p.closeInventory();
			}
		}
		for(AgarPlayer p:players.values()){
			p.done(true);
		}
		for(AgarPlayer ai:AIs){
			ai.done(true);
		}
	}
	
	/**
	 * NULLPOINTER ACCESS - We Expect To Find The Cell We Are Looking For Every Time!
	 * 
	 * @param p
	 * @return
	 */
	public AgarPlayer findPlayer(Player p){
		return players.get(p.getUniqueId());
	}
	
	/**
	 * Returns A Collection Of All Cells
	 * Amongst All Active Players.
	 * 
	 * @return
	 */
	public ArrayList<Cell> getAllCells(){
		ArrayList<Cell> all = new ArrayList<>();
		for(AgarPlayer p:players.values()){
			all.addAll(p.getCells());
		}
		for(AgarPlayer ai:AIs){
			all.addAll(ai.getCells());
		}
		return all;
	}
	
	/**
	 * Removes A Player
	 * 
	 * @param pp
	 */
	protected void remPlayer(AgarPlayer pp){
		if(!pp.isAI()){
			players.remove(pp.getPlayer().getUniqueId());
		} else {
			AIs.remove(pp);
			addName(pp.getName());
		}
	}
	
	/**
	 * Returns All Agar.MC Players
	 * 
	 * @return
	 */
	public ArrayList<AgarPlayer> getPlayersAL(){
		ArrayList<AgarPlayer> pls = new ArrayList<AgarPlayer>();
		pls.addAll(players.values());
		pls.addAll(AIs);
		return pls;
	}
	
	/**
	 * Returns All Agar.MC Players
	 * 
	 * @return
	 */
	protected HashMap<UUID,AgarPlayer> getPlayersHM(){
		return players;
	}
	
	/**
	 * Sorts The Players By Total
	 * Cell Mass For The LeaderBoard;
	 * 
	 */
	public ArrayList<AgarPlayer> sortPlayers(){		
		return sr.sort(getPlayersAL());
	}
	
	/**
	 * Sets Up The BOT Names
	 * 
	 */
	public void setUpNames(){
		String[] s = new String[]{"Paul","Billy","Connor","Brandon","Zack","Sarah","Kelly","Nathan","Chad","Brent","Andy","James","Logan","Jack","Danny","Alex","David","Grace","Natalie","Andrew"};
		for(String ss:s){
			addName(ss);
		}
	}
	
	/**
	 * Gets A Random Name Then
	 * Removes It From The List
	 * 
	 */
	public String grabName(){
		int i = (int)(Math.random()*names.size());
		String s = names.get(i);
		names.remove(i);
		return s;
	}
	
	/**
	 * Adds A Name
	 * 
	 */
	public void addName(String name){
		names.add(name.replaceAll("BOT ", ""));
	}
	
	/**
	 * Registers All Commands That
	 * Can Be Used
	 * 
	 */
	private void registerCommands(){
		SetLobbyCommand.getInstance(this);
		SetOriginCommand.getInstance(this);
		AICommand.getInstance(this);
		HelpCommand.getInstance(this);
		TeleportCommand.getInstance(this);
	}
	
	/**
	 * Broadcasts A Message To Players
	 * In A Certain World
	 * 
	 */
	public void broadcastMessage(String worldName, String message){
		for(AgarPlayer pp:players.values()){
			Player p = pp.getPlayer();
			if(p.getWorld().getName().equalsIgnoreCase(worldName)){
				p.sendMessage(message);
			}
		}
	}
	
	/**
	 * Stops The Software Due To A Fatal
	 * Error. Namely An Obvious Malfunction
	 * Brought About By An Operation
	 * Performed By Another Plugin. (Like WorldEdit)
	 * 
	 * STOP REMOVING MY FUCKING LOBBY SIGNS!
	 * 
	 */
	public void errorFatal(String addit){
		try{
			hM().remAllHolos();
		}catch(Exception e1){}
		Bukkit.broadcastMessage(re+bo+"AGAR.MC - FATAL ERROR - STOPPING SOFTWARE!");
		Bukkit.broadcastMessage(ye+"Additional Information:");
		Bukkit.broadcastMessage(ye+((addit.length()<1)?"None":addit));
		this.getServer().getPluginManager().disablePlugin(Main.getPlugin(Main.class));
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */