package com.gnarly.game.map;

import java.util.Random;

import com.gnarly.engine.display.Camera;
import com.gnarly.game.NumberDisplay;

public class Stack {

	public static final int
		DIFFICULTY_EASY   = 8,
		DIFFICULTY_MEDIUM = 10,
		DIFFICULTY_HARD   = 16;
	
	private Camera camera;
	
	private long num;
	
	private NumberDisplay stackDisplay;
	
	private int difficulty;
	
	public Stack(Camera camera, int difficulty) {
		this.camera = camera;
		this.difficulty = difficulty;
		num = rand();
		stackDisplay = new NumberDisplay(camera, 469, Map.MAP_Y_OFFSET + MapEntry.TILE_HEIGHT, 0, MapEntry.TILE_HEIGHT, NumberDisplay.JUSTIFICATION_LEFT, num, 8, difficulty);
	}
	
	public void render() {
		stackDisplay.render();
	}
	
	public long read() {
		long ret = num;
		num = rand();
		stackDisplay.setValue(num);
		return ret;
	}
	
	private long rand() {
		Random rand = new Random();
		long random = -1;
		while(random < 0)
			random = rand.nextLong();
		switch (difficulty) {
			case DIFFICULTY_EASY:
				random %= 0xffffffL;
			case DIFFICULTY_MEDIUM:
				random %= 100000000L;
			default:
				random %= 0xffffffffL;
		}
		return random;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
		stackDisplay.setDisplayFormat(difficulty);
		num = rand();
		stackDisplay.setValue(num);
	}
}
