package com.gnarly.engine.model;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.shaders.Shader;
import com.gnarly.engine.shaders.Shader2c;
import com.gnarly.engine.texture.Texture;
import com.gnarly.game.Main;

public class FadeRect extends Rect {

	protected Shader2c shader = Shader.SHADER2C;
	
	private float r, g, b, a, dr, dg, db, da;
	private double time;
	
	private boolean paused;
	
	public FadeRect(Camera camera, float x, float y, float z, float width, float height, float r, float g, float b, float a, boolean gui) {
		super(camera, x, y, z, width, height, 0, gui);
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.dr = 0;
		this.dg = 0;
		this.db = 0;
		this.da = 0;
		paused = false;
		time = 0;
	}
	
	public void update() {
		if(!paused) {
			double delta = Main.dtime;
			if (delta > time)
				delta = time;
			
			r += dr * delta;
			g += dg * delta;
			b += db * delta;
			a += da * delta;
			clamp();
			time -= delta;
		}
	}
	
	public void render() {
		shader.enable();
		shader.setColor(r, g, b, a);
		Matrix4f cmat = gui ? camera.getProjection() : camera.getMatrix();
		shader.setMVP(cmat.translate(position.add(width * scale / 2, height * scale / 2, 0, new Vector3f())).rotateZ(rotation * 3.1415927f / 180).scale(width * scale, height * scale, 1).translate(-0.5f, -0.5f, 0));
		vao.render();
		shader.disable();
	}
	
	public void fadeFromTo(float r, float g, float b, float a, float tr, float tg, float tb, float ta, double time) {
		setColor(r, g, b, a);
		fadeTo(tr, tg, tb, ta, time);
	}
	
	public void fadeTo(float r, float g, float b, float a, double time) {
		this.time = time;
		dr = (float) ((r - this.r) / time);
		dg = (float) ((g - this.g) / time);
		db = (float) ((b - this.b) / time);
		da = (float) ((a - this.a) / time);
	}
	
	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		stopFade();
		clamp();
	}
	
	public void setColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		stopFade();
		clamp();
	}
	
	public void setOpacity(float opacity) {
		a = opacity;
		stopFade();
		clamp();
	}
	
	public void pause() {
		paused = true;
	}
	
	public void resume() {
		paused = false;
	}
	
	private void stopFade() {
		dr = 0;
		dg = 0;
		db = 0;
		da = 0;
		time = 0;
	}
	
	private void clamp() {
		r = Math.min(Math.max(r, 0), 1);
		g = Math.min(Math.max(g, 0), 1);
		b = Math.min(Math.max(b, 0), 1);
		a = Math.min(Math.max(a, 0), 1);
	}
}