package me.virusbrandon.MC_Vegas;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class Entity {
	Entity(){}
	public static void fireWork(Location l){
		//Spawn the Firework, get the FireworkMeta.
	    Firework fw = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
	    FireworkMeta fwm = fw.getFireworkMeta();
	 
	    //Our random generator
	    Random r = new Random(); 

	    //Get the type
	    Type type = Type.BALL_LARGE;     
	    
	    //Get our random colours 
	    int r1i = r.nextInt(9) + 1;
	    int r2i = r.nextInt(9) + 1;
	    Color c1 = getColor(r1i);
	    Color c2 = getColor(r2i);
	 
	    //Create our effect with this
	    FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
	 
	    //Then apply the effect to the meta
	    fwm.addEffect(effect);
	 
	    //Generate some random power and set it
	    int rp = r.nextInt(2) + 1;
	    fwm.setPower(rp);
	 
	    //Then apply this to our rocket
	    fw.setFireworkMeta(fwm);         
	}
	
	private static Color getColor(int i){
		Color c = null;
		if(i==1){
			c=Color.AQUA;
		}
		if(i==2){
			c=Color.BLUE;
		}
		if(i==3){
			c=Color.LIME;
		}
		if(i==4){
			c=Color.NAVY;
		}
		if(i==5){
			c=Color.ORANGE;
		}
		if(i==6){
			c=Color.PURPLE;
		}
		if(i==7){
			c=Color.RED;
		}
		if(i==8){
			c=Color.WHITE;
		}
		if(i==9){
			c=Color.YELLOW;
		} 
		return c;
	}
}

/**
 * ©2014, UnderLordGames.com
 */