package me.virusbrandon.darkmatter;

import java.util.ArrayList;

import me.virusbrandon.game.Arena;
import me.virusbrandon.game.SmashVersePlayer;
import me.virusbrandon.smashverse.Main;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * I Am The Dark Matter...
 * 
 * I Don't Know Who You Are Or What
 * You Want, But If You Want Randsom, I Don't
 * Have Money, But What I Do Have Are A Unique
 * Set Of Skills... Skills That Make Me A Nightmare
 * For Players Like You.
 * 
 * Surrender Now And That Will Be The End Of It...
 * I Will Not Look For You...
 * 
 * But If You Do Not Surrender... I Will Look
 * For You... I Will Find You...
 * ... And I Will Kill You...
 *
 */
public class DarkMatter {
	Runnable timer = new Runnable() {
		public void run() {
			ar.getMan().sendItFlying(mine.getWorld().getBlockAt(mine));
			if((duration > 0)&&(dmgCsd<15)){
				Location t = target.getLocation();
				int x = mine.getBlockX(),y = mine.getBlockY(),z = mine.getBlockZ();
				x+=(((t.getX()==x)|sp.isKOd())?0:(t.getX()>x)?1:-1);
				y+=(((t.getY()==y)|sp.isKOd())?0:(t.getY()>y)?1:-1);
				z+=(((t.getZ()==z)|sp.isKOd())?0:(t.getZ()>z)?1:-1);
				mine = new Location(mine.getWorld(),x,y,z);
				mine.getWorld().getBlockAt(mine).setType(Material.COAL_BLOCK);
				if(ar.getMan().calcDist(mine, t)<2){
					target.playSound(target.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, .1f, .5f);
					if(!sp.isKOd()){
						sp.dmg(1, null);
						dmgCsd++;
					}
				}
				if((x==0)&(y==0)&(z==0)){} else {duration--;}
			} else {
				stop();
				mine.getWorld().playEffect(mine, Effect.EXPLOSION_HUGE, 2);
			}
		}	
	};
	
	private int id;
	private int duration = 100;
	private int dmgCsd = 0;
	private Arena ar;
	private Location mine;
	private Player target;
	private SmashVersePlayer sp;
	
	public DarkMatter(Player sender, Arena ar){
		this.ar = ar;
		this.mine = sender.getLocation();
		ArrayList<Player> pt = new ArrayList<>();
		for(Player p:ar.getPlayers()){
			if(!p.equals(sender)){
				pt.add(p);
			}
		}
		this.target = pt.get(((int)(Math.random()*pt.size())));
		this.sp = ar.findPlayer(target);
		mine.getWorld().getBlockAt(mine).setType(Material.COAL_BLOCK);
	}
	
	
	public void useWeapon(){
		start(3);		
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
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
