package aero.tav.tams.exporter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

/**
 * @author mehmetkorkut
 * created 10.08.2022 10:17
 * package - aero.tav.tams.exporter.properties
 * project - tams-exporter
 */
@Configuration
@ConfigurationProperties(prefix = "open-shift")
public class OpenShiftProperties {

    private String url;
    private String token;
    private String nameSpace;
    private boolean usingCertFile;
    private String certFile;
    private String tokenFile;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = new String(Base64.getDecoder().decode(token));
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public boolean isUsingCertFile() {
        return usingCertFile;
    }

    public void setUsingCertFile(boolean usingCertFile) {
        this.usingCertFile = usingCertFile;
    }

    public String getCertFile() {
        return certFile;
    }

    public void setCertFile(String certFile) {
        this.certFile = certFile;
    }

    public String getTokenFile() {
        return tokenFile;
    }

    public void setTokenFile(String tokenFile) {
        this.tokenFile = tokenFile;
    }

    @Override
    public String toString() {
        return "OpenShiftProperties{" +
               "url='" + url + '\'' +
               ", token='" + token + '\'' +
               ", nameSpace='" + nameSpace + '\'' +
               ", usingCertFile=" + usingCertFile +
               ", certFile='" + certFile + '\'' +
               ", keyFile='" + tokenFile + '\'' +
               '}';
    }
}
