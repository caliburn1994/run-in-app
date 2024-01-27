package github.caliburn1994.open.app;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.vfs.VirtualFile;
import github.caliburn1994.open.app.icon.OpenAppIcon;
import github.caliburn1994.open.app.intellij.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

//import static git4idea.GitUtil.getRepositoryManager;

/**
 * template:
 * 1. Github
 * [ssh://]git@github.com:user/project.git
 * https://github.com/user/project.git
 */
@Slf4j
public class OpenGitInBrowser extends AnAction implements DumbAware {

    private @NotNull VirtualFile virtualFile;
//    private @NotNull GitRepository repository;

//    private @NotNull GitRemoteBranch remoteBranch;

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


            var repositoryBuilder = new FileRepositoryBuilder();
            var repository = repositoryBuilder
                    .setGitDir(new File(virtualFile.getPath()))
                    .readEnvironment() // 扫描环境变量
                    .findGitDir()      // 扫描上层目录
                    .build();


//            repository = Objects.requireNonNull(getRepositoryManager(project).getRepositoryForFileQuick(virtualFile));
            var currentBranch = Objects.requireNonNull(repository.getBranch());
//            var branchTrackInfo = Objects.requireNonNull(repository.getBranchTrackInfo(currentBranch.getName()));
//            var branchTrackInfo = Objects.requireNonNull(repository.getBranchTrackInfo(currentBranch.getName()));
//            remoteBranch = branchTrackInfo.getRemoteBranch();
//            remoteUri = new ArrayList<>(repository.getRemotes()).get(0).getUrls().get(0);

            //noinspection RedundantIfStatement
            if (CommonUtils.localFiles(e).size() == 0) return false;

        } catch (NullPointerException e1) {
            return false;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
//        // relativePath
//        var repositoryRoot = repository.getRoot();
//        var relativePath = VfsUtilCore.getRelativePath(virtualFile, repositoryRoot) + "";
//
//        // branch
//        var branch = remoteBranch.getNameForRemoteOperations() + "";
//
//        // git uri
//        try {
//            remoteUri = URIUtils.getHttpsFromSsh(remoteUri);
//            remoteUri = URIUtils.parseHttps(relativePath, branch, remoteUri);
//        } catch (URISyntaxException | MalformedURLException ex) {
//            throw new RuntimeException(ex);
//        }
//
//        // open uri
//        BrowserUtil.browse(remoteUri);

    }


}
