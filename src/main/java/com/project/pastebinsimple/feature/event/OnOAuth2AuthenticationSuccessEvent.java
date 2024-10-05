package com.project.pastebinsimple.feature.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OnOAuth2AuthenticationSuccessEvent extends ApplicationEvent {

    private final OAuth2User oauth2User;

    public OnOAuth2AuthenticationSuccessEvent(OAuth2User oauth2User) {
        super(oauth2User);
        this.oauth2User = oauth2User;
    }

    public OAuth2User getOAuth2User() {
        return this.oauth2User;
    }

}
