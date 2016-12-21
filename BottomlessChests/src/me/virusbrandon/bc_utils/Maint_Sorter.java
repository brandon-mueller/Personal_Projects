package me.virusbrandon.bc_utils;

import java.util.ArrayList;

public class Maint_Sorter {
	private ArrayList<String> names = new ArrayList<>();
	private ArrayList<String> helper = new ArrayList<>();

	private int number;

	public ArrayList<String> sort(ArrayList<String> values) {
		for(String s:values){
			this.names.add(s);this.helper.add(s);
		}
		number = values.size();
		mergesort(0, number - 1);
		ArrayList<String> done = new ArrayList<>();
		for(int x=names.size()-1;x>=0;x--){
			done.add(names.get(x));
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
			helper.set(i, names.get(i));
		}
		int i = low;
		int j = middle + 1;
		int k = low;
		while (i <= middle && j <= high) {
			if (helper.get(i).toUpperCase().compareTo(helper.get(j).toUpperCase())>=0) {
				names.set(k, helper.get(i));
				i++;
			} else {
				names.set(k, helper.get(j));
				j++;
			}
			k++;
		}
		while (i <= middle) {
			names.set(k, helper.get(i));
			k++;i++;
		}
	}
}
