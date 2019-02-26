package games;

public class TriedNums {
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
