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
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;
import punto_venta.sombrilla_verde.model.entity.usuario.UsuarioEntity;
import punto_venta.sombrilla_verde.model.entity.venta.DetalleVentaEntity;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;
import punto_venta.sombrilla_verde.service.producto.categoria.CategoriaService;
import punto_venta.sombrilla_verde.service.producto.producto.ProductoService;
import punto_venta.sombrilla_verde.service.proveedor.ProveedorService;
import punto_venta.sombrilla_verde.service.usuario.UsuarioService;
import punto_venta.sombrilla_verde.service.venta.detalle_venta.DetalleVentaService;
import punto_venta.sombrilla_verde.service.venta.venta.VentaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "cajere/venta")
public class VentaController {
    @Autowired
    VentaService ventaService;
    @Autowired
    DetalleVentaService detalleVentaService;
    @Autowired
    ProveedorService proveedorService;
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    ProductoService productoService;
    @Autowired
    UsuarioService usuarioService;

    ProductosSeleccionados carrito = new ProductosSeleccionados();

    @GetMapping(value = "/")
    public String vistaVenta(Model model) {
        model.addAttribute("compra", this.carrito);
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("productos", productoService.findAll());
        return "vistas/vendedor/venta";
    }

    @GetMapping(value = "/buscar-producto")
    public String buscarProductos(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Integer proveedorId,
            HttpSession session,
            Model model) {

        try {
            cargarDatosProductos(model);
            List<ProductoEntity> productosFiltrados;
            if (codigo != null && !codigo.trim().isEmpty()) {
                try {
                    ProductoEntity producto;
                    if(productoService.findByCodigo(codigo) != null){
                        producto = productoService.findByCodigo(codigo);
                    }else{
                        producto = productoService.findByNombre(codigo);
                    }
                    if (producto != null) {
                        this.carrito.add(producto);
                    }else{
                        model.addAttribute("mensajeError", "Producto no encontrado");
                    }
                }catch (NumberFormatException e) {
                    model.addAttribute("mensajeError", "Código inválido");
                }
                cargarDatosProductos(model);
                model.addAttribute("compra", this.carrito);
                return "vistas/vendedor/venta";
            }else {
                cargarDatosProductos(model);
                productosFiltrados = productoService.buscarPorCategoriaYProveedor(categoriaId,proveedorId);
                model.addAttribute("productos", productosFiltrados);
                return "vistas/vendedor/venta";
            }
        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error en búsqueda: " + e.getMessage());
            cargarDatosProductos(model);
            return "vistas/vendedor/venta";
        }
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
        return "vistas/vendedor/venta";
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
        return "vistas/vendedor/venta";
    }

    @GetMapping("borrar-venta")
    public String borrarVenta(Model model) {
        this.carrito.clear();
        cargarDatosProductos(model);
        model.addAttribute("compra", this.carrito);
        return "vistas/vendedor/venta";
    }

    @PostMapping("actualizar-cantidad")
    public String actualizarCantidad(@RequestParam("productoId") Integer productoId,
                                     @RequestParam("cantidad") BigDecimal cantidad) {
        carrito.actualizarCantidad(productoId, cantidad);
        return "redirect:/cajere/venta/";
    }

    @Transactional
    @PostMapping("procesar-venta")
    public String procesarVenta(
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (carrito.getProductos().isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No hay productos en el carrito");
            return "redirect:/cajere/venta/";
        }

        for (ProductosSeleccionados.ProductosInter item : carrito.getProductos()) {
            ProductoEntity producto = productoService.findById(item.getProducto().getId());
            if (producto.getExistencia().compareTo(item.getCantidad()) < 0) {
                redirectAttributes.addFlashAttribute("mensajeError",
                        "Stock insuficiente para: " + producto.getNombre());
                return "redirect:/cajere/venta/";
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UsuarioEntity> vendedor = usuarioService.findByNombreUsuario(authentication.getName());

        VentaEntity venta = VentaEntity.builder()
                .fechaVenta(LocalDateTime.now())
                .vendedor(vendedor.get())
                .total(carrito.getPrecioTotal())
                .build();
        venta = ventaService.save(venta);

        for (ProductosSeleccionados.ProductosInter item : carrito.getProductos()) {
            ProductoEntity producto = item.getProducto();

            BigDecimal nuevoStock = producto.getExistencia().subtract(item.getCantidad());
            producto.setExistencia(nuevoStock);
            productoService.save(producto);

            DetalleVentaEntity detalle = DetalleVentaEntity.builder()
                    .venta(venta)
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecioVenta())
                    .totalLinea(producto.getPrecioVenta().multiply(item.getCantidad()))
                    .build();
            detalleVentaService.save(detalle);
        }
        carrito.clear();
        redirectAttributes.addFlashAttribute("mensajeExito",
                "Venta registrada con éxito (ID: " + venta.getId() + ")");
        return "redirect:/cajere/venta/";
    }

    private void cargarDatosProductos(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("codigo", "");
        // Si no hay búsqueda, mostrar todos los productos
        if (!model.containsAttribute("productos")) {
            model.addAttribute("productos", productoService.findAll());
        }
    }

}
