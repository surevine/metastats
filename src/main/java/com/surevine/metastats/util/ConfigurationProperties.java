package com.surevine.metastats.util;

import java.util.ResourceBundle;

public enum ConfigurationProperties {
	
	REPOSITORY_CACHE,
	REPOSITORY_PULL,
	REPOSITORY_POLL_PERIOD;

	private static final ResourceBundle bundle;
	
	static {
		bundle = ResourceBundle.getBundle("configuration");
	}
	
	public static String get(final ConfigurationProperties property) {
		return bundle.getString(property.name().toLowerCase().replace('_', '.'));
	}
	
	public static boolean is(final ConfigurationProperties property) {
		return get(property).equalsIgnoreCase("true");
	}
}
