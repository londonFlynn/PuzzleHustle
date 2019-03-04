package models;

import enums.Flag;

public class Cell {
	private boolean revealed;
	private boolean mine;
	private Flag flag;
	private int minesTouching;
	
	public Cell(boolean revealed, boolean mine, Flag flag, int minesTouching) {
		this.setRevealed(revealed);
		this.setMine(mine);
		this.setFlag(flag);
		this.setMinesTouching(minesTouching);
	}

	public Cell() {
		this.setRevealed(false);
		this.setMine(false);
		this.setFlag(Flag.BLANK);
		this.setMinesTouching(0);
	}
	
	public boolean isRevealed() {
		return revealed;
	}

	public void setRevealed(boolean revealed) {
		this.revealed = revealed;
	}

	public boolean isMine() {
		return mine;
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}

	public int getMinesTouching() {
		return minesTouching;
	}

	public void setMinesTouching(int minesTouching) {
		this.minesTouching = minesTouching;
	}
	
	
}
