package punto_venta.sombrilla_verde.controller.vendedor;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroEfectivoEntity;
import punto_venta.sombrilla_verde.model.entity.producto.CategoriaEntity;
import punto_venta.sombrilla_verde.model.entity.usuario.UsuarioEntity;
import punto_venta.sombrilla_verde.service.familia.retiro_efectivo.RetiroEfectivoService;
import punto_venta.sombrilla_verde.service.usuario.UsuarioService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "cajere/retiro-efectivo")
public class RetirosController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RetiroEfectivoService retiroEfectivoService;

    @GetMapping(value = "/")
    public String vistaRetiro(Model model) {
        RetiroEfectivoEntity retiroEfectivo = new RetiroEfectivoEntity();
        model.addAttribute("retiro", retiroEfectivo);
        return "vistas/vendedor/retiros/retiro-efectivo";
    }

    @PostMapping(value = "retiro")
    public String retiro(@Valid @ModelAttribute(value = "retiro") RetiroEfectivoEntity retiroEfectivo, BindingResult result, RedirectAttributes redirectAttributes , Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UsuarioEntity> vendedor = usuarioService.findByNombreUsuario(authentication.getName());
        retiroEfectivo.setUsuario(vendedor.get());
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
            model.addAttribute("contenido","Error al guardar el retiro");
            model.addAttribute("mensajeError", "Error al guardar el retiro");
            return "vistas/vendedor/retiros/retiro-efectivo";
        }
        retiroEfectivoService.save(retiroEfectivo);
        redirectAttributes.addFlashAttribute("mensajeExito", "Retiro Exitoso");
        return "redirect:/cajere/venta/";
    }
}


