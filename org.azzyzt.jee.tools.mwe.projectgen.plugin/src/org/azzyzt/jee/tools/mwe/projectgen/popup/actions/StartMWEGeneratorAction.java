package org.azzyzt.jee.tools.mwe.projectgen.popup.actions;

import java.lang.reflect.InvocationTargetException;

import org.azzyzt.jee.tools.mwe.projectgen.ProjectGen;
import org.azzyzt.jee.tools.mwe.projectgen.workers.MWEGeneratorWorker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class StartMWEGeneratorAction implements IObjectActionDelegate {

	private Shell shell;
	private ISelection selection;
	
	/**
	 * Constructor for StartMWEGeneratorAction.
	 */
	public StartMWEGeneratorAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		if (! (selection instanceof TreeSelection)) {
			MessageDialog.openError(shell, "Can't start generator on selected object", 
					"Please fix objectContribution/objectClass in plugin.xml");
			return;
		}
		TreeSelection ts = (TreeSelection)selection;
		Object element = ts.getFirstElement();
		IProject prj = (IProject)element;
		try {
			if (prj.hasNature(ProjectGen.AZZYZTED_NATURE_ID)) {
				performGenerate(prj);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public boolean performGenerate(final IProject prj) {
		final MWEGeneratorWorker worker = new MWEGeneratorWorker();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					worker.setMonitor(monitor);
					worker.callMWEGenerator(prj);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} catch (InterruptedException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			new ProgressMonitorDialog(shell).run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			e.printStackTrace();
			MessageDialog.openError(shell, "Error", realException.getMessage());
			return false;
		}
		return true;
	}
	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

}
