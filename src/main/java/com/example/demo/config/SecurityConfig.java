package com.example.demo.config;


import com.example.demo.domain.oauth2.entity.CustomOAuth2Provider;
import com.example.demo.domain.oauth2.service.CustomOAuth2UserService;
import com.example.demo.jwt.JwtAccessDeniedHandler;
import com.example.demo.jwt.JwtAuthenticationEntryPoint;
import com.example.demo.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.demo.domain.oauth2.entity.SocialType.*;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Spring security룰을 무시하게 하는 url규칙
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico")
                .antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**","/home",
                        "/sns","/loginSuccess", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        // CSRF 설정 Disable
        http.csrf().disable()
                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/user/**").access("hasRole('ROLE_USER')")
                .antMatchers("/api/v1/user").access("hasRole('ROLE_USER')")
                .antMatchers("/api/v1/board/**").access("hasRole('ROLE_USER')")
                .antMatchers("/api/v1/board").access("hasRole('ROLE_USER')")
                .antMatchers("/api/v1/reply").access("hasRole('ROLE_USER')")
                .antMatchers("/facebook").hasAuthority(FACEBOOK.getRoleType())
                .antMatchers("/google").hasAuthority(GOOGLE.getRoleType())
                .antMatchers("/kakao").hasAuthority(KAKAO.getRoleType())
                .antMatchers("/naver").hasAuthority(NAVER.getRoleType())
                .antMatchers("/api/v1/**", "/oauth2/**", "/login/**")
                .permitAll()
                .anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요
                .and()
                //OAuth2.0
                .oauth2Login()
                .userInfoEndpoint().userService(new CustomOAuth2UserService())  // 네이버 USER INFO의 응답을 처리하기 위한 설정
                .and()
                .defaultSuccessUrl("/loginSuccess")
                .failureUrl("/loginFailure")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/sns"))
                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider))
                .and()
                .cors();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(
            OAuth2ClientProperties oAuth2ClientProperties,
            @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String kakaoClientId,
            @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String kakaoClientSecret,
            @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String kakaoRedirectUri,
            @Value("${spring.security.oauth2.client.registration.kakao.client-name}") String kakaoClientName,
            @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}") String kakaoAuthUri,
            @Value("${spring.security.oauth2.client.provider.kakao.token-uri}") String kakaoTokenUri,
            @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}") String kakaoUserInfoUri,
            @Value("${spring.security.oauth2.client.provider.kakao.user-name-attribute}") String kakaoUserNameAttribute,
            @Value("${spring.security.oauth2.client.registration.naver.client-id}") String naverClientId,
            @Value("${spring.security.oauth2.client.registration.naver.client-secret}") String naverClientSecret,
            @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}") String naverRedirectUri,
            @Value("${spring.security.oauth2.client.registration.naver.client-name}") String naverClientName,
            @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}") String naverAuthUri,
            @Value("${spring.security.oauth2.client.provider.naver.token-uri}") String naverTokenUri,
            @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}") String naverUserInfoUri,
            @Value("${spring.security.oauth2.client.provider.naver.user-name-attribute}") String naverUserNameAttribute) {
        List<ClientRegistration> registrations = oAuth2ClientProperties
                .getRegistration().keySet().stream()
                .map(client -> getRegistration(oAuth2ClientProperties, client))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        registrations.add(CustomOAuth2Provider.KAKAO.getBuilder("kakao", kakaoRedirectUri,
                kakaoAuthUri, kakaoTokenUri, kakaoUserInfoUri, kakaoClientName, kakaoUserNameAttribute)
                .clientId(kakaoClientId)
                .clientSecret(kakaoClientSecret)
                .jwkSetUri("temp")
                .scope("profile_nickname", "profile_image", "account_email")
                .build());

        registrations.add(CustomOAuth2Provider.NAVER.getBuilder("naver", naverRedirectUri,
                naverAuthUri, naverTokenUri, naverUserInfoUri, naverClientName, naverUserNameAttribute)
                .clientId(naverClientId)
                .clientSecret(naverClientSecret)
                .jwkSetUri("temp")
                .build());
        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(OAuth2ClientProperties clientProperties, String client) {
        if ("google".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("google");
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .scope("email", "profile")
                    .build();
        }

        if ("facebook".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("facebook");
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,link")
                    .scope("email")
                    .build();
        }

        return null;
    }


}
