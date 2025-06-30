package punto_venta.sombrilla_verde.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import punto_venta.sombrilla_verde.model.entity.venta.DetalleVentaEntity;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;
import punto_venta.sombrilla_verde.service.venta.detalle_venta.DetalleVentaService;
import punto_venta.sombrilla_verde.service.venta.venta.VentaService;

import java.util.List;

@Controller
@RequestMapping(value = "admin/ventas")
public class VentasController {
    @Autowired
    private VentaService ventaService;
    @Autowired
    private DetalleVentaService detalleVentaService;

    private VentaEntity ventaEntity;

    @GetMapping(value = "/")
    public String listaVentas(Model model){
        List<VentaEntity> ventas = ventaService.findAll();
        model.addAttribute("ventas", ventas);
        return "vistas/admin/ventas/lista-ventas";
    }

    @GetMapping(value = "{id}")
    public String listaVentasDetalles(@PathVariable("id") Integer id,Model model){
        this.ventaEntity = ventaService.findById(id);
        List<DetalleVentaEntity> ventas = detalleVentaService.findByVenta(this.ventaEntity);
        model.addAttribute("venta", ventaEntity);
        model.addAttribute("detalles", ventas);
        return "vistas/admin/ventas/detalles-venta";
    }
}
