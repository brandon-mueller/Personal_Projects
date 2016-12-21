package me.virusbrandon.maps;

import java.text.DecimalFormat;
import java.util.ArrayList;

import me.virusbrandon.game.Arena;
import me.virusbrandon.smashverse.Main;
import me.virusbrandon.sv_utils.ArenaStatus.Status;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class TemplateBuilder {
	Runnable timer = new Runnable() {
		@SuppressWarnings("deprecation")
		public void run() {
			try{
				int i = current.getMan().tBS();
				for(int xx=0;xx<(i==5?1000:(i==4?500:(i==3)?250:(i==2)?100:50));xx++){
					Location n = new Location(or.getWorld(),(or.getX()+(slot*tMan.getLimit()))+pieces.get(0).X(),or.getY()+pieces.get(0).Y(),(or.getZ()+(slot*tMan.getLimit()))+pieces.get(0).Z());
					n.getWorld().getBlockAt(n).setTypeId(pieces.get(0).getID());
					n.getWorld().getBlockAt(n).setData(pieces.get(0).getData());
					pieces.remove(0);
					current.setGenProgress(df.format(((((finalPieces*1.0)-(pieces.size()*1.0))/(finalPieces*1.0))*100))+"%");
				}
			}catch(Exception e1){
				current.getMan().cLn("Arena "+slot+" Finishing Generating.");
				stop();
				current.setStatus(Status.WAITING);
				setBusy(false);
				tMan.buildTemplate(tMan.getNextQueuedArena());
			}
		}	
	};
	
	private int id;
	private int slot;
	private int finalPieces;
	private boolean isBusy = false;
	private TemplateManager tMan;
	private Arena current;
	private Location or;
	private ArrayList<Piece> pieces = new ArrayList<>();
	private DecimalFormat df = new DecimalFormat("###");
	
	/**
	 * The Timer Constructor:
	 * 
	 * @param chest
	 * @param mode
	 * 
	 * 
	 */
	
	public TemplateBuilder(TemplateManager tMan){
		this.tMan = tMan;
	}
	
	public void beginBuilding(Arena arena, Location origin, int slot){
		this.current = arena;
		pieces.clear();
		for(Piece piece:current.getTemplate().getPieces()){
			pieces.add(piece);
		}
		this.finalPieces = pieces.size();
		this.or = origin;
		this.slot = slot;
		setBusy(true);
		start(1);
		current.getMan().cLn("Arena "+slot+" Started Generating...");
	}
	
	public void setBusy(boolean busy){
		this.isBusy = busy;
	}
	
	public boolean isBusy(){
		return isBusy;
	}
	
	
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	public void start(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay, delay);
	}
	
	public void start(int delay1, int delay2){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay1, delay2);
	}

	
	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	public void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}