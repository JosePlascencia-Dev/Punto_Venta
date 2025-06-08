package punto_venta.sombrilla_verde.service.compra.detalles_compras;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import punto_venta.sombrilla_verde.model.entity.compra.DetallesCompraEntity;
import punto_venta.sombrilla_verde.repository.compra.DetallesCompraRepository;

import java.util.List;

@Service
public class DetallesCompraServiceImpl implements DetallesCompraService {
    @Autowired
    private DetallesCompraRepository detallesCompraRepository;

    @Override
    public DetallesCompraEntity save(DetallesCompraEntity compra) {
        return detallesCompraRepository.save(compra);
    }

    @Override
    public List<DetallesCompraEntity> findAll() {
        return detallesCompraRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        detallesCompraRepository.deleteById(id);
    }

    @Override
    public DetallesCompraEntity findById(Integer id) {
        return detallesCompraRepository.findById(id).orElse(null);
    }
}
