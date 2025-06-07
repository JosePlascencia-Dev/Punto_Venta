package punto_venta.sombrilla_verde.config;

import mx.aragon.unam.model.entity.usuario.TipoUsuario;
import mx.aragon.unam.service.usuario.UsuarioDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UsuarioDetailsServiceImpl usuarioDetailsService;

    public SecurityConfig(UsuarioDetailsServiceImpl usuarioDetailsService) {
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.userDetailsService(usuarioDetailsService)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/",
                            "/login",
                            "/css/**",
                            "/js/**",
                            "/images/**"
                    ).permitAll()
                    .requestMatchers("/admin/**").hasAuthority(TipoUsuario.ADMINISTRADOR.name())
                    .requestMatchers("/cajere/**").hasAuthority(TipoUsuario.VENDEDOR.name())
                    .requestMatchers("/finanzas/**").hasAuthority(TipoUsuario.FINANZAS.name())
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/login")
                    .successHandler(authenticationSuccessHandler()) // Usamos un custom success handler
                    .failureHandler(authenticationFailureHandler()) // Agrega este handler
                    .failureUrl("/login?error=true")
                    .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            )
            .exceptionHandling(ex -> ex
                    .accessDeniedPage("/acceso-denegado")
            );
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            if (authorities.stream().anyMatch(a -> a.getAuthority().equals(TipoUsuario.ADMINISTRADOR.name()))) {
                response.sendRedirect("/admin/inicio");
            }
            else if (authorities.stream().anyMatch(a -> a.getAuthority().equals(TipoUsuario.VENDEDOR.name()))) {
                response.sendRedirect("/cajere/inicio");
            }
            else if (authorities.stream().anyMatch(a -> a.getAuthority().equals(TipoUsuario.FINANZAS.name()))) {
                response.sendRedirect("/finanzas/inicio");
            }else if (authorities.stream().anyMatch(a -> a.getAuthority().equals(TipoUsuario.CLIENTE.name()))) {
                // Cerrar sesión inmediatamente si es cliente
                new SecurityContextLogoutHandler().logout(request, response, authentication);
                response.sendRedirect("/login?error=access_denied");
                return;
            }
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            String errorMessage;

            if (exception instanceof BadCredentialsException) {
                errorMessage = "Usuario o contraseña incorrectos";
            } else if (exception instanceof DisabledException) {
                errorMessage = "Cuenta deshabilitada";
            } else if (exception instanceof LockedException) {
                errorMessage = "Cuenta bloqueada";
            } else {
                errorMessage = "Error al iniciar sesión";
            }

            request.getSession().setAttribute("error", errorMessage);
            response.sendRedirect("/login?error=true");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SHA256PasswordEncoder();
    }

    public static List<SimpleGrantedAuthority> mapTiposUsuarioToAuthorities(List<TipoUsuario> tipos) {
        return tipos.stream()
                .map(tipo -> new SimpleGrantedAuthority(tipo.name()))
                .collect(Collectors.toList());
    }
}