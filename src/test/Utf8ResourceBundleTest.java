package test;

import static org.junit.Assert.assertTrue;

import java.util.ResourceBundle;

import org.junit.Test;

import com.github.gitreport.gui.language.Utf8ResourceBundle;

public class Utf8ResourceBundleTest {
	@Test
	public void testLoadResource() {
		ResourceBundle resource =  Utf8ResourceBundle
				.getBundle("Language");
		assertTrue(resource.containsKey("label.project.folder"));
	}
}
