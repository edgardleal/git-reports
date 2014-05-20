package com.github.gitreport.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.github.gitreport.util.Str;

public class TextFieldLabel extends JTextField implements ActionListener, KeyListener {

  /**
   * represents a internal increment for automatic position of all TextFieldLabel created
   */
  private static int yIncrement = 30;
  private String regex = null;
  private final Color validColor = Color.GREEN;
  private final Color invalidColor = Color.RED;
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
  private boolean valid;

  public TextFieldLabel() {
    super();
    init();
  }

  private void init() {
    this.addKeyListener(this);
    this.addActionListener(this);
  }

  public String getRegex() {
    return regex;
  }

  public void setRegex(String regex) {
    this.regex = regex;
  }

  public TextFieldLabel(Container c, String name) {
    super(Str.EMPTY);
    this.setName(name);
    this.setBounds(10, yIncrement, 300, 25);
    c.add(this);
    c.add(getLabel());
    yIncrement += 50;
    parentContaier = c;
    init();
  }

  /**
   * Add a button beside the JTextField and return this button for to be configurated
   * 
   * @param label
   * @return
   */
  public Button addButton(String label) {
    return getNewButton(label);
  }

  public JLabel getLabel() {
    if (internalLabel == null) {
      internalLabel = new JLabel(this.getName());
      internalLabel.setBounds(10, this.getLocation().y - 20, this.getSize().width, 25);
    }
    return internalLabel;
  }

  private Button getNewButton(String label) {
    Button result = new Button(label.replaceAll("&", Str.EMPTY));
    int index = label.indexOf('&');
    if (index > 0) {
      result.setMnemonic(label.charAt(index));
    }

    result.setBounds(this.getLocation().x + this.getSize().width + xIncrement,
        this.getLocation().y, label.length() * 10, this.getSize().height);
    xIncrement += result.getSize().width + SPACE;
    result.setName(label);
    if (parentContaier != null) {
      parentContaier.add(result);
    }
    return result;
  }

  public void actionPerformed(ActionEvent arg0) {

  }

  public void keyPressed(KeyEvent key) {
    if (getRegex() == null) {
      return;
    }
    int code = key.getKeyCode();
    String text = this.getText();
    if (code == 0 || code == 8 || code == 27 || code == 127 || code == 37 || code == 38
        || code == 39 || code == 40 || code == 117 || code == 17)
      ;
    else {
      text = this.getText() + key.getKeyChar();
    }
    if (text.matches(getRegex())) {
      this.setBackground(validColor);
    } else {
      this.setBackground(invalidColor);
    }
  }

  public void keyReleased(KeyEvent arg0) {

  }

  public void keyTyped(KeyEvent key) {

  }

  public boolean isValid() {
    if (getRegex() == null) {
      return true;
    }

    valid = this.getText().matches(getRegex());

    return valid;
  }
}
