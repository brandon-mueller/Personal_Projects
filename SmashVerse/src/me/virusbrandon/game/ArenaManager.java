package me.virusbrandon.game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.virusbrandon.localapis.SimpleScoreboard;
import me.virusbrandon.maps.TemplateManager;
import me.virusbrandon.smashverse.Main;
import me.virusbrandon.sv_utils.ArenaStatus.Status;
import me.virusbrandon.sv_utils.FBTracker;
import me.virusbrandon.sv_utils.LocSetting;

public class ArenaManager implements Listener{
	private ArrayList<Arena> ARENAS = new ArrayList<>();
	private ArrayList<Player> QUEUE = new ArrayList<>();
	private ArrayList<String> sc = new ArrayList<>(); /* Software Console */
	private ArrayList<Material> items = new ArrayList<>();
	private ArenaDriver queueStat;
	private TemplateManager tManager;
	private LobbySignManager lManager;
	private SimpleScoreboard scoreboard;
	private Main main;
	private YamlConfiguration config,locs,iDesc;
	private LocSetting LOBBY_LOCATION; 								/* AVOID MAKING LOBBY AND JOIN  */
	private LocSetting JOIN_LOCATION;								/* LOCATIONS TOO CLOSE TOGETHER	*/
	private LocSetting ORIGIN_LOCATION;								/* Arena Stacking Origin Point  */
	private String Join_Loc_Text = "Join Here!";
	private String Lobby_Loc_Text = "Lobby";
	private String Origin_Loc_Text = "Arena Stacker";
	private String re=ChatColor.RED+"",bo=ChatColor.BOLD+"",ye=ChatColor.YELLOW+"",gr=ChatColor.GREEN+"",bl=ChatColor.BLACK+"",wh=ChatColor.WHITE+"";
	private int JOIN_DIST = 4; /* Default - Will Be Overridden By Any Existing Configured Value */
	private int ARENA_SIZE_MAX = 50; /* Largest Size An Arena Can Be For Stacking */
	private int MAX_ARENAS = 3;
	private int START_LIVES = 3;
	private int START_DMG = 0;
	private int COUNTDOWN_SECONDS = 30;
	private int RESPAWN_SECONDS = 5;
	private int SUDDEN_DEATH_DMG = 300;
	private int PLAYERS_REQUIRED = 2;
	private int templateBuildSpeed = 3; /* Available Options: 1 = Slow, 2 = Medium, And 3 = Fast*/
	private CommandSender console = Bukkit.getConsoleSender();
	private Random r = new Random();
	private int[] FREE_FOR_ALL_TIMES = new int[]{120,180,240,300};
	
	@EventHandler
	public void leave(PlayerQuitEvent e){
		leave(e.getPlayer());
	}
	
	public ArenaManager(Main main){
		this.main = main;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		config();
		tManager = new TemplateManager(this,ARENA_SIZE_MAX);
		tManager.loadTemplates();
		start();
		queueStat = new ArenaDriver(this,2);
		queueStat.start(10);
		for(int x=0;x<20;x++){
			sc.add("");
		}
	}
	
	/**
	 * Join The Player Queue
	 * 
	 */
	public boolean join(Player p){
		if(reqMet()){
			if(!playerIsBusy(p)){
				QUEUE.add(p);
				checkArenas();
			}
		}
		return true;
	}
	
	public void leave(Player p){
		if(playerIsBusy(p)){
			if(QUEUE.contains(p)){
				QUEUE.remove(p);
				p.teleport(getLobby());
				p.sendMessage(main.getPfx()+gr+"You Left - Welcome Back To The Lobby!");
				p.setScoreboard(scoreboard.getBlankScoreboard());
			} else {
				for(Arena a:ARENAS){
					if(a.leave(p)){
						return;
					}
				}
			}
		} else {
			p.sendMessage(main.getPfx()+re+"You're Not In The Game Or In Queue?");
		}
	}
	
	/**
	 * Checks For Enough Players In The
	 * Queue To Start A New Game, And Then
	 * Looks For An Available Arena To Place
	 * Four Players In.
	 * 
	 */
	public void checkArenas(){
		if(QUEUE.size()>=PLAYERS_REQUIRED){
			for(Arena arena:ARENAS){
				while((arena.getStatus()==Status.WAITING)&QUEUE.size()>0){
					arena.join(QUEUE.remove(0));
				}
			}
		}
	}
	
	/**
	 * Helper Function For Arena Setup
	 * 
	 */
	public void start(){
		if(reqMet()){
			if(ARENAS.size()<1){
				setupArenas();
				this.lManager = new LobbySignManager(this,LOBBY_LOCATION.getLocation());
			} /* Arenas Appear To Already Be Setup - A Previously Set Location Is Being Revised */
			for(Entity e:ORIGIN_LOCATION.getLocation().getWorld().getEntities()){
				if(!(e instanceof Player)){
					e.remove();
				}
			}
		} else {
			console.sendMessage("\n"+main.getPfx()+" - Arenas Cannot Generate BECAUSE:"+((LOBBY_LOCATION==null)?"\n"+main.getPfx()+re+" - Lobby Location Needs To Be Set":"")+((JOIN_LOCATION==null)?"\n"+main.getPfx()+re+" - Join Location Needs To Be Set":"")+((ORIGIN_LOCATION==null)?"\n"+main.getPfx()+re+" - Arena Stacker Origin Location Needs To Be Set":"")+((tManager.getTemplateCount()<1)?"\n"+main.getPfx()+re+" - No Arena Templates Were Found":"")+"\n\n");
		}
	}
	
	/**
	 * Set The Lobby Location
	 * 
	 */
	public ArenaManager setLobby(Location loc){
		if(loc!=null){
			if(LOBBY_LOCATION != null){
				LOBBY_LOCATION.getTimer().stop();
			}
			LOBBY_LOCATION = new LocSetting(main, loc, re+bo+Lobby_Loc_Text);
			setConfig(locs,"Lobby_Location",loc);
			saveConfig();
			start();
		}
		return this;
	}
	
	/**
	 * Returns The Lobby Location
	 * 
	 */
	public Location getLobby(){
		if(LOBBY_LOCATION != null){
			return LOBBY_LOCATION.getLocation();
		}
		return null; /* NULLPOINT ACESS - DO NOT CALL If You Know This Field Is Not Set! */
	}
	
	/**
	 * Set The Join Location
	 * 
	 */
	public ArenaManager setJoin(Location loc){
		if(loc != null){
			if(JOIN_LOCATION != null){
				JOIN_LOCATION.getTimer().stop();
			} 
			JOIN_LOCATION = new LocSetting(main, loc,ye+bo+Join_Loc_Text);
			setConfig(locs,"Join_Location",loc);
			saveConfig();
			start();
		}
		return this;
	}
	
	/**
	 * Returns The Join Location
	 * 
	 */
	public Location getJoin(){
		if(JOIN_LOCATION != null){
			return JOIN_LOCATION.getLocation();
		}
		return null; /* NULLPOINTER ACESS - DO NOT CALL If You Know This Field Is Not Set! */
	}
	
	/**
	 * Set The Arena Stacking Original Point
	 * 
	 */
	public ArenaManager setOrigin(Location loc){
		if(loc != null){
			if(ORIGIN_LOCATION != null){
				ORIGIN_LOCATION.getTimer().stop();
			} 
			ORIGIN_LOCATION = new LocSetting(main, loc,gr+bo+Origin_Loc_Text);
			setConfig(locs,"Origin_Location",loc);
			saveConfig();
			start();
		}
		return this;
	}
	
	/**
	 * Returns The Origin Location
	 * 
	 */
	public Location getOrigin(){
		if(ORIGIN_LOCATION != null){
			return ORIGIN_LOCATION.getLocation();
		}
		return null;
	}
	
	public void corDir(Player source, Location target, int inOrOut, int strength){
		Location l = source.getLocation();
		Vector v = l.toVector().subtract(target.toVector()).multiply(inOrOut).normalize();
		source.setVelocity(new Location(l.getWorld(),l.getX(),l.getY(),l.getZ(),(180-(float)Math.toDegrees(Math.atan2(v.getX(), v.getZ()))),(90-(float)Math.toDegrees(Math.acos(v.getY())))).getDirection().multiply((calcDist(l,target)/strength)));
	}
	
	/**
	 * Returns The Arena Template Manager
	 * 
	 */
	public TemplateManager getTemplateManager(){
		return tManager;
	}
	
	/**
	 * Returns The Lobby Sign Manager
	 * 
	 */
	public LobbySignManager getLMan(){
		return lManager;
	}
	
	/**
	 * Returns Reference To Main
	 * 
	 */
	public Main getMain(){
		return main;
	}
	
	/**
	 * Gets The Distance In Blocks That A Player
	 * Can Be Away From The Join Point Before The
	 * Software Will Find Them And Add Them To The 
	 * Queue For The Next Match.
	 * 
	 */
	public int getJoinDist(){
		return JOIN_DIST;
	}
	
	/**
	 * Returns The Number Of Lives
	 * That SmashVerse Players Can Start
	 * With In A Stock Match. Does Not Apply
	 * To Free For All Mode.
	 * 
	 */
	public int getStartLives(){
		return START_LIVES;
	}
	
	/**
	 * Returns The Amount Of Damage
	 * That SmashVerse Players Are To Start With
	 * In Any Game Mode.
	 * 
	 */
	public int getStartDmg(){
		return START_DMG;
	}
	
	/**
	 * Gets The Max Arena Size
	 * 
	 */
	public int getMaxArenaSize(){
		return ARENA_SIZE_MAX;
	}
	
	/**
	 * Returns The Countdown Time
	 * 
	 */
	public int getCountDownTime(){
		return COUNTDOWN_SECONDS;
	}
	
	/**
	 * Returns The Player Respawn Delay
	 * 
	 */
	public int getRespawnTime(){
		return RESPAWN_SECONDS;
	}
	
	/**
	 * Returns The SuddenDeathDmg
	 * 
	 */
	public int getSuddentDeathDMG(){
		return SUDDEN_DEATH_DMG;
	}
	
	/**
	 * Returns The Number Of
	 * Required Players
	 * 
	 */
	public int getReqPlayers(){
		return PLAYERS_REQUIRED;
	}
	
	/**
	 * Returns TemplateBuildSpeed
	 * 
	 */
	public int tBS(){
		return templateBuildSpeed;
	}
	
	/**
	 * Checks If The Player Is Busy (In Queue Or In Game)
	 * 
	 */
	public boolean playerIsBusy(Player p){
		return(!QUEUE.contains(p)&&chkArenas(p))?false:true;
	}
	
	/**
	 * Checks Arenas For Your Presence To Avoid
	 * Arena Double Joining
	 * 
	 */
	private boolean chkArenas(Player player){
		for(Arena a:ARENAS){
			if(a.getPlayers().contains(player)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Begins The Arena Setup Routines Up To
	 * The Max Arenas Set In The Config.
	 * 
	 */
	public void setupArenas(){
		for(int x=0;x<MAX_ARENAS;x++){
			ARENAS.add(new Arena(main,ARENAS.size(),this));
		}
	}
	
	/**
	 * Ends All Arenas And Resets Them.
	 * Ideal For When The Software Is Shutting
	 * Down So We Can Cancel All Active Games
	 * And Move Players Back To The Lobby.
	 * 
	 */
	public void endAllArenas(boolean doRegen){
		try{
			for(Arena arena:ARENAS){
				arena.reset(doRegen);
			}
			if(lManager!=null){lManager.stop();}
			queueStat.stop();
		}catch(Exception e1){}
	}
	
	/**
	 * Checks If We're Ready To Set Up The
	 * Arenas Yet.
	 * 
	 */
	private boolean reqMet(){
		return ((tManager.getTemplateCount()>0)&(LOBBY_LOCATION!=null)&(JOIN_LOCATION!=null)&(ORIGIN_LOCATION!=null));
	}
	
	/**
	 * Returns The Running Arena
	 * Instances
	 * 
	 */
	public ArrayList<Arena> getArenas(){
		return ARENAS;
	}
	
	/**
	 * Returns The Number Of Running
	 * Arena Instances.
	 * 
	 */
	public int getArenaCount(){
		return ARENAS.size();
	}
	
	/**
	 * Returns A Random Amount Of
	 * Time For Match Duration.
	 * 
	 */
	public int chooseDuration(){
		return FREE_FOR_ALL_TIMES[(int)(Math.random()*FREE_FOR_ALL_TIMES.length)];
	}
	
	/**
	 * Returns The KO Hologram Text
	 * 
	 * Sorry... I wanted this to be hard
	 * coded due to it's susceptibility to
	 * being setup wrong if configurable.
	 * 
	 */
	public String[] KOtext(ChatColor c){
		String[] s = new String[]{"ooooooooooooooooooooooooooooooooooooooooooooooooooooooooo","ooo███ooooo███oooooo█████████ooooo███oooooo███oooo███oooo","ooo███oooo███ooooo███ooooooo███ooo███oooooo███ooo█████ooo","ooo███ooo███ooooo███ooooooooo███oo███oooooo███ooo█████ooo","ooo███oo███oooooo███ooooooooo███oo██ooooooo███ooo█████ooo","ooo███o███ooooooo███ooooooooo███oo█oooooooo███ooo█████ooo","ooo██████oooooooo███ooooooooo███ooooooooooo███ooo█████ooo","ooo██████oooooooo███ooooooooo███oooo██████████oooo███oooo","ooo███o███ooooooo███ooooooooo███ooo███oooo████ooooooooooo","ooo███oo███oooooo███ooooooooo███ooo███oooo████oooo███oooo","ooo███ooo███ooooo███ooooooooo███ooo███oooo████ooo█████ooo","ooo███oooo███ooooo███ooooooo███oooo███oooo████ooo█████ooo","ooo███ooooo███oooooo█████████ooooooo████████o█oooo███oooo","ooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"};
		for(int x=0;x<s.length;x++){
			s[x]=s[x].replaceAll("█",c+"█").replaceAll("o",bl+"█");
		}
		return s;
	}
	
	/**
	 * Returns The Marking That Indicates
	 * That There's An Item To Be Collected.
	 * 
	 * @return
	 */
	public String[] Itemtext(){
		String[] s = new String[]{"oooooooo","ooo██ooo","oo████oo","oo████oo","oo████oo","oo████oo","ooo██ooo","oooooooo","ooo██ooo","ooo██ooo","oooooooo"};
		for(int x=0;x<s.length;x++){
			s[x]=s[x].replaceAll("█",gr+"█").replaceAll("o",bl+"█");
		}
		return s;
	}
	 
	/**
	 * Updates Queue Scoreboard
	 * 
	 */
	protected boolean scoreboard(){
		try{
			for(int x=0;x<QUEUE.size();x++){
				scoreboard = new SimpleScoreboard(re+bo+"__ Super SmashVerse __");
				scoreboard.blankLine();
				scoreboard.add(ye+bo+"Name: ");
				scoreboard.add(ye+QUEUE.get(x).getName());
				scoreboard.blankLine();
				scoreboard.add(ye+bo+"You Are Currently");
				scoreboard.add(ye+(x+1)+getSuffix(x+1)+" In Queue");
				scoreboard.blankLine();
				scoreboard.build();
				scoreboard.send(QUEUE.get(x));
			}
		}catch(Exception e1){e1.printStackTrace();}
		return true;
	}
	
	/**
	 * Queue Position Suffix Helper Function
	 * 
	 */
	private String getSuffix(int i){
		String suff="th";
		if(((i%100)>19)|((i%100)<10)){
			switch(i%10){
				case 1:
					suff="st";
				break;	
				case 2:
					suff="nd";
				break;
				case 3:
					suff="rd";
				break;	
			}
		}
		return suff;
	}
	
	
	public ItemStack grabItem(){
		return new ItemStack(items.get(((int)(Math.random()*items.size()))),1);	
	}
	
	/**
	 * Returns The Console Sender
	 * 
	 */
	public CommandSender getConsole(){
		return console;
	}
	
	/**
	 * Adds Information To The Software Console
	 * 
	 */
	public void cLn(String ln){
		try{
			sc.add(wh+bo+"["+gr+new SimpleDateFormat("hh:mm:ss").format(new Date())+wh+bo+"] "+wh+ln);
			if(sc.size()>30){
				sc.remove(0);
			}
		}catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	/**
	 * Returns The Software Console
	 * 
	 */
	public String[] getSC(){
		String[] s = new String[sc.size()];
		for(int x=0;x<s.length;x++){
			s[x]=sc.get(x);
		}
		return s;
	}
	
	/**
	 * Manages Creating And Loading The Config
	 * 
	 */
	private void config(){
		try{
			File file = new File(main.getDataFolder(),"config.yml");
			if(!file.exists()){
				main.saveDefaultConfig();
				main.getConfig().options().copyDefaults(true);
			}
			this.config = YamlConfiguration.loadConfiguration(file);
			file = new File(main.getDataFolder(),"locations.yml");
			if(!file.exists()){
				file.createNewFile();
			}
			this.locs = YamlConfiguration.loadConfiguration(file);
			file = new File("plugins/SmashVerse/items.txt");
			if(!file.exists()){
				file.createNewFile();
				writeItems(file);
			}
			file = new File(main.getDataFolder(),"items.yml");
			if(!file.exists()){
				main.saveResource("items.yml",true);
			}
			this.iDesc = YamlConfiguration.loadConfiguration(file);
			cH();
		}catch(Exception e1){}
	}
	
	/**
	 * Read Items
	 * 
	 */
	public void readItems(){
		try{
			items.clear();
			Scanner in = new Scanner(new FileReader("plugins/SmashVerse/items.txt"));
			while(in.hasNext()){
				Material m = Material.valueOf(in.next());
				int i = in.nextInt();
				for(int x=0;x<i;x++){
					items.add(m);
				}
			}
			in.close();
		}catch(Exception e1){}
	}
	
	/**
	 * Write Items
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void writeItems(File file){
		int[] powerups = new int[]{260,368,426,46,288,369,385,268,272,267,276,280,173,335,377};
		try{
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			try{
				for(int x:powerups){
					bw.write(new ItemStack(x,1).getType().name() + " 0\n");
				}
			} catch(Exception e1){}
			bw.close();
		}catch(Exception e1){}	
	}
	
	/**
	 * Config Helper Function
	 * 
	 */
	private void cH(){
		this.Join_Loc_Text = config.getString("Join_Loc_Text");
		this.Lobby_Loc_Text = config.getString("Lobby_Loc_Text");
		this.Origin_Loc_Text = config.getString("Origin_Loc_Text");
		/* Grab Join Distance */
		int i = config.getInt("Join_Distance");
		this.JOIN_DIST = (((i>0)&&(i<=20))?i:4);
		
		/* Grab Max Arena Size */
		i = config.getInt("Arena_Max_Size");
		this.ARENA_SIZE_MAX = (((i>=30)&&(i<=100))?i:50);
		
		/* Grab Max Arenas */
		i = config.getInt("Max_Arenas");
		this.MAX_ARENAS = (((i>0)&&(i<=50))?i:3);
		
		/* Grab Starting Lives Amount */
		i = config.getInt("Start_Lives");
		this.START_LIVES = (((i>0)&&(i<=99))?i:3);
		
		/* Grab Starting Damage Amount */
		i = config.getInt("Start_Dmg");
		this.START_DMG = (((i>=0)&&(i<=999))?i:0);
		
		/* Grab Countdown Seconds */
		i = config.getInt("CountDown_Seconds");
		this.COUNTDOWN_SECONDS = (((i>0)&&(i<120))?i:5);
		
		/* Grab Respawn Seconds */
		i = config.getInt("Respawn_Seconds");
		this.RESPAWN_SECONDS = (((i>=0)&&(i<=30))?i:5);
		
		/* Grab Sudden Death Start Damage */
		i = config.getInt("SuddenDeathStartDmg");
		this.SUDDEN_DEATH_DMG = (((i>=300)&&(i<=999))?i:300);
		
		/* Grab Template Build Speed */
		i = config.getInt("TemplateBuildSpeed");
		this.templateBuildSpeed = (((i>0)&&(i<=5))?i:1);
		
		/* Grab Players Required */
		i = config.getInt("PlayersRequired");
		this.PLAYERS_REQUIRED = ((i>0)&&(i<=4))?i:4;
		try{
			Location loc = (Location)locs.get("Lobby_Location");
			if(loc != null){
				this.LOBBY_LOCATION = new LocSetting(main,loc,re+bo+Lobby_Loc_Text);
			}
			loc = (Location)locs.get("Join_Location");
			if(loc != null){
				this.JOIN_LOCATION = new LocSetting(main,loc,ye+bo+Join_Loc_Text);
			}
			loc = (Location)locs.get("Origin_Location");
			if(loc != null){
				this.ORIGIN_LOCATION = new LocSetting(main,loc,gr+bo+Origin_Loc_Text);
			}
		}catch(Exception e1){}
		readItems();
	}
	
	/**
	 * Saves The Config
	 * 
	 */
	public void saveConfig(){
		try{
			locs.save(new File("plugins/SmashVerse/locations.yml"));
		}catch(Exception e1){}
		try{
			iDesc.save(new File("plugins/SmashVerse/items.yml"));
		}catch(Exception e1){}
	}
	
	/**
	 * Config Setting Function
	 * 
	 */
	public ArenaManager setConfig(YamlConfiguration con,String pth,Object val){
		con.set(pth, val);
		return this;
	}
	
	/**
	 * Returns The Item Descriptions
	 * 
	 */
	public YamlConfiguration getIdesc(){
		return iDesc;
	}
	
	/**
	 * Throws Blocks Everywhere When
	 * An Explosion Occurs.. Pretty Awesome. :)
	 * 
	 * @param b
	 */
	@SuppressWarnings("deprecation")
	public void sendItFlying(Block b){
		int n1 = ((-1+(Math.random()*2))>=0)?1:-1;
    	int n2 = ((-1+(Math.random()*2))>=0)?1:-1;
    	float n3 = r.nextFloat()*3;
    	Byte blockData = b.getData();
    	FallingBlock fb = b.getLocation().getWorld().spawnFallingBlock(b.getLocation(), b.getType(), blockData);
    	fb.setVelocity(new Vector(n1*(.02+(r.nextInt(33)*.03)),n3,n2*(.02+(r.nextInt(33)*.03))));
    	fb.setDropItem(false);
    	new FBTracker(this,fb);
    	b.getWorld().getBlockAt(b.getLocation()).setType(Material.AIR);
	}
	
	/**
	 * The Distance Calculation Function:
	 * 
	 * Will Be Used In Both Handling Player
	 * Joining Events and Player In Game
	 * Death Events.
	 * 
	 * @param loc1
	 * @param loc2
	 * @return
	 */
	public double calcDist(Location loc1, Location loc2){
		return Math.sqrt(Math.pow(loc1.getBlockX()-loc2.getBlockX(),2)+Math.pow(loc1.getBlockY()- 
		loc2.getBlockY(),2)+Math.pow(loc1.getBlockZ()-loc2.getBlockZ(),2));
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
