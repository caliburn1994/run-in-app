package icu.kyakya.open.app;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitLocalBranch;
import git4idea.GitRemoteBranch;
import git4idea.repo.GitBranchTrackInfo;
import git4idea.repo.GitRepository;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import static git4idea.GitUtil.getRepositoryManager;
import static icu.kyakya.open.app.CommonUtils.localFiles;

public class OpenInGithub extends AnAction implements DumbAware {



    @Override
    public void update(AnActionEvent e) {
        //noinspection ConstantConditions
        e.getPresentation().setEnabledAndVisible(
                e.getProject() != null
                && localFiles(e).findAny().isPresent()
                && e.getData(CommonDataKeys.VIRTUAL_FILE) != null
                && e.getProject() != null
                && getRepositoryManager(e.getProject()).getRepositoryForFileQuick(e.getData(CommonDataKeys.VIRTUAL_FILE)) != null);
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

//        DataContext dataContext = e.getDataContext();
        VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virtualFile == null) return;
        System.out.println(virtualFile.getPath());

        Project project = e.getProject();
        if (project == null) return;

        GitRepository repository = getRepositoryManager(project).getRepositoryForFileQuick(virtualFile);
        if (repository == null) return;


        // relativePath
        VirtualFile repositoryRoot = repository.getRoot();
        String relativePath = VfsUtilCore.getRelativePath(virtualFile, repositoryRoot) + "";
        System.out.println("relativePath:" + relativePath);


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

        System.out.println();

//        val hosts = remote.urls.map(URLUtil::parseHostFromSshUrl).distinct()
//
//        if (hosts.contains("github.com")) return "github"
//        if (hosts.contains("gitlab.com")) return "gitlab"
//        if (hosts.contains("bitbucket.org")) return "bitbucket"
//
//        if (remote.urls.any { it.contains("github") }) return "github_custom"
//        if (remote.urls.any { it.contains("gitlab") }) return "gitlab_custom"
//        if (remote.urls.any { it.contains("bitbucket") }) return "bitbucket_custom"
//
//        return null


//
//        StringBuilder builder = new StringBuilder();
//        if (StringUtil.isEmptyOrSpaces(relativePath)) {
//            builder.append(.toUrl()).append("/tree/").append(branch)
//        }
//        else {
//            builder.append(path.toUrl()).append("/blob/").append(branch).append('/').append(URIUtil.encodePath(relativePath));
//        }
//        builder.


//        Properties prop = new Properties();
//        try {
//            //load a properties file from class path, inside static method
//            prop.load(new FileInputStream(".git/HEAD"));
//
//            //get the property value and print it out
//            System.out.println(prop.getProperty("ref"));
//
//        }
//        catch (IOException ex) {
//            ex.printStackTrace();
//        }

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
