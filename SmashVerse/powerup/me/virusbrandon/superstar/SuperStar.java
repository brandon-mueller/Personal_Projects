package me.virusbrandon.superstar;

import me.virusbrandon.game.SmashVersePlayer;
import me.virusbrandon.smashverse.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.inventivetalent.glow.GlowAPI;
import org.inventivetalent.glow.GlowAPI.Color;

import de.inventivegames.hologram.HologramAPI;

public class SuperStar {
	Runnable timer = new Runnable() {
		public void run() {
			try{
				duration--;
				cI++;
				cI=cI%gColors.length;
				delay++;
				if(delay>=2){
					delay = 0;
					for(Player p:svp.getCurrentArena().getPlayers()){
						p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BASS,.5f,((float)(.5+(Math.random()*1.5))));
					}
				}
				if(duration<=0){
					stop();
					svp.setInvincible(false);
					GlowAPI.setGlowing((Entity)svp.getPlayer(),Color.valueOf(svp.getPlayerColor()),svp.getCurrentArena().getPlayers());
				} else {
					Location l = svp.getPlayer().getLocation();
					svp.getCurrentArena().getMan().getMain().hM().addHolo(HologramAPI.createHologram(new Location(l.getWorld(),((l.getX()-3)+(Math.random()*6)),((l.getY()-3)+(Math.random()*6)),((l.getZ()-3)+(Math.random()*6))), ChatColor.valueOf(svp.getPlayerColor())+bo+"I'm Invincible! :D"),false);
					GlowAPI.setGlowing((Entity)svp.getPlayer(),gColors[cI],svp.getCurrentArena().getPlayers());
				}
			}catch(Exception e1){}
		}	
	};
	
	private int id;
	private int duration = 200;
	private int cI = 0;
	private int delay = 0;
	private SmashVersePlayer svp;
	private String bo=ChatColor.BOLD+"";
	private Color[] gColors = new Color[]{Color.RED,Color.YELLOW,Color.AQUA,Color.GREEN};
	
	public SuperStar(SmashVersePlayer svp){
		this.svp=svp;
	}
	
	public void useWeapon(){
		svp.setInvincible(true);
		start(1);		
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
