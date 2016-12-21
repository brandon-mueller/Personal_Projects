package me.virusbrandon.bosscompass;

import me.virusbrandon.bosscompass.CompassSetting.Mode;

import org.bukkit.entity.Player;

public class Pref {
	private String uuid;
	private Mode mode = Mode.COMPASS; /* Default Mode */
	
	/**
	 * Prefs Constructor
	 * 
	 * @param p
	 */
	public Pref(Player p){
		this.uuid = p.getUniqueId().toString();
	}
	
	/**
	 * Returns The Player UUID That Is
	 * Associated With These Prefs
	 * 
	 * @return
	 */
	public String UUID(){
		return uuid;
	}
	
	/**
	 * Switches The Mode Of The
	 * Boss Compass For This Player.
	 * 
	 * @return
	 */
	public String switchMode(){
		this.mode = ((mode==Mode.COMPASS)?Mode.CLOCK:Mode.COMPASS);
		return "Compass Mode Switched To: "+((mode==Mode.COMPASS)?"Compass":"Clock");
	}
	
	/**
	 * Returns The Currently Mode
	 * For This Players Boss Compass.
	 * 
	 * @return
	 */
	public Mode getMode(){
		return mode;
	}
}
