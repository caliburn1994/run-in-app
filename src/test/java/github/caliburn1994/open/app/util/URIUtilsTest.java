package github.caliburn1994.open.app.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import github.caliburn1994.open.app.utils.URIUtils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class URIUtilsTest {

    private final String httpsUrl = "https://github.com/caliburn1994/paste-image-to-git.git";

    @Test
    void parseHttps() throws MalformedURLException, URISyntaxException {
        Assertions.assertThat(URIUtils.parseHttps("src", "main", httpsUrl)).isEqualTo("https://github.com/caliburn1994/paste-image-to-git/tree/main/src/");
        Assertions.assertThat(URIUtils.parseHttps("", "main", httpsUrl)).isEqualTo("https://github.com/caliburn1994/paste-image-to-git/tree/main/");
        Assertions.assertThat(URIUtils.parseHttps("", "main", httpsUrl)).isEqualTo("https://github.com/caliburn1994/paste-image-to-git/tree/main/");
    }

    @Test
    void getHttpsFromSSH(){
        Assertions.assertThat(URIUtils.getHttpsFromSsh("git@github.com:caliburn1994/paste-image-to-git.git")).isEqualTo(httpsUrl);
        Assertions.assertThat(URIUtils.getHttpsFromSsh(httpsUrl)).isEqualTo(httpsUrl);
    }

}