package punto_venta.sombrilla_verde.service.familia.retiro_efectivo;

import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import punto_venta.sombrilla_verde.model.entity.familia.DetalleRetiroFamiliarEntity;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroEfectivoEntity;
import punto_venta.sombrilla_verde.repository.familia.RetiroEfectivoRepository;

import java.util.List;

@Service
public class RetiroEfectivoServiceImpl implements RetiroEfectivoService {
    @Autowired
    private RetiroEfectivoRepository retiroEfectivoRepository;

    @Override
    public RetiroEfectivoEntity save(RetiroEfectivoEntity retiroEfectivo) {
        return retiroEfectivoRepository.save(retiroEfectivo);
    }

    @Override
    public List<RetiroEfectivoEntity> findAll() {
        return retiroEfectivoRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        retiroEfectivoRepository.deleteById(id);
    }

    @Override
    public RetiroEfectivoEntity findById(Integer id) {
        return retiroEfectivoRepository.findById(id).orElse(null);
    }
}
