package controller;

import java.net.URL;

import javax.swing.ImageIcon;

public final class IconManager {
	private static String rootDirectory = "toolbarButtonGraphics";
	private static char separator = '/';
	private IconManager(){}
	
	public static ImageIcon getIcon(String category, String name){
		String[] tmp = {
				rootDirectory, category, name
		};
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < tmp.length; ++i){
			buf.append(separator);
			buf.append(tmp[i]);
		}
		String path = buf.toString();
		
		URL url = IconManager.class.getResource(path);
		ImageIcon icon = null;
		if (url != null){
			icon = new ImageIcon(url);
		}
		return icon;
	}
}
