package punto_venta.sombrilla_verde.service.venta.venta;

import punto_venta.sombrilla_verde.model.entity.usuario.UsuarioEntity;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;

import java.util.List;

public interface VentaService {
    VentaEntity save(VentaEntity venta);
    List<VentaEntity> findAll();
    void deleteById(Integer id);
    VentaEntity findById(Integer id);
}
