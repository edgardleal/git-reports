package com.github.gitreport.gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;

import com.github.gitreport.GitHubLinker;
import com.github.gitreport.Templates;
import com.github.gitreport.TotalHistoryReport;
import com.github.gitreport.gui.language.L10n;

import freemarker.template.Template;

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

	public Main() {
		try {
			L10n.load();
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		init();
	}

	private void init() {
		try {
			this.setSize(400, 400);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			new TextFieldLabel(getPnlProjectFind(),
					L10n.getString("label.project.folder")).addButton(
					L10n.getString("button.find.dir"))
					.setInnerMethod(
							this.getClass().getMethod("onButtonFindClick",
									new Class[0]), this);
			new TextFieldLabel(getPnlProjectFind(),
					L10n.getString(L10n.LABEL_PROJECT_TITLE));
			new TextFieldLabel(getPnlProjectFind(),
					L10n.getString("label.giturl"));
			new TextFieldLabel(getPnlProjectFind(),
					L10n.getString("label.report.target"));
			new TextFieldLabel(getPnlProjectFind(),
					L10n.getString("label.report.branch"));

			pnlProjectFind.add(getBtnGenerateReport());
			this.getContentPane().add(getPnlProjectFind());

			this.setTitle(L10n.getString("app.title"));
			this.setVisible(true);
		} catch (Exception ex) {
			System.err.println("Could not start the program");
			ex.printStackTrace();
			System.exit(ERROR);
		}
	}

	public void onButtonFindClick() {
		TextFieldLabel textField = null;
		if (getFileDialog().showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			textField = getPnlProjectFind().getTextFieldByName(
					L10n.getString("label.project.folder"));
		if (textField != null) {
			textField.setText(getFileDialog().getSelectedFile().toString());
		}
	}

	public static void main(String[] args) {
		new Main();
	}

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
			btnGenerateReport = new JButton(
					L10n.getString("button.report.show"));
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
			report.setProjectName(projectName);
			report.setProjectVersion(getProjectBranch());

			GitHubLinker linker = new GitHubLinker();
			linker.setBase(getProjectUrl());
			report.setLinker(linker);

			Repository repo = new FileRepository(getProjectFolder());
			report.run(repo,getProjectBranch());

			String projectReleaseString = "release";
			String projectTotalString = "total-history";
			Template tpl = Templates.getTemplate(projectTotalString);
			tpl.setOutputEncoding("UTF-8");
			File out = new File(projectName + ".html");
			FileWriter writer = new FileWriter(out);
			tpl.process(report, writer);
			checkFiles();
			Desktop.getDesktop().browse(out.toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkFiles(){
		// TODO: implement to others files
		File coreCss = new File("core.css");
		if(!coreCss.exists())
			saveURLtoFile(this.getClass().getResourceAsStream("core.css"), coreCss);
		File jquery = new File("jquery.js");
		if(!jquery.exists())
			saveURLtoFile(this.getClass().getResourceAsStream("jquery.js"), coreCss);
	}

	private void saveURLtoFile(InputStream stream, File file){
		try {
			int b = 0;
			FileOutputStream out = new FileOutputStream(file);
			while((b = stream.read())>0)
				out.write(b);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getProjectUrl() {
		return getPnlProjectFind().getTextFieldByName(
				L10n.getString(L10n.LABEL_GITURL)).getText();
	}

	public String getProjectBranch() {
		return getPnlProjectFind().getTextFieldByName(
				L10n.getString(L10n.LABEL_REPORT_BRANCH)).getText();
	}

	public String getProjectFolder() {
		return getPnlProjectFind().getTextFieldByName(
				L10n.getString(L10n.LABEL_PROJECT_FOLDER)).getText();
	}

	public String getProjectTitle() {
		return getPnlProjectFind().getTextFieldByName(
				L10n.getString(L10n.LABEL_PROJECT_TITLE)).getText();
	}
}
