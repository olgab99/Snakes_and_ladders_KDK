package com.olmatech.kindle.snakes;

import java.util.Locale;
import java.util.ResourceBundle;

public class Common {
	
	private final static Locale currentLocale = Locale.getDefault(); //new Locale("en", "US"); 
	public final static ResourceBundle TITLES = ResourceBundle.getBundle("titles", currentLocale);

}
