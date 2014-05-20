package com.github.gitreport.gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;

import com.github.gitreport.GitHubLinker;
import com.github.gitreport.Templates;
import com.github.gitreport.TotalHistoryReport;
import com.github.gitreport.data.RepositoryDTO;
import com.github.gitreport.data.RepositoryModel;
import com.github.gitreport.data.Setup;
import com.github.gitreport.gui.language.L10n;
import com.github.gitreport.util.RegexUtil;
import com.github.gitreport.util.Str;
import freemarker.log.Logger;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * 
 * @author Edgard Leal
 * @since 20-02-2013
 */
public class Main extends JFrame implements ActionListener {

  private static final long serialVersionUID = 8534650546678741454L;
  private JFileChooser fileDialog = null;
  private JButton btnGenerateReport = null;
  private Panel pnlProjectFind = null;
  private final RepositoryDTO dto = new RepositoryDTO();
  private final Logger logger = Logger.getLogger(getClass().getName());

  public Main() {
    try {
      L10n.load();
    } catch (IOException ex) {
      logger.error("Error loading L10n", ex);
    }
    init();
  }

  private void loadFirst() {
    if (dto.getList().size() > 0) {
      assignScreen(dto.getList().get(dto.getList().size() - 1));
    }
  }

  private void init() {
    try {
      this.setSize(400, 400);
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      TextFieldLabel projectFolder =
          new TextFieldLabel(getPnlProjectFind(), L10n.getString("label.project.folder"));
      projectFolder.addButton(L10n.getString("button.find.dir")).setInnerMethod(
          this.getClass().getMethod("onButtonFindClick", new Class[0]), this);
      projectFolder.setRegex(RegexUtil.PATH_PATTERN);

      new TextFieldLabel(getPnlProjectFind(), "projectTitle").setRegex(".+");
      new TextFieldLabel(getPnlProjectFind(), L10n.getString(L10n.LABEL_GITURL))
          .setRegex(RegexUtil.URL_PATTERN);
      new TextFieldLabel(getPnlProjectFind(), L10n.getString("label.report.target"))
          .setText(new File(String.format(".%sreports", File.separator)).toString());
      new TextFieldLabel(getPnlProjectFind(), L10n.getString("label.report.branch"));

      pnlProjectFind.add(getBtnGenerateReport());
      this.getContentPane().add(getPnlProjectFind());

      this.setTitle(L10n.getString("app.title"));
      this.setVisible(true);
      new Setup().checkFolders();
      new Setup().checkFiles();
      loadFirst();
    } catch (NoSuchMethodException | SecurityException ex) {
      logger.error("Error inicializing application", ex);
      logger.info("exiting...");
      System.exit(ERROR);
    }
  }

  public static Logger getLogger(Class<?> c) {
    Logger logger = Logger.getLogger(c.getName());

    return logger;
  }

  private RepositoryModel assignModel(RepositoryModel model) {
    model.setTitle(getProjectTitle());
    model.setFolder(getProjectFolder());
    model.setReportTarget(Str.EMPTY);
    model.setUrl(getProjectUrl());
    model.getBranchs().add(getProjectBranch());
    return model;
  }

  private void assignScreen(RepositoryModel model) {
    getPnlProjectFind().getTextFieldByName(L10n.getString(L10n.LABEL_PROJECT_TITLE)).setText(
        model.getTitle());
    getPnlProjectFind().getTextFieldByName(L10n.getString(L10n.LABEL_PROJECT_FOLDER)).setText(
        model.getFolder());
    getPnlProjectFind().getTextFieldByName(L10n.getString(L10n.LABEL_REPORT_TARGET)).setText(
        model.getReportTarget());
    getPnlProjectFind().getTextFieldByName(L10n.getString(L10n.LABEL_GITURL)).setText(
        model.getUrl());
    getPnlProjectFind().getTextFieldByName(L10n.getString(L10n.LABEL_REPORT_BRANCH)).setText(
        model.getBranchs().size() > 0 ? model.getBranchs().first() : Str.EMPTY);
  }

  public void onButtonFindClick() {
    TextFieldLabel textField = null;
    if (getFileDialog().showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      textField = getPnlProjectFind().getTextFieldByName(L10n.getString("label.project.folder"));
    }
    if (textField != null) {
      textField.setText(getFileDialog().getSelectedFile().toString());
    }
  }

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1].getClassName());
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }

    Main main = new Main();
  }

  @Override
  public void actionPerformed(ActionEvent a) {
    if (a.getSource() == getBtnGenerateReport()) {
      generateReport();
    }
  }

  public JFileChooser getFileDialog() {
    if (fileDialog == null) {
      fileDialog = new JFileChooser(new File("."));
      fileDialog.setAcceptAllFileFilterUsed(false);
      fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }
    return fileDialog;
  }

  public void setFileDialog(JFileChooser fileDialog) {
    this.fileDialog = fileDialog;
  }

  public JButton getBtnGenerateReport() {
    if (btnGenerateReport == null) {
      btnGenerateReport = new JButton(L10n.getString("button.report.show"));
      btnGenerateReport.addActionListener(this);
      btnGenerateReport.setBounds(10, 300, 100, 20);
    }
    return btnGenerateReport;
  }

  public void setBtnGenerateReport(JButton btnGenerateReport) {
    this.btnGenerateReport = btnGenerateReport;
  }

  public Panel getPnlProjectFind() {
    if (pnlProjectFind == null) {
      pnlProjectFind = new Panel();
      pnlProjectFind.setLayout(null);
    }
    return pnlProjectFind;
  }

  public void setPnlProjectFind(Panel pnlProjectFind) {
    this.pnlProjectFind = pnlProjectFind;
  }

  public void generateReport() {
    try {
      String projectName = getProjectTitle();
      TotalHistoryReport report = new TotalHistoryReport();
      // ReleaseReport report = new ReleaseReport();

      report.setProjectName(projectName);
      report.setProjectVersion(getProjectBranch());

      GitHubLinker linker = new GitHubLinker();
      linker.setBase(getProjectUrl());
      report.setLinker(linker);

      Repository repo = new FileRepository(getProjectFolder());
      report.run(repo, "master");

      String projectReleaseString = "release";
      String projectTotalString = "total-history";
      Template template = Templates.getTemplate(projectTotalString);
      template.setOutputEncoding("UTF-8");

      File out = new File(String.format("reports%1$s%2$s.html", File.separator, projectName));
      FileWriter writer = new FileWriter(out);
      template.process(report, writer);
      Desktop.getDesktop().browse(out.toURI());
      dto.insert(assignModel(new RepositoryModel()));
      System.gc();
    } catch (TemplateException | IOException e) {
      logger.error("ERROR", e);
      JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
    }
  }

  public String getProjectUrl() {
    return getPnlProjectFind().getTextFieldByName(L10n.getString(L10n.LABEL_GITURL)).getText();
  }

  public String getProjectBranch() {
    return getPnlProjectFind().getTextFieldByName(L10n.getString(L10n.LABEL_REPORT_BRANCH))
        .getText();
  }

  public String getProjectFolder() {
    return getPnlProjectFind().getTextFieldByName(L10n.getString(L10n.LABEL_PROJECT_FOLDER))
        .getText();
  }

  public String getProjectTitle() {
    return getPnlProjectFind().getTextFieldByName("projectTitle").getText();
  }
}
