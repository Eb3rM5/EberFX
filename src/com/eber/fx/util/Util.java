package com.eber.fx.util;

import java.util.List;

import javafx.css.Styleable;
import javafx.scene.Scene;
import javafx.scene.layout.Region;

public final class Util {

	public static final String CSS_PACKAGE = "/com/eber/fx/resource/css/",
								CSS_EXT = ".css";
	
	public static final Scene addSheets(Scene scene, String ... sheets) {
		return addSheets(CSS_PACKAGE, scene, sheets);
	}
	
	public static final Scene addSheets(String packageLocation, Scene scene, String ... sheets) {
		if (sheets != null && sheets.length > 0) {
			List<String> stylesheets = scene.getStylesheets();
			for (String s : sheets) stylesheets.add(packageLocation + s + CSS_EXT);
		}
		
		return scene;
	}
	
	public static final <T extends Styleable> T addClass(T node, String ... classes){
		if (classes != null && classes.length > 0) node.getStyleClass().addAll(classes);
		return node;
	}
	
	public static final void maxWidth(Region ... nodes) {
		if (nodes != null && nodes.length > 0) for (Region n : nodes) n.setMaxWidth(Double.MAX_VALUE);
	}
	
	public static final void maxHeight(Region ... nodes) {
		if (nodes != null && nodes.length > 0) for (Region n : nodes) n.setMaxHeight(Double.MAX_VALUE);
	}
	
	private Util() {}
	
}
