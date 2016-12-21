package me.virusbrandon.sv_utils;

import java.util.ArrayList;

import me.virusbrandon.game.SmashVersePlayer;

public class Sorter {
	private ArrayList<SmashVersePlayer> stats = new ArrayList<>();
	private ArrayList<SmashVersePlayer> helper = new ArrayList<>();

	private int number;

	public ArrayList<SmashVersePlayer> sort(ArrayList<SmashVersePlayer> values) {
		for(SmashVersePlayer s:values){
			this.stats.add(s);this.helper.add(s);
		}
		number = values.size();
		mergesort(0, number - 1);
		ArrayList<SmashVersePlayer> done = new ArrayList<>();
		for(int x=stats.size()-1;x>=0;x--){
			done.add(stats.get(x));
		}
		return done;
	}

	private void mergesort(int low, int high) {
		if (low < high) {
			int middle = low + (high - low) / 2;
			mergesort(low, middle);
			mergesort(middle + 1, high);
			merge(low, middle, high);
		}
	}

	private void merge(int low, int middle, int high) {
		for (int i = low; i <= high; i++) {
			helper.set(i, stats.get(i));
		}
		int i = low;
		int j = middle + 1;
		int k = low;
		while (i <= middle && j <= high) {
			if (helper.get(i).getPoints()<helper.get(j).getPoints()) {
				stats.set(k, helper.get(i));
				i++;
			} else {
				stats.set(k, helper.get(j));
				j++;
			}
			k++;
		}
		while (i <= middle) {
			stats.set(k, helper.get(i));
			k++;i++;
		}
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}