package punto_venta.sombrilla_verde.controller.vendedor;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import punto_venta.sombrilla_verde.model.domain.ProductosSeleccionados;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;
import punto_venta.sombrilla_verde.service.compra.compra.CompraService;
import punto_venta.sombrilla_verde.service.compra.detalles_compras.DetallesCompraService;
import punto_venta.sombrilla_verde.service.producto.categoria.CategoriaService;
import punto_venta.sombrilla_verde.service.producto.producto.ProductoService;
import punto_venta.sombrilla_verde.service.proveedor.ProveedorService;

import java.util.List;

@Controller
@RequestMapping(value = "cajere/compra")
public class CompraController {
    @Autowired
    CompraService compraService;
    @Autowired
    DetallesCompraService detallesCompraService;
    @Autowired
    ProveedorService proveedorService;
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    ProductoService productoService;

    ProductosSeleccionados carrito = new ProductosSeleccionados();

    @GetMapping(value = "/")
    public String vistaVenta(Model model) {
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("productos", productoService.findAll());
        return "vistas/vendedor/compra";
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
                return "vistas/vendedor/compra";
            }else {
                cargarDatosProductos(model);
                productosFiltrados = productoService.buscarPorCategoriaYProveedor(categoriaId,proveedorId);
                model.addAttribute("productos", productosFiltrados);
                return "vistas/vendedor/compra";
            }
        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error en búsqueda: " + e.getMessage());
            cargarDatosProductos(model);
            return "vistas/vendedor/compra";
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

    @GetMapping("borrar-venta")
    public String borrarVenta(Model model) {
        this.carrito.clear();
        cargarDatosProductos(model);
        model.addAttribute("compra", this.carrito);
        return "vistas/vendedor/compra";
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
