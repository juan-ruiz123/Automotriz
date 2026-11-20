package com.usta.automotriz.config;

import com.usta.automotriz.enums.Rol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String rolAdmin = Rol.Administrador.name();

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/usuarios/**").hasRole(rolAdmin)
                        .requestMatchers(
                                "/modelos/crear", "/modelos/guardar", "/modelos/editar/**", "/modelos/eliminar/**",
                                "/vehiculos/crear", "/vehiculos/guardar", "/vehiculos/editar/**", "/vehiculos/eliminar/**",
                                "/repuestos/crear", "/repuestos/guardar", "/repuestos/editar/**", "/repuestos/eliminar/**",
                                "/reparaciones/cancelar/**"
                        ).hasRole(rolAdmin)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }
}