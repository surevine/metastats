package com.surevine.metastats.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.google.common.io.Resources;

@SuppressWarnings("unchecked")
public class Language {

	private static Map<String, String> byExtension = new HashMap<String, String>();
	
	private static Map<String, String> byFilename = new HashMap<String, String>();
	
	static {
		try {
			final String languages = Resources.toString(Resources.getResource("languages.yml"), Charset.defaultCharset());

			final Map<?, ?> yaml = (Map<?, ?>) new Yaml().load(languages);
			
			for (final Object key : yaml.keySet()) {
				final Map<?, ?> yamlLanguage = (Map<?, ?>) yaml.get(key);
				
				final String language = String.valueOf(key);
				byExtension.put(String.valueOf(yamlLanguage.get("primary_extension")), language);

				final List<String> extensions = (List<String>) yamlLanguage.get("extensions");
				if (extensions != null) {
					for (final String extension : extensions) {
						byExtension.put(extension, language);
					}
				}
				
				final List<String> filenames = (List<String>) yamlLanguage.get("filenames");
				if (filenames != null) {
					for (final String filename : filenames) {
						byFilename.put(filename, language);
					}
				}
			}
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String get(final String filename) {
		final String language = byFilename.get(filename);
		
		if (language != null) {
			return language;
		} else if (filename.indexOf('.') > -1) {
			return byExtension.get(filename.substring(filename.lastIndexOf('.')));
		} else {
			return null;
		}
	}
}
