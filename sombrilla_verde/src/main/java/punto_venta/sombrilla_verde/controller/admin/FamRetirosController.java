package punto_venta.sombrilla_verde.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import punto_venta.sombrilla_verde.service.familia.detalles_retiro_familiar.DetallesRetiroFamiliarService;
import punto_venta.sombrilla_verde.service.familia.retiro_efectivo.RetiroEfectivoService;
import punto_venta.sombrilla_verde.service.familia.retiro_familiar.RetiroFamiliarService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping(value = "admin/retiros")
public class FamRetirosController {
    @Autowired
    RetiroFamiliarService retiroFamiliarService;
    @Autowired
    DetallesRetiroFamiliarService detallesRetiroFamiliarService;
    @Autowired
    RetiroEfectivoService retiroEfectivoService;

    LocalDateTime fechaSeleccionada = null;

    @GetMapping(value = "/")
    public String opcionesRetiros(Model model) { return "vistas/admin/retiros/retiros-menu"; }

    @GetMapping(value = "efectivo")
    public String retirosEfectivo(Model model) {
        model.addAttribute("retiros", retiroEfectivoService.findAll());
        return "vistas/admin/retiros/ret-efectivo";
    }

    @GetMapping(value = "productos")
    public String retirosProductos(Model model) {
        model.addAttribute("retiros", retiroFamiliarService.findAll());
        return "vistas/admin/retiros/ret-productos";
    }

    @PostMapping("efectivo/filtrar")
    public String seleccionarFechaE(Model model, @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio
            , RedirectAttributes redirectAttributes) {
        // Convertir LocalDate a LocalDateTime (inicio del día)
        this.fechaSeleccionada = fechaInicio.atStartOfDay();
        model.addAttribute("fechaSeleccionada", this.fechaSeleccionada);
        model.addAttribute("retiros", retiroEfectivoService.findAllDesdeFecha(this.fechaSeleccionada));
        model.addAttribute("cantidadRetiros", retiroEfectivoService.findAllDesdeFecha(this.fechaSeleccionada).size());
        model.addAttribute("totalRetirado", retiroEfectivoService.sumMontoTotalDesdeFecha(this.fechaSeleccionada));
        return "vistas/admin/retiros/ret-efectivo";
    }

    @PostMapping("productos/filtrar")
    public String seleccionarFechaP(Model model, @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio
            ,RedirectAttributes redirectAttributes) {
        // Convertir LocalDate a LocalDateTime (inicio del día)
        this.fechaSeleccionada = fechaInicio.atStartOfDay();
        model.addAttribute("fechaSeleccionada", this.fechaSeleccionada);
        model.addAttribute("retiros", retiroFamiliarService.findAllDesdeFecha(this.fechaSeleccionada));
        model.addAttribute("cantidadRetiros", retiroFamiliarService.findAllDesdeFecha(this.fechaSeleccionada).size());
        model.addAttribute("totalRetirado", retiroFamiliarService.sumTotalCostoDesdeFecha(this.fechaSeleccionada));
        return "vistas/admin/retiros/ret-productos";
    }
}
