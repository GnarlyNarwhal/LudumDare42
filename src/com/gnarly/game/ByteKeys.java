package com.gnarly.game;

import static com.gnarly.engine.display.Window.*;
import static com.gnarly.game.PathUtils.res;
import static com.gnarly.game.PathUtils.theme;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_J;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SEMICOLON;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.engine.model.FadeTexRect;

public class ByteKeys {

	private Window window;
	private Camera camera;
	
	private FadeTexRect[] letTexs;
	
	public ByteKeys(Window window, Camera camera) {
		this.window = window;
		this.camera = camera;
		
		letTexs = new FadeTexRect[8];
		letTexs[0] = new FadeTexRect(camera, "res/" + theme + "/letters/" + res + "/a.png",         0,   0, 0, 16, 16, 0, 0, 0, 1, true);
		letTexs[1] = new FadeTexRect(camera, "res/" + theme + "/letters/" + res + "/s.png",         16,  0, 0, 16, 16, 0, 0, 0, 1, true);
		letTexs[2] = new FadeTexRect(camera, "res/" + theme + "/letters/" + res + "/d.png",         32,  0, 0, 16, 16, 0, 0, 0, 1, true);
		letTexs[3] = new FadeTexRect(camera, "res/" + theme + "/letters/" + res + "/f.png",         48,  0, 0, 16, 16, 0, 0, 0, 1, true);
		letTexs[4] = new FadeTexRect(camera, "res/" + theme + "/letters/" + res + "/j.png",         64,  0, 0, 16, 16, 0, 0, 0, 1, true);
		letTexs[5] = new FadeTexRect(camera, "res/" + theme + "/letters/" + res + "/k.png",         80,  0, 0, 16, 16, 0, 0, 0, 1, true);
		letTexs[6] = new FadeTexRect(camera, "res/" + theme + "/letters/" + res + "/l.png",         96,  0, 0, 16, 16, 0, 0, 0, 1, true);
		letTexs[7] = new FadeTexRect(camera, "res/" + theme + "/letters/" + res + "/semicolon.png", 112, 0, 0, 16, 16, 0, 0, 0, 1, true);
	}
	
	public void update() {
		for (int i = 0; i < 8; ++i)
			letTexs[i].update();
		checkKey(GLFW_KEY_A,         0);
		checkKey(GLFW_KEY_S,         1);
		checkKey(GLFW_KEY_D,         2);
		checkKey(GLFW_KEY_F,         3);
		checkKey(GLFW_KEY_J,         4);
		checkKey(GLFW_KEY_K,         5);
		checkKey(GLFW_KEY_L,         6);
		checkKey(GLFW_KEY_SEMICOLON, 7);
	}
	
	public void render() {
		for (int i = 0; i < 8; ++i)
			letTexs[i].render();
	}
	
	
	private void checkKey(int key, int index) {
		if (window.keyPressed(key) >= BUTTON_PRESSED)
			letTexs[index].setColor(1, 0, 0, 1);
		else if(window.keyPressed(key) == BUTTON_RELEASED)
			letTexs[index].fadeTo(0, 0, 0, 1, 500);
	}
}
