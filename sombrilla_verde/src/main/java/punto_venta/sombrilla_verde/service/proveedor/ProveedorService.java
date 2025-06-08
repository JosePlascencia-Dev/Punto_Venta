package punto_venta.sombrilla_verde.service.proveedor;

import punto_venta.sombrilla_verde.model.entity.proveedor.ProveedorEntity;

import java.util.List;

public interface ProveedorService {
    ProveedorEntity save(ProveedorEntity provedor);
    List<ProveedorEntity> findAll();
    void deleteById(Integer id);
    ProveedorEntity findById(Integer id);
}
