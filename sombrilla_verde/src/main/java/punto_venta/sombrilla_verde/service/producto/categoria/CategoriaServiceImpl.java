package punto_venta.sombrilla_verde.service.producto.categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import punto_venta.sombrilla_verde.model.entity.producto.CategoriaEntity;
import punto_venta.sombrilla_verde.repository.producto.CategoriaRepository;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public CategoriaEntity save(CategoriaEntity provedor) {
        return categoriaRepository.save(provedor);
    }

    @Override
    public List<CategoriaEntity> findAll() {
        return categoriaRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        categoriaRepository.deleteById(id);
    }

    @Override
    public CategoriaEntity findById(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }
}
