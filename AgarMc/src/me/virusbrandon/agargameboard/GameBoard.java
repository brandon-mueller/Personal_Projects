package me.virusbrandon.agargameboard;

import java.io.File;
import java.util.*;

import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;

import me.virusbrandon.agarlobbysigns.*;
import me.virusbrandon.agarmc.*;
import me.virusbrandon.agarutils.*;

public class GameBoard {
	Runnable timer = new Runnable() {
		@SuppressWarnings("deprecation")
		public void run() {
			try{
				if(zz<=targetZ){
					for(int xx=x;xx<targetX;xx++){
						ori.getWorld().getBlockAt(xx,ori.getBlockY(),zz).getChunk().load();
						ori.getWorld().getBlockAt(xx,ori.getBlockY(),zz).setTypeId(boardBlock[0]);
						ori.getWorld().getBlockAt(xx,ori.getBlockY(),zz).setData((byte)boardBlock[1]);
						ori.getWorld().getBlockAt(xx,ori.getBlockY(),zz+1).setTypeId(boardBlock[0]);
						ori.getWorld().getBlockAt(xx,ori.getBlockY(),zz+1).setData((byte)boardBlock[1]);
						ori.getWorld().getBlockAt(xx,ori.getBlockY(),zz+2).setTypeId(boardBlock[0]);
						ori.getWorld().getBlockAt(xx,ori.getBlockY(),zz+2).setData((byte)boardBlock[1]);
						ori.getWorld().getBlockAt(xx,ori.getBlockY(),zz+3).setTypeId(boardBlock[0]);
						ori.getWorld().getBlockAt(xx,ori.getBlockY(),zz+3).setData((byte)boardBlock[1]);
						ori.getWorld().getBlockAt(xx,ori.getBlockY(),zz+4).setTypeId(boardBlock[0]);
						ori.getWorld().getBlockAt(xx,ori.getBlockY(),zz+4).setData((byte)boardBlock[1]);
						ori.getWorld().getBlockAt(xx,250,zz).setType(Material.GLASS);
						ori.getWorld().getBlockAt(xx,250,zz+1).setType(Material.GLASS);
						ori.getWorld().getBlockAt(xx,250,zz+2).setType(Material.GLASS);
						ori.getWorld().getBlockAt(xx,250,zz+3).setType(Material.GLASS);
						ori.getWorld().getBlockAt(xx,250,zz+4).setType(Material.GLASS);
					}
					zz+=5;
				} else {
					stop();
					setBundles((int)((boardSize - (boardPadding*2))*.25));
					for(int x=0;x<(bundles*50);x++){
						Mass m = new Mass(main);
						Location l = m.getLocation();
						String s = ((int)l.getBlockX())+""+((int)l.getBlockZ());
						allMass.put(s,m);
					}
					for(int x=0;x<bundles;x++){
						new Germ(main);
					}
					setReady(true);
					setInProgress(false);
					Location l = new Location(lobby.getWorld(),lobby.getX(),lobby.getY(),lobby.getZ());
					l.getWorld().getBlockAt(l).setTypeId(getBoardBlock()[0]);
					l.getWorld().getBlockAt(l).setData((byte)getBoardBlock()[1]);
					if(maxAIs > 0){
						startAIs(1);
					}
				}
			} catch(Exception e1){
				e1.printStackTrace();
				stop();
				setInProgress(false);
				Bukkit.broadcastMessage(re+bo+"╞"+ss+"╡");
				Bukkit.broadcastMessage("");				
				Bukkit.broadcastMessage(re+"┌ "+re+bo+"GameBoard Init Encountered An Error!");
				Bukkit.broadcastMessage(ye+"├ We think you gave us an origin world that does not exist.");
				Bukkit.broadcastMessage(ye+"├ Re-run the command: /Agarmc setorigin <w> <x> <y> <z>");
				Bukkit.broadcastMessage(ye+"├ With correct information.");
				Bukkit.broadcastMessage(ye+"├ Once you've done that, the gameboard will.");
				Bukkit.broadcastMessage(ye+"└ Make another attempt at generation.");
				Bukkit.broadcastMessage("");				
				Bukkit.broadcastMessage(re+bo+"╞"+ss+"╡");
			}
		}	
	};
	
	Runnable ais = new Runnable() {
		public void run() {
			tics++;
			if((tics>(secsBetAIs*20)) && main.getAllAIs().size()<maxAIs){
				main.createAI();
				tics=0;
			}
		}	
	};
	
	private Main main;
	private YamlConfiguration config;
	private LobbySignManager man;
	private Location ori;					/* GameBoard Origin Location - To Start Generation */
	private Location lobby;					/* Game Lobby Location */
	private int boardSize;					/* Square Size Of GameBoard (x-Squared) */
	private int cellPadding;				/* Levitation Distance From Cell To GameBoard (Height) */
	private int boardPadding;				/* Distance From Edge Of Board To Stop Cell Movement */
	private int cellMetaSec;				/* The Time In Seconds By Which Cells Passively Lose Mass */
	private int maxMass;					/* The Highest Mass A Cell Can Get To */
	private int startMass;					/* The Starting Mass For The First Cell Of A Game */
	private int maxCells;					/* The Maximum Number Of Cells A Player Can Split Down To */
	private int secsBetAIs;					/* The Number Of Seconds Between AI Spawns */
	private int maxAIs;						/* The Maximum AIs That Will Be On The Gameboard At Any Time */
	private int[] boardBlock;				/* The Block That The GameBoard Will Be Made Of (Random) */
	private boolean ready = false;			/* GameBoard Ready Flag - Prevents Player Join Before Generation Is Complete */
	private boolean inProgress = false;		/* GameBoard Generating In Progress Flag */
	private int id,id2,x,z,zz,targetX,targetZ,tics;
	private int bundles;
	private HashMap<String,Mass> allMass = new HashMap<>();
	private HashMap<String,Germ> allGerms = new HashMap<>();
	private String LOBBY_LOC_TEXT = "Agar.MC Lobby";
	private String re=ChatColor.RED+"",bo=ChatColor.BOLD+"",ye=ChatColor.YELLOW+"",ul=ChatColor.UNDERLINE+"";
	private String ss;
	private int[][] bBlocks = new int[][]{{35,15},{22,0},{168,2},{152,0},{159,10},{159,4},{1,0},{7,0},{35,11},{133,0},{57,0},{159,14},{35,2},{41,0},{155,0},{49,0},{173,0},{216,0}};
	private LocSetting set;
	private HashMap<String,RGBContainer> colors = new HashMap<String,RGBContainer>(){
		private static final long serialVersionUID = 1L;
		{
		   put("BLACK", new RGBContainer(0,0,0));
		   put("DARK_BLUE", new RGBContainer(0,0,170));
		   put("DARK_GREEN", new RGBContainer(0,170,0));
		   put("DARK_AQUA", new RGBContainer(0,170,170));
		   put("DARK_RED", new RGBContainer(170,0,0));
		   put("DARK_PURPLE", new RGBContainer(170,0,170));
		   put("GOLD", new RGBContainer(255,170,0));
		   put("GRAY", new RGBContainer(170,170,170));
		   put("DARK_GRAY", new RGBContainer(85,85,85));
		   put("BLUE", new RGBContainer(85,85,255));
		   put("GREEN", new RGBContainer(85,255,85));
		   put("AQUA", new RGBContainer(85,255,255));
		   put("RED", new RGBContainer(255, 85, 85));
		   put("LIGHT_PURPLE", new RGBContainer(255,85,255));
		   put("YELLOW", new RGBContainer(255,255,85));
		   put("WHITE", new RGBContainer(255,255,255));
		}};
	
	
	/**
	 * The GameBoard Constructor.
	 * 
	 * @param main
	 */
	public GameBoard(Main main){
		this.main = main;
		this.boardBlock = bBlocks[(int)(Math.random()*bBlocks.length)];
		loadConfig();
	}

	/**
	 * Initiates The GameBoard Generation
	 * Process. Once Finished, Players Can
	 * Start Competing.
	 * 
	 */
	private void genBoard(){
		man = LobbySignManager.getInstance(this);
		this.x = ori.getBlockX();
		this.z = ori.getBlockZ();
		this.zz = z;
		this.targetX = (x+boardSize);
		this.targetZ = (z+boardSize);
		setReady(false);
		setInProgress(true);
		start(1);
	}
	
	/**
	 * Returns The Padding Between
	 * The Very Edge Of The Gameboard
	 * And The Edge Of The Playable Area.
	 * 
	 */
	public int getBoardPadding(){
		return boardPadding;
	}
	
	/**
	 * Returns The Cell Padding.
	 * (Distance Between Your Cell(s)
	 * And The Floor Of The Gameboard)
	 * @return
	 */
	public int getCellPadding(){
		return cellPadding;
	}
	
	/**
	 * Returns The Center Of The
	 * Gameboard
	 * 
	 */
	public Location getGameboardCenter(){
		Location l = getOrigin();
		return new Location(l.getWorld(),(l.getX()+(main.gB().getBoardSize()/2)),l.getY(),(l.getZ()+(main.gB().getBoardSize()/2)));
	}
	
	
	/**
	 * Returns The Origin Location.
	 * 
	 */
	public Location getOrigin(){
		return ori;
	}
	
	/**
	 * Sets The GameBoard Origin Point.
	 * 
	 * Restarts Generation If You Set
	 * A New Origin Location.
	 * 
	 * @param ori
	 */
	public String setOri(Location ori){
		if(!inProgress){
			this.ori = ori;
			if((ori!=null)&(lobby!=null)){
				genBoard();
			}
			return "Alright, Origin Set!";
		} else {
			return re+"Origin NOT Set, All Changes Locked Out Until Current GameBoard Generation Is Complete.";
		}
	}
	
	/**
	 * Returns The Lobby Location
	 * 
	 */
	public Location getLobby(){
		return lobby;
	}
	
	/**
	 * Sets The Lobby Location That Players
	 * Will Returns To When Their Game Ends.
	 * 
	 */
	public String setLobby(Location lobby){
		this.lobby = lobby;
		if(set!=null){
			set.getTimer().stop();
		}
		set = new LocSetting(main,lobby,re+bo+LOBBY_LOC_TEXT);
		if((ori!=null)&(lobby!=null)){
			genBoard();
		}
		return "Alright, Lobby Set!";
	}
	
	/**
	 * Loads In Any Existing Information From The
	 * Config File And Prepares The GameBoard For
	 * Generation.
	 * 
	 * [[-+-] Optimal Default Settings [-+-]]
	 * FIELDS:
	 * 
	 * GameBoard Size:
	 * MIN: 50  MAX: 500  DEFAULT: 500
	 * 
	 * GameBoard Padding:
	 * MIN: 1  MAX: 500  DEFAULT: 100
	 * 
	 * Cell Metabolism Seconds:
	 * MIN: 1  MAX: 10  DEFAULT: 10
	 * 
	 * Cell Time Before Merge Seconds:
	 * MIN: 1  MAX: 90  DEFAULT: 30
	 * 
	 * Cell Max Mass:
	 * MIN: 500  MAX: 3000  MERGE: 2500
	 * 
	 * Cell Start Mass:
	 * MIN: 250  MAX: 1000  DEFAULT: 250
	 * 
	 * Max Cells:
	 * MIN: 1  MAX: 16 DEFAULT: 16
	 * 
	 */
	private void loadConfig(){
		this.ss = main.gF().draw("═", 30, "");
		try{
			File file = new File("plugins/AgarMC/config.yml");
			if(!file.exists()){
				file = new File("plugins/AgarMC");
				file.mkdir();
				file = new File("plugins/AgarMC/config.yml");
				file.createNewFile();
			}
			this.config = YamlConfiguration.loadConfiguration(file);
			int i = config.getInt("AgarMc.GameBoard.size.value");
			this.boardSize = (((i>=50)&&(i<=500))?i:500);
			i = config.getInt("AgarMc.GameBoard.boardPadding.value");
			this.boardPadding = (((i>=1)&&(i<=500))?i:100);
			i = config.getInt("AgarMc.GameBoard.cellPadding.value");
			this.cellPadding = (((i>=1)&&(i<=10))?i:1);
			i = config.getInt("AgarMc.Cell.MetabolismSeconds.value");
			this.cellMetaSec = (((i>=1)&&(i<30))?i:10);
			i = config.getInt("AgarMc.Cell.MaxMass.value");
			this.maxMass = (((i>=500)&&(i<=5000))?i:2500);
			i = config.getInt("AgarMc.Cell.StartMass.value");
			this.startMass = (((i>=250)&&(i<=1000))?i:250);
			i = config.getInt("AgarMc.Cell.MaxCells.value");
			this.maxCells = (((i>=1)&&(i<=16))?i:16);
			i = config.getInt("AgarMc.AI.SecBetSpawn.value");
			this.secsBetAIs = (((i>=1)&&(i<=60))?i:30);
			i = config.getInt("AgarMc.AI.MaxAIs.value");
			this.maxAIs = (((i>=0)&&(i<=20))?i:0);
			String s = config.getString("AgarMc.GameBoard.Lobby_Loc_Text");
			this.LOBBY_LOC_TEXT = (s!=null)?s:LOBBY_LOC_TEXT;
			Location l = ((Location)config.get("AgarMc.GameBoard.Locations.Origin"));
			if(l!=null){
				setOri(l);
			}
			l = ((Location)config.get("AgarMc.GameBoard.Locations.Lobby"));
			if(l!=null){
				setLobby(l);
			}
			if(!inProgress){
				if(isRunning("CleanroomGenerator","Multiverse-Core")){
					new AutomatedSetup(main);
				} else {
					Bukkit.broadcastMessage(main.pfx()+re+"Automated Setup Is"+re+bo+" Disabled:");
					Bukkit.broadcastMessage(main.pfx()+ye+"Multiverse And CleanroomGenerator Are Required!");
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(re+bo+"╞"+ss+"╡");
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(ye+"┌ "+ul+"To Get Started:");
					Bukkit.broadcastMessage(ye+"└ Set The Lobby Location: "+bo+"/AgarMc setLobby");
					Bukkit.broadcastMessage(ye+"┌ Set GameBoard Origin Point: "+bo+"/AgarMc setOrigin");
					Bukkit.broadcastMessage(ye+"├ Recommended: Empty World At ( 0,0,0 )");
					Bukkit.broadcastMessage(ye+"└ Example: /AgarMc setOrigin (World Name) 0 0 0");
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(re+bo+"╞"+ss+"╡");
				}
			}
		}catch(Exception e1){}
	}
	
	/**
	 * Saves The Config.
	 * 
	 */
	public void saveConfig(){
		try{
			if(ori!=null){
				config.set("AgarMc.GameBoard.Locations.Origin",ori);
			}
			if(lobby!=null){
				config.set("AgarMc.GameBoard.Locations.Lobby", lobby);
			}
			config.set("AgarMc.GameBoard.size.comment", "Board Size ( MIN: 50 - MAX: 500 )");
			config.set("AgarMc.GameBoard.size.value", boardSize);
			config.set("AgarMc.GameBoard.boardPadding.comment", "Area Around Gameboard ( MIN: 1 - MAX: 500 )");
			config.set("AgarMc.GameBoard.boardPadding.value",boardPadding);
			config.set("AgarMc.GameBoard.cellPadding.comment", "Space Between Cells And GameBoard ( MIN: 1 - MAX: 10 )");
			config.set("AgarMc.GameBoard.cellPadding.value",cellPadding);
			config.set("AgarMc.GameBoard.Lobby_Loc_Text",LOBBY_LOC_TEXT);
			config.set("AgarMc.Cell.MetabolismSeconds.comment", "Time Before Cells Lose Some Mass ( MIN: 1 - MAX: 30 )");
			config.set("AgarMc.Cell.MetabolismSeconds.value",cellMetaSec);
			config.set("AgarMc.Cell.MaxMass.comment","Maximum Mass Allowed On Gameboard ( MIN: 500 - MAX: 5000 )");
			config.set("AgarMc.Cell.MaxMass.value",maxMass);
			config.set("AgarMc.Cell.StartMass.comment","Mass That Cells Spawn In With ( MIN: 250 - MAX: 1000 )");
			config.set("AgarMc.Cell.StartMass.value",startMass);
			config.set("AgarMc.Cell.MaxCells.comment","Max Cells That A Player Can Have ( MIN: 1 - MAX: 16 )");
			config.set("AgarMc.Cell.MaxCells.value",maxCells);
			config.set("AgarMc.AI.SecBetSpawn.comment","Number Of Seconds Between AI Spawns ( MIN: 1 - MAX: 60 )");
			config.set("AgarMc.AI.SecBetSpawn.value",secsBetAIs);
			config.set("AgarMc.AI.MaxAIs.comment","The Maximum Number Of AIs ( MIN: 0 - MAX: 20 )");
			config.set("AgarMc.AI.MaxAIs.value",maxAIs);
			config.save(new File("plugins/AgarMC/config.yml"));
			stop();
			allMass.clear();
			allGerms.clear();
		}catch(Exception e1){e1.printStackTrace();}
		if(man!=null){
			man.stop();
		}
		stopAIs();
	}
	
	/**
	 * Sets Whether This GameBoard Is
	 * Ready To Receive Players Yet.
	 * 
	 * @param ready
	 */
	private void setReady(boolean ready){
		this.ready = ready;
	}
	
	/**
	 * Returns Whether The GameBoard Is
	 * Ready To Receive Players Yet.
	 * 
	 * @return
	 */
	public boolean isReady(){
		return ready;
	}
	
	/**
	 * Sets Whether The GameBoard Is
	 * Being Generated Or Not.
	 * 
	 */
	private void setInProgress(boolean inProgress){
		this.inProgress  = inProgress;
	}
	
	/**
	 * Returns Whether The GameBoard Is
	 * Being Generated Or Not;
	 */
	public boolean isInProgress(){
		return inProgress;
	}
	
	/**
	 * Returns A Percentage Of When
	 * The GameBoard Will Be Ready.
	 * 
	 * Only Relevant When The Software
	 * Is Starting Up.
	 * 
	 */
	public int getProgress(){
		return (int)(((zz-ori.getZ())/(targetZ-ori.getZ()))*100);
	}
	
	/**
	 * Returns Colors Map
	 * 
	 */
	public RGBContainer getColor(String s){
		return colors.get(s);
	}
	
	/**
	 * Returns The Min X Value Of
	 * The GameBoard
	 * 
	 */
	public int getBoardMinX(){
		return (x+boardPadding);
	}
	
	/**
	 * Returns The Max X Value Of
	 * The GameBoard
	 * 
	 */
	public int getBoardMaxX(){
		return (targetX-boardPadding);
	}
	
	/**
	 * Returns The Min Z Value Of
	 * The GameBoard
	 * 
	 */
	public int getBoardMinZ(){
		return (z+boardPadding);
	}
	
	/**
	 * Returns The Max Z Value Of
	 * The GameBoard
	 * 
	 */
	public int getBoardMaxZ(){
		return (targetZ-boardPadding);
	}
	
	/**
	 * Returns The GameBoard Size.
	 * 
	 */
	public int getBoardSize(){
		return boardSize;
	}
	
	/**
	 * Returns The Board Blocks
	 * 
	 */
	public int[][] getBBlocks(){
		return bBlocks;
	}
	
	/**
	 * Returns The Block Blocks Length
	 * 
	 */
	public int getBBlockLen(){
		return bBlocks.length;
	}
	
	/**
	 * Returns The Current Board Block
	 * 
	 */
	public int[] getBoardBlock(){
		return boardBlock;
	}
	
	/**
	 * Returns Cell Metabolism Rate
	 * In Seconds;
	 * 
	 */
	public int getCellMetaRate(){
		return cellMetaSec;
	}
	
	/**
	 * Returns The Max Mass That
	 * A Cell Can Get To.
	 * 
	 */
	public int getMaxMass(){
		return maxMass;
	}
	
	/**
	 * Returns The Starting Mass That
	 * A Player's First Cell Will Be.
	 * 
	 */
	public int getStartMass(){
		return startMass;
	}
	
	/**
	 * Returns The Max Number Of
	 * Cells That A Player Can Split Down To.
	 * 
	 */
	public int getMaxCells(){
		return maxCells;
	}
	
	/**
	 * Returns The Max Number
	 * Of AIs Allows As Set In
	 * The Config.
	 * 
	 */
	public int getMaxAIs(){
		return maxAIs;
	}
	
	/**
	 * Returns A Random Location Within The
	 * Confines Of The Allowable GameBoard Space.
	 * 
	 */
	public Location rndLoc(){
		return new Location(ori.getWorld(),(ori.getX()+boardPadding)+(int)(Math.random()*(boardSize-(boardPadding*2))),(ori.getY()+cellPadding),(ori.getZ()+boardPadding)+(int)(Math.random()*(boardSize-(boardPadding*2))));
	}
	
	/**
	 * Returns The Base Height
	 * (Origin Y + Cell Levitation Padding)
	 * 
	 */
	public int baseHeight(){
		return ((ori.getBlockY()+cellPadding)+2);
	}
	
	private void setBundles(int bundles) {
		this.bundles = bundles;
	}
	
	protected int getBundles(){
		return bundles;
	}
	
	/**
	 * Returns All Mass
	 * 
	 */
	public HashMap<String,Mass> getAllMass(){
		return allMass;
	}
	
	/**
	 * Add Mass To The GameBoard
	 * 
	 */
	public void addMass(Mass m){
		Location l = m.getLocation();
		String s = ((int)l.getBlockX())+""+((int)l.getBlockZ());
		allMass.put(s, m);
	}
	
	/**
	 * Returns The Size Of The
	 * Germ List
	 * 
	 */
	protected int getAllGermSize(){
		return allGerms.size();
	}
	
	/**
	 * Returns All Germs
	 * 
	 */
	public HashMap<String,Germ> getAllGerms(){
		return allGerms;
	}
	
	/**
	 * Returns All Lobby Signs.
	 * 
	 */
	public ArrayList<LobbySign> getAllLobbySigns(){
		return man.getSigns();
	}
	
	/**
	 * Returns Reference To Main.
	 * 
	 */
	public Main getMain(){
		return main;
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void start(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay, delay);
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	private void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void startAIs(int delay){
		id2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), ais, delay, delay);
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	private void stopAIs(){
		Bukkit.getScheduler().cancelTask(id2);
	}
	
	/**
	 * Checks If All Plugins From The Supplied List
	 * Are Currently Running On The Host Server.
	 * 
	 */
	public boolean isRunning(String ... plugins){
		for(String s:plugins){
			if(Bukkit.getServer().getPluginManager().getPlugin(s)==null){
				return false;
			}
		}
		return true;
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */