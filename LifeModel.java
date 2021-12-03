package lifePkg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import javax.swing.Timer;

public class LifeModel implements ActionListener {

	/*
	 * This is the Model component.
	 */

	private static int SIZE = 60;
	private LifeCell[][] grid;

	LifeView myView;
	Timer timer;

	
	/** Construct a new model using a particular file */
	public LifeModel(LifeView view, String fileName) throws IOException {
		int r, c;
		grid = new LifeCell[SIZE][SIZE];
		for (r = 0; r < SIZE; r++)
			for (c = 0; c < SIZE; c++)
				grid[r][c] = new LifeCell();

		if (fileName == null) // use random population
		{
			for (r = 0; r < SIZE; r++) {
				for (c = 0; c < SIZE; c++) {
					if (Math.random() > 0.70) // 15% chance of a cell starting alive
						grid[r][c].setAliveNow(true);
				}
			}
		} else {
			Scanner input = new Scanner(new File(fileName));
			int numInitialCells = input.nextInt();
			for (int count = 0; count < numInitialCells; count++) {
				r = input.nextInt();
				c = input.nextInt();
				grid[r][c].setAliveNow(true);
			}
			input.close();
		}

		myView = view;
		myView.updateView(grid);

	}

	/** Constructor a randomized model */
	public LifeModel(LifeView view) throws IOException {
		this(view, null);
	}

	/** pause the simulation (the pause button in the GUI */
	public void pause() {
		timer.stop();
	}

	/** resume the simulation (the pause button in the GUI */
	public void resume() {
		timer.restart();
	}
	
	public boolean color() {
		myView.isColor = !myView.isColor;
		return myView.isColor;
	}

	/** run the simulation (the pause button in the GUI */
	public void run() {
		timer = new Timer(50, this);
		timer.setCoalesce(true);
		timer.start();
	}

	/** called each time timer fires */
	public void actionPerformed(ActionEvent e) {
		oneGeneration();
		myView.updateView(grid);
	}

	/** main logic method for updating the state of the grid / simulation */
	private void oneGeneration() {
		/*
		 * student code here
		 */
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				if (grid[r][c].isAliveNow()) {
					int neighbors = numNeighbors(r, c);
					if (neighbors < 2 || neighbors > 3)
						grid[r][c].setAliveNext(false);
					else
						grid[r][c].setAliveNext(true);
				}
				if (!grid[r][c].isAliveNow()) {
					int neighbors = numNeighbors(r, c);
					if (neighbors == 3)
						grid[r][c].setAliveNext(true);
				}
			}
		}
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[x].length; y++) {
				grid[x][y].setAliveNow(grid[x][y].isAliveNext());
				grid[x][y].setAliveNext(false);

			}
		}

	}

	private int numNeighbors(int row, int col) {
		int neighbors = 0;
		for (int r = row - 1; r < row + 2; r++) {
			if (r < 0 || r >= grid.length) {
				continue;
			}
			for (int c = col - 1; c < col + 2; c++) {
				if (c < 0 || c >= grid[r].length) {
					continue;
				}

				if (r == row && c == col) {
					continue;
				}

				if (grid[r][c].isAliveNow()) {
					neighbors++;
				}
			}
		}
		return neighbors;

	}
}