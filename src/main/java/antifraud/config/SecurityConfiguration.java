package antifraud.config;

import antifraud.mapper.AntiFraudMapper;
import antifraud.model.consts.UserRole;
import antifraud.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req -> req
                .requestMatchers(POST, "/api/auth/user").permitAll()
                .requestMatchers(antMatcher(DELETE, "/api/auth/user/**")).hasAuthority(UserRole.ADMINISTRATOR.name())
                .requestMatchers(GET, "/api/auth/list").hasAnyAuthority(UserRole.ADMINISTRATOR.name(), UserRole.SUPPORT.name())
                .requestMatchers(antMatcher(POST,"/api/antifraud/transaction/**")).hasAuthority(UserRole.MERCHANT.name())
                .requestMatchers(antMatcher(PUT, "/api/auth/access/**")).hasAuthority(UserRole.ADMINISTRATOR.name())
                .requestMatchers(antMatcher(PUT, "/api/auth/role/**")).hasAuthority(UserRole.ADMINISTRATOR.name())
                .requestMatchers(antMatcher(POST, "/api/antifraud/suspicious-ip")).hasAuthority(UserRole.SUPPORT.name())
                .requestMatchers(antMatcher(DELETE, "/api/antifraud/suspicious-ip/**")).hasAuthority(UserRole.SUPPORT.name())
                .requestMatchers(antMatcher(GET, "/api/antifraud/suspicious-ip")).hasAuthority(UserRole.SUPPORT.name())
                .requestMatchers(antMatcher(POST, "/api/antifraud/stolencard")).hasAuthority(UserRole.SUPPORT.name())
                .requestMatchers(antMatcher(DELETE, "/api/antifraud/stolencard")).hasAuthority(UserRole.SUPPORT.name())
                .requestMatchers(antMatcher(GET, "/api/antifraud/stolencard")).hasAuthority(UserRole.SUPPORT.name())
                .requestMatchers(antMatcher(GET, "/api/antifraud/history/**")).hasAuthority(UserRole.SUPPORT.name())
                .requestMatchers(antMatcher(PUT, "/api/antifraud/transaction")).hasAuthority(UserRole.SUPPORT.name())
                .requestMatchers("/actuator/shutdown").permitAll()
                .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(antMatcher("/**/api-docs/**")).permitAll()
                .anyRequest().authenticated()
        );
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.httpBasic().authenticationEntryPoint(authenticationEntryPoint());
        http.csrf().disable();
        http.headers().frameOptions().disable();

        return http.build();
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return ((request, response, authException) ->
                response.sendError(HttpStatus.UNAUTHORIZED.value(), authException.getMessage()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            UserRepository repository,
            AntiFraudMapper mapper
    ) {
        return username -> repository.findByUsernameIgnoreCase(username)
                .map(mapper::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }
}
