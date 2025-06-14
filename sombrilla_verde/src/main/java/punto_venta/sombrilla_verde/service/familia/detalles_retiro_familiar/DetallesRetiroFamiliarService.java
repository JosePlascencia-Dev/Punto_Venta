package punto_venta.sombrilla_verde.service.familia.detalles_retiro_familiar;

import punto_venta.sombrilla_verde.model.entity.compra.DetallesCompraEntity;
import punto_venta.sombrilla_verde.model.entity.familia.DetalleRetiroFamiliarEntity;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroFamiliarEntity;

import java.util.List;

public interface DetallesRetiroFamiliarService {
    DetalleRetiroFamiliarEntity save(DetalleRetiroFamiliarEntity detalleRetiroFamiliarEntity);
    List<DetalleRetiroFamiliarEntity> findAll();
    void deleteById(Integer id);
    DetalleRetiroFamiliarEntity findById(Integer id);
    List<DetalleRetiroFamiliarEntity> findByRetiro(RetiroFamiliarEntity retiro);
}
