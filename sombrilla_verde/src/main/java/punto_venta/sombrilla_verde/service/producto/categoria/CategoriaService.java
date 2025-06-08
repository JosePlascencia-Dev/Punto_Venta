package punto_venta.sombrilla_verde.service.producto.categoria;

import punto_venta.sombrilla_verde.model.entity.producto.CategoriaEntity;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;

import java.util.List;

public interface CategoriaService {
    CategoriaEntity save(CategoriaEntity provedor);
    List<CategoriaEntity> findAll();
    void deleteById(Integer id);
    CategoriaEntity findById(Integer id);
}
