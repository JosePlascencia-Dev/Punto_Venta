package punto_venta.sombrilla_verde.service.historial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import punto_venta.sombrilla_verde.model.entity.historial.HistorialProductosEntity;
import punto_venta.sombrilla_verde.repository.historial.HistorialProductoRepository;

import java.util.List;

@Service
public class HistorialProductoServiceImpl implements HistorialProductoService {
    @Autowired
    private HistorialProductoRepository historialProductoRepository;

    @Override
    public HistorialProductosEntity save(HistorialProductosEntity provedor) {
        return historialProductoRepository.save(provedor);
    }

    @Override
    public List<HistorialProductosEntity> findAll() {
        return historialProductoRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        historialProductoRepository.deleteById(id);
    }

    @Override
    public HistorialProductosEntity findById(Long id) {
        return historialProductoRepository.findById(id).orElse(null);
    }
}
