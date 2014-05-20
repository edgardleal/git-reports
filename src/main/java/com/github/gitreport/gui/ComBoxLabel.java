package com.github.gitreport.gui;

import java.awt.Container;
import java.util.Vector;

import javax.swing.JComboBox;

public class ComBoxLabel<T> extends JComboBox<T> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ComBoxLabel(String label, Container c){
		super();
		init();
	}

	public ComBoxLabel(Vector<T> items, String Label, Container c){
		super(items);
		init();
	}

	private void init(){

	}

}
