package github.caliburn1994.open.app.icon;

import com.intellij.ui.IconManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class OpenAppIcon {

    private static @NotNull Icon load(@NotNull String path) {
        return IconManager.getInstance().getIcon(path, OpenAppIcon.class.getClassLoader());
    }

    /** 16x16 */ public static final @NotNull Icon git = load("/icons/git.svg");
}
