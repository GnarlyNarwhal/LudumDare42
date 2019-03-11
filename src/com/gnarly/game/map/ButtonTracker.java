package com.gnarly.game.map;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.engine.model.FadeTexRect;

public class ButtonTracker extends FadeTexRect {

	public static final int
		TRACK_DOWN     = 0,
		TRACK_PRESSED  = 1,
		TRACK_RELEASED = 2;
	
	private Window window;
	
	private int key;
	private int method;
	private boolean state;
	
	public ButtonTracker(Window window, Camera camera, String path, float x, float y, float z, float width, float height, int key, int method) {
		super(camera, path, x, y, z, width, height, 1, 1, 1, 1, true);
		this.window = window;
		this.key = key;
		this.method = method;
		state = false;
	}
	
	public void update() {
		super.update();
		if (state && !checkKey()) { 
			state = false;
			fadeTo(1, 1, 1, 1, 500);
		}
		else if(!state && checkKey()) {	
			state = true;
			setColor(0, 1, 0, 1);
		}
	}
	
	private boolean checkKey() {
		switch (method) {
			case TRACK_DOWN:
				return window.keyPressed(key) > Window.BUTTON_UNPRESSED;
			case TRACK_PRESSED:
				return window.keyPressed(key) == Window.BUTTON_PRESSED;
			case TRACK_RELEASED:
				return window.keyPressed(key) == Window.BUTTON_RELEASED;
		}
		return false;
	}
}
