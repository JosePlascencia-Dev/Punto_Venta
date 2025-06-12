package punto_venta.sombrilla_verde.controller.vendedor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroEfectivoEntity;
import punto_venta.sombrilla_verde.service.familia.retiro_efectivo.RetiroEfectivoService;

@Controller
@RequestMapping(value = "cajere/retiro-efectivo")
public class RetirosController {

    @Autowired
    private RetiroEfectivoService retiroEfectivoService;

    @GetMapping(value = "/")
    public String vistaVenta(Model model) {
        RetiroEfectivoEntity retiroEfectivo = new RetiroEfectivoEntity();
        model.addAttribute("retiro", retiroEfectivo);
        return "vistas/vendedor/retiros/retiro-efectivo";
    }
}


