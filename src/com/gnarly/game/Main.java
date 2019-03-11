package com.gnarly.game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

import com.gnarly.engine.audio.ALManagement;
import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.engine.properties.Properties;
import com.gnarly.engine.properties.PropertyReader;
import com.gnarly.engine.properties.UndeclaredPropertyException;
import com.gnarly.engine.shaders.Shader;

public class Main {

	public static final int
		PANEL_PLAY = 1,
		PANEL_END  = 2;
	
	private int FPS = 60;
	
	public static double dtime;
	public static double ttime;	
	
	private ALManagement al;
	
	private Window window;
	private Camera camera;
			
	private int state = 0;
	
	private PlayPanel play;
	private EndPanel  end;
	
	private Panel panel;
	
	public void start() {
		long curTime, pastTime, startTime, nspf = 1000000000 / FPS;
		init();
		pastTime = System.nanoTime();
		startTime = pastTime;
		while(!window.shouldClose()) {
			curTime = System.nanoTime();
			if(curTime - pastTime > nspf) {
				dtime = (curTime -  pastTime) / 1000000d;
				ttime = (curTime - startTime) / 1000000d;
				update();
				render();
				pastTime = curTime;
			}
		}
		al.destroy();
		Window.terminate();
	}
	
	private void init() {
		al = new ALManagement();
		window = new Window("TBD", true);
		camera = new Camera(576, 324);
		Shader.init();
		PathUtils.init("res/options.prop");	
		
		play = new PlayPanel(window, camera);
		end = new EndPanel(window, camera);
		
		panel = end;
		state = PANEL_END;
	}
	
	private void update() {
		window.update();
		panel.update();
		camera.update();
		
		switch(state) {
			case PANEL_PLAY: {
				int state = play.getState();
				if(state == PANEL_END) {
					end.setActive();
					panel = end;
					this.state = state;
				}
				break;
			}
			case PANEL_END: {
				int state = end.getState();
				if(state == PANEL_PLAY) {
					play.setActive(end.getDifficulty());
					panel = play;
					this.state = state;
				}
				break;
			}
		}
	}
	
	private void render() {
		window.clear();
		panel.render();
		window.swap();
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}
