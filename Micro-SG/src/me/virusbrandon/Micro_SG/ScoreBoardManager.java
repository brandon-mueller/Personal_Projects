package me.virusbrandon.Micro_SG;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreBoardManager {
	ScoreBoardManager(){}
	static Objective obj;
	static Random RAND = new Random();
	public static void updateScoreBoard(ArrayList<Player> players, ArrayList<String> s) {
		for(Player p:players){
			clearScoreBoard(p);
			if (Bukkit.getScoreboardManager().getMainScoreboard().equals(p.getScoreboard()) || p.getScoreboard() == null)
				p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
				obj = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
			if (obj == null) {
				obj = p.getScoreboard().registerNewObjective(String.format("%016x", RAND.nextLong()), "dummy");
				obj.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Micro SG");
				obj.setDisplaySlot(DisplaySlot.SIDEBAR);
				for (int i = 0; i < s.size(); i++)
					obj.getScore(s.get(i)).setScore(s.size() - i);
			}
		}
	}
	
	public static void clearScoreBoard(Player player){
		Scoreboard b = player.getScoreboard();
		b.clearSlot(DisplaySlot.SIDEBAR);
	}
	
	public static void removeScoreBoard(Player pl){
		pl.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
}

/**
 * ©2014, UnderLordGames.com
 */