package punto_venta.sombrilla_verde.service.venta.detalle_venta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import punto_venta.sombrilla_verde.model.entity.venta.DetalleVentaEntity;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;
import punto_venta.sombrilla_verde.repository.venta.DetalleVentaRepository;

import java.util.List;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Override
    public DetalleVentaEntity save(DetalleVentaEntity venta) {
        return detalleVentaRepository.save(venta);
    }

    @Override
    public List<DetalleVentaEntity> findAll() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        detalleVentaRepository.deleteById(id);
    }

    @Override
    public DetalleVentaEntity findById(Integer id) {
        return detalleVentaRepository.findById(id).orElse(null);
    }

    @Override
    public List<DetalleVentaEntity> findByVenta(VentaEntity venta) {
        return detalleVentaRepository.findByVenta(venta);
    }
}
