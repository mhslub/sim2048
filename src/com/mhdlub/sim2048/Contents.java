package com.mhdlub.sim2048;

/*
 * 
 * This is the Contents class which includes the main features of the game.
 * Contents are from type grid_item.
 */
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import android.graphics.Color;

public class Contents {

	private ArrayList<grid_item> contents;// the list of tiles(a value & a
											// color)
	private Hashtable<String, Integer> tile_color = new Hashtable<String, Integer>();// this
																						// is
																						// used
																						// to
																						// map
																						// colors
																						// to
																						// specific
																						// numbers
	private int back_color = Color.argb(255, 155, 155, 100);// fixed background
															// color of the grid
	private Random ran = new Random();

	//the following are the get and set of the contents by reference
	public ArrayList<grid_item> getContents() {
		return contents;
	}

	public void setContents(ArrayList<grid_item> contents) {
		this.contents = contents;
	}

	public void change_conts(ArrayList<grid_item> c) {
		// contents.clear();
		for (int i = 0; i < c.size(); i++) {
			contents.get(i).setText(c.get(i).getText());
			contents.get(i).setCol(c.get(i).getCol());
		}
	}

	public Contents() {
		contents = new ArrayList<grid_item>();
		generateColorMap();// fill the hashtable randomly
		generateContents();// generate the first contents
	}

	private void generateColorMap() {
		/*
		 * This is to map a color to a number
		 * using a hashtable
		 */
		ArrayList<Integer> used_colors = new ArrayList<Integer>();
		used_colors.add(back_color);//back color has been used
		int col;
		//for each possible number
		for (int i = 2; i <= 2048 * 64; i *= 2) {
			// generate a random color for this number and map them inside the
			// Hashtable
			col = Color.argb(255, ran.nextInt(256), ran.nextInt(256),
					ran.nextInt(256));// generate a random color
			// the following is to avoid associate similar colors to different
			// numbers
			while (used_colors.contains(col))
				col = Color.argb(255, ran.nextInt(256), ran.nextInt(256),
						ran.nextInt(256));// generate a random color
			tile_color.put(String.valueOf(i), col);

			used_colors.add(col);

		}
	}

	// random help to make empty cells when game is over
	public void randomHelp() {
		int num = ran.nextInt(5); // number of cells is [0,4]
		num++;//avoid zero
		int pos;

		for (int i = 0; i <= num; i++) {
			pos = ran.nextInt(16);
			if (!contents.get(pos).getText().equals("")
					&& Integer.valueOf(contents.get(pos).getText()) < 128) {
				grid_item empty = new grid_item("", back_color);
				empty.setCurrent_pos(pos);
				contents.set(pos, empty);
			}
		}

	}

	// detect gameover
	public boolean detectGameOver() {
		// is it possible to move in a direction (checks merging too)
		return !(moreMoves() || moreMergers(1) || moreMergers(2)
				|| moreMergers(3) || moreMergers(4));
	}

	// detect if 2048 is reached
	public boolean detectWin() {
		boolean tile2048 = false;

		int i = 0;
		while (!tile2048 && i < contents.size()) {
			// if there is at least one tile left
			if (contents.get(i).getText().equals("2048")) {
				tile2048 = true;
				break;
			}

			i++;
		}

		return tile2048;
	}

	// scan the contents to detect if there are empty cells
	private boolean moreMoves() {
		boolean more_moves = false;

		int i = 0;
		while (!more_moves && i < contents.size()) {
			// if there is at least one tile left
			if (contents.get(i).getText().equals(""))
				more_moves = true;

			i++;
		}

		return more_moves;
	}

	// add 2 at a random position for each move
	private boolean addOneNum() {

		if (moreMoves()) {

			int pos = ran.nextInt(16);
			while (!contents.get(pos).getText().equals(""))
				pos = ran.nextInt(16);
			grid_item temp2 = new grid_item("2", tile_color.get("2"));
			temp2.setCurrent_pos(pos);
			contents.set(pos, temp2);
			return true;
		} else
			return false;
	}

	public void generateContents() {
		/*
		 * this is used to generate contents for a new game
		 * all are empty except for two random tiles
		 */
		contents.clear();
		int curr_pos = 0;
		// initialize with empty tiles
		for (int i = 0; i < 16; i++) {
			grid_item item = new grid_item("", back_color);
			item.setCurrent_pos(curr_pos);
			curr_pos++;
			contents.add(item);
		}
		// choose two random places
		// add the first number
		int pos = ran.nextInt(16);
		grid_item temp1 = new grid_item("2", tile_color.get("2"));
		temp1.setCurrent_pos(pos);
		contents.set(pos, temp1);

		// add the next number

		while (contents.get(pos).getText().equals("2"))
			pos = ran.nextInt(16);
		grid_item temp2 = new grid_item("2", tile_color.get("2"));
		temp2.setCurrent_pos(pos);
		contents.set(pos, temp2);
	}

	// perform move up
	public boolean moveUp(Score sc) {
		boolean tr = move_oneStep(1);
		// while we can move up
		while (tr)
			tr = move_oneStep(1);

		// merge one time
		int s = mergeOneTime(1);
		sc.increase(s);
		// move up again
		boolean tr2 = move_oneStep(1);
		while (tr2)
			tr2 = move_oneStep(1);

		// add a number
		addOneNum();
		return tr || tr2;
	}

	// perform move down
	public boolean moveDown(Score sc) {
		boolean tr = move_oneStep(4);

		// while we can move
		while (tr)
			tr = move_oneStep(4);

		// merge one time
		int s = mergeOneTime(4);
		sc.increase(s);
		// move again
		boolean tr2 = move_oneStep(4);
		while (tr2)
			tr2 = move_oneStep(4);
		// add a number
		addOneNum();
		return tr || tr2;
	}

	// perform move right
	public boolean moveRight(Score sc) {
		boolean tr = move_oneStep(2);

		// while we can move
		while (tr)
			tr = move_oneStep(2);

		// merge one time
		int s = mergeOneTime(2);
		sc.increase(s);
		// move again
		boolean tr2 = move_oneStep(2);
		while (tr2)
			tr2 = move_oneStep(2);
		// add a number
		addOneNum();
		return tr || tr2;
	}

	// perform move left
	public boolean moveLeft(Score sc) {
		boolean tr = move_oneStep(3);

		// while we can move
		while (tr)
			tr = move_oneStep(3);

		// merge one time
		int s = mergeOneTime(3);
		sc.increase(s);
		// move again
		boolean tr2 = move_oneStep(3);
		while (tr2)
			tr2 = move_oneStep(3);
		// add a number
		addOneNum();
		return tr || tr2;
	}

	// perform move one step action in a direction
	// 1: up
	// 2: right
	// 3: left
	// 4:down
	private boolean move_oneStep(int dir) {
		boolean res = false;

		/*
		 * 
		 */
		for (int i = 0; i < 16; i++) {
			// get the current tile
			grid_item current_item = contents.get(i);
			if (!current_item.getText().equals("")) {
				//get its neighbor
				grid_item val = contents.get(i).getVal(contents, dir);

				// move one step if the neighbor is empty (available) 
				if (val != null && val.getText().equals("")) {

					// put empty here
					grid_item empty = new grid_item("", back_color);
					empty.setCurrent_pos(current_item.getCurrent_pos());
					contents.set(current_item.getCurrent_pos(), empty);

					// move the item
					current_item.setCurrent_pos(val.getCurrent_pos());
					contents.set(val.getCurrent_pos(), current_item);

					res = true;

				}

			}
		}

		return res;

	}

	// detect if there are possible merges
	private boolean moreMergers(int dir) {
		/*
		 * this is only to determine if there are more possible 
		 * merges without changing the contents (for detecting gameover)
		 */
		int pos;
		boolean res = false;

		ArrayList<grid_item> temp = new ArrayList<grid_item>();
		for (int i = 0; i < contents.size(); i++) {
			temp.add(contents.get(i));
		}

		// for all columns
		for (int i = 0; i < 4; i++) {
			// scan each column based on the direction
			for (int j = 1; j < 4; j++) {
				switch (dir) {
				case 1: // moving up
				{
					pos = j * 4 + i;
					break;
				}

				case 3: // moving Left
				{
					pos = j + i * 4;
					break;
				}

				case 2: // moving Right
				{
					pos = (3 - j) + i * 4;
					break;
				}
				case 4: // moving Down
				{
					pos = (12 - j * 4) + i;
					break;
				}

				default:
					pos = 0;

				}
				// get current element
				grid_item current_item = temp.get(pos);
				if (!current_item.getText().equals("")) {
					grid_item neighbor = temp.get(pos).getVal(temp, dir);
					// merge two tiles if they have the same number
					if (neighbor != null
							&& neighbor.getText()
									.equals(current_item.getText())) {

						res = true;
						break;
					}
				}

			}
		}

		return res;
	}

	// the following is to perform the merging when moving in a direction
	private int mergeOneTime(int dir) {
		int pos;
		int sum = 0; // the sum of all successful merges for the score

		//copy the contents into temp
		ArrayList<grid_item> temp = new ArrayList<grid_item>();
		for (int i = 0; i < contents.size(); i++) {
			temp.add(contents.get(i));
		}

		/*
		 * since the direction affects choosing which tiles to merge
		 * scan each direction using different indexes
		 */
		// for all columns
		for (int i = 0; i < 4; i++) {
			// scan each column based on the direction
			for (int j = 1; j < 4; j++) {
				switch (dir) {
				case 1: // moving up
				{
					pos = j * 4 + i;
					break;
				}

				case 3: // moving Left
				{
					pos = j + i * 4;
					break;
				}

				case 2: // moving Right
				{
					pos = (3 - j) + i * 4;
					break;
				}
				case 4: // moving Down
				{
					pos = (12 - j * 4) + i;
					break;
				}

				default:
					pos = 0;

				}
				// get current element
				grid_item current_item = temp.get(pos);
				if (!current_item.getText().equals("")) {
					grid_item neighbor = temp.get(pos).getVal(temp, dir);
					// merge two tiles if they have the same number
					if (neighbor != null
							&& neighbor.getText()
									.equals(current_item.getText())) {
						// put the empty here
						grid_item empty = new grid_item("", back_color);
						empty.setCurrent_pos(current_item.getCurrent_pos());
						contents.set(current_item.getCurrent_pos(), empty);

						// modify the neighbor
						int num = Integer.valueOf(neighbor.getText());
						grid_item new_val = new grid_item(
								String.valueOf(num * 2), tile_color.get(String
										.valueOf(num * 2)));
						new_val.setCurrent_pos(neighbor.getCurrent_pos());
						contents.set(neighbor.getCurrent_pos(), new_val);

						sum += num * 2;

						// if one successful merge then break scanning the
						// current column / row because the rule we follow is 
						//one merge per column / row
						break;
					}
				}

			}
		}

		return sum;
	}

}
