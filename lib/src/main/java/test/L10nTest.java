package test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.junit.Test;

import com.github.gitreport.gui.language.L10n;
import com.github.gitreport.util.Str;


public class L10nTest {

	@Test
	public void testResourse(){
		InputStream file = this.getClass().getResourceAsStream("/com/github/gitreport/gui/language/Language_pt_BR.properties");
		byte[] b = new byte[1024];
		try {
			PrintWriter out = new PrintWriter(new File("out.txt"));
			int _b = 0;
			while((_b = file.read()) > -1)
			  out.write(_b);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue(b.length > 0);
	}

	@Test
	public void testLoad(){

		try {
			L10n.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = L10n.getString("label.project.folder");
		assertTrue(!Str.isNullOrEmpty(result));
	}
}
