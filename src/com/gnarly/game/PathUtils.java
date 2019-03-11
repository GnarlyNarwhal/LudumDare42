package com.gnarly.game;

import com.gnarly.engine.display.Window;
import com.gnarly.engine.properties.Properties;
import com.gnarly.engine.properties.PropertyReader;
import com.gnarly.engine.properties.UndeclaredPropertyException;

public class PathUtils {

	public static String res;
	public static String theme;
	
	public static void init(String path) {
		Properties props = PropertyReader.readProperties(path);
		try {
			res   = props.getAsString("texRes");
			theme = props.getAsString("theme");
			if (res.equals("dynamic")) {
				if(Window.SCREEN_HEIGHT > 2160)
					res  = "8k";
				else if(Window.SCREEN_HEIGHT > 1440)
					res  = "4k";
				else if(Window.SCREEN_HEIGHT > 1080)
					res  = "1440p";
				else if(Window.SCREEN_HEIGHT > 720)
					res  = "1080p";
				else
					res  = "720p";
			}
		} catch (UndeclaredPropertyException e) {
			e.printStackTrace();
			res = "4k";
			theme = "default";
		}
	}
}
