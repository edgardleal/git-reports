package com.github.gitreport.util;

public class RegexUtil {
	public static final String URL_PATTERN = "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)";
	public static final String PATH_PATTERN = "^(.+)([\\/]([^/]+))+$";
}
