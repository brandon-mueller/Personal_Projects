package me.virusbrandon.hudhb;

import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

/**
 * Encalsulated ArmorStand Object...
 * 
 * I wanted to string all the setters
 * together in the "open" function of the
 * HUD Class, Because... Less Lines Of Code FTW!
 * 
 * @author Brandon
 *
 */

public class EncapArmorStand {
	private ArmorStand stand;
	
	public EncapArmorStand(ArmorStand stand){
		this.stand = stand;
	}
	
	public EncapArmorStand setVisible(boolean b){
		stand.setVisible(b);
		return this;
	}
	
	public EncapArmorStand setInvulnerable(boolean b){
		stand.setInvulnerable(b);
		return this;
	}
	
	public EncapArmorStand setSmall(boolean b){
		stand.setSmall(b);
		return this;
	}
	
	public EncapArmorStand setGravity(boolean b){
		stand.setGravity(b);
		return this;
	}
	
	public EncapArmorStand setCollidable(boolean b){
		stand.setCollidable(b);
		return this;
	}
	
	public EncapArmorStand setItemInHand(ItemStack i){
		stand.setItemInHand(i);
		return this;
	}
	
	public EncapArmorStand setRightArmPose(EulerAngle a){
		stand.setRightArmPose(a);
		return this;
	}
	
	public EncapArmorStand setCustomName(String s){
		stand.setCustomName(s);
		return this;
	}
	
	public ArmorStand getResultingArmorStand(){
		return stand;
	}
}
