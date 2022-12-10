package github.caliburn1994.open.app.intellij;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;
import java.util.stream.Stream;

public class CommonUtils {
    public static List<VirtualFile> localFiles(AnActionEvent e) {
        var files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        if (files == null) {
            return List.of();
        }

        return  Stream.of(files).filter(f -> f.isValid() && f.isInLocalFileSystem()).toList();
    }
}
