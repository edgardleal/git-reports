package com.github.gitreport.gui.language;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.gitreport.data.Config;
import com.github.gitreport.data.Constant;

/**
 * Takes care of HonModMan localization.
 * 
 * Gets proper text strings from the resources according to the default or set locale.
 */
public class L10n {

  // This is where property files with translations are
  private static final String RESOURCE_NAME = "Language";
  private static final String DEFAULT_LOCALE = "en";
  private static ResourceBundle resource;
  private static ResourceBundle defaultResource;
  private static Preferences prefs;
  private static Log logger = LogFactory.getLog(L10n.class);

  private static Locale currentLocale;
  private static String languageLocale;
  public static final String APP_TITLE = "app.title";
  public static final String LABEL_PROJECT_FOLDER = "label.project.folder";
  public static final String LABEL_PROJECT_TITLE = "label.project.title";
  public static final String LABEL_REPORT_TARGET = "label.report.target";
  public static final String LABEL_REPORT_BRANCH = "label.report.branch";
  public static final String LABEL_GITURL = "label.giturl";
  public static final String BUTTON_FIND_DIR = "button.find.dir";
  public static final String BUTTON_REPORT_SHOW = "button.report.show";

  static {
  }

  /**
   * Creates a new instance of L10n
   */
  public L10n() {}

  /**
   * Loads l10n resources
   * 
   * If user didn't set language preference the resources are loaded according to default locale.
   * Otherwise according to the user preference.
   * 
   * @throws IOException if the resource is not found.
   */
  public static void load() throws IOException {
    prefs = Preferences.userNodeForPackage(L10n.class);
    languageLocale = prefs.get("locale", "DUMMY_DEFAULT");
    load(languageLocale);
  }

  public static boolean load(String locale) throws IOException {
    boolean result = false;
    Locale loc;
    logger.info(String.format(Constant.LOCALE_INFO, locale));
    if (locale.equals("DUMMY_DEFAULT")) {
      loc = Locale.getDefault();
      logger.info("Using default locale " + loc.toString());
    } else { // parse the string of format language_country_variant
      String[] s = locale.split("_");
      switch (s.length) {
        case 1:
          loc = new Locale(s[0]);
          break;
        case 2:
          loc = new Locale(s[0], s[1]);
          break;
        case 3:
          loc = new Locale(s[0], s[1], s[2]);
          break;
        default: // this should never happen
          loc = Locale.getDefault();
          logger.error(String.format(Constant.LOAD_LANGUAGE_PROBLEM, locale));
      }
    }
    try {
      if (locale.equals("file")) {
        resource =
            new PropertyResourceBundle(new InputStreamReader(new FileInputStream(
                Config.MANAGER_FOLDER + File.separator + "gitreport.properties"), "UTF-8"));
      } else {
        resource = Utf8ResourceBundle.getBundle(RESOURCE_NAME, loc);
      }
      Config.getInstance().setLanguage(loc.toString());
      result = true;
    } catch (IOException e) {
      e.printStackTrace();
      logger.error("Error loading language " + loc.toString(), e);
      Config.getInstance().setLanguage(DEFAULT_LOCALE);
    }
    try {
      defaultResource = Utf8ResourceBundle.getBundle(RESOURCE_NAME, new Locale(DEFAULT_LOCALE));
    } catch (Exception e) {
      e.printStackTrace();
    }
    currentLocale = loc;
    return result;
  }

  /**
   * Gets string for the given key
   * 
   * Removes the first ampersand sign (&) because it is assumed that it is an indiaction of a
   * mnemonic.
   * 
   * @param key Key of the required value
   * @return
   * @throws NullPointerException in case that <code>load()</code> wasn't called first or it failed.
   * @throws MissingResourceException in case the <code>key</code> is not defined!
   */
  public static String getString(String key) {

    try {
      StringBuilder sb;
      sb = new StringBuilder(resource.getString(key));
      int i = sb.indexOf("&");
      if (i >= 0) {
        sb.deleteCharAt(i);
      }
      return sb.toString();
    } catch (MissingResourceException e) {
      try {
        logger.warn("The key \"" + key + "\" is not defined in the property file for "
            + currentLocale);
        StringBuilder sb;
        sb = new StringBuilder(defaultResource.getString(key));
        int i = sb.indexOf("&");
        if (i >= 0) {
          sb.deleteCharAt(i);
        }
        return sb.toString();
      } catch (MissingResourceException ex) {
        logger.warn("The key \"" + key + "\" is not defined in the DEFAULT property file!");
        return key; // nothing else we can do...
      }
    }
  }

  /**
   * Returns mnemonic for the given key.
   * 
   * If the mnemonic wasn't set in the string for the key then returns -1.
   * 
   * @param key Key of the required value
   * @return
   * @trhows NullPointerException in case that load() wasn't called first or it failed.
   */
  public static int getMnemonic(String key) {
    try {
      StringBuilder sb;
      sb = new StringBuilder(resource.getString(key));
      int i = sb.indexOf("&");
      if (i < 0) {
        return -1;
      }
      if (i + 1 == sb.length()) {
        return -1;
      }
      Character c = sb.charAt(i + 1);
      if (i >= 0) {
        sb.deleteCharAt(i);
      }
      // return Character.toUpperCase(c);
      return c;
    } catch (MissingResourceException e) {
      logger.warn("The key \"" + key
          + "\" is not defined in the property file! Couldn't return mnemonic.");
      return 0;
    }
  }

  /**
   * Returns current locale.
   * 
   * @return current locale.
   */
  public static Locale getCurrentLocale() {
    return (Locale) currentLocale.clone();
  }

  /**
   * Returns string representing current locale.
   * 
   * @return String denoting the current locale.
   */
  public static String getLanguageLocale() {
    return languageLocale;
  }

  public static String getDefaultLocale() {
    return DEFAULT_LOCALE;
  }

  public static ResourceBundle getResource() {
    return resource;
  }

  public static void setResource(ResourceBundle resource) {
    L10n.resource = resource;
  }
}
