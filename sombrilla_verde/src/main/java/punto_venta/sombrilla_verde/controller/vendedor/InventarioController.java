package punto_venta.sombrilla_verde.controller.vendedor;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import punto_venta.sombrilla_verde.model.domain.ProductosSeleccionados;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;
import punto_venta.sombrilla_verde.service.producto.categoria.CategoriaService;
import punto_venta.sombrilla_verde.service.producto.producto.ProductoService;
import punto_venta.sombrilla_verde.service.proveedor.ProveedorService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping(value = "cajere/inventario")
public class InventarioController {
    @Autowired
    ProveedorService proveedorService;
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    ProductoService productoService;

    ProductosSeleccionados carrito = new ProductosSeleccionados();

    @GetMapping(value = "/")
    public String vistaVenta(Model model) {
        model.addAttribute("compra", this.carrito);
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("productos", productoService.findAll());
        return "vistas/vendedor/inventario";
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
                return "vistas/vendedor/inventario";
            }else {
                cargarDatosProductos(model);
                productosFiltrados = productoService.buscarPorCategoriaYProveedor(categoriaId,proveedorId);
                model.addAttribute("productos", productosFiltrados);
                return "vistas/vendedor/inventario";
            }
        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error en búsqueda: " + e.getMessage());
            cargarDatosProductos(model);
            return "vistas/vendedor/inventario";
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
        return "vistas/vendedor/inventario";
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
        return "vistas/vendedor/inventario";
    }

    @GetMapping("borrar-venta")
    public String borrarVenta(Model model) {
        this.carrito.clear();
        cargarDatosProductos(model);
        model.addAttribute("compra", this.carrito);
        return "vistas/vendedor/inventario";
    }

    @PostMapping("actualizar-cantidad")
    public String actualizarCantidad(@RequestParam("productoId") Integer productoId,
                                     @RequestParam("cantidad") BigDecimal cantidad) {
        carrito.actualizarCantidad(productoId, cantidad);
        return "redirect:/cajere/inventario/";
    }

    @PostMapping("actualizar")
    public String actualizarInventario(
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            // Validar que hay productos seleccionados
            if (carrito.getProductos().isEmpty()) {
                redirectAttributes.addFlashAttribute("mensajeError", "No hay productos seleccionados para actualizar");
                return "redirect:/cajere/inventario/";
            }

            // Actualizar el stock para cada producto
            for (ProductosSeleccionados.ProductosInter item : carrito.getProductos()) {
                ProductoEntity producto = productoService.findById(item.getProducto().getId());

                // Validar que el producto existe
                if (producto == null) {
                    redirectAttributes.addFlashAttribute("mensajeError",
                            "Producto con ID " + item.getProducto().getId() + " no encontrado");
                    return "redirect:/cajere/inventario/";
                }

                // Actualizar la existencia (sumar la cantidad)
                BigDecimal nuevaExistencia = item.getCantidad();
                producto.setExistencia(nuevaExistencia);

                // Guardar el producto actualizado
                productoService.save(producto);
            }

            // Limpiar el carrito después de la actualización
            carrito.clear();
            session.setAttribute("carrito", carrito);

            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Inventario actualizado correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error al actualizar inventario: " + e.getMessage());
        }

        return "redirect:/cajere/inventario/";
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
