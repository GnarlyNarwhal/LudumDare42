package com.gnarly.engine.model;

import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.shaders.Shader;
import com.gnarly.engine.shaders.Shader2t;
import com.gnarly.engine.texture.Texture;

public class TexRect extends Rect {

	private Texture texture;
	private Shader2t shader = Shader.SHADER2T;
	
	public TexRect(Camera camera, String path, float x, float y, float z, float width, float height, float rotation, boolean gui) {
		super(camera, x, y, z, width, height, rotation, gui);
		texture = new Texture(path);
	}
	
	public TexRect(Camera camera, Texture texture, float x, float y, float z, float width, float height, float rotation, boolean gui) {
		super(camera, x, y, z, width, height, rotation, gui);
		this.texture = texture;
	}
	
	public void render() {
		texture.bind();
		shader.enable();
		Matrix4f cmat = gui ? camera.getProjection() : camera.getMatrix();
		shader.setMVP(cmat.translate(position.add(width * scale / 2, height * scale / 2, 0, new Vector3f())).rotateZ(rotation * 3.1415927f / 180).scale(width * scale, height * scale, 1).translate(-0.5f, -0.5f, 0));
		vao.render();
		shader.disable();
		texture.unbind();
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
