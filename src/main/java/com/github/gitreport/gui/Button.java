package com.github.gitreport.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JButton;

/**
 * 
 * @author Edgard
 * 
 */
public class Button extends JButton implements ActionListener {

  /**
   *
   */
  private static final long serialVersionUID = -6916592704070659158L;
  private Method innerMethod = null;
  private Object methodParent;

  public Button() {
    super();
    init();
  }

  public Button(String label) {
    super(label);
    init();
  }

  private void init() {
    this.addActionListener(this);
  }

  public Method getInnerMethod() {
    return innerMethod;
  }

  /**
   * This Method need dont have any parameter
   * 
   * @param innerMethod a {@link Method} of the class {@link Container} from this button
   * @param methodParent
   */
  public void setInnerMethod(Method innerMethod, Object methodParent) {
    if (innerMethod != null && innerMethod.getParameterTypes().length > 0) {
      throw new RuntimeException("The method need don't have any parameter");
    }
    if (methodParent == null) {
      throw new RuntimeException("The parameter methodParent cold not be null");
    }

    this.methodParent = methodParent;
    this.innerMethod = innerMethod;
  }

  public void actionPerformed(ActionEvent e) {
    if (getInnerMethod() != null) {
      try {
        getInnerMethod().invoke(this.methodParent, new Object[0]);
      } catch (IllegalAccessException ex) {
        ex.printStackTrace();
      } catch (IllegalArgumentException ex) {
        ex.printStackTrace();
      } catch (InvocationTargetException ex) {
        ex.printStackTrace();
      }
    }
  }
}
