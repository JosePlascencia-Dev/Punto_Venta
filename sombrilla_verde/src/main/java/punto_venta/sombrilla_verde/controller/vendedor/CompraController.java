package punto_venta.sombrilla_verde.controller.vendedor;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import punto_venta.sombrilla_verde.model.domain.ProductosSeleccionados;
import punto_venta.sombrilla_verde.model.entity.compra.CompraEntity;
import punto_venta.sombrilla_verde.model.entity.compra.DetallesCompraEntity;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;
import punto_venta.sombrilla_verde.model.entity.proveedor.ProveedorEntity;
import punto_venta.sombrilla_verde.model.entity.usuario.UsuarioEntity;
import punto_venta.sombrilla_verde.service.compra.compra.CompraService;
import punto_venta.sombrilla_verde.service.compra.detalles_compras.DetallesCompraService;
import punto_venta.sombrilla_verde.service.producto.categoria.CategoriaService;
import punto_venta.sombrilla_verde.service.producto.producto.ProductoService;
import punto_venta.sombrilla_verde.service.proveedor.ProveedorService;
import punto_venta.sombrilla_verde.service.usuario.UsuarioService;
import punto_venta.sombrilla_verde.service.venta.venta.VentaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping(value = "cajere/compra")
public class CompraController {
    @Autowired
    private CompraService compraService;
    @Autowired
    private DetallesCompraService detallesCompraService;
    @Autowired
    private VentaService ventaService;
    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private UsuarioService usuarioService;

    ProveedorEntity proveedor = null;
    ProductosSeleccionados carrito = new ProductosSeleccionados();

    @GetMapping(value = "/")
    public String vistaVenta(Model model) {
        cargarDatosProductos(model);
        model.addAttribute("compra", this.carrito);
        return "vistas/vendedor/compra";
    }

    @PostMapping(value = "/seleccionar-proveedor")
    public String seleccionar(
            @RequestParam(required = true) Integer proveedorId,
            Model model) {
        this.proveedor = proveedorService.findById(proveedorId);
        cargarDatosProductos(model);
        return "vistas/vendedor/compra";
    }

    @PostMapping("agregar-producto/{id}")
    public String agregarProducto(@PathVariable("id") Integer id, Model model) {
        if(id != null){
            ProductoEntity producto = productoService.findById(id);
            this.carrito.add(producto);
        } else {
            model.addAttribute("mensajeError", "Error al agregar producto");
        }
        cargarDatosProductos(model);
        model.addAttribute("compra", this.carrito);
        return "vistas/vendedor/compra";
    }

    @PostMapping("eliminar-producto/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id, Model model) {
        if(id != null){
            ProductoEntity producto = productoService.findById(id);
            carrito.remove(producto);
        } else {
            model.addAttribute("mensajeError", "Error al agregar producto");
        }
        cargarDatosProductos(model);
        model.addAttribute("compra", this.carrito);
        return "vistas/vendedor/compra";
    }

    @GetMapping("borrar-compra")
    public String borrarVenta(Model model) {
        this.carrito.clear();
        cargarDatosProductos(model);
        model.addAttribute("compra", this.carrito);
        return "vistas/vendedor/compra";
    }

    @Transactional
    @PostMapping("procesar-compra")
    public String procesarVenta(
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (carrito.getProductos().isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No hay productos en el carrito");
            return "redirect:/cajere/compra/";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioEntity encargado = usuarioService.findByNombreUsuario(authentication.getName()).orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        CompraEntity compra = CompraEntity.builder()
                .proveedor(this.proveedor)
                .fechaCompra(LocalDateTime.now())
                .usuarioRegistro(encargado)
                .total(carrito.getCostoTotal())
                .build();
        compra = compraService.save(compra);
        for (ProductosSeleccionados.ProductosInter item : carrito.getProductos()) {
            ProductoEntity producto = productoService.findById(item.getProducto().getId());
            BigDecimal nuevaExistencia = producto.getExistencia().add(item.getCantidad());
            producto.setExistencia(nuevaExistencia);
            productoService.save(producto);

            DetallesCompraEntity detalle = DetallesCompraEntity.builder()
                    .compra(compra)
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .costoUnitario(producto.getPrecioCompra())
                    .totalLinea(producto.getPrecioCompra().multiply(item.getCantidad()))
                    .build();
            detallesCompraService.save(detalle);
        }

        // 5) Limpiar carrito y mostrar mensaje
        carrito.clear();
        redirectAttributes.addFlashAttribute("mensajeExito",
                "Compra registrada con éxito (ID: " + compra.getId() + ")");
        return "redirect:/cajere/compra/";
    }

    private void cargarDatosProductos(Model model) {
        model.addAttribute("proveedores", proveedorService.findAll());
        if (!model.containsAttribute("productos") && this.proveedor != null) {
            model.addAttribute("productos", productoService.findByProveedor(this.proveedor));
            model.addAttribute("proveedorSeleccionado", proveedor);
            model.addAttribute("ventaTotal", ventaService.totalVentasDesdeUltimaCompra(this.proveedor.getId()));
            model.addAttribute("costoTotal", ventaService.costoVentasDesdeUltimaCompra(this.proveedor.getId()));
        }
    }

}
