package com.kilopo.vape.configuration.external;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("login")
public class LoginConfig {
    private String authorizeUrl;
    private String clientId;

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setAuthorizeUrl(String authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
