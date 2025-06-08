package punto_venta.sombrilla_verde.service.familia.retiro_familiar;

import punto_venta.sombrilla_verde.model.entity.familia.RetiroEfectivoEntity;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroFamiliarEntity;

import java.util.List;

public interface RetiroFamiliarService {
    RetiroFamiliarEntity save(RetiroFamiliarEntity retiroFamiliar);
    List<RetiroFamiliarEntity> findAll();
    void deleteById(Integer id);
    RetiroFamiliarEntity findById(Integer id);
}
