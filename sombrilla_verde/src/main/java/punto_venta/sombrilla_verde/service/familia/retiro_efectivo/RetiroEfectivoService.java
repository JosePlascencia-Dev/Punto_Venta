package punto_venta.sombrilla_verde.service.familia.retiro_efectivo;

import org.springframework.data.repository.query.Param;
import punto_venta.sombrilla_verde.model.entity.familia.DetalleRetiroFamiliarEntity;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroEfectivoEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface RetiroEfectivoService {
    RetiroEfectivoEntity save(RetiroEfectivoEntity retiroEfectivo);
    List<RetiroEfectivoEntity> findAll();
    void deleteById(Integer id);
    RetiroEfectivoEntity findById(Integer id);
    List<RetiroEfectivoEntity> findAllDesdeFecha(LocalDateTime fechaInicio);
    BigDecimal sumMontoTotalDesdeFecha(LocalDateTime fechaInicio);
}
