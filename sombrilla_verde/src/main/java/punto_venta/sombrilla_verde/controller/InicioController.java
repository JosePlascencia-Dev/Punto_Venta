package punto_venta.sombrilla_verde.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import punto_venta.sombrilla_verde.model.entity.usuario.TipoUsuario;

@Controller
public class InicioController {

    @RequestMapping(value = "/inicio", method = RequestMethod.GET)
    public String redirecionInicio() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String authority = authentication.getAuthorities().iterator().next().getAuthority();

        try {
            TipoUsuario autoridad = TipoUsuario.valueOf(authority);
            return switch (autoridad) {
                case ADMINISTRADOR -> "redirect:/admin/inicio";
                case VENDEDOR -> "redirect:/cajere/inicio";
                default -> "redirect:/login?error=unauthorized";
            };
        } catch (IllegalArgumentException e) {
            return "redirect:/login?error=invalid_role";
        }
    }
}
