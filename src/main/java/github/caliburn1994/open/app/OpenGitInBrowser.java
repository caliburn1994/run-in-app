package github.caliburn1994.open.app;

import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitLocalBranch;
import git4idea.GitRemoteBranch;
import git4idea.repo.GitBranchTrackInfo;
import git4idea.repo.GitRepository;
import github.caliburn1994.open.app.icon.OpenAppIcon;
import github.caliburn1994.open.app.intellij.CommonUtils;
import github.caliburn1994.open.app.utils.URIUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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


    @Override
    public void update(@NotNull AnActionEvent e) {
        // enable and visible
        e.getPresentation().setEnabledAndVisible(true);


        // check basic data
        VirtualFile virtualFile;
        GitRepository repository;
        try {
            virtualFile = Objects.requireNonNull(e.getData(CommonDataKeys.VIRTUAL_FILE));
            var project = Objects.requireNonNull(e.getProject());
            repository = Objects.requireNonNull(getRepositoryManager(project).getRepositoryForFileQuick(virtualFile));
        } catch (NullPointerException e1) {
            e.getPresentation().setEnabledAndVisible(false);
            return;
        }

        // icon
        String remoteUri = new ArrayList<>(repository.getRemotes()).get(0).getUrls().get(0);
        if (remoteUri.contains("github.com")) {
            e.getPresentation().setIcon(AllIcons.Vcs.Vendors.Github);
        } else {
            e.getPresentation().setIcon(OpenAppIcon.git);
        }
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // check basic data
        VirtualFile virtualFile;
        GitRepository repository;
        GitRemoteBranch remoteBranch;
        GitLocalBranch currentBranch;
        String branchName = "";
        String remoteUri = "";
        try {
            virtualFile = Objects.requireNonNull(e.getData(CommonDataKeys.VIRTUAL_FILE));
            var project = Objects.requireNonNull(e.getProject());
            repository = Objects.requireNonNull(getRepositoryManager(project).getRepositoryForFileQuick(virtualFile));
            currentBranch = Objects.requireNonNull(repository.getCurrentBranch());
        } catch (NullPointerException e1) {
            return;
        }
        if (CommonUtils.localFiles(e).isEmpty()) return;

        // build remote uri
        remoteUri = new ArrayList<>(repository.getRemotes()).get(0).getUrls().get(0);
        var branchTrackInfo = repository.getBranchTrackInfo(currentBranch.getName());
        if (branchTrackInfo != null) {
            remoteBranch = branchTrackInfo.getRemoteBranch();
            branchName = remoteBranch.getNameForRemoteOperations();
        } else {
            branchName = repository.getCurrentBranch().getName();
        }

        //  build relativePath
        var repositoryRoot = repository.getRoot();
        var relativePath = VfsUtilCore.getRelativePath(virtualFile, repositoryRoot);

        // git uri
        try {
            remoteUri = URIUtils.getHttpsFromSsh(remoteUri);
            remoteUri = URIUtils.parseHttps(relativePath, branchName, remoteUri);
        } catch (URISyntaxException | MalformedURLException ex) {
            throw new RuntimeException(ex);
        }

        // open uri
        BrowserUtil.browse(remoteUri);

    }


}
