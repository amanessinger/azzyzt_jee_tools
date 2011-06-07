/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or � as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * 
 * For convenience a plain text copy of the English version 
 * of the Licence can be found in the file LICENCE.txt in
 * the top-level directory of this software distribution.
 * 
 * You may obtain a copy of the Licence in any of 22 European
 * Languages at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package org.azzyzt.jee.tools.mwe.projectgen.wizards;

import java.util.SortedMap;
import java.util.TreeMap;

import org.azzyzt.jee.tools.mwe.projectgen.workers.NewAzzyztedProjectWorker;
import org.azzyzt.jee.tools.project.Context;
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

	@SuppressWarnings("unused")
	private ISelection selection;

	/**
	 * Constructor for NewAzzyztedProjectWizardPage.
	 * 
	 * @param pageName
	 */
	public NewAzzyztedProjectWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Azzyzted JEE FacetedProject");
		setDescription("This wizard creates a new azzyzted JEE project consisting of EAR, EJB, EJB client and WAR.");
		this.selection = selection; // FindBugs unused warning OK
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
		label.setText("&FacetedProject base name:");

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
		for (IRuntime r : worker.getContext().getTargetRuntimes()) {
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
		Context context = worker.getContext();
		context.setProjectBaseName(projectBaseNameText.getText());
		context.setPackageName(packageText.getText());
		int selectionIndex = runtimes.getSelectionIndex();
		if (selectionIndex >= 0) {
			context.setSelectedRuntime(runtimeMap.get(runtimes.getItem(selectionIndex)));
		}
		String errorMsg = context.validate();
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