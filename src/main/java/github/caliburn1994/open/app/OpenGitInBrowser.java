package github.caliburn1994.open.app;

import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitRemoteBranch;
import git4idea.repo.GitRepository;
import github.caliburn1994.open.app.icon.OpenAppIcon;
import github.caliburn1994.open.app.intellij.CommonUtils;
import github.caliburn1994.open.app.utils.URIUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

import static git4idea.GitUtil.getRepositoryManager;

/**
 * template:
 * 1. Github
 * [ssh://]git@github.com:user/project.git
 * https://github.com/user/project.git
 */
@Slf4j
public class OpenGitInBrowser extends AnAction implements DumbAware {

    private @NotNull VirtualFile virtualFile;
    private @NotNull GitRepository repository;

    private @NotNull GitRemoteBranch remoteBranch;

    private @NotNull String remoteUri;


    @Override
    public void update(@NotNull AnActionEvent e) {
        var enable = init(e);
        if (!enable) return;

        // enable and visible
        e.getPresentation().setEnabledAndVisible(enable);

        // icon
        if (remoteUri.contains("github.com")) {
            e.getPresentation().setIcon(AllIcons.Vcs.Vendors.Github);
        } else {
            e.getPresentation().setIcon(OpenAppIcon.git);
        }
    }

    private boolean init(AnActionEvent e) {
        try {
            virtualFile = Objects.requireNonNull(e.getData(CommonDataKeys.VIRTUAL_FILE));
            var project = Objects.requireNonNull(e.getProject());
            repository = Objects.requireNonNull(getRepositoryManager(project).getRepositoryForFileQuick(virtualFile));
            var currentBranch = Objects.requireNonNull(repository.getCurrentBranch());
            var branchTrackInfo = Objects.requireNonNull(repository.getBranchTrackInfo(currentBranch.getName()));
            remoteBranch = branchTrackInfo.getRemoteBranch();
            remoteUri = new ArrayList<>(repository.getRemotes()).get(0).getUrls().get(0);

            //noinspection RedundantIfStatement
            if (CommonUtils.localFiles(e).size() == 0) return false;

        } catch (NullPointerException e1) {
            return false;
        }
        return true;
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // relativePath
        var repositoryRoot = repository.getRoot();
        var relativePath = VfsUtilCore.getRelativePath(virtualFile, repositoryRoot) + "";

        // branch
        var branch = remoteBranch.getNameForRemoteOperations() + "";

        // git uri
        try {
            remoteUri = URIUtils.getHttpsFromSsh(remoteUri);
            remoteUri = URIUtils.parseHttps(relativePath, branch, remoteUri);
        } catch (URISyntaxException | MalformedURLException ex) {
            throw new RuntimeException(ex);
        }

        // open uri
        BrowserUtil.browse(remoteUri);

    }


}
