package punto_venta.sombrilla_verde.service.producto.producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;
import punto_venta.sombrilla_verde.repository.producto.ProductoRepository;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public ProductoEntity save(ProductoEntity provedor) {
        return productoRepository.save(provedor);
    }

    @Override
    public List<ProductoEntity> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        productoRepository.deleteById(id);
    }

    @Override
    public ProductoEntity findById(Integer id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public List<ProductoEntity> findAllOrderByExistenciaAsc() {
        return productoRepository.findAllOrderByExistenciaAsc();
    }

    @Override
    public ProductoEntity findByNombre(String nombre) {
        return productoRepository.findByNombre(nombre);
    }

    @Override
    public ProductoEntity findByCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo);
    }

    @Override
    public List<ProductoEntity> buscarPorCategoriaYProveedor(Integer categoriaId, Integer proveedorId) {
        return productoRepository.buscarPorCategoriaYProveedor(categoriaId, proveedorId);
    }
}
