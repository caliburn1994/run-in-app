package github.caliburn1994.open.app.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import github.caliburn1994.open.app.utils.URIUtils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class URIUtilsTest {

    private final String httpsUrl = "https://github.com/caliburn1994/caliburn1994.github.io.git";

    @Test
    void parseHttps() throws MalformedURLException, URISyntaxException {
        Assertions.assertThat(URIUtils.parseHttps(".idea", "master", httpsUrl)).isEqualTo("https://github.com/caliburn1994/caliburn1994.github.io/tree/master/.idea");
        Assertions.assertThat(URIUtils.parseHttps("", "master", httpsUrl)).isEqualTo("https://github.com/caliburn1994/caliburn1994.github.io/tree/master");
        Assertions.assertThat(URIUtils.parseHttps("Security/Authentication/password/Hashed password的必要性.md", "master", httpsUrl)).isEqualTo("https://github.com/caliburn1994/paste-image-to-git/tree/main/");

    }

    @Test
    void getHttpsFromSSH(){
        Assertions.assertThat(URIUtils.getHttpsFromSsh("git@github.com:caliburn1994/paste-image-to-git.git")).isEqualTo(httpsUrl);
        Assertions.assertThat(URIUtils.getHttpsFromSsh(httpsUrl)).isEqualTo(httpsUrl);
    }

}