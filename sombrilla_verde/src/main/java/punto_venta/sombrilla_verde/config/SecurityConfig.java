package punto_venta.sombrilla_verde.config;

import org.springframework.beans.factory.annotation.Autowired;
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
import punto_venta.sombrilla_verde.model.entity.usuario.TipoUsuario;
import punto_venta.sombrilla_verde.service.usuario.UsuarioDetailsServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
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
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/login").permitAll()
                        .requestMatchers("/admin/**").hasAuthority(TipoUsuario.ADMINISTRADOR.name())
                        .requestMatchers("/cajere/**").hasAuthority(TipoUsuario.VENDEDOR.name())
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Página de login
                        .loginProcessingUrl("/login") // Action del formulario
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/acceso-denegado"));

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
            }else {
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