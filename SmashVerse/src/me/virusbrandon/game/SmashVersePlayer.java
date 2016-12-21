package me.virusbrandon.game;

import java.util.ArrayList;

import me.virusbrandon.smashverse.Main;
import me.virusbrandon.sv_utils.ArenaStatus.Mode;
import me.virusbrandon.sv_utils.ArenaStatus.Status;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import de.inventivegames.hologram.HologramAPI;

public class SmashVersePlayer {
	private Main main;
	private Player PLAYER;
	private Arena current;
	private int DMG_PER;
	private int START_LIVES;
	private int LIVES_LEFT;
	private int KOs = 0;
	private int standing = 1;
	private int PLAYER_NUMBER;
	private boolean ALIVE = true;
	private boolean KOd = false;
	private boolean INVINCIBLE = false;
	private String PLAYER_COLOR;
	private String PLAYER_UUID;
	private String PLAYER_NAME;
	private String wh = ChatColor.WHITE+"",go=ChatColor.GOLD+"",re=ChatColor.RED+"",dr=ChatColor.DARK_RED+"",bo=ChatColor.BOLD+"",ga=ChatColor.GRAY+"",bl=ChatColor.BLACK+"";
	private int id;
	private long respawn;
	private SmashVersePlayer lastHit;
	private SmashVersePlayer lastHitBy;
	
	Runnable timer = new Runnable() {
		public void run() {
			PLAYER.sendMessage(current.getMain().getPfx()+"Respawning In "+((respawn-System.currentTimeMillis())/1000)+" Seconds");
			if(System.currentTimeMillis()>=(respawn-1000)){
				stop();
				PLAYER.setGameMode(GameMode.SURVIVAL);
				PLAYER.teleport(current.getSpawn());
				KOd=false;
			}
		}	
	};
	
	/**
	 * The Super SmashVerse Player Constructor
	 * 
	 * Allows For The Initial Setup Of A Super
	 * SmashVerse Participant Prior To Match Begin.
	 * 
	 * @param PLAYER
	 * @param LIVES
	 * @param START_PERCENT
	 */
	public SmashVersePlayer(Main main, Player PLAYER, int LIVES, int START_PERCENT, int PLAYER_NUMBER){
		try{
			this.main = main;
			this.PLAYER = PLAYER;
			if(PLAYER == null){
				this.ALIVE = false;
			}
			this.START_LIVES = LIVES;
			this.LIVES_LEFT = START_LIVES;
			this.DMG_PER = START_PERCENT;
			this.PLAYER_UUID = PLAYER.getUniqueId().toString();
			this.PLAYER_NAME = PLAYER.getName();
			this.PLAYER_NUMBER = PLAYER_NUMBER;
		}catch(Exception e1){}
	}
	
	public SmashVersePlayer setArena(Arena current){
		this.current = current;
		return this;
	}
	
	/**
	 * Heal A Player
	 * 
	 */
	public boolean heal(int amt){
		if(DMG_PER>0){
			this.DMG_PER-=amt;
			if(DMG_PER<0){
				this.DMG_PER=0;
			}
		}
		return true;
	}
	
	/**
	 * Call When The Player's Health State Changes.
	 * 
	 * Allows For A Positive Integer To Be Passed In
	 * When The Player Takes Damage.
	 * 
	 * @param dmg
	 * @return
	 */
	public SmashVersePlayer dmg(int dmg,Player byPlayer){
		try{
			if(DMG_PER<999){
				this.DMG_PER+=(dmg*(3+(Math.random()*5)));
				if(DMG_PER>999){
					this.DMG_PER = 999;
				}
			}
			byPlayer.playSound(byPlayer.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
			PLAYER.playSound(PLAYER.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
			if(lastHitBy != null){
				lastHitBy.setLastHit(null);
			}
			setLastHitBy(current.findPlayer(byPlayer));
			lastHitBy.setLastHit(this);
		}catch(Exception e1){}
		return this;
	}
	
	/**
	 * The Players Has Either Left Or
	 * Has Been Defeated.
	 * 
	 */
	public SmashVersePlayer eliminate(){
		this.ALIVE = false;
		this.standing=current.getActivePlayerCount();
		current.setActivePlayers(current.getActivePlayerCount()-1);
		return this;
	}
	
	/**
	 * The Player Has KOd
	 * Another Player
	 * 
	 */
	public SmashVersePlayer kill(){
		if(current.getGameMode()==Mode.FREE_FOR_ALL){
			this.KOs++;
			lastHit.setLastHitBy(null);
			setLastHit(null);
		}
		return this;
	}
	
	/**
	 * The Player Has Died
	 * 
	 */
	@SuppressWarnings("serial")
	public SmashVersePlayer death(){
		if(!KOd){
			this.KOd = true;
			PLAYER.setGameMode(GameMode.SPECTATOR);
			koDisp();
			Location l = PLAYER.getLocation();
			PLAYER.getWorld().createExplosion(l, 3);
			PLAYER.playSound(l,Sound.ENTITY_BLAZE_DEATH, 1, .5f);
			if(current.getGameMode()==Mode.FREE_FOR_ALL){
				this.KOs--;
				try{
					lastHitBy.kill();
				}catch(Exception e1){}
			} else {
				this.LIVES_LEFT--;
				if(LIVES_LEFT<1){
					eliminate();
					if(current.getActivePlayerCount()>=2){
						ChatColor c = ChatColor.valueOf(PLAYER_COLOR);
						current.getMain().tM().sendTitle(new ArrayList<Player>(){{addAll(current.getPlayers());addAll(current.getSpectators());}}, 5, 40, 5,c+bo+"Player "+PLAYER_NUMBER,dr+bo+"DEFEATED!");
					}
					return this;
				}
			}
			respawn = (System.currentTimeMillis()+(current.getMan().getRespawnTime()*1000));
			DMG_PER=(current.getStatus()==Status.SUDDENDEATH)?current.getMan().getSuddentDeathDMG():0;
			start(20);
		}
		return this;
	}
	
	private void koDisp(){
		Location l = PLAYER.getLocation();
		double d = l.getY();
		String[] s = current.getMan().KOtext(ChatColor.valueOf(PLAYER_COLOR));
		for(int x=0;x<s.length;x++){
			main.hM().addHolo(HologramAPI.createHologram(new Location(l.getWorld(),l.getX(),d-(.3*x),l.getZ()),s[x]),100,true);
		}
	}
	
	/**
	 * Call To Set The Player's Color
	 * 
	 */
	public SmashVersePlayer setPlayerColor(String PLAYER_COLOR){
		this.PLAYER_COLOR = PLAYER_COLOR;
		return this;
	}
	
	/**
	 * Returns The Name Of This
	 * SmashVerse Player's Color.
	 * 
	 */
	public String getPlayerColor(){
		return PLAYER_COLOR;
	}
	
	/**
	 * Returns The Players Damage
	 * Percentage To Be Displayed
	 * On The Actionbar
	 * 
	 * @return
	 */
	public String ABarStatus(){
		if(PLAYER_COLOR!=null){
			return bl+bo+"  [ "+fmtDmg()+((current.getGameMode()==Mode.STOCK)?((LIVES_LEFT<10)?main.gF().draw(ChatColor.valueOf(PLAYER_COLOR)+"▍",LIVES_LEFT,"")+main.gF().draw(ga+"▍",(((START_LIVES>10)?10:START_LIVES)-LIVES_LEFT),""):ChatColor.valueOf(PLAYER_COLOR)+"▍ x"+LIVES_LEFT)+bl+bo+" ]":(ChatColor.valueOf(PLAYER_COLOR)+bo+KOs+" Pts"+bl+bo+" ]"));
		} else {
			return dr+"ERR_001";
		}
	}
	
	/**
	 * Returns Formatted Dmg Value
	 * 
	 */
	public String fmtDmg(){
		return dmgClr()+((ALIVE&(!KOd))?(DMG_PER<100?(DMG_PER<10?"00"+DMG_PER:"0"+DMG_PER):DMG_PER):"---")+"%  ";
	}
	
	/**
	 * Returns The ChatColor Associated
	 * With How Much Damage This Player Has
	 * Taken In Battle.
	 * 
	 */
	private String dmgClr(){
		return((DMG_PER<200&ALIVE)?(DMG_PER<150)?(DMG_PER<100?(DMG_PER<50?wh:go):re):dr:dr)+bo;
	}
	
	/**
	 * Returns The Minecraft Player
	 * Associated With This SmashVerse
	 * Player.
	 * 
	 */
	public Player getPlayer(){
		return PLAYER;
	}
	
	/**
	 * Returns The SmashVerse Player's UUID
	 * 
	 */
	public String getUUID(){
		return PLAYER_UUID;
	}
	
	/**
	 * Returns The SmashVerse Player's Name
	 * 
	 */
	public String getName(){
		return PLAYER_NAME;
	}
	
	/**
	 * Returns This SmashVerse Players's
	 * Current Damage Percentage.
	 * 
	 */
	public int getDamagePercent(){
		return DMG_PER;
	}
	
	/**
	 * Sets This SmashVerse Player's
	 * Current Damage Percentage. (Sudden Death Usually)
	 * 
	 */
	public SmashVersePlayer setDamagePercent(int percent){
		this.DMG_PER = percent;
		return this;
	}
	
	/**
	 * Returns This SmashVersePlayer's
	 * Current Number Of Lives Left
	 * 
	 */
	public int getLivesLeft(){
		return LIVES_LEFT;
	}
	
	/**
	 * Sets This SmashVersePlayers's
	 * Current Number Of Lives Left
	 * 
	 */
	public SmashVersePlayer setLivesLeft(int lives){
		this.LIVES_LEFT = lives;
		return this;
	}
	
	/**
	 * Returns Standing
	 * 
	 */
	public int getStanding(){
		return standing;
	}
	
	/**
	 * Returns Points
	 * 
	 */
	public int getPoints(){
		return KOs;
	}
	
	/**
	 * Sets KOs
	 * 
	 */
	public void setPoints(int KOs){
		this.KOs=KOs;
	}
	
	/**
	 * Returns Your Player Number (Player 1 For Example)
	 * 
	 */
	public int getPlayerNumber(){
		return PLAYER_NUMBER;
	}
	
	/**
	 * Sets The Last Person
	 * You Were Hit By.
	 * 
	 */
	public void setLastHitBy(SmashVersePlayer lastHitBy){
		this.lastHitBy = lastHitBy;
	}
	
	/**
	 * Returns Whether You Are Currently
	 * Waiting To Respawn.
	 * 
	 */
	public boolean isKOd(){
		return KOd;
	}
	
	/**
	 * Sets This Players As KOd Or Not.
	 * 
	 */
	public SmashVersePlayer setKOd(boolean KOd){
		this.KOd = KOd;
		return this;
	}
	
	/**
	 * Returns Whether You Are Currently
	 * Invincible Or Not.
	 * 
	 */
	public boolean isInvincible(){
		return INVINCIBLE;
	}
	
	/**
	 * Sets This Player As Invincible Or Not.
	 * 
	 */
	public SmashVersePlayer setInvincible(boolean INVINCIBLE){
		this.INVINCIBLE = INVINCIBLE;
		return this;
	}
	
	/**
	 * Sets The Last Person
	 * You Hit.
	 * 
	 */
	public void setLastHit(SmashVersePlayer lastHit){
		this.lastHit = lastHit;
	}
	
	/**
	 * Returns The Current Arena That This
	 * Player Is In.
	 * 
	 */
	public Arena getCurrentArena(){
		return current;
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	public void start(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay, delay);
	}
	
	public void start(int delay1, int delay2){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay1, delay2);
	}
	
	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	public void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */
