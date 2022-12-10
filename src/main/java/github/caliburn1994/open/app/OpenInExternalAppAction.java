
package github.caliburn1994.open.app;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.vfs.VirtualFile;
import github.caliburn1994.open.app.intellij.CommonUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * DumbAware = not need to wait for indexing
 *
 * @see com.intellij.ide.actions.SynchronizeCurrentFileAction
 */
public class OpenInExternalAppAction extends AnAction implements DumbAware {


    @Override
    public void update(AnActionEvent e) {
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);

        // if the selected is dir, not be visible
        if (file != null && file.isDirectory()) {
            e.getPresentation().setVisible(false);
        }

        e.getPresentation().setEnabledAndVisible(CommonUtils.localFiles(e).size() != 0);
    }

    /**
     * This default constructor is used by the IntelliJ Platform framework to instantiate this class based on plugin.xml
     * declarations. Only needed in {@link OpenInExternalAppAction} class because a second constructor is overridden.
     *
     * @see AnAction#AnAction()
     */
    public OpenInExternalAppAction() {
        super();
    }

    /**
     * This constructor is used to support dynamically added menu actions.
     * It sets the text, description to be displayed for the menu item.
     * Otherwise, the default AnAction constructor is used by the IntelliJ Platform.
     *
     * @param text        The text to be displayed as a menu item.
     * @param description The description of the menu item.
     * @param icon        The icon to be used with the menu item.
     */
    public OpenInExternalAppAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        var project = getEventProject(e);
        var files = CommonUtils.localFiles(e);
        if (project == null || files.isEmpty()) return;

        for (VirtualFile file : files) {
            try {
                var desktop = Desktop.getDesktop();
                desktop.open(new File(file.getPath()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }




}
