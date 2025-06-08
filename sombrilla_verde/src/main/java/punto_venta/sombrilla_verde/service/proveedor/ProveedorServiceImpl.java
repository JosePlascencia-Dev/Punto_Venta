package punto_venta.sombrilla_verde.service.proveedor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import punto_venta.sombrilla_verde.model.entity.proveedor.ProveedorEntity;
import punto_venta.sombrilla_verde.repository.proveedor.ProveedorRepository;

import java.util.List;

@Service
public class ProveedorServiceImpl implements ProveedorService {
    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public ProveedorEntity save(ProveedorEntity provedor) {
        return proveedorRepository.save(provedor);
    }

    @Override
    public List<ProveedorEntity> findAll() {
        return proveedorRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        proveedorRepository.deleteById(id);
    }

    @Override
    public ProveedorEntity findById(Integer id) {
        return proveedorRepository.findById(id).orElse(null);
    }
}
