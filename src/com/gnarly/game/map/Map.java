package com.gnarly.game.map;

import static com.gnarly.game.map.MapEntry.TILE_HEIGHT;
import static com.gnarly.game.map.MapEntry.TILE_WIDTH;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import org.joml.Vector3f;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.engine.model.FadeRect;
import com.gnarly.engine.model.TexRect;
import com.gnarly.game.Main;
import com.gnarly.game.NumberDisplay;

public class Map {
	
	public static final int
		MAP_WIDTH    = 32,
		MAP_HEIGHT   = 24,
		MAP_X_OFFSET = 144,
		MAP_Y_OFFSET = TILE_WIDTH + TILE_HEIGHT;
	
	private Window window;
	private Camera camera;
	
	private MapEntry[][] map;
	
	private TexRect border;
	
	private Stack stack;
	
	private Falling falling;
	
	private int end;
	
	private double runTime = 0;
	private NumberDisplay timeDisplay;
	
	private NumberDisplay score, deletions;
	private float scoreMul;
	
	private TexRect timeWord, scoreWord, groupWord, stackWord, groupSizeWord;
	
	private int selectX;
	private int selectY;
	
	private boolean[][] bucket;
	
	private FadeRect selected;
	
	private NumberDisplay groupSize;
	
	public Map(Window window, Camera camera, String path, int difficulty) {
		this.window = window;
		this.camera = camera;
		map = new MapEntry[MAP_WIDTH][MAP_HEIGHT];
		bucket = new boolean[MAP_WIDTH][MAP_HEIGHT];
		for (int i = 0; i < MAP_WIDTH; i++)
			for (int j = 0; j < MAP_HEIGHT; j++)
				map[i][j] = new MapEntry(camera, MAP_X_OFFSET + i * TILE_WIDTH, MAP_Y_OFFSET + j * TILE_HEIGHT);
		clearBucket();
		border = new TexRect(camera, "mapBorder.png", 0, 0, -0.1f, camera.getWidth(), camera.getHeight(), 0, true);
		stack = new Stack(camera, difficulty);
		falling = new Falling(window, camera, this, stack);
		end = 0;
		timeDisplay = new NumberDisplay(camera, 36, 33, 0, TILE_HEIGHT, NumberDisplay.JUSTIFICATION_LEFT, 0, 8, NumberDisplay.DISPLAY_DEC);
		score = new NumberDisplay(camera, 36, 93, 0, TILE_HEIGHT, NumberDisplay.JUSTIFICATION_LEFT, 0, 8, NumberDisplay.DISPLAY_DEC);
		deletions = new NumberDisplay(camera, 36, 153, 0, TILE_HEIGHT, NumberDisplay.JUSTIFICATION_LEFT, 0, 8, NumberDisplay.DISPLAY_DEC);
		groupSize = new NumberDisplay(camera, 36, 213, 0, TILE_HEIGHT, NumberDisplay.JUSTIFICATION_LEFT, 0, 8, NumberDisplay.DISPLAY_DEC);
		reset(difficulty);
		
		timeWord      = new TexRect(camera, "time.png",      27,  8,   0, 36, TILE_HEIGHT, 0, true);
		scoreWord     = new TexRect(camera, "score.png",     27,  68,  0, 45, TILE_HEIGHT, 0, true);
		groupWord     = new TexRect(camera, "groups.png",    27,  128, 0, 54, TILE_HEIGHT, 0, true);
		groupSizeWord = new TexRect(camera, "groupsize.png", 27,  188, 0, 90, TILE_HEIGHT, 0, true);
		stackWord     = new TexRect(camera, "stack.png",     460, 8,   0, 45, TILE_HEIGHT, 0, true);
	
		selectX = -1;
		selectY = -1;
		
		selected = new FadeRect(camera, 0, 0, -0.7f, TILE_WIDTH, TILE_HEIGHT, 0, 0, 0, 0, false);
	}
	
	public void update() {
		for (int i = 0; i < MAP_WIDTH; i++) {
			for (int j = 0; j < MAP_HEIGHT; j++) {
				map[i][j].update();
				if(map[i][j].isActive())
					map[i][j].getRect().fadeTo(1, (float) j / (float) MAP_HEIGHT, (float) j / (float) MAP_HEIGHT, 1, 250);
				else
					map[i][j].getRect().fadeTo(0.045f, 0.045f, 0.045f, 1, 250);
			}
		}
		if(window.mousePressed(GLFW_MOUSE_BUTTON_1) == Window.BUTTON_PRESSED) {
			if (selectX != -1) {
				Vector3f mouse = window.getMouseCoords(camera);
				int mx = getTileX(mouse.x);
				int my = getTileY(mouse.y);
				if(mx > -1 && my > -1 && mx < MAP_WIDTH && my < MAP_HEIGHT && map[mx][my].isActive()) {
					int temp = map[mx][my].getNum();
					map[mx][my].setNum(map[selectX][selectY].getNum());
					map[selectX][selectY].setNum(temp);
				}
				selectX = -1;
				selectY = -1;
				selected.fadeTo(0, 0, 0, 0, 250);
			}
			else {
				Vector3f mouse = window.getMouseCoords(camera);
				selectX = getTileX(mouse.x);
				selectY = getTileY(mouse.y);
				if(selectX > -1 && selectY > -1 && selectX < MAP_WIDTH && selectY < MAP_HEIGHT && map[selectX][selectY].isActive()) {
					selected.setPosition(MAP_X_OFFSET + selectX * TILE_WIDTH, MAP_Y_OFFSET + selectY * TILE_HEIGHT);
					selected.fadeTo(0, 0.6f, 0, 1, 250);
				}
				else {
					selectX = -1;
					selectY = -1;
				}
			}
		}
		else if(window.mousePressed(GLFW_MOUSE_BUTTON_2) == Window.BUTTON_PRESSED) {
			Vector3f mouse = window.getMouseCoords(camera);
			int mx = getTileX(mouse.x);
			int my = getTileY(mouse.y);
			bucketScore(mx, my);
		}
		selected.update();
		falling.update();
		
		runTime += Main.dtime;
		timeDisplay.setValue((int) (runTime / 1000.0));
		Vector3f mouse = window.getMouseCoords(camera);
		int mx = getTileX(mouse.x);
		int my = getTileY(mouse.y);
		if(mx > -1 && my > -1 && mx < MAP_WIDTH && my < MAP_HEIGHT)
			groupSize.setValue(bucketCount(map[mx][my].getNum(), mx, my));
		else
			groupSize.setValue(0);
		clearBucket();
	}
	
	public void render() {
		selected.render();
		for (int i = 0; i < MAP_WIDTH; i++)
			for (int j = 0; j < MAP_HEIGHT; j++)
				map[i][j].render();
		border.render();
		stack.render();
		if (end == 0)
			falling.render();
		groupSize.render();
		timeDisplay.render();
		score.render();
		deletions.render();
		timeWord.render();
		scoreWord.render();
		groupWord.render();
		groupSizeWord.render();
		stackWord.render();
	}
	
	private int getTileX(float x) {
		return (int) ((x - MAP_X_OFFSET) / TILE_WIDTH);
	}
	
	private int getTileY(float y) {
		return (int) ((y - MAP_Y_OFFSET) / TILE_HEIGHT);
	}
	
	public void clear() {
		for (int i = 0; i < MAP_WIDTH; i++) {
			for (int j = 0; j < MAP_HEIGHT; j++) {
				map[i][j].setNum(0);
				map[i][j].setActive(false);
				map[i][j].getRect().fadeTo(0.045f, 0.045f, 0.045f, 1, 500);
			}
		}
	}
	
	public MapEntry[][] getMap() {
		return map;
	}
	
	public void triggerEnd(int end) {
		this.end = end;
	}
	
	public boolean checkEnd() {
		return end == 1;
	}
	
	public void reset(int difficulty) {
		stack.setDifficulty(difficulty);
		falling.setDifficulty(difficulty);
		falling.reset();
		clear();
		Random random = new Random();
		for (int i = MAP_HEIGHT - difficulty / 2; i < MAP_HEIGHT; ++i) {
			for (int j = 0; j < MAP_WIDTH; ++j) {
				map[j][i].setNum(random.nextInt(difficulty));
				map[j][i].setActive(true);
			}
		}
		end = 0;
		score.setValue(0);
		deletions.setValue(0);
		timeDisplay.setValue(0);
		scoreMul = 1;
	}
	
	private void bucketScore(int x, int y) {
		int count = bucketCount(map[x][y].getNum(), x, y);
		if(count > 7) {
			score.add((int) (count * scoreMul));
			deletions.increment();
			scoreMul *= count / 6.0;
			falling.increaseRate(25);
			for (int i = 0; i < MAP_WIDTH; ++i)
				for (int j = 0; j < MAP_HEIGHT; ++j)
					if(bucket[i][j]) {
						map[i][j].setActive(false);
						map[i][j].setNum(0);
					}
			waterfall();
			for (int i = 0; i < MAP_WIDTH; ++i)
				for (int j = 0; j < MAP_HEIGHT; ++j)
					if(bucket[i][j])
						map[i][j].getRect().setColor(0, 1, 0);
		}
		else {
			for (int i = 0; i < MAP_WIDTH; ++i)
				for (int j = 0; j < MAP_HEIGHT; ++j)
					if(bucket[i][j])
						map[i][j].getRect().setColor(0.5f, 0.045f, 0.3f, 1);
		}
		clearBucket();
	}
	
	private int bucketCount(int search, int x, int y) {
		if(!map[x][y].isActive() || map[x][y].getNum() != search || bucket[x][y])
			return 0;
		int num = 1;
		bucket[x][y] = true;
		if (x > 0)
			num += bucketCount(search, x - 1, y);
		if (y > 0)
			num += bucketCount(search, x, y - 1);
		if (x < MAP_WIDTH  - 1)
			num += bucketCount(search, x + 1, y);
		if (y < MAP_HEIGHT - 1)
			num += bucketCount(search, x, y + 1);
		return num;
	}
	
	private void clearBucket() {
		for (int i = 0; i < MAP_WIDTH; ++i)
			for (int j = 0; j < MAP_HEIGHT; ++j)
				bucket[i][j] = false;
	}
	
	private void waterfall() {
		for(int i = 0; i < MAP_WIDTH; ++i) {
			for(int j = MAP_HEIGHT - 2; j > -1; --j) {
				if(map[i][j].isActive()) {
					int ty = j + 1;
					while (ty < Map.MAP_HEIGHT && !map[i][ty].isActive())
						++ty;
					--ty;
					int temp = map[i][j].getNum();
					map[i][j].setActive(false);
					map[i][j].setNum(0);
					map[i][ty].setNum(temp);
					map[i][ty].setActive(true);
				}
			}
		}
	}
	
	public void checkScore() {
		File file = new File("res/high.sco");
		try {
			if(file.exists()) {
				Scanner scanner = new Scanner(file);
				int score = scanner.nextInt();
				int high = Math.max(score, (int) this.score.getValue());
				scanner.close();
				
				PrintWriter writer = new PrintWriter(file);
				writer.write(Long.toString(high));
				writer.close();
			}
			else {
				PrintWriter writer = new PrintWriter(file);
				writer.write(Long.toString(score.getValue()));
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
