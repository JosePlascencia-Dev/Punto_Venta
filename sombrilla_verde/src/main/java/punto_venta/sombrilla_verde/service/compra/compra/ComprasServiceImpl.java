package punto_venta.sombrilla_verde.service.compra.compra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import punto_venta.sombrilla_verde.model.entity.compra.CompraEntity;
import punto_venta.sombrilla_verde.repository.compra.CompraRepository;

import java.util.List;

@Service
public class ComprasServiceImpl implements CompraService{
    @Autowired
    private CompraRepository compraRepository;

    @Override
    public CompraEntity save(CompraEntity compra) {
        return compraRepository.save(compra);
    }

    @Override
    public List<CompraEntity> findAll() {
        return compraRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        compraRepository.deleteById(id);
    }

    @Override
    public CompraEntity findById(Integer id) {
        return compraRepository.findById(id).orElse(null);
    }
}
