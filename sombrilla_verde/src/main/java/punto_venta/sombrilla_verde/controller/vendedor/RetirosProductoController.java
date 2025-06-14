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
import punto_venta.sombrilla_verde.model.entity.familia.DetalleRetiroFamiliarEntity;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroEfectivoEntity;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroFamiliarEntity;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;
import punto_venta.sombrilla_verde.model.entity.usuario.UsuarioEntity;
import punto_venta.sombrilla_verde.model.entity.venta.DetalleVentaEntity;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;
import punto_venta.sombrilla_verde.service.familia.detalles_retiro_familiar.DetallesRetiroFamiliarService;
import punto_venta.sombrilla_verde.service.familia.retiro_efectivo.RetiroEfectivoService;
import punto_venta.sombrilla_verde.service.familia.retiro_familiar.RetiroFamiliarService;
import punto_venta.sombrilla_verde.service.producto.categoria.CategoriaService;
import punto_venta.sombrilla_verde.service.producto.producto.ProductoService;
import punto_venta.sombrilla_verde.service.usuario.UsuarioService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "cajere/retiro-producto")
public class RetirosProductoController {
    @Autowired
    private RetiroFamiliarService retiroFamiliarService;
    @Autowired
    private DetallesRetiroFamiliarService detallesRetiroFamiliarService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    UsuarioService usuarioService;

    // Aquí usamos el carrito para los productos seleccionados para retiro
    private final ProductosSeleccionados carrito = new ProductosSeleccionados();

    @GetMapping(value = "/")
    public String vistaRetiro(Model model) {
        model.addAttribute("retiro", carrito);
        cargarDatosProductos(model);
        return "vistas/vendedor/retiros/retiro-producto";
    }

    @GetMapping(value = "/buscar-producto")
    public String buscarProductos(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Integer proveedorId,
            Model model) {

        try {
            cargarDatosProductos(model);
            List<ProductoEntity> productosFiltrados;

            if (codigo != null && !codigo.trim().isEmpty()) {
                try {
                    ProductoEntity producto = productoService.findByCodigo(codigo);

                    if (producto == null) {
                        producto = productoService.findByNombre(codigo);
                    }

                    if (producto != null) {
                        carrito.add(producto);
                    } else {
                        model.addAttribute("mensajeError", "Producto no encontrado");
                    }

                } catch (NumberFormatException e) {
                    model.addAttribute("mensajeError", "Código inválido");
                }

                model.addAttribute("retiro", this.carrito);
                return "vistas/vendedor/retiros/retiro-producto";
            } else {
                productosFiltrados = productoService.buscarPorCategoriaYProveedor(categoriaId, proveedorId);
                model.addAttribute("productos", productosFiltrados);
                model.addAttribute("retiro", carrito);
                return "vistas/vendedor/retiros/retiro-producto";
            }

        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error en la búsqueda: " + e.getMessage());
            cargarDatosProductos(model);
            model.addAttribute("retiro", carrito);
            return "vistas/vendedor/retiros/retiro-producto";
        }
    }

    @PostMapping("agregar-producto/{id}")
    public String agregarProducto(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            ProductoEntity producto = productoService.findById(id);
            carrito.add(producto);
        } else {
            model.addAttribute("mensajeError", "Error al agregar producto");
        }

        cargarDatosProductos(model);
        model.addAttribute("retiro", carrito);
        return "vistas/vendedor/retiros/retiro-producto";
    }

    @PostMapping("eliminar-producto/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            ProductoEntity producto = productoService.findById(id);
            carrito.remove(producto);
        } else {
            model.addAttribute("mensajeError", "Error al eliminar producto");
        }

        cargarDatosProductos(model);
        model.addAttribute("retiro", carrito);
        return "vistas/vendedor/retiros/retiro-producto";
    }

    @GetMapping("borrar-retiro")
    public String borrarRetiro(Model model) {
        carrito.clear();
        cargarDatosProductos(model);
        model.addAttribute("retiro", carrito);
        return "vistas/vendedor/retiros/retiro-producto";
    }

    @PostMapping("actualizar-cantidad")
    public String actualizarCantidad(@RequestParam("productoId") Integer productoId,
                                     @RequestParam("cantidad") BigDecimal cantidad) {
        carrito.actualizarCantidad(productoId, cantidad);
        return "redirect:/cajere/retiro-producto/";
    }

    @Transactional
    @PostMapping("procesar-retiro")
    public String procesarVenta(
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (carrito.getProductos().isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No hay productos en el carrito");
            return "redirect:/cajere/retiro-producto/";
        }

        for (ProductosSeleccionados.ProductosInter item : carrito.getProductos()) {
            ProductoEntity producto = productoService.findById(item.getProducto().getId());
            if (producto.getExistencia().compareTo(item.getCantidad()) < 0) {
                redirectAttributes.addFlashAttribute("mensajeError",
                        "Stock insuficiente para: " + producto.getNombre());
                return "redirect:/cajere/retiro-producto/";
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UsuarioEntity> vendedor = usuarioService.findByNombreUsuario(authentication.getName());

        RetiroFamiliarEntity retiro = RetiroFamiliarEntity.builder()
                .fecha(LocalDateTime.now())
                .usuario(vendedor.get())
                .totalCosto(carrito.getPrecioTotal())
                .build();
        retiro = retiroFamiliarService.save(retiro);

        for (ProductosSeleccionados.ProductosInter item : carrito.getProductos()) {
            ProductoEntity producto = item.getProducto();

            BigDecimal nuevoStock = producto.getExistencia().subtract(item.getCantidad());
            producto.setExistencia(nuevoStock);
            productoService.save(producto);

            DetalleRetiroFamiliarEntity detalle = DetalleRetiroFamiliarEntity.builder()
                    .retiro(retiro)
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .costoUnitario(producto.getPrecioCompra())
                    .totalLinea(producto.getPrecioCompra().multiply(item.getCantidad()))
                    .build();
            detallesRetiroFamiliarService.save(detalle);
        }
        carrito.clear();
        redirectAttributes.addFlashAttribute("mensajeExito",
                "Venta registrada con éxito (ID: " + retiro.getId() + ")");
        return "redirect:/cajere/retiro-producto/";
    }

    private void cargarDatosProductos(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("codigo", "");

        if (!model.containsAttribute("productos")) {
            model.addAttribute("productos", productoService.findAll());
        }
    }
}

