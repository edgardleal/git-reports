package com.github.gitreport.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Setup {
	private String[] folders = { ".%1$sdata", ".%1$sresource", ".%1$sreports" };

	public boolean checkFolders() {
		boolean result = false;
		for (String folder : folders) {
			File f = new File(String.format(folder, File.separator));
			if (!f.exists()) {
				result = true;
				f.mkdir();
			}
		}
		return result;
	}

	public void checkFiles() {
		// TODO: implement to others files
		File coreCss = new File(String.format(".%1$sreports%1$score.css", File.separator));
		if (!coreCss.exists())
			saveURLtoFile(this.getClass().getResourceAsStream("core.css"),
					coreCss);
		File jquery = new File(String.format(".%1$sreports%1$sjquery.js", File.separator));
		if (!jquery.exists())
			saveURLtoFile(this.getClass().getResourceAsStream("jquery.js"),
					jquery);
	}

	private void saveURLtoFile(InputStream stream, File file) {
		try {
			int b = 0;
			FileOutputStream out = new FileOutputStream(file);
			while ((b = stream.read()) > 0)
				out.write(b);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
