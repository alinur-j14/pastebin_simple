package com.project.pastebinsimple.feature.listener;

import com.project.pastebinsimple.feature.event.OnOAuth2AuthenticationSuccessEvent;
import com.project.pastebinsimple.feature.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessEventListener {

    private final UserService userService;

    @EventListener
    public void handleOAuth2AuthenticationSuccess(OnOAuth2AuthenticationSuccessEvent event) {
        OAuth2User user = event.getOAuth2User();
        userService.saveUser(user);
    }

}
