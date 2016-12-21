package me.virusbrandon.powerblock;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class JAnimate {
	private Main main;
	private Player player;
	private Timer timer = new Timer(this,2);
	private double amt = 0.0;
	private int exp = 0,snd = 0;
	private String bo=ChatColor.BOLD+"",re=ChatColor.RED+"";
	private DecimalFormat df = new DecimalFormat("#########.##");
	private String[] sym = new String[]{"❃","❂","❁","❀"};
	
	public JAnimate(Main main, Player player){
		this.main = main;
		this.player = player;
		figEx();
		timer.start(20,1);
		main.getTitle().sendTitle(player, 10, 20, 5,re+bo+"WIN THIS JACKPOT!", "");
	}
	
	public int tick(){
		if((amt+Math.pow(10, exp))<(main.getPot())){
			amt+=Math.pow(10, exp);
			main.getTitle().sendTitle(player, 0, 10, 100,null,getTitle());
		} else {
			exp--;
			if(exp<=-3){
				timer.stop();
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASEDRUM,2.0f,1.6f);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP,2.0f,1.6f);
				main.findPlayer(player,0).setSBReady(true);
			}
		}
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASEDRUM,1,((snd+1)*.4f));
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP,.7f,((snd+1)*.4f));
		snd++;
		snd=snd%4;
		return 0;
	}
	
	private String getTitle(){
		return main.randColor()+sym[snd]+spc(((exp+2)*2),"")+"$"+df.format(amt)+spc(((exp+2)*2),"")+sym[snd];
	}
	
	private void figEx(){
		exp =((Math.floor(main.getPot())%1)!=0)?(df.format((main.getPot())).length()-4):(df.format((Math.floor(main.getPot())+.01)).length()-4); 
	}
	
	private String spc(int spaces, String res){
		res+=" ";
		if(spaces>0){
			return spc(spaces-1,res);
		} else {
			return res;
		}
	}
}
