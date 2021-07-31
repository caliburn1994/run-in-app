
package icu.kyakya.open.apps;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DumbAware = not need to wait for indexing
 *
 * @see com.intellij.ide.actions.SynchronizeCurrentFileAction
 */
public class OpenInExternalAppAction extends AnAction implements DumbAware {


    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null && localFiles(e).findAny().isPresent());
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
        Project project = getEventProject(e);
        List<VirtualFile> files = localFiles(e).collect(Collectors.toList());
        if (project == null || files.isEmpty()) return;

        for (VirtualFile file : files) {
            try {
                Desktop.getDesktop().open(new File(file.getPath()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static Stream<VirtualFile> localFiles(AnActionEvent e) {
        VirtualFile[] files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        return files != null ? Stream.of(files).filter(f -> f.isValid() && f.isInLocalFileSystem()) : Stream.empty();
    }


}
