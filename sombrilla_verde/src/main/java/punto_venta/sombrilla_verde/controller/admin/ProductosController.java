package punto_venta.sombrilla_verde.controller.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;
import punto_venta.sombrilla_verde.service.producto.categoria.CategoriaService;
import punto_venta.sombrilla_verde.service.producto.producto.ProductoService;
import punto_venta.sombrilla_verde.service.proveedor.ProveedorService;

import java.util.List;

@Controller
@RequestMapping(value = "admin/productos")
public class ProductosController {
    @Autowired
    ProductoService productoService;
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    ProveedorService proveedorService;

    @GetMapping(value = "/")
    public String listaProductos(Model model) {
        List<ProductoEntity> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "vistas/admin/productos/lista-productos";
    }

    @GetMapping(value = "alta")
    public String altaProducto(Model model) {
        ProductoEntity producto = new ProductoEntity();
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("contenido", "Nuevo Producto");
        return "vistas/admin/productos/alta-producto";
    }

    @PostMapping(value = "alta")
    public String guardarProducto(@Valid @ModelAttribute("producto") ProductoEntity producto,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.findAll());
            model.addAttribute("proveedores", proveedorService.findAll());
            model.addAttribute("contenido", "Error al guardar el producto");
            model.addAttribute("mensajeError", "Error al guardar el producto");
            return "vistas/admin/productos/alta-producto";
        }

        if (producto.getId() != null) {
            ProductoEntity existente = productoService.findById(producto.getId());
            if (existente != null) {
                existente.setNombre(producto.getNombre());
                existente.setCodigo(producto.getCodigo());
                existente.setCategoria(producto.getCategoria());
                existente.setProveedor(producto.getProveedor());
                existente.setPrecioCompra(producto.getPrecioCompra());
                existente.setPrecioVenta(producto.getPrecioVenta());
                existente.setExistencia(producto.getExistencia());
                existente.setUnidadMedida(producto.getUnidadMedida());
                existente.setActivo(producto.getActivo());
                productoService.save(existente);
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", "El producto a modificar no existe");
                return "redirect:/admin/productos/";
            }
        } else {
            productoService.save(producto);
        }

        redirectAttributes.addFlashAttribute("mensajeExito", "Producto guardado con éxito");
        return "redirect:/admin/productos/";
    }

    @GetMapping(value = "modificar/{id}")
    public String modificarProducto(@PathVariable("id") Integer id, Model model) {
        ProductoEntity producto = productoService.findById(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("contenido", "Modificar Producto");
        return "vistas/admin/productos/alta-producto";
    }

    @GetMapping(value = "eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        productoService.deleteById(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Producto eliminado con éxito");
        return "redirect:/admin/productos/";
    }

    @GetMapping(value = "cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        ProductoEntity producto = productoService.findById(id);
        if (producto != null) {
            producto.setActivo(!producto.getActivo());
            productoService.save(producto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Estado del producto actualizado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "No se encontró el producto");
        }
        return "redirect:/admin/productos/";
    }
}
