package me.virusbrandon.Micro_SG;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class Entity {
	Entity(){}
	public static void fireWork(Location f){
	    Firework fw = (Firework) f.getWorld().spawnEntity(f, EntityType.FIREWORK);
	    FireworkMeta fwm = fw.getFireworkMeta();
	    Type type = Type.BALL_LARGE;     
	    FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.RED).withFade(Color.RED).with(type).trail(true).build();
	    fwm.addEffect(effect);
	    fwm.setPower(0);
	    fw.setFireworkMeta(fwm);         
	}
}

/**
 * ©2014, UnderLordGames.com
 */