package com.github.gitreport.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RepositoryDTO {
	private ArrayList<RepositoryModel> innerList = null;

	public ArrayList<RepositoryModel> getList() {
		return getInnerList();
	}

	public void insert(RepositoryModel model) {
		int index = getList().indexOf(model);
		if (index > -1)
			getList().get(index).setTitle(model.getTitle());
		else
			getList().add(model);
		flush();
	}

	public void flush() {
		saveList();
	}

	private ArrayList<RepositoryModel> getInnerList() {
		if (innerList == null) {
			loadList();
			if (innerList == null)
				innerList = new ArrayList<RepositoryModel>();
		}
		return innerList;
	}

	private void loadList() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					new File(getFileName())));
			innerList = (ArrayList<RepositoryModel>) in.readObject();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveList() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(new File(getFileName())));
			out.writeObject(getInnerList());
			out.flush();
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getFileName() {
		return String.format("data%1$srepository.dat", File.separator);
	}
}
