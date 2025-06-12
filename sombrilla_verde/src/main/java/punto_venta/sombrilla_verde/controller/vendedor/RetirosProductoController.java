package punto_venta.sombrilla_verde.controller.vendedor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import punto_venta.sombrilla_verde.model.domain.ProductosSeleccionados;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroEfectivoEntity;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroFamiliarEntity;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;
import punto_venta.sombrilla_verde.service.familia.retiro_efectivo.RetiroEfectivoService;
import punto_venta.sombrilla_verde.service.familia.retiro_familiar.RetiroFamiliarService;
import punto_venta.sombrilla_verde.service.producto.categoria.CategoriaService;
import punto_venta.sombrilla_verde.service.producto.producto.ProductoService;

import java.util.List;

@Controller
@RequestMapping(value = "cajere/retiro-producto")
public class RetirosProductoController {

    @Autowired
    private RetiroFamiliarService retiroFamiliarService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

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

    private void cargarDatosProductos(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("codigo", "");

        if (!model.containsAttribute("productos")) {
            model.addAttribute("productos", productoService.findAll());
        }
    }
}

