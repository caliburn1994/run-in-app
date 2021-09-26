package icu.kyakya.open.app.icon;

import com.intellij.ui.IconManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class OpenAppIcon {

    private static @NotNull Icon load(@NotNull String path, long cacheKey, int flags) {
        return IconManager.getInstance().loadRasterizedIcon(path, OpenAppIcon.class.getClassLoader(), cacheKey, flags);
    }

    /** 16x16 */ public static final @NotNull Icon git = load("icons/git.svg", -6566091381586036573L, 2);
}
