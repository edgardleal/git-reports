package com.github.gitreport.gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.github.gitreport.util.Str;

public class TextFieldLabel extends JTextField implements ActionListener{

	/**
	 * represents a internal increment for automatic position of all
	 * TextFieldLabel created
	 */
	private static int yIncrement = 30;

	/**
	 *
	 */
	private static final long serialVersionUID = -3011239764846637078L;
	/**
	 * Space between TextField and the button
	 */
	private static final int SPACE = 5;
	/**
	 * Horizontal increment for the buttons added inside this TextField
	 */
	private int xIncrement = SPACE;
	private JLabel internalLabel = null;
	private Container parentContaier = null;

	public TextFieldLabel() {

	}

	public TextFieldLabel(Container c, String name) {
		super(Str.EMPTY);
		this.setName(name);
		this.setBounds(10, yIncrement, 300, 25);
		c.add(this);
		c.add(getLabel());
		yIncrement += 50;
		parentContaier = c;
	}

	/**
	 * Add a button beside the JTextField and return this button for to be
	 * configurated
	 *
	 * @return
	 */
	public Button addButton(String label) {
		return getNewButton(label);
	}

	public JLabel getLabel() {
		if (internalLabel == null) {
			internalLabel = new JLabel(this.getName());
			internalLabel.setBounds(10, this.getLocation().y - 20,
					this.getSize().width, 25);
		}
		return internalLabel;
	}

	private Button getNewButton(String label) {
		Button result = new Button(label.replaceAll("&", Str.EMPTY));
		int index = label.indexOf('&');
		if (index > 0)
			result.setMnemonic(label.charAt(index));

		result.setBounds(this.getLocation().x + this.getSize().width + xIncrement,
				this.getLocation().y, label.length() * 10, this.getSize().height);
		xIncrement += result.getSize().width + SPACE;
		result.setName(label);
		if(parentContaier!=null)
			parentContaier.add(result);
		return result;
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
