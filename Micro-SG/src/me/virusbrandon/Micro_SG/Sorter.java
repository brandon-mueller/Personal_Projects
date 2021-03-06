package me.virusbrandon.Micro_SG;

import java.util.ArrayList;

public class Sorter {
	private ArrayList<Stats> stats = new ArrayList<>();
	private ArrayList<Stats> helper = new ArrayList<>();

	private int number;

	public ArrayList<Stats> sort(ArrayList<Stats> values) {
		for(Stats s:values){
			this.stats.add(s);this.helper.add(s);
		}
		number = values.size();
		mergesort(0, number - 1);
		ArrayList<Stats> done = new ArrayList<>();
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
			if (helper.get(i).getLTPoints()- ((helper.get(i).getDeaths() * 5) + (helper.get(i).getRevives() * 2)) < helper.get(j).getLTPoints()- ((helper.get(j).getDeaths() * 5) + (helper.get(j).getRevives() * 2))) {
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
}
