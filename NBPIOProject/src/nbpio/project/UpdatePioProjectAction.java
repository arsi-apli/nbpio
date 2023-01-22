package nbpio.project;

import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import org.netbeans.api.project.Project;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.DynamicMenuContent;
import org.openide.filesystems.FileObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

@ActionID(
    category = "Project",
        id = "nbpio.project.UpdatePioProjectAction"
)
@ActionRegistration(lazy = true,
        displayName = "#UpdatePioProjectAction.name"
)
@ActionReference(path="Projects/Actions")

@NbBundle.Messages("UpdatePioProjectAction.name=PIO Update NB Project")
public final class UpdatePioProjectAction extends AbstractAction implements ContextAwareAction {

    public @Override void actionPerformed(ActionEvent e) {assert false;}
    
    public @Override Action createContextAwareInstance(Lookup context) {
        return new ContextAction(context);
    }
    
    private static final class ContextAction extends AbstractAction {
        
        private final Project p;
        
        public ContextAction(Lookup context) {
            p = context.lookup(Project.class);
            FileObject iniFile = p.getProjectDirectory().getFileObject("platformio.ini");
            setEnabled( iniFile != null );
            putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
            putValue(NAME, NbBundle.getMessage(UpdatePioProjectAction.class, "OpenLibraryManagerAction.name"));
        }
        
        public @Override
        void actionPerformed(ActionEvent e) {
            try {
                Process process = PlatformIO.startProjectUpdateProcess(p.getProjectDirectory().getPath());
                process.waitFor();
            } catch (IOException ex) {
                NotifyDescriptor nd = new NotifyDescriptor.Message("Unable to reinit NB Project", NotifyDescriptor.ERROR_MESSAGE);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}