package me.virusbrandon.skillstop;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import me.virusbrandon.localapis.GUIFactory;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	
	@EventHandler
	public void leave(PlayerQuitEvent e){
		for(UUID s:gip.keySet()){
			gip.get(s).unreg();
		}
	}
	
	private GUIFactory fact;
	private HashMap<UUID,SkillWindow> gip = new HashMap<>();
	private String re=ChatColor.RED+"",gr=ChatColor.GREEN+"",bo=ChatColor.BOLD+"",aq=ChatColor.AQUA+"",wh=ChatColor.WHITE+"";
	private YamlConfiguration config;
	private double CURR_JACKPOT = 0;
	private double PRICE_PER_PLAY = 1.0;
	private double PRICE_PERCENT_TO_POT = .5;
	private int PLAYS_REQ_FOR_WIN = 10;
	private int TIME_LIMIT = 30;
	private Economy econ;
	
	@Override
	public void onEnable(){
		if(startupReq()){
			Bukkit.getServer().getPluginManager().registerEvents(this, this);
			fact = new GUIFactory();
			config();
		} else {
			Bukkit.getConsoleSender().sendMessage(re+bo+"SkillStop Only Supports 1.9 And 1.10 ... SORRY O_O");
			try{
				throw new InvalidServerException(re+"This Server Is Not Supported!");
			}catch(Exception e1){}
			this.getServer().getPluginManager().disablePlugin(Main.getPlugin(Main.class));
		}
	}
	
	@Override
	public void onDisable(){
		for(UUID sk:gip.keySet()){
			SkillWindow t = gip.get(sk);
			if(t.getOwner().getOpenInventory().getTopInventory().getTitle().equalsIgnoreCase("[ST]")){
				t.getOwner().closeInventory();
			}
			t.unreg();
		}
		sConfig();
	}
	
	public boolean onCommand(CommandSender se, Command c, String lbl, String[] args){
		if(lbl.equalsIgnoreCase("Skill")){
			if(se instanceof Player){
				Player p = ((Player)se);
				try{
					findWindow(p).play(p);	
				}catch(Exception e1){}
			} else {
				se.sendMessage(pfx()+re+bo+"Sorry, Only In Game Players Can Play!");
			}
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private SkillWindow findWindow(Player p){
		if(gip.containsKey(p.getUniqueId())){
			return gip.get(p.getUniqueId());
		}
		double d = econ.getBalance(p.getName());
		if(d>=PRICE_PER_PLAY){
			econ.withdrawPlayer(p.getName(), PRICE_PER_PLAY);
			p.sendMessage(pfx()+re+"Spent "+gr+econ.format(PRICE_PER_PLAY)+re+" - Remaing Balance: "+gr+econ.format(d-PRICE_PER_PLAY)+re+".");
			gip.put(p.getUniqueId(),new SkillWindow(this,p));
			return gip.get(gip.size()-1);
		} else {
			p.sendMessage(pfx()+re+"You Have "+gr+econ.format(d)+", But It Costs "+econ.format(PRICE_PER_PLAY));
		}
		return null; /* Not Enough Money - Can't Play - NULL POINTER ACCESS!*/
	}
	
	private void config(){
		File file = new File("plugins/SkillStop/config.yml");
		if(!file.exists()){
			saveDefaultConfig();
		}
		this.config = YamlConfiguration.loadConfiguration(file);
		this.CURR_JACKPOT = config.getDouble("CurrentJackpot");
		this.PRICE_PER_PLAY = config.getDouble("PricePerPlay");
		double d = config.getDouble("PercentOfPriceToPot");
		this.PRICE_PERCENT_TO_POT = (((d>=0)&&(d<=1.0))?d:.5); /* Yeah, This Needs To Be Between 0 And 1 */
		this.PLAYS_REQ_FOR_WIN = config.getInt("PlaysRequiredForWin");
		this.TIME_LIMIT = config.getInt("TimeLimit");
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			econ = economyProvider.getProvider();
		} else {
			Bukkit.getConsoleSender().sendMessage(pfx()+re+bo+"Economy Is Not Working Properly On Your Server! Please Resolve The Issue And Restart Skill-Stop!");
			this.getPluginLoader().disablePlugin(this);
		}
	}
	
	/**
	 * Saves The Config
	 * 
	 */
	private void sConfig(){
		config.set("CurrentJackpot",CURR_JACKPOT);
		config.set("PlaysRequiredForWin",PLAYS_REQ_FOR_WIN);
		try{
			config.save(new File("plugins/SkillStop/config.yml"));
		}catch(Exception e1){}
	}
	
	/**
	 * Returns The Current Jackpot.
	 * 
	 * @return
	 */
	public String pot(){
		return econ.format(CURR_JACKPOT);
	}
	
	/**
	 * Updates The Jackpot Amount And The
	 * Number Of Plays Required Until Someone
	 * Is Able To Win The Jackpot.
	 * 
	 */
	public void spn(){
		this.CURR_JACKPOT+=(PRICE_PERCENT_TO_POT*PRICE_PER_PLAY);
		this.PLAYS_REQ_FOR_WIN--;
	}
	
	/**
	 * Someone Won The Jackpot! YAY
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void swj(Player p){
		econ.depositPlayer(p.getName(), CURR_JACKPOT);
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, .5f);
		p.sendMessage(pfx()+re+bo+"Nice!"+re+" You Just Won The Jackpot For "+gr+econ.format(CURR_JACKPOT)+re+"!");
		p.sendMessage(pfx()+re+"Your New Balance Is: "+gr+econ.format(econ.getBalance(p.getName()))+re+"!");
		for(Player pp:Bukkit.getOnlinePlayers()){
			if(!pp.equals(p)){
				pp.playSound(pp.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
				pp.sendMessage(pfx()+re+bo+p.getName()+re+" Just Won The Jackpot");
				pp.sendMessage(pfx()+re+"On The Skill Stop Machine For "+gr+econ.format(CURR_JACKPOT)+re+"!");
			}
		}
		this.CURR_JACKPOT = config.getDouble("DefaultJackpot");
		PLAYS_REQ_FOR_WIN = config.getInt("DefaultPlaysNeededForWin");
	}
	
	/**
	 * Returns The Number Of Players
	 * Required Before Anyone Can Win (Rigged ^_^ )
	 * 
	 * @return
	 */
	public int getReqPlays(){
		return PLAYS_REQ_FOR_WIN;
	}
	
	/**
	 * Returns The Amount Of Time A
	 * User Has Before Their Game Automatically
	 * Ends By Waiting Too Long.
	 * 
	 * @return
	 */
	public int getTimeLimit(){
		return TIME_LIMIT;
	}
	
	/**
	 * Returns Config
	 * 
	 * @return
	 */
	public YamlConfiguration getConf(){
		return config;
	}
	
	/**
	 * Returns The GUI Factory
	 * 
	 * @return
	 */
	public GUIFactory getFact(){
		return fact;
	}
	
	/**
	 * Removes A Skill Stop Window
	 * After The Game Is Finished
	 * 
	 * @param w
	 */
	public void remST(SkillWindow w){
		gip.remove(UUID.fromString(w.getUUID()));
	}
	
	/**
	 * Returns A Prefix For This Plugin
	 * 
	 * @return
	 */
	public String pfx(){
		return gr+bo+"["+aq+" Skill-Stop "+gr+bo+"] "+wh;
	}
	
	/**
	 * Determines Whether Or Not
	 * This Software Is Being Run
	 * On A Support Server Version
	 * 
	 */
	private boolean startupReq(){
		String nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		return(nmsver.startsWith("v1_9_")|nmsver.startsWith("v1_10_"));
	}
}
