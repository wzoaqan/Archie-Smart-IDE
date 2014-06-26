
package archie.editor.commands;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;

import archie.editor.TimEditor;
import archie.model.Tim;
import archie.model.shapes.CodeElement;

public class MarkCodeElementAction extends SelectionAction
{
	public static final String MARK = "Mark Code Element";

	private Request request;
	private Tim tim;

	public MarkCodeElementAction(IWorkbenchPart part, Tim inTim)
	{
		super(part);
		this.tim = inTim;
		request = new Request(MARK);
		setText(MARK);
		setId(MARK);
		setToolTipText("Marks that there has been a change in code associated with given architectural pattern");
		setImageDescriptor(ImageDescriptor.createFromFile(TimEditor.class, "icons/mark16.png"));
	}

	@Override
	protected boolean calculateEnabled()
	{
		if (getSelectedObjects().isEmpty())
			return false;
		List<?> parts = getSelectedObjects();
		for (Object o : parts)
		{
			if (!(o instanceof EditPart))
				return false;
			Object model = ((EditPart) o).getModel();
			if (!(model instanceof CodeElement) || ((CodeElement) model).isMarked())
				return false;
		}
		return true;
	}

	@Override
	public void run()
	{
		List<?> editparts = getSelectedObjects();
		CompoundCommand cc = new CompoundCommand();
		cc.setDebugLabel("Mark elements");
		for (Object o : editparts)
		{
			Command cmd = ((EditPart) o).getCommand(request);
			((MarkCodeElementCommand)cmd).setTim(tim);
			cc.add(cmd);
		}
		execute(cc);
	}
}
