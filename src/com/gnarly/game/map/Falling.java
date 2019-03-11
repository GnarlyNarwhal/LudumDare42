package com.gnarly.game.map;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import java.util.Random;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.game.Main;
import com.gnarly.game.NumberDisplay;

public class Falling {

	private static final double KEY_RATE       = 50;
	private static final double KEY_DOWN_RATE  = 150;
	private static final double TOP_STALL      = 750;
	
	private Window window;
	private Camera camera;
	
	private Map map;
	private Stack stack;	
	private NumberDisplay numbers;
	
	private int x, y;
	
	private double rate, time;
	
	private double keyTime, downTime;
	
	private int init;
	
	private MapEntry[][] array;
	
	public Falling(Window window, Camera camera, Map map, Stack stack) {
		this.window = window;
		this.camera = camera;
		this.map = map;
		this.stack = stack;
		this.array = map.getMap();
		numbers = new NumberDisplay(camera, 0, 0, 0, MapEntry.TILE_HEIGHT, NumberDisplay.JUSTIFICATION_LEFT, 0, 8, stack.getDifficulty());
	}
	
	public void update() {
		time += Main.dtime;
		if(time > rate || (y == -1 && time > TOP_STALL + rate / 100) ) {
			if(init == 0) {
				Random random = new Random();
				x = random.nextInt(Map.MAP_WIDTH - 8);
				y = -1;
				numbers.setValue(stack.read());
				init = 1;
			}
			else {
				++y;
				if(y == Map.MAP_HEIGHT || checkBelow()) {
					--y;
					finalize();
					return;
				}
			}
			if(time > rate)
				time -= rate;
			else
				time -= TOP_STALL + rate / 100;
		}
		if(y != -1) {
			keyTime += Main.dtime;
			if(keyTime > KEY_RATE) {
				if(window.keyPressed(GLFW_KEY_A) > Window.BUTTON_UNPRESSED && !checkLeft())
					--x;
				if(window.keyPressed(GLFW_KEY_D) > Window.BUTTON_UNPRESSED && !checkRight())
					++x;
				keyTime -= KEY_RATE;
			}
			downTime += Main.dtime;
			if (downTime > KEY_DOWN_RATE) {
				if(window.keyPressed(GLFW_KEY_S) > Window.BUTTON_UNPRESSED) {
					++y;
					if (checkBelow() || y == Map.MAP_HEIGHT - 1) {
						--y;
						finalize();
						return;
					}
				}
			}
			if(window.keyPressed(GLFW_KEY_SPACE) == Window.BUTTON_PRESSED) {
				finalize();
				return;
			}
		}
		if(y == -1)
			numbers.setPosition(Map.MAP_X_OFFSET + x * MapEntry.TILE_WIDTH, 8);
		else
			numbers.setPosition(Map.MAP_X_OFFSET + x * MapEntry.TILE_WIDTH, Map.MAP_Y_OFFSET + y * MapEntry.TILE_HEIGHT);
	}
	
	private boolean checkBelow() {
		if(y >= Map.MAP_HEIGHT - 1)
			return false;
		for(int i = 0; i < 8; ++i)
			if(array[x + i][y].isActive())
				return true;
		return false;
	}
	
	private boolean checkLeft() {
		return x - 1 < 0 || array[x - 1][y].isActive();
	}
	
	private boolean checkRight() {
		return x + 8 == Map.MAP_WIDTH || array[x + 8][y].isActive();
	}
	
	public void finalize() {
		if(y == -1)
			map.triggerEnd(1);
		else {
			for(int i = 0; i < 8; ++i) {
				int ty = y;
				while (ty < Map.MAP_HEIGHT && !array[x + i][ty].isActive())
					++ty;
				--ty;
				array[x + i][ty].setNum(numbers.getDigit(7 - i));
				array[x + i][ty].setActive(true);
			}
			init = 0;
			time = rate + 1;
		}
	}
	
	public void render() {
		numbers.render();
	}
	
	public void setDifficulty(int difficulty) {
		numbers.setDisplayFormat(difficulty);
		numbers.setValue(stack.read());
	}
	
	public void increaseRate(double time) {
		rate -= time;
		if (rate < 250)
			rate = 250;
	}
	
	public void reset() {
		rate = 1000;
		time = 0;
		keyTime = 0;
		downTime = 0;
		Random random = new Random();
		x = random.nextInt(Map.MAP_WIDTH - 8);
		y = -1;
		numbers.setValue(stack.read());
		init = 1;
	}
}
