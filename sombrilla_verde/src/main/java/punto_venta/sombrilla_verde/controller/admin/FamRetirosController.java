package punto_venta.sombrilla_verde.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "admin/retiros")
public class FamRetirosController {
    @GetMapping(value = "/")
    public String opcionesRetiros(Model model) {
        return "vistas/admin/retiros/retiros-menu";
    }
}
