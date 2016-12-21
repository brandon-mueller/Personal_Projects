package me.virusbrandon.bc_utils;

import java.util.*;

import org.bukkit.Material;

import me.virusbrandon.bottomlesschests.*;

public class Sorter {
	private ArrayList<ChestItem> it = new ArrayList<>();
	private ArrayList<ChestItem> he = new ArrayList<>();
	private int number;
	private Main main;
	private Chest owner;
	
	public Sorter(Main main,Chest c){
		this.main = main;
		this.owner = c;
	}

	public HashMap<Integer,ChestItem> sort(ArrayList<ChestItem> values) {
		it.clear();he.clear();
		for(ChestItem s:values){
			this.it.add(s);this.he.add(s);
		}
		number = ((owner.getMaxRows()*7)<values.size())?owner.getMaxRows()*7:values.size();
		mergesort(0, number - 1);
		HashMap<Integer,ChestItem> done = new HashMap<>();
		ArrayList<ChestItem> wait = new ArrayList<>();
		for(int x=0;x<it.size();x++){
			if(it.get(x).getItemId()!=0){
				done.put(done.size(),it.get(x));
			} else {
				wait.add(it.get(x));
			}
		}
		for(ChestItem i:wait){
			done.put(done.size(),i);
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

	@SuppressWarnings("deprecation")
	private void merge(int low, int middle, int high) {
		for (int i = low; i <= high; i++) {
			he.set(i, it.get(i));
		}
		int i = low;
		int j = middle + 1;
		int k = low;
		while (i <= middle && j <= high) {
			if(main.getSettings().getItemSortOption()==1){
				if(he.get(i).getItemId()!=0){
					if ((he.get(i).getItemId() <= he.get(j).getItemId())) {
						it.set(k, he.get(i));
						i++;
					} else {
						it.set(k, he.get(j));
						j++;
					}
				} else {
					it.set(k, he.get(j));
					j++;
				}
			} else if(main.getSettings().getItemSortOption()==2){
				if ((Material.getMaterial(he.get(i).getItemId()).name()).compareTo((Material.getMaterial(he.get(j).getItemId()).name()))<=0) {
						it.set(k, he.get(i));
						i++;
				} else {
					it.set(k, he.get(j));
					j++;
				}
			}
			k++;
		}
		while (i <= middle) {
			it.set(k, he.get(i));
			k++;i++;
		}
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}