    package com.example.crudApp.config;

    import com.example.crudApp.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.NoOpPasswordEncoder;
    import org.springframework.security.provisioning.InMemoryUserDetailsManager;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration
    public class SecurityConfig {
        @Autowired
        private JwtFilter jwtFilter;


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.requestMatchers("/register","/login","swagger-ui/index.html","/v3/api-docs","/swagger-ui.html", "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/v3/api-docs/**","/api/products","/api/ask","/api/send","/api/**").permitAll()
                            .anyRequest().authenticated()) .sessionManagement(session ->
                            session.sessionCreationPolicy(
                                    SessionCreationPolicy.STATELESS
                            )
                    )
                    .addFilterBefore(
                            jwtFilter,
                            UsernamePasswordAuthenticationFilter.class
                    );

            return http.build();
        }
        @Autowired
        UserService userService;
        @Bean
        public AuthenticationProvider authenticationProvider()
        {
            DaoAuthenticationProvider provider =  new DaoAuthenticationProvider();
            provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
            provider.setUserDetailsService(userService);
            return provider;
        }
        @Bean
        public AuthenticationManager authenticationManager(
                AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }
    }


