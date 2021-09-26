package icu.kyakya.open.app;

import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitLocalBranch;
import git4idea.repo.GitBranchTrackInfo;
import git4idea.repo.GitRepository;
import icu.kyakya.open.app.icon.OpenAppIcon;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static git4idea.GitUtil.getRepositoryManager;
import static icu.kyakya.open.app.CommonUtils.localFiles;

/**
 * template:
 * 1. Github
 *  [ssh://]git@github.com:user/project.git
 *  https://github.com/user/project.git
 */
public class OpenGitInBrowser extends AnAction implements DumbAware {

    private VirtualFile virtualFile;
    private Project project;
    private GitRepository repository;

    @Override
    public void update(@NotNull AnActionEvent e) {

        boolean isEnable = isEnable(e);

        e.getPresentation().setEnabledAndVisible(
                e.getProject() != null
                && localFiles(e).findAny().isPresent()
                && isEnable);

        // icon
        String url = new ArrayList<>(repository.getRemotes()).get(0).getUrls().get(0);
        if (url.contains("github.com")) {
            e.getPresentation().setIcon(AllIcons.Vcs.Vendors.Github);
        } else {
            e.getPresentation().setIcon(OpenAppIcon.git);
        }

    }

    private boolean isEnable(AnActionEvent e) {
        virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virtualFile == null) return false;

        project = e.getProject();
        if (project == null) return false;

        repository = getRepositoryManager(project).getRepositoryForFileQuick(virtualFile);
        //noinspection RedundantIfStatement
        if (repository == null) return false;

        return true;
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virtualFile == null) return;

        Project project = e.getProject();
        if (project == null) return;

        GitRepository repository = getRepositoryManager(project).getRepositoryForFileQuick(virtualFile);
        if (repository == null) return;

        // relativePath
        VirtualFile repositoryRoot = repository.getRoot();
        String relativePath = VfsUtilCore.getRelativePath(virtualFile, repositoryRoot) + "";

        // branch
        GitLocalBranch currentBranch = repository.getCurrentBranch();
        if (currentBranch == null) {return;}
        GitBranchTrackInfo branchTrackInfo = repository.getBranchTrackInfo(currentBranch.getName());
        if (branchTrackInfo ==null) return;
        String branch = branchTrackInfo.getRemoteBranch().getNameForRemoteOperations();

        // git url
        String url = new ArrayList<>(repository.getRemotes()).get(0).getUrls().get(0);//firstOrNull
        if (!url.startsWith("https")) {
            url = parseHttpsFromSshUrl(url);
        }
        if (url.endsWith(".git")) {
            url = url.substring(0, url.length() - 4);
        }

        StringBuilder builder = new StringBuilder();
        try {
            url = builder.append(url).append("/tree/").append(branch).append('/').append(URIUtil.encodePath(relativePath)).toString();
        } catch (URIException ex) {
            ex.printStackTrace();
        }

        // open url
        BrowserUtil.browse(url);

    }

    public static @NotNull String parseHttpsFromSshUrl(@NotNull String sshUrl) {
        // [ssh://]git@github.com:user/project.git
        String httpUrl = sshUrl;

        // remove [ssh://]git
        int at = httpUrl.lastIndexOf('@') ;
        if (at > 0) {
            httpUrl = httpUrl.substring(at+ 1);
        } else {
            int firstColon = httpUrl.indexOf(':');
            if (firstColon > 0) {
                httpUrl = httpUrl.substring(firstColon + 4);
            }
        }

        httpUrl = httpUrl.replaceFirst(":", "/");
        httpUrl = "https://" + httpUrl;
        return httpUrl;
    }


}
