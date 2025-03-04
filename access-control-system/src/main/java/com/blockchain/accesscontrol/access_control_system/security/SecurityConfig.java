package com.blockchain.accesscontrol.access_control_system.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig 
{
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
	{
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        	.csrf(csrf -> csrf.disable())
        	.authorizeHttpRequests(auth -> auth
        			.requestMatchers("/ws/**").permitAll() // Allow WebSocket connections
                    .requestMatchers(
                        "/api/auth/login",
                        "/v3/api-docs/**",
                        "/ws/**",
                        "/ws/info",
                        "/api/role-token/assign-role", 
                        "/api/auth/login",  
                        "/swagger-ui/**"
                    ).permitAll()
                    .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() 
	{
		CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(List.of("http://localhost:3000"));
	    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
	    configuration.setAllowCredentials(true);
	    configuration.addAllowedOriginPattern("http://localhost:3000");

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
		
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(List.of("http://localhost:3000"));
//        configuration.setAllowedMethods(List.of("*"));
//        configuration.setAllowedHeaders(List.of("*"));
////        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
////        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//        configuration.setExposedHeaders(List.of("Authorization"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
    }

    @Bean
    public UserDetailsService userDetailsService(PeerRepository peerRepository) {
         return new PeerUserDetailsService(peerRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
    }
}
