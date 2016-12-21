package me.virusbrandon.fileio;

import java.io.File;
import java.util.*;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.virusbrandon.bc_utils.Friend;
import me.virusbrandon.bottomlesschests.*;

public class Reader{
	private Chest chest;
	private boolean method;
	private Player player;
	private String name;
	private String uuid;
	
	/**
	 * FileIO.Reader Constructor
	 * 
	 * @param chest
	 * @param player
	 * @param method
	 * 
	 * 
	 */
	public Reader(Chest chest, Player player, boolean method){
		this.chest = chest;
		this.player = player;
		this.name = player.getName();
		this.uuid = player.getUniqueId().toString();
		this.method = method;
	}
	
	/**
	 * FileIO.Reader Constructor
	 * 
	 */
	public Reader(Chest chest, String name, String uuid, boolean method){
		this.chest = chest;
		this.name = name;
		this.uuid = uuid;
		this.method = method;
	}
	
	/**
	 * Read Function
	 * 
	 * 
	 */
	public void read(){
		try{
			File config;
			if(!method){
				config = new File("plugins/BottomLessChests/Chests/Name", name+".yml");
			} else {
				config = new File("plugins/BottomLessChests/Chests/UUID", uuid+".yml");
			}
			YamlConfiguration Config = YamlConfiguration.loadConfiguration(config);
			String cL = "C.";
			HashMap<Integer,ChestItem> items = new HashMap<>();
			try{
				int ii = (chest.getMain().getSettings().isMaintenance()?1000:(chest.getMain().getAllowedChestSize(player)))*7;
				for(int x = 0;x<ii;x++){
					try{
						ChestItem item = new ChestItem(((ItemStack)Config.get(cL+x+".STK")),chest.getMain(),chest);
						item.setOrgWorld(Config.getString(cL+x+".OW"));
						items.put(items.size(),item);
					}catch(Exception e1){}
				}
				int i = chest.getMain().getSettings().getItemSortOption();
				ArrayList<ChestItem> iii = new ArrayList<ChestItem>(items.values());
				chest.setChest(((i>0)&(i<3))?chest.getSorter().sort(iii):items);
			}catch(Exception e1){}
			try{
				for(int y = 0;y<Config.getInt(cL+"F.T");y++){
					chest.getFriends().add(new Friend(Config.getString(cL+"F."+y+".N"),Config.getString(cL+"F."+y+".U")));
				}
			}catch(Exception e1){}
			if(chest.getChest().size()<1){
				chest.createChest();
			}
		}catch(Exception e1){
			chest.createChest();
		}
		try{
			chest.getView().refresh(chest.getSlot());
		}catch(Exception e1){}
		chest.setLoading(false);
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
