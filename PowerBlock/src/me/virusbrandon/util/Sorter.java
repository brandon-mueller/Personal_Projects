package me.virusbrandon.util;

import java.util.ArrayList;

import me.virusbrandon.powerblock.Ticket;

public class Sorter {
	private ArrayList<Ticket> tickets = new ArrayList<>();
	private ArrayList<Ticket> helper = new ArrayList<>();

	private int number;

	public ArrayList<Ticket> sort(ArrayList<Ticket> values) {
		tickets.clear();helper.clear();
		for(Ticket s:values){
			this.tickets.add(s);this.helper.add(s);
		}
		number = values.size();
		mergesort(0, number - 1);
		ArrayList<Ticket> done = new ArrayList<>();
		for(int x=tickets.size()-1;x>=0;x--){
			done.add(tickets.get(x));
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
			helper.set(i, tickets.get(i));
		}
		int i = low;
		int j = middle + 1;
		int k = low;
		while (i <= middle && j <= high) {
			if (helper.get(i).getWinningsAmt() < helper.get(j).getWinningsAmt()) {
				tickets.set(k, helper.get(i));
				i++;
			} else {
				tickets.set(k, helper.get(j));
				j++;
			}
			k++;
		}
		while (i <= middle) {
			tickets.set(k, helper.get(i));
			k++;i++;
		}
	}
}
