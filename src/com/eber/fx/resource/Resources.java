package com.eber.fx.resource;

import java.io.InputStream;

public class Resources {
	
	public static InputStream get(Class<?> fromClass, String name) {
		return fromClass.getResourceAsStream(name);
	}
	
	public static InputStream get(String name) {
		return get(Resources.class, name);
	}
	
	private Resources() {}
	
}
