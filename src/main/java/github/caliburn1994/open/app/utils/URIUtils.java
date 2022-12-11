package github.caliburn1994.open.app.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public class URIUtils {


    /**
     * @param relativePath src
     * @param branch main
     * @param url https://github.com/caliburn1994/paste-image-to-git.git
     * @return
     * @throws URISyntaxException
     */
    public static String parseHttps(String relativePath,@NotNull String branch, String url) throws URISyntaxException, MalformedURLException {
        if (url.endsWith(".git")) {
            url = url.substring(0, url.length() - 4);
        }


        relativePath = Arrays.stream(relativePath.split("/"))
                .filter(StringUtils::isNoneEmpty)
                .map(str -> new URIBuilder().setPath(str).toString())
                .collect(Collectors.joining("/"));
        url = url + "/tree/" + branch + relativePath;
        return url;
    }



    @NotNull
    public static String getHttpsFromSsh(@NotNull String uri) {
        if (uri.startsWith("https")) {
            return uri;
        }

        // [ssh://]git@github.com:user/project.git
        var httpUrl = uri;

        // remove [ssh://]git
        int at = httpUrl.lastIndexOf('@');
        if (at > 0) {
            httpUrl = httpUrl.substring(at + 1);
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
