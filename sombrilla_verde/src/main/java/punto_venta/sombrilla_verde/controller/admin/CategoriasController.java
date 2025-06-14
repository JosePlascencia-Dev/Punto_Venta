package punto_venta.sombrilla_verde.controller.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import punto_venta.sombrilla_verde.model.entity.producto.CategoriaEntity;
import punto_venta.sombrilla_verde.service.producto.categoria.CategoriaService;

import java.util.List;

@Controller
@RequestMapping(value = "admin/categorias")
public class CategoriasController {
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping(value = "/")
    public String listaCategoria(Model model){
        List<CategoriaEntity> categorias = categoriaService.findAll();
        model.addAttribute("categorias", categorias);
        return "vistas/admin/categorias/lista-categorias";
    }

    @GetMapping(value = "alta")
    public String altaCategoria(Model model){
        CategoriaEntity categoria = new CategoriaEntity();
        model.addAttribute("categoria", categoria);
        model.addAttribute("contenido","Nueva Categoria");
        return "vistas/admin/categorias/alta-categoria";
    }

    @PostMapping(value = "alta")
    public String guardarCategoria(@Valid @ModelAttribute(value = "categoria") CategoriaEntity categoria, BindingResult result, RedirectAttributes redirectAttributes , Model model) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
            model.addAttribute("contenido","Error al guardar la categoria");
            model.addAttribute("mensajeError", "Error al guardar la categoria");
            return "vistas/admin/categorias/alta-categoria";
        }
        if (categoria.getId() != null) {
            CategoriaEntity existente = categoriaService.findById(categoria.getId());
            if (existente != null) {
                existente.setNombre(categoria.getNombre());
                existente.setDetalles(categoria.getDetalles());
                existente.setActivo(categoria.getActivo());
                categoriaService.save(existente);
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", "La categoría a modificar no existe");
                return "redirect:/admin/categorias";
            }
        } else {
            categoriaService.save(categoria);
        }

        model.addAttribute("mensajeExito", "Categoría guardada con éxito");
        List<CategoriaEntity> categorias = categoriaService.findAll();
        model.addAttribute("categorias", categorias);
        return "vistas/admin/categorias/lista-categorias";
    }

    @GetMapping(value = "modificar/{id}")
    public String modificarCategoria(@PathVariable("id") Integer id, Model model) {
        CategoriaEntity categoria = categoriaService.findById(id);
        model.addAttribute("categoria", categoria);
        model.addAttribute("contenido","Modificar Categoria");
        return "vistas/admin/categorias/alta-categoria";
    }

    @GetMapping(value = "eliminar/{id}")
    public String eliminarCategoria(@PathVariable("id") Integer id, Model model) {
        categoriaService.deleteById(id);
        model.addAttribute("mensajeExito", "Categoria eliminada con éxito");
        return "redirect:/admin/categorias/";
    }

    @GetMapping(value = "cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        CategoriaEntity categoria = categoriaService.findById(id);
        if (categoria != null) {
            categoria.setActivo(!categoria.getActivo());
            categoriaService.save(categoria);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Estado de categoría actualizado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "No se encontró la categoría");
        }
        return "redirect:/admin/categorias/";
    }
}
