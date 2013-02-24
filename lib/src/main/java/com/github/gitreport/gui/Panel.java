package com.github.gitreport.gui;

import javax.swing.JPanel;

public class Panel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 4443424372867852117L;


	public TextFieldLabel getTextFieldByName(String name){
		for (int i = 0; i < this.getComponentCount(); i++) {
			if(this.getComponent(i) instanceof TextFieldLabel)
				if(((TextFieldLabel)this.getComponent(i)).getName().equals(name))
					return (TextFieldLabel)this.getComponent(i);
		}
		return null;
	}
}
