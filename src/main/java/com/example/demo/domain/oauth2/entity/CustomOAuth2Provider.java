package com.example.demo.domain.oauth2.entity;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public enum CustomOAuth2Provider {

    KAKAO {
        @Override
        public ClientRegistration.Builder getBuilder(String registrationId, String redirectUri, String authUri,
                                                     String tokenUri, String userInfoUri, String clientName, String usreNameAttribute) {
            ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.POST,
                    redirectUri, authUri, tokenUri, userInfoUri, clientName, usreNameAttribute);
            builder.authorizationUri(authUri);
            builder.tokenUri(tokenUri);
            builder.userInfoUri(userInfoUri);
            builder.userNameAttributeName(usreNameAttribute);
            builder.clientName(clientName);
            return builder;
        }
    },
    NAVER {
        @Override
        public ClientRegistration.Builder getBuilder(String registrationId, String redirectUri, String authUri,
                                                     String tokenUri, String userInfoUri, String clientName, String usreNameAttribute) {
            ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.POST,
                    redirectUri, authUri, tokenUri, userInfoUri, clientName, usreNameAttribute);

            builder.authorizationUri(authUri);
            builder.tokenUri(tokenUri);
            builder.userInfoUri(userInfoUri);
            builder.userNameAttributeName("id");
            builder.clientName(clientName);
            builder.userNameAttributeName(usreNameAttribute);
            return builder;
        }
    };


    protected final ClientRegistration.Builder getBuilder(
            String registrationId, ClientAuthenticationMethod method, String redirectUri, String authUri,
            String tokenUri, String userInfoUri, String clientName, String usreNameAttribute) {
        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
        builder.clientAuthenticationMethod(method);
        builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
        builder.redirectUriTemplate(redirectUri);
        builder.authorizationUri(authUri);
        builder.tokenUri(tokenUri);
        builder.userInfoUri(userInfoUri);
        builder.clientName(clientName);
        builder.userNameAttributeName(usreNameAttribute);
        return builder;
    }

    public abstract ClientRegistration.Builder getBuilder(String registrationId, String redirectUri, String authUri,
                                                          String tokenUri, String userInfoUri, String clientName, String usreNameAttribute);
}