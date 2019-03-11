package com.gnarly.game.map;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.model.FadeTexRect;
import com.gnarly.engine.model.Rect;
import com.gnarly.engine.texture.Texture;
import com.gnarly.game.Main;
import com.gnarly.game.NumberDisplay;

public class MapEntry extends Rect {

	public static Texture[] nums;
	
	public static final int
		TILE_WIDTH  = 9,
		TILE_HEIGHT = 12;
	
	private FadeTexRect num;

	private boolean active;
	
	private int digit;
	
	public MapEntry(Camera camera, float x, float y) {
		super(camera, x, y, -0.5f, TILE_WIDTH, TILE_HEIGHT, 0, false);
		if(nums == null) {
			nums = new Texture[18];
			nums[0]  = new Texture("zero.png");
			nums[1]  = new Texture("one.png");
			nums[2]  = new Texture("two.png");
			nums[3]  = new Texture("three.png");
			nums[4]  = new Texture("four.png");
			nums[5]  = new Texture("five.png");
			nums[6]  = new Texture("six.png");
			nums[7]  = new Texture("seven.png");
			nums[8]  = new Texture("eight.png");
			nums[9]  = new Texture("nine.png");
			nums[10] = new Texture("a.png");
			nums[11] = new Texture("b.png");
			nums[12] = new Texture("c.png");
			nums[13] = new Texture("d.png");
			nums[14] = new Texture("e.png");
			nums[15] = new Texture("f.png");
			nums[16] = new Texture("r.png");
			nums[17] = new Texture("o.png");
			NumberDisplay.nums = nums;
		}
		num = new FadeTexRect(camera, nums[0], 0, 0, -0.5f, TILE_WIDTH, TILE_HEIGHT, 0.045f, 0.045f, 0.045f, 1, true);
		this.camera = camera;
		active = false;
		digit  = (byte) (Math.random() * 2);
	}
	
	public void update() {
		num.update();
	}
	
	public void render() {
		num.setPosition(position);
		num.setTexture(nums[digit]);
		num.render();
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public FadeTexRect getRect() {
		return num;
	}

	public int getNum() {
		return digit;
	}
	
	public void setNum(int digit) {
		this.digit = digit;
	}
	
	public void setNum(char digit) {
		if(digit >= 'a')
			this.digit = digit - 87;
		else
			this.digit = digit - 48;
	}
}
