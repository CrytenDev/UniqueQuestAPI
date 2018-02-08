package de.cryten.utils;

import java.util.Random;

public class RandomManager {
	
    /**
     * Give random int back from min and max number
     */
	public int generatedRandomInt(int min, int max) {
		Random rnd = new Random();
		int rndnumber = rnd.nextInt(max);
		while(rndnumber < min) {
			rndnumber = rnd.nextInt(max);
		}
		return rndnumber;
	}
}
