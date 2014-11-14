package com.mhdlub.sim2048;

/*
 * The main activity.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends Activity {

	float x1 = 0, x2, y1 = 0, y2;// to save coordinates for swiping detection
	final static Contents conts = new Contents();// static to save the contents
													// in landscape
	static boolean continue_game = false;
	static int help_nums = 0;// number of help times
	grid_adapter adapter;
	TextView Tscore, Tbest_score;
	static Score current_score = new Score();// total score increased at each
												// move (passes by reference)
	ArrayList<grid_item> prev_conts;// to save previous state for undo
	int prev_score, prev_bestScore; //save previous score and previous best score
	static int file_bestScore, current_bestScore;

	// the following is to save the previous state
	//perform deep cloning.
	private void update_prev() {
		prev_conts = new ArrayList<grid_item>();
		for (int i = 0; i < conts.getContents().size(); i++) {
			grid_item item = new grid_item(
					conts.getContents().get(i).getText(), conts.getContents()
							.get(i).getCol());
			prev_conts.add(item);
		}
	}

	// read best score from the file at the beginning 
	private int read_bestScore() {
		// the file contains only one number and is located on the internal storage
		File my_best_score = new File(getFilesDir(), "my_best_score.txt");
		if (my_best_score.exists()) {

			//read the contents as bytes
			int res = 0;
			try {
				FileInputStream fin = openFileInput("my_best_score.txt");
				InputStreamReader isr = new InputStreamReader(fin);
				BufferedReader buffreader = new BufferedReader(isr);

				res = Integer.valueOf(buffreader.readLine());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return res;// previously saved best score
		} else
			return 0;// for the first time the best score=0

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// =====================================================
		// --------------------------------------------

		Button bnew = (Button) findViewById(R.id.NewButton);
		Button bUndo = (Button) findViewById(R.id.UndoButton);
		Button bExit = (Button) findViewById(R.id.Exit_buttn);
		MainActivity.file_bestScore = read_bestScore();// get best score

		Tscore = (TextView) findViewById(R.id.textScore);
		Tbest_score = (TextView) findViewById(R.id.textBestScore);
		Tscore.setText(String.valueOf(current_score.getSc()));// at the
																// beginning
		if (file_bestScore > current_score.getSc())
			current_bestScore = file_bestScore;

		else
			current_bestScore = current_score.getSc();

		Tbest_score.setText(String.valueOf(current_bestScore));
		prev_score = current_score.getSc();
		prev_bestScore = current_bestScore;

		update_prev();// save current

		GridView tile_container = (GridView) findViewById(R.id.gridView1);

		// Create the Adapter and make it monitor changes on our contents.
		adapter = new grid_adapter(this, conts.getContents());

		// Set the Adapter to GridView
		tile_container.setAdapter(adapter);
		// detect the swipe direction and perform the right action
		/*
		 * swipe is three stages: click down, hold, and then up! 
		 */
		tile_container.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				switch (event.getAction()) {
				// from the first screen touch we get the position
				case MotionEvent.ACTION_DOWN: {
					x1 = event.getX();
					x2 = event.getX();
					y1 = event.getY();
					y2 = event.getY();

					break;
				}

				case MotionEvent.ACTION_UP: {

					//get the second set of coords
					x2 = event.getX();
					y2 = event.getY();

					//if changes are on x axes
					// sweep left to right // right to left
					if (Math.abs(x1 - x2) > 50
							&& Math.abs(x1 - x2) > Math.abs(y1 - y2)) {

						// sweep right to left
						if (x1 > x2) {
							update_prev();
							prev_score = current_score.getSc();
							prev_bestScore = current_bestScore;
							conts.moveLeft(current_score);//perform left move
							adapter.notifyDataSetChanged();//notify changes
							//check score
							Tscore.setText(String.valueOf(current_score.getSc()));
							if (current_score.getSc() > current_bestScore) {
								current_bestScore = current_score.getSc();
								Tbest_score.setText(String
										.valueOf(current_bestScore));
							}
							// detect win
							if (!continue_game)
								detectWin();
							// detect gameover
							detectGameOver();

						} else {
							// sweep left to right
							update_prev();
							prev_score = current_score.getSc();
							prev_bestScore = current_bestScore;
							conts.moveRight(current_score);//move right
							adapter.notifyDataSetChanged();//notify changes
							//check score
							Tscore.setText(String.valueOf(current_score.getSc()));
							if (current_score.getSc() > current_bestScore) {
								current_bestScore = current_score.getSc();
								Tbest_score.setText(String
										.valueOf(current_bestScore));
							}

							// detect win
							if (!continue_game)
								detectWin();
							// detect gameover
							detectGameOver();
						}
					} else {
						if (Math.abs(y1 - y2) > 50
								&& Math.abs(x1 - x2) < Math.abs(y1 - y2)) {
							// sweep up to down
							if (y1 < y2) {
								update_prev();
								prev_score = current_score.getSc();
								prev_bestScore = current_bestScore;
								conts.moveDown(current_score);//move down
								adapter.notifyDataSetChanged();//notify changes
								//check score
								Tscore.setText(String.valueOf(current_score
										.getSc()));
								if (current_score.getSc() > current_bestScore) {
									current_bestScore = current_score.getSc();
									Tbest_score.setText(String
											.valueOf(current_bestScore));
								}
								// detect win
								if (!continue_game)
									detectWin();
								// detect gameover
								detectGameOver();
							}

							// sweep down to up
							else {
								update_prev();
								prev_score = current_score.getSc();
								prev_bestScore = current_bestScore;
								conts.moveUp(current_score);//move up
								adapter.notifyDataSetChanged();//notify changes
								//check score
								Tscore.setText(String.valueOf(current_score
										.getSc()));
								if (current_score.getSc() > current_bestScore) {
									current_bestScore = current_score.getSc();
									Tbest_score.setText(String
											.valueOf(current_bestScore));
								}
								// detect win
								if (!continue_game)
									detectWin();
								// detect gameover
								detectGameOver();
							}
						}
					}
					break;
				}
				}

				return false;
			}
		});

		/*
		 * new game button click action
		 */
		bnew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// generate new contents and update the adapter
				conts.generateContents();
				update_prev();
				current_score.setSc(0);
				Tscore.setText("0");
				prev_score = current_score.getSc();
				prev_bestScore = current_bestScore;
				adapter.notifyDataSetChanged();

				help_nums = 0;
				continue_game = false;
			}
		});

		/*
		 * undo button click action
		 */
		bUndo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// backtrack to previous state
				conts.change_conts(prev_conts);
				current_score.setSc(prev_score);
				current_bestScore = prev_bestScore;
				Tscore.setText(String.valueOf(prev_score));
				Tbest_score.setText(String.valueOf(current_bestScore));
				adapter.notifyDataSetChanged();
			}
		});

		/*
		 * close app button action
		 */
		bExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onDestroy();
				finish();
				java.lang.System.exit(0);
			}
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		if (file_bestScore < current_bestScore) {
			// update file best score if necessary

			try {
				// the following creates the file if not already there
				FileOutputStream outputStream = openFileOutput(
						"my_best_score.txt", Context.MODE_PRIVATE);
				outputStream
						.write(String.valueOf(current_bestScore).getBytes());
				outputStream.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// detect win
	private void detectWin() {
		/*
		 * here delegate detect win to Contents.detectWin
		 */
		if (conts.detectWin()) { 
			//if 2048 is found (WIN)
			//show a message with two options
			final CharSequence[] options = { "New Game", "Continue" };
			AlertDialog.Builder message2 = new AlertDialog.Builder(
					MainActivity.this);
			message2.setTitle("Congratulation! You are awesome!");
			message2.setItems(options, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if (options[which].equals("New Game")) {
						// generate new contents and update the adapter
						conts.generateContents();
						update_prev();
						current_score.setSc(0);
						Tscore.setText("0");
						prev_score = current_score.getSc();
						prev_bestScore = current_bestScore;
						adapter.notifyDataSetChanged();

						help_nums = 0;
						continue_game = false;
					} else {
						// continue the same game
						continue_game = true;
					}
				}
			});

			message2.show();
		}
	}

	// detect game over
	private void detectGameOver() {
		/*
		 * here delegate detect gameover to Contents.detectGameOver
		 */
		if (conts.detectGameOver()) {
			//if game is over (no more possible merges nor moves.
			//check help times
			if (help_nums < 3) {
				//if can help then get use confirmation
				final CharSequence[] options = { "Random Help", "New Game" };
				AlertDialog.Builder message1 = new AlertDialog.Builder(
						MainActivity.this);
				message1.setTitle("Game Over!");
				message1.setItems(options,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (options[which].equals("Random Help")) {
									conts.randomHelp();
									update_prev();
									prev_score = current_score.getSc();
									adapter.notifyDataSetChanged();

									help_nums++;
								} else {
									// generate new contents and update the
									// adapter
									conts.generateContents();
									update_prev();
									current_score.setSc(0);
									Tscore.setText("0");
									prev_score = current_score.getSc();
									prev_bestScore = current_bestScore;
									adapter.notifyDataSetChanged();

									help_nums = 0;
									continue_game = false;
								}
							}
						});

				message1.show();
			} else {
				//cann't help
				final CharSequence[] options = { "New Game" };
				AlertDialog.Builder message2 = new AlertDialog.Builder(
						MainActivity.this);
				message2.setTitle("Game Over!");
				message2.setItems(options,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (options[which].equals("New Game")) {
									// generate new contents and update the
									// adapter
									conts.generateContents();
									update_prev();
									current_score.setSc(0);
									Tscore.setText("0");
									prev_score = current_score.getSc();
									prev_bestScore = current_bestScore;
									adapter.notifyDataSetChanged();

									help_nums = 0;
									continue_game = false;
								}
							}
						});

				message2.show();

			}
		}
	}
}
