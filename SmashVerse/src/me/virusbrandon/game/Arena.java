package me.virusbrandon.game;

import java.util.ArrayList;

import me.virusbrandon.blackhole.BlackHole;
import me.virusbrandon.darkmatter.DarkMatter;
import me.virusbrandon.laser.Laser;
import me.virusbrandon.localapis.SimpleScoreboard;
import me.virusbrandon.magicstick.MagicStick;
import me.virusbrandon.maps.Template;
import me.virusbrandon.meteorstorm.MeteorStorm;
import me.virusbrandon.smashverse.Main;
import me.virusbrandon.superstar.SuperStar;
import me.virusbrandon.sv_utils.ArenaStatus.Mode;
import me.virusbrandon.sv_utils.ArenaStatus.Status;
import me.virusbrandon.sv_utils.DBlock;
import me.virusbrandon.sv_utils.Item;
import me.virusbrandon.sv_utils.Sorter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;
import org.inventivetalent.glow.GlowAPI.Color;

import de.inventivegames.hologram.HologramAPI;

public class Arena implements Listener{
	private Main main;
	private int ARENA_SLOT;
	private ArenaManager man;
	private ArrayList<Player> PLAYERS = new ArrayList<>();
	private ArrayList<Player> SPECTATORS = new ArrayList<>();
	private ArrayList<SmashVersePlayer> SV_PLAYERS = new ArrayList<>();
	private ArrayList<DBlock> toReplace = new ArrayList<>();
	private ArrayList<Item> items = new ArrayList<>();
	private Template template;
	private int ACTIVE_PLAYERS = 0;
	private int endingD = 40;
	private int finishedD = 200;
	private int cCounter = 0;
	private Status status = Status.NO_SIGNAL;
	private Mode mode;
	private long S_TIMESTAMP;
	private long F_TIMESTAMP;
	private long START_STAMP;
	private ArenaDriver init,driver;
	private SimpleScoreboard scoreboard;
	private String bo=ChatColor.BOLD+"",re=ChatColor.RED+"",go=ChatColor.GOLD+"",ye=ChatColor.YELLOW+"",gr=ChatColor.GREEN+"",wh=ChatColor.WHITE+"",bl=ChatColor.BLACK+"",dr=ChatColor.DARK_RED+"";
	private String[] playerColors = new String[]{"AQUA","RED","YELLOW","GREEN"};
	private Sorter sorter;
	private String pro = "";
	private MeteorStorm storm = null;
	
	@SuppressWarnings("serial")
	@EventHandler
	public void cmd(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		if(new ArrayList<Player>(){{addAll(PLAYERS);addAll(SPECTATORS);}}.contains(p)){
			if(e.getMessage().equalsIgnoreCase("/Leave")){
				leave(p);
			} else {
				p.sendMessage(man.getMain().getPfx()+"Only [/Leave] Is Available In Game!");
			}
			e.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(PLAYERS.contains(p)){
			switch(status){
				case STARTING:
				case ENDING:
				case FINISHED:
					sbd(p);
				break;
				case IN_GAME:
				case SUDDENDEATH:
					if(findPlayer(p).isKOd()){
						sbd(p);
					} else {
						if(findPlayer(p).isInvincible()){
							for(Player pp:PLAYERS){
								if((pp!=p)&&(man.calcDist(p.getLocation(), pp.getLocation()))<2){
									man.corDir(pp,p.getLocation(), -1,1);
								}
							}
						}
						rbd(p);
					}
				break;
				default:
			}
		} else if(SPECTATORS.contains(p)){
			rbd(p);
		} else {
			try{
				if((man.calcDist(p.getLocation(), getSpawn())<(man.getMaxArenaSize()*.55))&(!p.isOp())){
					p.teleport(man.getLobby());
				}
			}catch(Exception e1){}
		}
	}
	
	@EventHandler
	public void dmg(EntityDamageEvent e){
		if((e.getEntity() instanceof Player)){
			Player p = (Player)e.getEntity();
			if(PLAYERS.contains(p)&e.getCause()!=DamageCause.ENTITY_ATTACK){
				if(e.getCause()==DamageCause.FALL){e.setDamage(0);e.setCancelled(true);return;}
				if(e.getCause()==DamageCause.SUFFOCATION){Location l = p.getLocation();p.teleport(new Location(l.getWorld(),l.getX(),l.getY()+1,l.getZ()));}
				try{
					switch(status){
						case IN_GAME:
						case SUDDENDEATH:
						SmashVersePlayer ss = findPlayer(p);
						if(ss.isInvincible()){e.setCancelled(true);return;}
						ss.dmg((int)e.getDamage(),null);
						main.hM().addHolo(HologramAPI.createHologram(p.getLocation(), ss.fmtDmg()),false);
						Vector v = e.getEntity().getLocation().getDirection().multiply(-1);
						Vector fling = new Vector(v.getX()*getVDir(ss),((v.getY()>0)?v.getY()*1.5:0)+(v.getY()*getVDir(ss)),v.getZ()*getVDir(ss));
						p.setVelocity((fling.getY()<0)?fling.multiply(-1):fling);
						break;
						default:
					}
					e.setDamage(0);
				}catch(Exception e1){}
			}
		}
	}
	
	@EventHandler
	public void edmg(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(PLAYERS.contains(p)){
				if(e.getCause()==DamageCause.FALL){e.setDamage(0);e.setCancelled(true);return;}
				try{
					switch(status){
						case IN_GAME:
						case SUDDENDEATH:
						SmashVersePlayer ss = findPlayer(p);
						if(ss.isInvincible()){e.setCancelled(true);return;}
						ss.dmg((int)e.getDamage(),(Player)e.getDamager());
						main.hM().addHolo(HologramAPI.createHologram(p.getLocation(), ss.fmtDmg()),false);
						Vector v = e.getDamager().getLocation().getDirection();
						Vector fling = new Vector(v.getX()*getVDir(ss),getVDir(ss)+(v.getY()*getVDir(ss)),v.getZ()*getVDir(ss));
						p.setVelocity(fling);
						break;
						default:
					}
					e.setDamage(0);
				}catch(Exception e1){/* For Rediculous Calls */}
			}
		}
	}
	
	@EventHandler
	public void cSpawn(CreatureSpawnEvent e){
		try{
			if(man.calcDist(e.getLocation(), getSpawn())<(man.getMaxArenaSize()*.55)){
				e.setCancelled(true);
			}
		}catch(Exception e1){}
	}
	
	@EventHandler
	public void eChng(EntityChangeBlockEvent e){
		try{
			FallingBlock b = null;
			if(e.getEntity() instanceof FallingBlock){
				b = (FallingBlock)e.getEntity();
			}
			switch(getStatus()){
				case IN_GAME:
				case SUDDENDEATH:
				if(man.calcDist(b.getLocation(), getSpawn())<(man.getMaxArenaSize()*.55)){
					if(b.getMaterial()!=Material.CHEST){
						e.setCancelled(true);
					} else {
						items.add(new Item(this,b.getLocation()));
						for(Player p:PLAYERS){
							p.playSound(p.getLocation(), Sound.BLOCK_CHEST_LOCKED, .4f, 1);
						}
						e.setCancelled(true);
					}
				}
				break;
				default:
				if(man.calcDist(b.getLocation(), getSpawn())<(man.getMaxArenaSize()*.55)){
					e.setCancelled(true);
				}
			}
		}catch(Exception e1){}
	}
	
	@EventHandler
    public void place(BlockPlaceEvent e){
    	Player p = e.getPlayer();
    	if(PLAYERS.contains(p)){
    		e.setCancelled(true);
    	}
    }
	

	@EventHandler
	public void dmg(BlockDamageEvent e){
		Block b = e.getBlock();
		if(man.calcDist(b.getLocation(),getSpawn())<(man.getMaxArenaSize()*.55)){
			if(e.getBlock().getType() == Material.CHEST){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void bBoom(BlockExplodeEvent e){
		try{
			if(man.calcDist(e.getBlock().getLocation(), getSpawn())<(man.getMaxArenaSize()*.55)){
				for(;e.blockList().size()>0;){
					toReplace.add(new DBlock(e.blockList().get(0)));
					man.sendItFlying(e.blockList().get(0));
					e.blockList().remove(0);
				}
			}
		}catch(Exception e1){}
	}
	
	@EventHandler
	public void boom(EntityExplodeEvent e){
		try{
			if(man.calcDist(e.getLocation(), getSpawn())<(man.getMaxArenaSize()*.55)){
				for(;e.blockList().size()>0;){
					toReplace.add(new DBlock(e.blockList().get(0)));
					man.sendItFlying(e.blockList().get(0));
					e.blockList().remove(0);
				}
			}
		}catch(Exception e1){}
	}

	
	/* The Blackhole Powerup */
    @EventHandler
    public void pearl(PlayerTeleportEvent e) {
	    Player p = e.getPlayer();
	    if(PLAYERS.contains(p)){
	    	if (e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL){
	    		return;
	    	} else {
	    		e.setCancelled(true);
				new BlackHole(e.getTo(),this).useWeapon();
	    	}
	    }
    }
    
    /* All Other Powerups */
    @SuppressWarnings("deprecation")
    @EventHandler
	public void inter(PlayerInteractEvent e){
    	switch(getStatus()){
	    	case IN_GAME:
	    	case SUDDENDEATH:
	    		Player p = e.getPlayer();
	    		Location l = p.getLocation();
	    		boolean success = true;
	        	if(PLAYERS.contains(p)){
	        		switch(p.getItemInHand().getType()){
		        	case BLAZE_ROD:    /* The Laser Powerup */
		        		new Laser(this).beam(l, l.getDirection(),ChatColor.valueOf(findPlayer(p).getPlayerColor())+"█",20, p);
		        	break;
		        	case  TNT:
		    			l.getWorld().spawnEntity(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 5, l.getBlockZ()),EntityType.PRIMED_TNT);
		    			p.playSound(p.getLocation(), Sound.ENTITY_TNT_PRIMED, 1, 1);
		        	break;
		        	case FIREBALL:
		    			p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
		        		p.getWorld().spawn(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 3, l.getBlockZ()), Fireball.class).setDirection(p.getLocation().getDirection());
		        	break;
		        	case FEATHER:
		        		p.setVelocity(new Vector(0, 1, 0));
		    			p.playSound(p.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
		        	break;
		        	case END_CRYSTAL:
		        		if(!findPlayer(p).isInvincible()){
		            		new SuperStar(findPlayer(p)).useWeapon();
		        		}
		        	break;
		        	case STICK:
		        		new MagicStick(p).useWeapon();
		        	break;
		        	case COAL_BLOCK:
		        		new DarkMatter(p,this).useWeapon();
		        	break;
		        	case BLAZE_POWDER:
		        		if(storm == null){
			        		this.storm = new MeteorStorm(this);
		        		} else {
		        			success = false;
		        		}
		        	break;
		        	default:
		        	}
	        		if(success){
		        		p.getInventory().clear();
	        		}
	        	}
	    	break;	
	    	default:
    	}
    }
    
    /**
     * Sets The State Of A MeteorStorm
     * For This Arena.
     */
    public void setStorming(MeteorStorm me){
    	this.storm = me;
    }
	
    /**
     * Determines How Much Damage Percentage A Player
     * Will Take When They Get Hit.
     * 
     * @param s
     * @return
     */
	private double getVDir(SmashVersePlayer s){
		return (((s.getDamagePercent()*1.0)/(40*1.0))*2.0);
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(PLAYERS.contains(p)){
			leave(p);
		}
	}
	
	/**
	 * Regular Arena Boundary Helper Function
	 * 
	 */
	private void rbd(Player p){
		if(PLAYERS.contains(p)){
			if(man.calcDist(p.getLocation(),getSpawn())>(man.getMaxArenaSize()*.55)){
				findPlayer(p).death();
				man.corDir(p, getSpawn(),1,20);
			}
			checkVel(p.getVelocity(),p);
		} else if(SPECTATORS.contains(p)){
			if(man.calcDist(p.getLocation(), getSpawn())>(man.getMaxArenaSize()*.55)){
				man.corDir(p, getSpawn(),1,20);
			}
		}
	}
	
	/**
	 * Small Arena Boundary Helper Function
	 * 
	 */
	private void sbd(Player p){
		if(man.calcDist(p.getLocation(),getSpawn())>(0)){
			man.corDir(p, getSpawn(),1,20);
		}
	}
	
	/**
	 * The Arena Constructor
	 * 
	 * @param main
	 * @param slot
	 * @param man
	 */
	public Arena(Main main, int slot, ArenaManager man){
		this.main = main;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		this.ARENA_SLOT = slot;
		this.man = man;
		this.sorter = new Sorter();
		this.init = new ArenaDriver(this,0);
		this.driver = new ArenaDriver(this,1);
		init.start(100);
	}
	
	/**
	 * After A Short Delay While Other Sections
	 * Of The Software Initialize, We'll Continue
	 * With Startup By Preparing This Arena For Battle.
	 * 
	 * @return
	 */
	protected boolean go(){
		try{
			this.template = man.getTemplateManager().chooseMap();
			man.getTemplateManager().buildTemplate(this);
			setGameMode((Math.random()>=.5)?Mode.STOCK:Mode.FREE_FOR_ALL);
			this.ACTIVE_PLAYERS=0;
			PLAYERS.clear();
			SV_PLAYERS.clear();
			init.stop();
			driver.start(1);
		}catch(Exception e1){}
		return true;
	}
	
	/**
	 * This Is The Arena Tick Loop, Which Will Be
	 * Essential For Keeping Player Information And
	 * External Interfaces Current.
	 * 
	 * @return
	 */
	@SuppressWarnings("serial")
	protected boolean tick(){
		String bar = "";
		switch(status){
		case STARTING:
			scoreboard();
			if(System.currentTimeMillis()>=START_STAMP){
				setStatus(Status.IN_GAME);
				S_TIMESTAMP = System.currentTimeMillis();
				if(mode==Mode.FREE_FOR_ALL){
					F_TIMESTAMP = (System.currentTimeMillis()+(man.chooseDuration()*1000));
				}
				for(Player pp:PLAYERS){
					pp.setScoreboard(scoreboard.getBlankScoreboard());
				}
				for(int x=0;x<SV_PLAYERS.size();x++){
					if(x<PLAYERS.size()){
						GlowAPI.setGlowing((Entity)PLAYERS.get(x),Color.valueOf(SV_PLAYERS.get(x).getPlayerColor()),PLAYERS);
					} else {
						break;
					}
				}
				main.tM().sendTitle(new ArrayList<Player>(){{addAll(PLAYERS);addAll(SPECTATORS);}}, 0, 20, 5,dr+bo+"GO!!!","+"+main.gF().draw("-", 20, "")+"+");
			} else if(START_STAMP-System.currentTimeMillis()<3000){
				main.tM().sendTitle(new ArrayList<Player>(){{addAll(PLAYERS);addAll(SPECTATORS);}}, 0, 10, 5,re+bo+(((START_STAMP-System.currentTimeMillis())/1000)+1)+"","+"+main.gF().draw("-", 20, "")+"+");
			}
		break;
		case IN_GAME:
			for(int x = 0;x<SV_PLAYERS.size();x++){
				if((x==(SV_PLAYERS.size()/2))&(mode == Mode.FREE_FOR_ALL)){
					bar+=bl+bo+"  ["+wh+bo+fmtTime()+bl+bo+"]";
				}
				bar+=SV_PLAYERS.get(x).ABarStatus();
			}
			if(mode==Mode.FREE_FOR_ALL){
				if(System.currentTimeMillis()>=(F_TIMESTAMP-1000)){ /* Time's Up */
					setStatus(Status.ENDING);
				} else if(F_TIMESTAMP-System.currentTimeMillis()<6000){
					main.tM().sendTitle(new ArrayList<Player>(){{addAll(PLAYERS);addAll(SPECTATORS);}}, 0, 20, 5,dr+bo+(((F_TIMESTAMP-System.currentTimeMillis())/1000)),"+"+main.gF().draw("-", 20, "")+"+");
				}
			}
			for(Player p:new ArrayList<Player>(){{addAll(PLAYERS);addAll(SPECTATORS);}}){
				main.bM().sendActionBar(p, bar);
				p.setFoodLevel(20);
				p.setHealth(20.0);
			}
			br();sc();chkGM(SPECTATORS,GameMode.SPECTATOR);
		break;
		case ENDING:
			String ss = re+bo+((mode==Mode.STOCK)?"GAME!":"TIME!");
			main.tM().sendTitle(new ArrayList<Player>(){{addAll(PLAYERS);addAll(SPECTATORS);}}, 0, 100, 10,ss,"+"+main.gF().draw("-", 20, "")+"+");
			for(SmashVersePlayer s:SV_PLAYERS){
				s.stop();
				main.bM().sendActionBar(s.getPlayer(), ss);
			}
			if(endingD>0){
				endingD--;
			} else {
				setStatus(Status.FINISHED);
			}
		break;
		case SUDDENDEATH:
			for(SmashVersePlayer sv:SV_PLAYERS){
				bar+=sv.ABarStatus();
			}
			for(Player p:new ArrayList<Player>(){{addAll(PLAYERS);addAll(SPECTATORS);}}){
				main.bM().sendActionBar(p, bar);
				p.setFoodLevel(20);
			}
			br();sc();chkGM(SPECTATORS,GameMode.SPECTATOR);
		break;
		case FINISHED:
			if(finishedD>0){
				finishedD--;
			} else {
				setStatus(Status.CLEAR_READY);
				reset(true);
			}
		break;
		default:
		}
		return true;
	}
	
	/**
	 * Private Block Restoration
	 * Helper Function.
	 * 
	 */
	private void br(){
		if(toReplace.size()>0){
			toReplace.remove(0).setBlock();
		}
	}
	
	/**
	 * Private Chest Spawn
	 * Helper Function.
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void sc(){
		cCounter++;
		if(cCounter>40){
			cCounter = cCounter%40;
			Location l = new Location(getSpawn().getWorld(),((getSpawn().getX()-15)+Math.random()*30),(getSpawn().getY()+20),(getSpawn().getZ()-15)+Math.random()*30);
			FallingBlock fb = l.getWorld().spawnFallingBlock(l,Material.CHEST,new Byte("0"));
			fb.setVelocity(new Vector(0,1,0));
	    	fb.setDropItem(false);
		}
	}
	
	/**
	 * remItem
	 * 
	 */
	public void remItem(Item i){
		items.remove(i);
	}
	
	/**
	 * Sets The Current Number Of
	 * Active Players
	 * 
	 */
	protected Arena setActivePlayers(int active){
		if(ACTIVE_PLAYERS>0){
			this.ACTIVE_PLAYERS = active;
			if((mode == Mode.STOCK)&&(ACTIVE_PLAYERS)<2){
				setStatus(Status.ENDING);
			}
		}
		return this;
	}
	
	/**
	 * Returns The Number Of
	 * Active Players
	 * 
	 */
	public int getActivePlayerCount(){
		return ACTIVE_PLAYERS;
	}
	
	/**
	 * Sets The Status For This Arena
	 * 
	 * @param status
	 * @return
	 */
	public Arena setStatus(Status status){
		this.status = status;
		switch(status){
		case IN_GAME:
			for(Player p:PLAYERS){
				p.setGameMode(GameMode.SURVIVAL);
				p.setWalkSpeed(.4f);
				p.getInventory().clear();
			}
		break;
		case SUDDENDEATH:
			for(Player p:PLAYERS){
				p.playSound(p.getLocation(),Sound.ENTITY_ENDERDRAGON_GROWL,1,1);
			}
		break;
		case ENDING:
			for(Player p:PLAYERS){
				p.setGameMode(GameMode.SPECTATOR);
			}
		break;
		case FINISHED:
			scoreboard();
		break;
		default:
		}
		man.cLn("Arena "+getSlot()+" Changed To Status: "+getStatus());
		return this;
	}
	
	/**
	 * Returns This Current Status
	 * Of This Arena.
	 * 
	 * @return
	 */
	public Status getStatus(){
		return status;
	}
	
	/**
	 * Sets This Arena's Gamemode
	 * @param mode
	 * @return
	 */
	private Arena setGameMode(Mode mode){
		this.mode = mode;
		return this;
	}
	
	/**
	 * Returns This Arena's Gamemode
	 * 
	 * @return
	 */
	public Mode getGameMode(){
		return mode;
	}
	
	/**
	 * Returns The Template For This Arena
	 * Map.
	 * 
	 * @return
	 */
	public Template getTemplate(){
		return template;
	}
	
	/**
	 * Returns The Template Generation
	 * Percentage
	 * 
	 */
	public String genPercent(){
		return pro;
	}
	
	/**
	 * Sets The Template Generation
	 * Progress
	 * 
	 */
	public void setGenProgress(String pro){
		this.pro = pro;
	}
	
	/**
	 * Returns This Arena's Spawn
	 * @return
	 */
	public Location getSpawn(){
		return new Location(man.getOrigin().getWorld(),template.X()+(man.getOrigin().getX()+(man.getMaxArenaSize()*ARENA_SLOT)),(man.getOrigin().getY()+template.Y()),template.Z()+(man.getOrigin().getZ()+(man.getMaxArenaSize()*ARENA_SLOT)),template.P(),template.YA());		
	}
	
	/**
	 * Returns This Arena Slot
	 * 
	 * @return
	 */
	public int getSlot(){
		return ARENA_SLOT;
	}
	
	/**
	 * Returns The Arena Manager
	 * 
	 */
	public ArenaManager getMan(){
		return man;
	}
	
	/**
	 * Joins A Player Into This Arena
	 * 
	 * @param p
	 */
	protected void join(Player p){
		if(getStatus() == Status.WAITING){
			PLAYERS.add(p);
			ACTIVE_PLAYERS++;
			SV_PLAYERS.add(new SmashVersePlayer(main,p,((mode==Mode.STOCK)?man.getStartLives():0),man.getStartDmg(),(SV_PLAYERS.size()+1)));
			SV_PLAYERS.get(SV_PLAYERS.size()-1).setArena(this).setPlayerColor(playerColors[SV_PLAYERS.size()-1]);
			if(ACTIVE_PLAYERS>=man.getReqPlayers()){
				setStatus(Status.STARTING);
				START_STAMP = System.currentTimeMillis()+(man.getCountDownTime()*1000);
				for(Player pp:PLAYERS){
					pp.teleport(getSpawn());
				}
			}
		}
	}
	
	/**
	 * Allows An Outside Player To
	 * Being Spectating This Match.
	 * 
	 */
	public void spectate(Player p){
		switch(getStatus()){
			case IN_GAME:
			case SUDDENDEATH:
				man.leave(p);
				SPECTATORS.add(p);
				p.teleport(getSpawn());
				while(p.getGameMode()!=GameMode.SPECTATOR){
					p.setGameMode(GameMode.SPECTATOR);
				}
				p.sendMessage(main.getPfx()+gr+"You Are Now Spectating.");
			break;
			default:
		}
	}
	
	/**
	 * Removes A Player From This Arena
	 * 
	 * @param p
	 * @return
	 */
	protected boolean leave(Player p){
		try{
			if(PLAYERS.contains(p)){
				findPlayer(p).eliminate().stop();
				GlowAPI.setGlowing((Entity)p, false, PLAYERS);
				p.teleport(main.aM().getLobby());
				p.setGameMode(GameMode.SURVIVAL);
				p.setWalkSpeed(.2f);
				try{p.setScoreboard(scoreboard.getBlankScoreboard());}catch(Exception e1){}
				p.sendMessage(main.getPfx()+gr+"You Left - Welcome Back To The Lobby!");
				PLAYERS.remove(p);
				if((status!=Status.WAITING)&ACTIVE_PLAYERS<2){
					setStatus(Status.ENDING);
				}
				return true;
			} else if(SPECTATORS.contains(p)){
				p.teleport(main.aM().getLobby());
				p.setGameMode(GameMode.SURVIVAL);
				p.sendMessage(main.getPfx()+gr+"You Left - Welcome Back To The Lobby!");
				SPECTATORS.remove(p);
				return true;
			} else {
				return false;
			}
		}catch(Exception e1){
			e1.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Adds A Block To The Restore
	 * Block List.
	 * 
	 */
	public void addDBlock(Block b){
		if(!toReplace.contains(b)){
			toReplace.add(new DBlock(b));
		}
	}
	
	/**
	 * Sets Up The Scoreboard, Then Sends
	 * It To Each Of The Players
	 * 
	 */
	@SuppressWarnings("serial")
	private void scoreboard(){
		try{
			try{scoreboard.reset();}catch(Exception e1){}
			scoreboard = new SimpleScoreboard(re+bo+"__ Super SmashVerse __");
			scoreboard.blankLine();
			switch(status){
			case STARTING:
				scoreboard.add(re+bo+"Map:");
				scoreboard.add(re+template.getName());
				scoreboard.blankLine();
				scoreboard.add(go+bo+"Players:");
				scoreboard.add(go+ACTIVE_PLAYERS);
				scoreboard.blankLine();
				scoreboard.add(ye+bo+"Starting In:");
				scoreboard.add(ye+(((START_STAMP-System.currentTimeMillis())/1000)+1)+" Sec");
				scoreboard.blankLine();
				scoreboard.build();
				scoreboard.send(new ArrayList<Player>(){{addAll(PLAYERS);addAll(SPECTATORS);}});
			break;
			case FINISHED:
				SmashVersePlayer[] sss = getResults();
				if(sss[0]!=null){ /* On SuddenDeath This Will Be Null And The Scoreboard Does NOT Appear */
					scoreboard.add(re+bo+"GAME RESULTS:");
					scoreboard.blankLine();
					for(SmashVersePlayer s:sss){
						try{
							scoreboard.add(ChatColor.valueOf(s.getPlayerColor())+prefix(s.getStanding())+" "+((getGameMode()==Mode.FREE_FOR_ALL)?s.getName()+" : "+s.getPoints()+"pts":s.getName()));	
						} catch(Exception e1) {}
					}
					scoreboard.blankLine();
					scoreboard.build();
					scoreboard.send(new ArrayList<Player>(){{addAll(PLAYERS);addAll(SPECTATORS);}});
				}
			break;
			default:
			}
		}catch(Exception e1){}	
	}
	
	/**
	 * Returns The Results To Be Displayed On A Scoreboard
	 * 
	 * @return
	 */
	@SuppressWarnings("serial")
	private SmashVersePlayer[] getResults(){
		SmashVersePlayer[] svp = new SmashVersePlayer[SV_PLAYERS.size()];
		try{
			if(mode == Mode.STOCK){
				for(SmashVersePlayer pp:SV_PLAYERS){
					svp[pp.getStanding()-1] = pp;
				}
			} else if(mode == Mode.FREE_FOR_ALL){
				if(sdCheck()&&(ACTIVE_PLAYERS>=2)){
					setStatus(Status.SUDDENDEATH);
					setGameMode(Mode.STOCK);
					for(SmashVersePlayer sv:SV_PLAYERS){
						Player p = sv.setDamagePercent(man.getSuddentDeathDMG()).setLivesLeft(1).setKOd(false).getPlayer();
						p.teleport(getSpawn());
						p.setGameMode(GameMode.SURVIVAL);
					}
					main.tM().sendTitle(new ArrayList<Player>(){{addAll(PLAYERS);addAll(SPECTATORS);}}, 0, 20, 10, dr+bo+"SUDDEN DEATH!",re+bo+"GO!");
				} else {				
					for(SmashVersePlayer pp:SV_PLAYERS){
						svp[pp.getStanding()-1] = pp;
					}
				}
			}
		}catch(Exception e1){e1.printStackTrace();}
		return svp;
	}
	
	/**
	 * Should We Go To SuddenDeath?
	 * 
	 */
	private boolean sdCheck(){
		ArrayList<SmashVersePlayer> svl = sorter.sort(SV_PLAYERS);
		int hi = svl.get(0).getPoints();
		int occ = 0;
		for(SmashVersePlayer p:svl){
			if(hi==p.getPoints()){
				occ++;
			}
		}
		for(int x=occ;x<svl.size();x++){
			if(PLAYERS.contains(svl.get(x).getPlayer())){
				svl.get(x).eliminate();
			}
		}
		this.SV_PLAYERS = svl;
		return (occ>=2);
	}
	
	/**
	 * Returns A Standing Prefix
	 * 
	 */
	private String prefix(int i){
		return(i==1)?i+"st":(i==2)?i+"nd":(i==3)?i+"rd":i+"th";
	}
	
	/**
	 * Sends A Sound To All Players
	 * In This Arena
	 * 
	 */
	protected void sendSound(Sound sound,float vol,float pit){
		for(Player p:PLAYERS){
			p.playSound(p.getLocation(),sound,vol,pit);
		}
	}
	
	/**
	 * Sends A Message To All Players
	 * In This Arena
	 * 
	 */
	protected void sendMessage(String s){
		for(Player p:PLAYERS){
			p.sendMessage(s);
		}
	}
	
	/**
	 * Returns The Amount Of Time Until The Games Begins
	 * 
	 */
	protected String startingIn(){
		if(status == Status.STARTING){
			return ((START_STAMP-System.currentTimeMillis())/1000)+" Sec";
		} else {
			return "meh";
		}
	}
	
	/**
	 * Checks Player Velocity
	 * 
	 */
	private void checkVel(Vector v,Player p){
		if((Math.abs(v.getX())>=.65)|(Math.abs(v.getY())>=.65)|(Math.abs(v.getZ())>=.65)){
			p.playSound(p.getLocation(), Sound.ENTITY_CAT_HISS, .05f, .5f);
			main.hM().addHolo(HologramAPI.createHologram(p.getLocation(),ChatColor.valueOf(findPlayer(p).getPlayerColor())+"█"),false);
		}
	}
	
	/**
	 * Formats Time
	 * 
	 */
	private String fmtTime(){
		long time = (F_TIMESTAMP-System.currentTimeMillis());
		return (time/60000)+":"+((((time/1000)%60)<10)?"0"+((time/1000)%60):((time/1000)%60));
	}
	
	/**
	 * Returns The Start TimeStamp (Applies To Free For All GameMode)
	 * 
	 */
	protected long getSTimeStamp(){
		return S_TIMESTAMP;
	}
	
	/**
	 * Returns The Finish TimeStamp (Applies To Free For All GameMode)
	 * 
	 */
	protected long getFTimeStamp(){
		return F_TIMESTAMP;
	}
	
	/**
	 * Resets This Arena
	 * 
	 */
	protected void reset(boolean doRegen){
		try{
			this.endingD=40;
			this.finishedD=200;
			for(Player pp:PLAYERS){
				pp.teleport(man.getLobby());
				pp.getInventory().clear();
				pp.setScoreboard(scoreboard.getBlankScoreboard());
				pp.setGameMode(GameMode.SURVIVAL);
				pp.setWalkSpeed(.2f);
				try{GlowAPI.setGlowing((Entity)pp.getPlayer(), false, PLAYERS);}catch(Exception e1){}
			}
			for(Player pp:SPECTATORS){
				pp.teleport(man.getLobby());
				pp.setGameMode(GameMode.SURVIVAL);
				pp.setScoreboard(scoreboard.getBlankScoreboard());
			}
			for(SmashVersePlayer pp:SV_PLAYERS){
				pp.stop();
			}
			this.sorter = new Sorter();
			this.PLAYERS = new ArrayList<Player>();
			this.SPECTATORS = new ArrayList<Player>();
			this.SV_PLAYERS = new ArrayList<SmashVersePlayer>();
			this.ACTIVE_PLAYERS = 0;
			driver.stop();
			if(doRegen){
				go();
			}
		}catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	/**
	 * Finds A Specific SmashVerse Player
	 * 
	 * @param p
	 * @return
	 */
	public SmashVersePlayer findPlayer(Player p){
		for(SmashVersePlayer svp:SV_PLAYERS){
			if(p.getUniqueId().toString().equalsIgnoreCase(svp.getUUID().toString())){
				return svp;
			}
		}
		return null;
	}
	
	/**
	 * Returns All The Players
	 * 
	 * @return
	 */
	public ArrayList<Player> getPlayers(){
		return PLAYERS;
	}
	
	/**
	 * Returns All The Spectators
	 * 
	 */
	public ArrayList<Player> getSpectators(){
		return SPECTATORS;
	}
	
	/**
	 * Check GameMode
	 * 
	 */
	public void chkGM(ArrayList<Player> a,GameMode m){
		for(Player p:a){
			if(p.getGameMode()!=m){
				p.setGameMode(m);
			}
		}
	}
	
	/**
	 * Returns Reference To Main
	 * 
	 * @return
	 */
	protected Main getMain(){
		return main;
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */

}