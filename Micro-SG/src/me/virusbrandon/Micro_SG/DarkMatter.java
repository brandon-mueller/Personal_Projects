package me.virusbrandon.Micro_SG;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class DarkMatter {
	private Player victim;
	private Location target;
	private Arena arena;
	private int w,x,y,z;
	private int xDir,yDir,zDir;
	private double dist;
	private int val = 0;
	private Random r = new Random();
	
	DarkMatter(Location l,Arena arena){
		this.x = l.getBlockX();
		this.y = l.getBlockY();
		this.z = l.getBlockZ();
		for(int a = 0;a<Bukkit.getWorlds().size();a++){
			if(Bukkit.getWorlds().get(a).getName().equalsIgnoreCase(l.getWorld().getName())){
				this.w = a;
			}
		}
		this.arena = arena;
	}
	
	public void tick(){
		try{
			xDir=0;yDir=0;zDir=0;
			Bukkit.getWorlds().get(w).getBlockAt(x,y,z).setType(Material.AIR);
			for(Player p:arena.getPlayers()){
				dist = calcDist(p.getLocation(),getDMLoc());
				if((victim == null) && (dist <=10)){
					if(!p.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
						this.victim = p;
						this.target = null;
						if(arena.isFinished()==false){
							p.sendMessage("\n"+arena.getM().e()+"DARK MATTER LOCKED ON!!!");
							p.playSound(p.getLocation(),Sound.ANVIL_LAND,2,1);
						}
					}
				} else {
					if(dist <= 10){
						if(dist <= 2){
							p.playSound(p.getLocation(), Sound.SLIME_ATTACK, 1,.5F);
							p.damage(2);
							p.setVelocity(p.getLocation().getDirection().multiply(3));
						} else {
							p.playSound(p.getLocation(), Sound.ZOMBIE_INFECT,((float)(.5+(dist*.2))),((float)(2.0-(dist*.15))));
						}
					}
				}
			}
			for(Player p:arena.getSpectators()){
				dist = calcDist(p.getLocation(),getDMLoc());
				if(dist <= 10){
					p.playSound(p.getLocation(), Sound.ZOMBIE_INFECT,((float)(.5+(dist*.2))),((float)(2.0-(dist*.15))));
				}
			}
			if(victim == null){
				if(target==null){
					target=setTarget();
				}
				switch(r.nextInt(3)){
					case 0:
						if(target.getBlockX()<x){
							xDir = -1;
						} else {
							xDir = 1;
						}
						
					break;
					case 1:
						if(target.getBlockY()<y){
							yDir = -1;
						} else {
							yDir = 1;
						}
					break;
					case 2:
						if(target.getBlockZ()<z){
							zDir = -1;
						} else {
							zDir = 1;
						}
					break;
				}
				if(calcDist(target,getDMLoc())<2){
					this.target=setTarget();
				}
				x+=xDir;y+=yDir;z+=zDir;
			} else {
				switch(val){
					case 0:
						if(victim.getLocation().getBlockX()<x){
							x--;
						} else {
							x++;
						}
					break;
					case 1:
						if(victim.getLocation().getBlockY()<y){
							y--;
						} else {
							y++;
						}
					break;
					case 2:
						if(victim.getLocation().getBlockZ()<z){
							z--;
						} else {
							z++;
						}
					break;
				}
				val++;
				val = val%3;
			}
			if(arena.isFinished()==false)
			Bukkit.getWorlds().get(w).getBlockAt(x,y,z).setType(Material.COAL_BLOCK);
		}catch(Exception e1){e1.printStackTrace();}
	}
	
	public Sound victimDied(Player p){
		if(victim.equals(p)){
			if(dist <= 2){
				for(Player pl:arena.getPlayers()){
					pl.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, .5f);
					pl.sendMessage("\n"+arena.getM().hT());
					pl.sendMessage(arena.getM().sd()+arena.getM().e()+p.getName()+" Was");
					pl.sendMessage(arena.getM().sd()+arena.getM().e()+"Digested By Dark Matter!");
					pl.sendMessage(arena.getM().hB());
				}
			}
			this.victim = null;
			return Sound.ENDERDRAGON_GROWL;
		}
		return null;
	}
	
	public Location getDMLoc(){
		return new Location(Bukkit.getWorlds().get(w),x,y,z);
	}
	
	public void setVictim(Player p){
		try{
			this.victim = p;
			this.target = null;
			p.sendMessage("\n"+arena.getM().e()+"Dark Matter LOCKED ON!!!");
			p.playSound(p.getLocation(),Sound.ANVIL_LAND,2,1);
		}catch(Exception e1){}
	}
	
	public Location setTarget(){
		Location l = arena.getSpawnPoints().get(r.nextInt(arena.getM().getMaxPlayers())).getSpawnPoint(arena.getSlotNumber());
		if(calcDist(getDMLoc(),l)>65|getDMLoc().getBlockY()<=2){
			return l;
		} else {
			int xx = r.nextInt(40);int yy = r.nextInt(40);int zz = r.nextInt(40);
			for(int x = 0;x<3;x++){
				switch(x){
				case 0:
					if(r.nextInt(2)==0){
						xx = (xx*-1)+l.getBlockX();
					} else {
						xx+=l.getBlockX();
					}
				break;
				case 1:
					if(r.nextInt(2)==0){
						yy = (yy*-1)+l.getBlockY();
					} else {
						yy+=l.getBlockY();
					}
				break;
				case 2:
					if(r.nextInt(2)==0){
						zz = (zz*-1)+l.getBlockZ();
					} else {
						zz+=l.getBlockZ();
					}
				break;
				}
			}
			return new Location(l.getWorld(),xx,yy,zz);
		}
	}
	
	public void setDMLoc(Location l){
		l.getWorld().getBlockAt(l).setType(Material.AIR);
		this.x = l.getBlockX();
		this.y = l.getBlockY();
		this.z = l.getBlockZ();
	}
	
	public double calcDist(Location loc1, Location loc2){
		return Math.sqrt(Math.pow(loc1.getBlockX()-loc2.getBlockX(),2)+Math.pow(loc1.getBlockY()- 
		loc2.getBlockY(),2)+Math.pow(loc1.getBlockZ()-loc2.getBlockZ(),2));
	}
}
