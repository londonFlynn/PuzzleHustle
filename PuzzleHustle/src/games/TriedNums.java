package games;

import java.io.Serializable;

public class TriedNums implements Serializable{

	private static final long serialVersionUID = 1L;
	private int[] tried;
	
	public TriedNums(int clsqr) {
		tried = new int[clsqr];
	}
	
	public boolean tried(int num) {
		boolean hasBeenTried = false;
		if (tried[num-1] == num) {
			hasBeenTried = true;
		} else {
			tried[num-1] = num;
		}
		return hasBeenTried;
	}
	
	public boolean triedAll() {
		boolean all = true;
		for (int num : tried) {
			all = num == 0? false : all;
		}
		return all;
	}

}
