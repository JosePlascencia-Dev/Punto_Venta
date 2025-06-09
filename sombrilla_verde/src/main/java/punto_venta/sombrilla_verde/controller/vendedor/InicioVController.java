package punto_venta.sombrilla_verde.controller.vendedor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;
import punto_venta.sombrilla_verde.service.producto.producto.ProductoService;
import punto_venta.sombrilla_verde.service.venta.venta.VentaService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping(value = "cajere")
public class InicioVController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    @GetMapping(value = "inicio")
    public String vistaInicioCajere(Model model) {
        Long ventasHoy = ventaService.countVentasDelDia();
        BigDecimal totalHoy = ventaService.totalVentasDelDia();
        BigDecimal ganaciaHoy = ventaService.calcularGananciaDelDia();
        List<ProductoEntity> productos = productoService.findAllOrderByExistenciaAsc();
        model.addAttribute("ventasHoy", ventasHoy);
        model.addAttribute("totalHoy", totalHoy);
        model.addAttribute("gananciasHoy", ganaciaHoy);
        model.addAttribute("pedidosPendientes", 0);
        model.addAttribute("productosOrdenadosPorStock", productos);
        return "vistas/vendedor/inicio";
    }
}
