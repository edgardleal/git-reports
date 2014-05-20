package com.github.gitreport.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Setup {
	private String[] folders = { ".%1$sdata", ".%1$sresource", ".%1$sreports" };
	private String[] files = { "bootstrap-tabs.js", "core.css", "jquery.js",
			"bootstrap.min.css" };

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
		for (String string : files) {
			File file = new File(String.format(".%1$sreports%1$s%2$s",
					File.separator, string));
			if (!file.exists())
				saveURLtoFile(this.getClass().getResourceAsStream(string), file);
		}
	}

	private void saveURLtoFile(InputStream stream, File file) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			FileInputStream in = new FileInputStream(file);
			try {
				out.getChannel().transferFrom(in.getChannel(), 0,
						in.getChannel().size());
				out.flush();
			} finally {
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (file != null)
				System.out.println(file.getAbsolutePath());
		}
	}
}
