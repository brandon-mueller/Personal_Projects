package me.virusbrandon.agarutils;

import java.text.DecimalFormat;

public class TPS implements Runnable {

	public static int TICK_COUNT = 0;
	public static long[] TICKS = new long[600];
	public static long LAST_TICK = 0L;

	public static String getTPS() {
		return getTPS(200);
	}

	public static String getTPS(int ticks) {
		if (TICK_COUNT < ticks) {
			return "TPS: init";
		}
		int target = (TICK_COUNT - 1 - ticks) % TICKS.length;
		long elapsed = System.currentTimeMillis() - TICKS[target];

		return "TPS: "+new DecimalFormat("##.00").format(ticks / (elapsed / 1000.0D));
	}

	public static long getElapsed(int tickID) {
		if (TICK_COUNT - tickID >= TICKS.length) {
		}

		long time = TICKS[(tickID % TICKS.length)];
		return System.currentTimeMillis() - time;
	}

	public void run() {
		TICKS[(TICK_COUNT % TICKS.length)] = System.currentTimeMillis();
		TICK_COUNT += 1;
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */