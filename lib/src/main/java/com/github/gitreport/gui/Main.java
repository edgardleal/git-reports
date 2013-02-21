package com.github.gitreport.gui;

import javax.swing.JFrame;
/**
 *
 * @author Edgard Leal
 * @since  20-02-2013
 */
public class Main extends JFrame {

	private static final long serialVersionUID = 8534650546678741454L;

	public Main(){
		init();
	}

	private void init() {
		this.setSize(400,400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Main();
	}
}
