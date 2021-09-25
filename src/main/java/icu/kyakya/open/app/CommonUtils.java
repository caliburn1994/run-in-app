package icu.kyakya.open.app;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.stream.Stream;

public class CommonUtils {
    public static Stream<VirtualFile> localFiles(AnActionEvent e) {
        VirtualFile[] files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        return files != null ? Stream.of(files).filter(f -> f.isValid() && f.isInLocalFileSystem()) : Stream.empty();
    }
}
