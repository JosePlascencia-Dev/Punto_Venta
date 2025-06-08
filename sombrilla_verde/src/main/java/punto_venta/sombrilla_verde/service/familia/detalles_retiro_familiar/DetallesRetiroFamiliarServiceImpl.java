package punto_venta.sombrilla_verde.service.familia.detalles_retiro_familiar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import punto_venta.sombrilla_verde.model.entity.familia.DetalleRetiroFamiliarEntity;
import punto_venta.sombrilla_verde.repository.familia.DetallesRetiroFamiliarRepository;

import java.util.List;

@Service
public class DetallesRetiroFamiliarServiceImpl implements DetallesRetiroFamiliarService {
    @Autowired
    private DetallesRetiroFamiliarRepository retiroFamiliarRepository;

    @Override
    public DetalleRetiroFamiliarEntity save(DetalleRetiroFamiliarEntity detalleRetiroFamiliarEntity) {
        return retiroFamiliarRepository.save(detalleRetiroFamiliarEntity);
    }

    @Override
    public List<DetalleRetiroFamiliarEntity> findAll() {
        return retiroFamiliarRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        retiroFamiliarRepository.deleteById(id);
    }

    @Override
    public DetalleRetiroFamiliarEntity findById(Integer id) {
        return retiroFamiliarRepository.findById(id).orElse(null);
    }
}
