package com.mhdlub.sim2048;
/*
 * This class represents on tile
 */
import java.util.ArrayList;

public class grid_item {

	private String text; //the value (a number or empty)
	private int col;//color of the tile
	private int current_pos;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public grid_item(String text, int c) {
		this.text = text;
		this.col = c;
	}

	public int getCurrent_pos() {
		return current_pos;
	}

	public void setCurrent_pos(int current_pos) {
		this.current_pos = current_pos;
	}

	// get the upper value
	public grid_item getUpVal(ArrayList<grid_item> c) {
		if (current_pos >= 4)
			return c.get(current_pos - 4);
		else
			return null;
	}

	// get the lower value
	public grid_item getDownVal(ArrayList<grid_item> c) {
		if (current_pos < 12)
			return c.get(current_pos + 4);
		else
			return null;
	}

	// get the left value
	public grid_item getLeftVal(ArrayList<grid_item> c) {
		if (current_pos != 0 && current_pos != 4 && current_pos != 8
				&& current_pos != 12)
			return c.get(current_pos - 1);
		else
			return null;
	}

	// get the right value
	public grid_item getRightVal(ArrayList<grid_item> c) {
		if (current_pos != 3 && current_pos != 7 && current_pos != 11
				&& current_pos != 15)
			return c.get(current_pos + 1);
		else
			return null;
	}

	// get value of a direction
	public grid_item getVal(ArrayList<grid_item> c, int dir) {

		switch (dir) {
		case 1: {
			return getUpVal(c);

		}

		case 2: {
			return getRightVal(c);

		}

		case 3: {
			return getLeftVal(c);

		}

		case 4: {
			return getDownVal(c);

		}

		default:
			return null;

		}

	}
}
