package me.virusbrandon.game;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

public class LobbySign {
	private Location loc;
	private Vector v;
	private Block b;
	private Sign sign;
	private String bl=ChatColor.BLACK+"",gr=ChatColor.GREEN+"";
	
	public LobbySign(Location loc){
		this.loc = loc;
		this.b = loc.getWorld().getBlockAt(loc);
		this.v = b.getLocation().getDirection();
		setText(new String[]{bl+"Sign",bl+"Detected..","",gr+"Using It"});
	}
	
	public void setText(String[] text){
		try{
			sign = ((Sign)loc.getWorld().getBlockAt(loc).getState());
			for(int x=0;x<4;x++){
				sign.setLine(x, text[x]);
			}
		}catch(Exception e1){
			b.setType(Material.WALL_SIGN);
			b.getLocation().setDirection(v);	
		}
		sign.update();
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */