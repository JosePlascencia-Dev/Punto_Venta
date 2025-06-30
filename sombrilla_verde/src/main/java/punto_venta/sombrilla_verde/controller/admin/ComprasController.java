package punto_venta.sombrilla_verde.controller.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import punto_venta.sombrilla_verde.model.entity.compra.CompraEntity;
import punto_venta.sombrilla_verde.model.entity.compra.DetallesCompraEntity;
import punto_venta.sombrilla_verde.service.compra.compra.CompraService;
import punto_venta.sombrilla_verde.service.compra.detalles_compras.DetallesCompraService;

import java.util.List;

@Controller
@RequestMapping(value = "admin/compras")
public class ComprasController {
    @Autowired
    private CompraService compraService;
    @Autowired
    private DetallesCompraService detallesCompraService;

    private CompraEntity compraEntity;

    @GetMapping(value = "/")
    public String listaVentas(Model model){
        List<CompraEntity> ventas = compraService.findAll();
        model.addAttribute("compras", ventas);
        return "vistas/admin/compras/lista-compras";
    }

    @GetMapping(value = "{id}")
    public String listaVentasDetalles(@PathVariable("id") Integer id, Model model){
        this.compraEntity = compraService.findById(id);
        List<DetallesCompraEntity> ventas = detallesCompraService.findByCompra(this.compraEntity);
        model.addAttribute("compra", compraEntity);
        model.addAttribute("detalles", ventas);
        return "vistas/admin/compras/detalles-compra";
    }
}
