package punto_venta.sombrilla_verde.service.venta.venta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;
import punto_venta.sombrilla_verde.repository.venta.VentaRepository;

import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public VentaEntity save(VentaEntity venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public List<VentaEntity> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        ventaRepository.deleteById(id);
    }

    @Override
    public VentaEntity findById(Integer id) {
        return ventaRepository.findById(id).orElse(null);
    }
}
