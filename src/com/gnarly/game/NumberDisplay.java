package com.gnarly.game;

import org.joml.Vector2f;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.model.FadeTexRect;
import com.gnarly.engine.texture.Texture;
import com.gnarly.game.map.MapEntry;

public class NumberDisplay {

	public static final int
		DISPLAY_BIN = 0,
		DISPLAY_OCT = 8,
		DISPLAY_DEC = 10,
		DISPLAY_HEX = 16;
	
	public static final int
		JUSTIFICATION_LEFT   = 0,
		JUSTIFICATION_CENTER = 1,
		JUSTIFICATION_RIGHT  = 2;						

	public static Texture[] nums;
	
	private static final float DIMENSION_RATIO = (float) (9.0 / 12.0);
	
	private Camera camera;
	
	private long num;
	private int minDigits;
	
	private int justification;
	private float x, y, height;
	
	private FadeTexRect display;
	
	private int displayFormat;
	
	public NumberDisplay(Camera camera, float x, float y, float z, float height, int justification, long number, int minDigits, int displayFormat) {
		this.camera = camera;
		this.num = number;
		this.minDigits = minDigits;
		this.justification = justification;
		this.x = x;
		this.y = y;
		this.height = height;
		this.displayFormat = displayFormat;
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
			MapEntry.nums = nums;
		}
		display = new FadeTexRect(camera, nums[0], 0, 0, z, 0, 0, 1, 1, 1, 1, true);
	}
	
	public void render() {
		float width = height * DIMENSION_RATIO;
		float x = this.x;
		String text = "error";
		switch (displayFormat) {
			case DISPLAY_BIN:
				text = Long.toBinaryString(num);
				break;
			case DISPLAY_OCT:
				text = Long.toOctalString(num);
				break;
			case DISPLAY_DEC:
				text = Long.toString(num);
				break;
			case DISPLAY_HEX:
				text = Long.toHexString(num);
				break;
		}
		int numDigits = Math.max(minDigits, text.length());
		if(justification == JUSTIFICATION_CENTER)
			x -= (numDigits / 2.0f) * width;
		else if(justification == JUSTIFICATION_RIGHT)
			x -= numDigits * width;
		display.setX(x);
		display.setY(y);
		display.setHeight(height);
		display.setWidth(width);
		for(int i = 0; i < numDigits; ++i) {
			int index = 0;
			if(i + text.length() - numDigits > -1) {
				if(text.charAt(i + text.length() - numDigits) == 'r')
					index = 16;
				else if(text.charAt(i + text.length() - numDigits) == 'o')
					index = 17;
				else if(text.charAt(i + text.length() - numDigits) >= 'a')
					index = text.charAt(i + text.length() - numDigits) - 87;
				else
					index = text.charAt(i + text.length() - numDigits) - 48;
			}
			display.setTexture(nums[index]);
			display.render();
			display.translate(width, 0, 0);
		}
	}
	
	private int getNumDigits() {
		String text = "error";
		switch (displayFormat) {
			case DISPLAY_BIN:
				text = Long.toBinaryString(num);
				break;
			case DISPLAY_OCT:
				text = Long.toOctalString(num);
				break;
			case DISPLAY_DEC:
				text = Long.toString(num);
				break;
			case DISPLAY_HEX:
				text = Long.toHexString(num);
				break;
		}
		return text.length();
	}
	
	public int getDigit(int index) {
		return (int) (num / Math.pow(displayFormat, index)) % displayFormat;
	}
	
	public long getValue() {
		return num;
	}
	
	public void setValue(long num) {
		this.num = num;
	}
	
	public void add(long num) {
		this.num += num;
	}
	
	public void sub(long num) {
		this.num -= num;
	}
	
	public void increment() {
		++num;
	}
	
	public void decrement() {
		--num;
	}
	
	public FadeTexRect getDisplay() {
		return display;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public void setDigitWidth(float width) {
		this.height = width / DIMENSION_RATIO;
	}
	
	public void setJustification(int justification) {
		float x = getX(justification);
		this.justification = justification;
		this.x = x;
	}
	
	public float getX() {
		return getX(JUSTIFICATION_LEFT);
	}

	public float getX(int justification) {
		return this.x + ((justification - this.justification) * height * DIMENSION_RATIO * getNumDigits() / 2);
	}
	
	public float getY() {
		return y;
	}
	
	public Vector2f getPosition() {
		return new Vector2f(x, y);
	}
	
	public Vector2f getPosition(int justification) {
		return new Vector2f(x, y);
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setDisplayFormat(int displayFormat) {
		this.displayFormat = displayFormat;
	}
}
