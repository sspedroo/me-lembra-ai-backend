package org.remind.melembraai.infra.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.remind.melembraai.infra.security.filters.JwtAuthFilter;
import org.remind.melembraai.infra.security.handlers.SecurityAccessDeniedHandler;
import org.remind.melembraai.infra.security.handlers.SecurityAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import java.util.*;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    public static final String BEARER_PREFIX = "Bearer ";

    private final SecurityAuthenticationEntryPoint securityAuthenticationEntryPoint;
    private final SecurityAccessDeniedHandler securityAccessDeniedHandler;
    private final JwtAuthFilter jwtAuthFilter;

    public static final List<String> WHITELIST = Arrays.asList(
            "/h2-console/**",
            "/actuator/*",
            "/configuration/security",
            "/configuration/ui",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-resources/configuration/security",
            "/swagger-resources/configuration/ui",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/v3/api-docs/**",
            "/webjars/**",
            "/webjars/swagger-ui/**",
            "/error"
    );

    public static boolean isWhiteList(HttpServletRequest request) {
        return is(request, null, SecurityConfig.WHITELIST.toArray(String[]::new));
    }

    public static boolean isPublic(HttpServletRequest request) {
        return is(request, "/public/**");
    }

    public static boolean isAuth(HttpServletRequest request) {
        return is(request, "/auth/**");
    }

    public static boolean is(HttpServletRequest request, String path, String... otherPaths) {
        final String servletPath = request.getServletPath();
        final AntPathMatcher antPathMatcher = new AntPathMatcher();

        final List<String> urls = Objects.isNull(path) ? new ArrayList<>() : new ArrayList<>(Collections.singletonList(path));
        Collections.addAll(urls, otherPaths);

        return urls.stream().anyMatch(path1 -> antPathMatcher.match(path1, servletPath));
    }

    @Bean
    public SecurityFilterChain tokenJwtFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(WHITELIST.toArray(String[]::new)).permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/users/register").permitAll()
                        .requestMatchers("/users/{id}/activate").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterAfter(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(securityAuthenticationEntryPoint)
                        .accessDeniedHandler(securityAccessDeniedHandler));
                return http.build();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
