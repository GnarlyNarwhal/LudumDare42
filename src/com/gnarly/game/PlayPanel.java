package com.gnarly.game;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.engine.model.ColRect;
import com.gnarly.game.map.Map;
import com.gnarly.game.map.Stack;

public class PlayPanel extends Panel {

	private Window window;
	private Camera camera;

	private Map map;
	
	public PlayPanel(Window window, Camera camera) {
		this.window = window;
		this.camera = camera;
		
		state = Main.PANEL_PLAY;
		
		map = new Map(window, camera, null, Stack.DIFFICULTY_MEDIUM);
	}
	
	public void update() {
		map.update();
		if(map.checkEnd()) {
			state = Main.PANEL_END;
			map.checkScore();
		}
	}

	public void render() {
		map.render();
	}
	
	public int getState() {
		int temp = state;
		state = Main.PANEL_PLAY;
		return temp;
	}
	
	public void setActive(int difficulty) {
		map.reset(difficulty);
	}
}
