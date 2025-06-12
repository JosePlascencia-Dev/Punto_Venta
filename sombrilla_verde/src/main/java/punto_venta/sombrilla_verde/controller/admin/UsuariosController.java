package punto_venta.sombrilla_verde.controller.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import punto_venta.sombrilla_verde.model.entity.usuario.TipoUsuario;
import punto_venta.sombrilla_verde.model.entity.usuario.UsuarioEntity;
import punto_venta.sombrilla_verde.service.usuario.UsuarioService;

import java.util.List;

@Controller
@RequestMapping(value = "admin/usuarios")
public class UsuariosController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/")
    public String listaUsuarios(Model model) {
        List<UsuarioEntity> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "vistas/admin/usuarios/lista-usuarios";
    }

    @GetMapping(value = "/alta")
    public String altaUsuario(Model model) {
        UsuarioEntity usuario = UsuarioEntity.builder()
                .tipo(TipoUsuario.VENDEDOR) // Fijamos el tipo como VENDEDOR
                .activo(true) // Por defecto activo
                .build();

        model.addAttribute("usuario", usuario);
        return "vistas/admin/usuarios/alta-usuario";
    }

    @PostMapping("/alta")
    public String guardarUsuario(
            @Valid @ModelAttribute("usuario") UsuarioEntity usuario,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Validaciones básicas
        if (result.hasErrors()) {
            model.addAttribute("mensajeError", "Datos inválidos");
            return "vistas/admin/usuarios/alta-usuario";
        }

        // Validar que no intenten cambiar el tipo a ADMINISTRADOR
        if (usuario.getTipo() != TipoUsuario.VENDEDOR) {
            model.addAttribute("mensajeError", "Solo se permiten usuarios de tipo VENDEDOR");
            return "vistas/admin/usuarios/alta-usuario";
        }

        boolean esNuevo = (usuario.getId() == null);

        // Validar nombre de usuario único
        if (esNuevo) {
            if (usuarioService.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
                model.addAttribute("mensajeError", "El nombre de usuario ya está registrado");
                return "vistas/admin/usuarios/alta-usuario";
            }
        } else {
            UsuarioEntity existente = usuarioService.findById(usuario.getId());
            if (existente == null) {
                model.addAttribute("mensajeError", "El usuario a modificar no existe");
                return "vistas/admin/usuarios/alta-usuario";
            }
            // Si cambió el nombre de usuario, verificar unicidad
            if (!existente.getNombreUsuario().equals(usuario.getNombreUsuario())
                    && usuarioService.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
                model.addAttribute("mensajeError", "El nombre de usuario ya está registrado por otro usuario");
                return "vistas/admin/usuarios/alta-usuario";
            }
        }

        // Gestionar contraseña
        if (esNuevo) {
            // En creación, es obligatorio enviar contraseña
            if (usuario.getContrasenaHash() == null || usuario.getContrasenaHash().isEmpty()) {
                model.addAttribute("mensajeError", "La contraseña es requerida");
                return "vistas/admin/usuarios/alta-usuario";
            }
            usuario.setContrasenaHash(passwordEncoder.encode(usuario.getContrasenaHash()));
        } else {
            // En edición:
            if (usuario.getContrasenaHash() != null && !usuario.getContrasenaHash().isEmpty()) {
                // Si envían nueva contraseña, la codificamos
                usuario.setContrasenaHash(passwordEncoder.encode(usuario.getContrasenaHash()));
            } else {
                // Si no, conservamos la anterior
                UsuarioEntity existente = usuarioService.findById(usuario.getId());
                usuario.setContrasenaHash(existente.getContrasenaHash());
            }
        }

        // Guardar usuario
        usuarioService.save(usuario);

        redirectAttributes.addFlashAttribute("mensajeExito",
                esNuevo ? "Vendedor creado correctamente" : "Vendedor modificado correctamente");
        return "redirect:/admin/usuarios/";
    }

    @GetMapping("/modificar/{id}")
    public String modificarUsuario(@PathVariable("id") Integer id,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        UsuarioEntity usuario = usuarioService.findById(id);
        if (usuario == null || usuario.getTipo() != TipoUsuario.VENDEDOR) {
            redirectAttributes.addFlashAttribute("mensajeError", "Vendedor no encontrado");
            return "redirect:/admin/usuarios/";
        }

        model.addAttribute("usuario", usuario);
        return "vistas/admin/usuarios/alta-usuario";
    }

    @GetMapping(value = "eliminar/{id}")
    public String eliminarCategoria(@PathVariable("id") Integer id, Model model) {
        usuarioService.deleteById(id);
        model.addAttribute("mensajeExito", "Usu eliminada con éxito");
        return "redirect:/admin/usuarios/";
    }

    @GetMapping("/cambiar-estado/{id}")
    public String cambiarEstadoUsuario(@PathVariable("id") Integer id,
                                       RedirectAttributes redirectAttributes) {
        UsuarioEntity usuario = usuarioService.findById(id);
        if (usuario == null || usuario.getTipo() != TipoUsuario.VENDEDOR) {
            redirectAttributes.addFlashAttribute("mensajeError", "Vendedor no encontrado");
            return "redirect:/admin/usuarios/";
        }

        usuario.setActivo(!usuario.getActivo());
        usuarioService.save(usuario);

        redirectAttributes.addFlashAttribute("mensajeExito",
                usuario.getActivo() ? "Vendedor activado correctamente" : "Vendedor desactivado correctamente");
        return "redirect:/admin/usuarios/";
    }
}