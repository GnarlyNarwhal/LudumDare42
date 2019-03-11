package com.gnarly.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.engine.model.FadeRect;
import com.gnarly.engine.model.TexRect;
import com.gnarly.game.map.MapEntry;
import com.gnarly.game.map.Stack;

public class EndPanel extends Panel {

	private double time;
	
	private TexRect gameOver, logo, play, scoreText;
	
	private Button easy, medium, hard, helpButton, prev, next, back;
	
	private int difficulty;
	
	private boolean end = false;
	
	private NumberDisplay highscore;
	
	private FadeRect overlay;
	
	private boolean helpActive;
	private int index;
	private TexRect[] help;
	
	public EndPanel(Window window, Camera camera) {
		gameOver = new TexRect(camera, "gameover.png", 144, 55,-0.1f, 290, 100, 0, true);
		logo     = new TexRect(camera, "logo.png",     144, 55, -0.1f, 290, 100, 0, true);
		
		play = new TexRect(camera, "playicon.png", 144, 140, -0.1f, 290, 45, 0, false);
		
		easy = new Button(window, camera, "easyone.png", "easytwo.png", "easythree.png", 144, 200, -0.1f, 90, 45, false);
		medium = new Button(window, camera, "medone.png", "medtwo.png", "medthree.png", 244, 200, -0.1f, 90, 45, false);
		hard = new Button(window, camera, "hardone.png", "hardtwo.png", "hardthree.png", 344, 200, -0.1f, 90, 45, false);

		helpButton = new Button(window, camera, "helpone.png", "helptwo.png", "helpthree.png", 268, 252, -0.1f, 40, 20, false);

		back = new Button(window, camera, "backone.png", "backtwo.png", "backthree.png", camera.getWidth() - 78, 6, 0, 20, 20, false);
		prev = new Button(window, camera, "prevone.png", "prevtwo.png", "prevthree.png", camera.getWidth() - 52, 6, 0, 20, 20, false);
		next = new Button(window, camera, "nextone.png", "nexttwo.png", "nextthree.png", camera.getWidth() - 26, 6, 0, 20, 20, false);
		
		int highScore = 0;
		File file = new File("res/high.sco");
		if(file.exists()) {
			try {
				Scanner scanner = new Scanner(file);
				highScore = scanner.nextInt();
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		highscore = new NumberDisplay(camera, 297, 280, -0.1f, MapEntry.TILE_HEIGHT, NumberDisplay.JUSTIFICATION_LEFT, highScore, 8, NumberDisplay.DISPLAY_DEC);
		scoreText = new TexRect(camera, "highscore.png", 207, 280, -0.1f, MapEntry.TILE_WIDTH * 9, MapEntry.TILE_HEIGHT, 0, false);

		overlay = new FadeRect(camera, 0, 0, 0, camera.getWidth(), camera.getHeight(), 0, 0, 0, 0, false);
		
		helpActive = false;
		index = 0;
		help = new TexRect[8];
		help[0] = new TexRect(camera, "help/premise.png",    0, 0, 0, camera.getWidth(), camera.getHeight(), 0, false);
		help[1] = new TexRect(camera, "help/overview.png",   0, 0, 0, camera.getWidth(), camera.getHeight(), 0, false);
		help[2] = new TexRect(camera, "help/interface.png",  0, 0, 0, camera.getWidth(), camera.getHeight(), 0, false);
		help[3] = new TexRect(camera, "help/controls.png",   0, 0, 0, camera.getWidth(), camera.getHeight(), 0, false);
		help[4] = new TexRect(camera, "help/thebyte.png",    0, 0, 0, camera.getWidth(), camera.getHeight(), 0, false);
		help[5] = new TexRect(camera, "help/groups.png",     0, 0, 0, camera.getWidth(), camera.getHeight(), 0, false);
		help[6] = new TexRect(camera, "help/swapping.png",   0, 0, 0, camera.getWidth(), camera.getHeight(), 0, false);
		help[7] = new TexRect(camera, "help/difficulty.png", 0, 0, 0, camera.getWidth(), camera.getHeight(), 0, false);
	}

	@Override
	public void update() {
		if(!helpActive) {
			easy.update();
			medium.update();
			hard.update();
			helpButton.update();
			if(easy.getState() == Button.RELEASED) {
				state = Main.PANEL_PLAY;
				difficulty = Stack.DIFFICULTY_EASY;
			}
			else if(medium.getState() == Button.RELEASED) {
				state = Main.PANEL_PLAY;
				difficulty = Stack.DIFFICULTY_MEDIUM;
			}
			else if(hard.getState() == Button.RELEASED) {
				state = Main.PANEL_PLAY;
				difficulty = Stack.DIFFICULTY_HARD;
			}
			else if(helpButton.getState() == Button.RELEASED) {
				helpActive = true;
				index = 0;
			}
			overlay.update();
		}
		else {
			back.update();
			if(back.getState() == Button.PRESSED)
				helpActive = false;
			if(index != 0) {
				prev.update();
				if(prev.getState() == Button.PRESSED) {
					--index;
				}
			}
			else
				prev.setTex(2);
			if(index != help.length - 1) {
				next.update();
				if(next.getState() == Button.PRESSED) {
					++index;
				}
			}
			else
				next.setTex(2);
		}
	}

	@Override
	public void render() {
		if(!helpActive) {
			if(end)
				gameOver.render();
			else
				logo.render();
			easy.render();
			medium.render();
			hard.render();
			play.render();
			highscore.render();
			scoreText.render();
			helpButton.render();
			overlay.render();
		}
		else {
			back.render();
			prev.render();
			next.render();
			help[index].render();
		}
	}
	
	public int getState() {
		int temp = state;
		state = Main.PANEL_END;
		return temp;
	}
	
	public void setActive() {
		overlay.fadeFromTo(0, 0, 0, 1, 0, 0, 0, 0, 1000);
		time = 0;
		end = true;

		int highScore = 0;
		File file = new File("res/high.sco");
		if(file.exists()) {
			try {
				Scanner scanner = new Scanner(file);
				highScore = scanner.nextInt();
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		this.highscore.setValue(highScore);
	}
	
	public int getDifficulty() {
		return difficulty;
	}
}
