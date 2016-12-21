package me.virusbrandon.MC_Vegas;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Slot implements Listener{
	@EventHandler
	public void onClick(InventoryClickEvent e){
		try{
			if(e.getWhoClicked().getName().equalsIgnoreCase(p.getName())){
				if(e.getInventory().getTitle().contains(bl+bo+"Slots Menu ")){
					e.setCancelled(true);
				}
			}
		} catch(Exception e1){}
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		try{
			if(e.getPlayer().getName().equalsIgnoreCase(p.getName())){
				p.teleport(e.getFrom());
			}
		} catch(Exception e1){}
	}
	
	@EventHandler
	public void blockbreak(BlockBreakEvent e){
		try{
			if(e.getPlayer().getName().equalsIgnoreCase(p.getName())){
				e.setCancelled(true);
			}
		} catch(Exception e1){}
	}
	
	@EventHandler
	public void cmd(PlayerCommandPreprocessEvent e){
		try{
			if(e.getPlayer().getName().equalsIgnoreCase(p.getName())){
				e.setCancelled(true);
				e.getPlayer().sendMessage(w()+"Commands Are Disabled Here");
			}
		} catch(Exception e1){}
	}
	
	Runnable reelTimer = new Runnable() {
		public void run() {
			fillReels();
		}	
	};
	Runnable spinmusic = new Runnable() {
		public void run() {
			sm+=dir;
			int i = (int)(Math.random()*(s.length));
			p.playSound(p.getLocation(), s[i], 1, sm);
			if(sm >= 2.0){
				dir = -0.1F;
			}
			if(sm <= 0.5F){
				dir = 0.1F;
			}
		}	
	};
	Runnable winsounds = new Runnable() {
		public void run() {
			cnt++;
			cnt2++;
			cnt2=(cnt2%10);
			jackpot(cnt2);
			ws+=dir2;
			p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, ws);
			if(ws<=1){
				ws=2.0F;
			}
			if(cnt > 109){
				cnt = 0;
				Bukkit.getScheduler().cancelTask(id3);
			}
		}	
	};
	
	Runnable outofcoins = new Runnable() {
		public void run() {
			try{
				if(count1 == 0){
					p.playSound(p.getLocation(), Sound.WOLF_HOWL, .5F, 2);
					p.sendMessage(e()+"You Ran Out Of Coins!");
					p.sendMessage(w()+"Purchase More On Our Website!");
					count1++;
				} else if(count1 == 1){
					Bukkit.getScheduler().cancelTask(id4);
					try{m.playerLeft(p);}catch(Exception e1){}
				}
			} catch(Exception e){
				Bukkit.getScheduler().cancelTask(id4);
				e.printStackTrace();
			}
			
		}	
	};
	
	ArrayList<Block> blocks;
	//ArrayList<Win> wins;
	Random r = new Random();
	SlotManager m;
	Player p;
	Sound[] s = new Sound[]{Sound.HORSE_JUMP};
	float sm = 2.0F;
	float ws = 2.0F,cnt = 0;
	float dir = -0.1F,dir2 = -0.2F;
	int cnt2 = 0;
	int counter = 25; //Block To Cycle Through The Reels Before It Stops
	int space = 5;
	int count = 0,count1 = 0,id,id2,id3,id4;
	int reelSize = 25;
	int[] reel1 = new int[3];int[] reel2 = new int[3];int[] reel3 = new int[3];
	int[] pB = new int[]{1,2,3,56,155,174,24,73,129,14,4,5,7}; //Possible Blocks On A Reel
	int[][] reels = new int[3][]; //Slot Machine Reels
	int[] stoppedOn = new int[3];
	int x,y,z,w; //This Slot Machine Origin Point
	int zw,yh,xw,aux;
	int r1Pos = 0,r2Pos = 0,r3Pos = 0;
	GUI g = new GUI();
	Win win;
	boolean open,spinning = true;
	String gr = ChatColor.GREEN +"",re=ChatColor.RED+"",ye=ChatColor.YELLOW+"",bo=ChatColor.BOLD+"",wh=ChatColor.WHITE+"",go=ChatColor.GOLD+"",aq = ChatColor.AQUA+"",bl=ChatColor.BLACK+"";
	Slot(SlotManager m,Location l, ArrayList<Block> blocks, Player p){
		Bukkit.getServer().getPluginManager().registerEvents(this,Main.getPlugin(Main.class));
		this.m = m;
		this.x = l.getBlockX();
		this.y = l.getBlockY();
		this.z = l.getBlockZ();
		for(int a = 0;a<Bukkit.getWorlds().size();a++){
			if(Bukkit.getWorlds().get(a).getName().equalsIgnoreCase(l.getWorld().getName())){
				this.w = a;
			}
		}
		this.blocks = blocks;
		makeReels();
		fillReels();
		this.p = p;
		this.open = false;
		p.teleport(new Location(Bukkit.getWorlds().get(w),(x+3.5),(y+1),(z+2.5)));
		p.getInventory().clear();
		ItemStack it = new ItemStack(Material.ENDER_PEARL,1);
		ItemMeta met = it.getItemMeta();
		for(int i = 0;i<9;i++){
			met.setDisplayName(wh+bo+"Slots Menu " + (i+1));
			it.setItemMeta(met);
			p.getInventory().setItem(i,it);
		}
	}
	
	//Starts The Slot Machine
	public String spin(){
		makeReels();
		counter = r.nextInt(25)+10;
		space = r.nextInt(15)+5;
		if(spinning == false){
			spinning = true;
			count = 0;
			id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), reelTimer, 2, 2);
			id2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), spinmusic, 1, 1);
			m.addPlay();
			return s()+"Spinning...";
		} else {
			return e()+"A Spin Is Already In Progress!";
		}
	}
	
	public boolean isSpinning(){
		return spinning;
	}
	
	//Advance Method Advances The Third Reel For Premature Triple Matches
	public void advance(){
		fillReels();
		checkReels();
	}
	
	//Builds The Slot Machine From A Template When A Player Joins
	@SuppressWarnings("deprecation")
	public void buildMachine(int xw,int zw,int yh){
		try{
			int i = 0;
			if(zw >= 2){
				aux = (zw-2);
			} else {aux = zw;}
			this.zw = zw;this.yh = yh;this.xw = xw;
			for(int a = x;a<(x+xw)+2;a++){
				for(int b = z;b < (z+zw)+1;b++){
					for(int c = y;c<(y+yh)+1;c++){
						Bukkit.getWorlds().get(w).getBlockAt(a,c,b).setTypeId(blocks.get(i).getID());
						Bukkit.getWorlds().get(w).getBlockAt(a,c,b).setData(blocks.get(i).getData());
						i++;
					}
				}
			}
		} catch(Exception e1){}
		fillReels();
		spinning = false;
	}
	
	//Destroys The Slot Machine When A Play Is Finished
	@SuppressWarnings("deprecation")
	public void destroyMachine(int xw,int zw, int yh){
		Bukkit.getScheduler().cancelTask(id);
		Bukkit.getScheduler().cancelTask(id2);
		Bukkit.getScheduler().cancelTask(id3);spinning = false;
		this.p = null;
		for(int a = (x+xw);a>(x-xw);a--){
			for(int b = y;b<(y+(yh+1));b++){
				for(int c = (z+zw);c>(z-zw);c--){
					Bukkit.getWorlds().get(w).getBlockAt(a,b,c).setTypeId(0);
				}
			}
		}
		this.p = null;
	}
	
	@SuppressWarnings("deprecation")
	public void fillReels(){
		if(spinning == true){
			for(int i = 0;i < 3;i++){
				if(count < (counter)){
					if(count == (counter-1)){
						stoppedOn[0] = reels[0][r1Pos];
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 5, 1);
					}
					r1Pos--;
					if(r1Pos<=0){r1Pos = reels[0].length-1;} else {r1Pos=r1Pos%reels[0].length;}
					Bukkit.getWorlds().get(w).getBlockAt((x+5),((y+1)+(i)),z+(aux)).setTypeId(reels[0][r1Pos]);
				} else {
					checkBlock(0);
				}
				if(count < counter + space){
					if(count == ((counter+space)-1)){
						stoppedOn[1] = reels[1][r2Pos];
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 5, 1);
					}
					r2Pos--;
					if(r2Pos<=0){r2Pos = reels[1].length-1;} else {r2Pos=r2Pos%reels[1].length;}
					Bukkit.getWorlds().get(w).getBlockAt((x+3),((y+1)+(i)),z+(aux)).setTypeId(reels[1][r2Pos]);
				} else {
					checkBlock(1);
				}
				if(count < counter + (space*2)){
					if(count == ((counter+(space*2))-1)){stoppedOn[2] = reels[2][r3Pos];}
					r3Pos--;
					if(r3Pos<=0){r3Pos = reels[2].length-1;} else {r3Pos=r3Pos%reels[2].length;}
					Bukkit.getWorlds().get(w).getBlockAt((x+1),((y+1)+(i)),z+(aux)).setTypeId(reels[2][r3Pos]);
				} else {
					Bukkit.getScheduler().cancelTask(id);
					Bukkit.getScheduler().cancelTask(id2);
					checkReels();
					break;
				}	
			}
			if(r1Pos+2<reels[0].length){r1Pos+=2;};if(r2Pos+2<reels[1].length){r2Pos+=2;}if(r3Pos+2<reels[2].length){r3Pos+=2;};
			count++;
		}
	}
	
	public void makeReels(){
		for(int x = 0;x<3;x++){
			reels[x] = new int[reelSize];
			for(int y = 0;y<reelSize;y++){
				reels[x][y] = pB[r.nextInt(pB.length)];
			}
			int i;
			for(int a = 0;a<8;a++){
				for(int y = 0;y<m.getWins().size();y++){
					i = r.nextInt(reels[x].length);
					reels[x][i] = m.getWins().get(y).getID();
				}
			}
		}
	}
	
	public void checkReels(){
		if(stoppedOn[0] == stoppedOn[1]){
			if(stoppedOn[1] == stoppedOn[2]){
				for(int d = 0;d<m.getWins().size();d++){
					if(stoppedOn[2]==m.getWins().get(d).getID()){
						if(m.getPlays()<m.getWins().get(d).getRequired()){
							counter++;
							advance();
							break;
						} else {
							checkBlock(2);
							win = m.getWins().get(d);
							id3 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), winsounds, 1, 1);
							if(win.getCmd()!=0){
								win.runCmd(p);
							}
							m.recordWinner(m.getWins().get(d), p, new Date());
							m.announceWinner(p,win);
							m.setPlays(0);
							spinning = false;
							m.getWins().get(d).setAvailable(m.getWins().get(d).getAvailable()-1);
							if(m.getWins().get(d).getAvailable()<1){
								m.getWins().remove(d);
								makeReels();fillReels();
							}
							Main.pO(p).setBalance(Main.pO(p).getBalance()+1);
							break;
						}
					}
				}
			} else {checkBlock(2);}
		} else {checkBlock(2);}
		p.playSound(p.getLocation(), Sound.NOTE_PLING, 5, 1);
		spinning = false;
		if(Main.pO(p).getBalance() < 1){
			p.closeInventory();
			p.getInventory().clear();
			id4 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), outofcoins, 60, 60);
		}
	}
	
	public void checkBlock(int b){
		if(count == counter + (space*b)){
			for(int a = 0;a<m.getWins().size();a++){
				if(stoppedOn[b] == m.getWins().get(a).getID()){
					Location l = new Location(Bukkit.getWorlds().get(w),(x+5)-(b*2),(y+2),z+(aux));
					l.getWorld().playEffect(l, Effect.POTION_BREAK, 2);
				}
			}
		}
	}
	
	public String append(int i){
		String add = "";
		for(;i>0;i--){
			add+=" █ ";
		}
		return add;
	}
	
	public void jackpot(int i){
		p.sendMessage(wh+bo+append(cnt2));
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage(go+bo+"JACKPOT HIT!!!!");
		p.sendMessage(aq+bo+"By Player: " + p.getName());
		p.sendMessage(gr+bo+"Prize Won: " + win.getPrize());
		p.sendMessage(re+bo+"CONGRATULATIONS!!!! WE HAVE A WINNER!");
		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage(wh+bo+append(cnt2));
		Entity.fireWork(new Location(p.getLocation().getWorld(),p.getLocation().getX(),p.getLocation().getY(),p.getLocation().getZ()+2));
	}
	
	public GUI GUI(){
		return g;
	}
	
	public Player getOwner(){
		return p;
	}
	
	public String getOwnerName(){
		if(open == false){
			return p.getName();
		} else {
			return "";
		}
	}
	
	public boolean getOpen(){
		return open;
	}
	
	public void setOpen(boolean open){
		this.open = open;
	}
	
	public String s(){ //A Success Icon
		return gr+bo+"[  " + wh+bo+"!" + gr+bo+"  ] ";
	}
	
	public String w(){ //A Warning Icon
		return ye+bo+"[  " + wh+bo+"!" + ye+bo+"  ] ";
	}
	
	public String e(){ //An Error Icon
		return re+bo+"[  " + wh+bo+"!" + re+bo+"  ] ";
	}
}
