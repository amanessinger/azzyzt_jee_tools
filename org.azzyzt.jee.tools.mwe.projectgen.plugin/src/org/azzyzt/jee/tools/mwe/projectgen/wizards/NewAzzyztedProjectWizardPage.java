package org.azzyzt.jee.tools.mwe.projectgen.wizards;

import java.util.SortedMap;
import java.util.TreeMap;

import org.azzyzt.jee.tools.mwe.projectgen.workers.AzzyztedProjectParameters;
import org.azzyzt.jee.tools.mwe.projectgen.workers.NewAzzyztedProjectWorker;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;


/**
 * The "New" wizard page allows setting the JEE runtime to be used, as well
 * as the project base name and a package name for application classes. 
 */

public class NewAzzyztedProjectWizardPage extends WizardPage {
	
	private Text projectBaseNameText;

	private Text packageText;
	
	private Combo runtimes;
	
	private NewAzzyztedProjectWorker worker;
	
	private SortedMap<String, IRuntime> runtimeMap = new TreeMap<String, IRuntime>();

	private ISelection selection;

	/**
	 * Constructor for NewAzzyztedProjectWizardPage.
	 * 
	 * @param pageName
	 */
	public NewAzzyztedProjectWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Structured JEE Project");
		setDescription("This wizard creates a new structured JEE project consisting of EAR, EJB, EJB client and WAR.");
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText("&Project base name:");

		projectBaseNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		projectBaseNameText.setLayoutData(gd);
		projectBaseNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("&Package name:");

		packageText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		packageText.setLayoutData(gd);
		packageText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("&Target runtime:");
		
		runtimes = new Combo(container, NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		runtimes.setLayoutData(gd);
		runtimes.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Initialize the dialog fields
	 */
	private void initialize() {
		worker = new NewAzzyztedProjectWorker();
		projectBaseNameText.setText("");
		packageText.setText("");
		for (IRuntime r : worker.getParameters().getTargetRuntimes()) {
			runtimeMap.put(r.getLocalizedName(), r);
		}
		for (String name : runtimeMap.keySet()) {
			runtimes.add(name);
		}
		// TODO remember selection between invocations
		runtimes.select(0);
	}

	/**
	 * Ensures that parameters are valid
	 */
	private void dialogChanged() {
		AzzyztedProjectParameters parameters = worker.getParameters();
		parameters.setProjectBaseName(projectBaseNameText.getText());
		parameters.setPackageName(packageText.getText());
		int selectionIndex = runtimes.getSelectionIndex();
		if (selectionIndex >= 0) {
			parameters.setSelectedRuntime(runtimeMap.get(runtimes.getItem(selectionIndex)));
		}
		String errorMsg = parameters.validate();
		updateStatus(errorMsg);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public NewAzzyztedProjectWorker getWorker() {
		return worker;
	}
}