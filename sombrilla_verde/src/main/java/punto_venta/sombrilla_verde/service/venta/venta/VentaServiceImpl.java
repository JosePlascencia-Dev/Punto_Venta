package punto_venta.sombrilla_verde.service.venta.venta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;
import punto_venta.sombrilla_verde.repository.venta.VentaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Override
    public Long countVentasDelDia() {
        return ventaRepository.countVentasDelDia();
    }

    @Override
    public BigDecimal totalVentasDelDia() {
        return ventaRepository.totalVentasDelDia();
    }

    @Override
    public BigDecimal calcularGananciaDelDia() {
        return ventaRepository.calcularGananciaDelDia();
    }

    @Override
    public BigDecimal totalVentasDesdeFecha(Integer proveedorId, LocalDateTime fechaInicio) {
        return ventaRepository.totalVentasDesdeFecha(proveedorId, fechaInicio);
    }

    @Override
    public BigDecimal costoVentasDesdeFecha(Integer proveedorId, LocalDateTime fechaInicio) {
        return ventaRepository.costoVentasDesdeFecha(proveedorId, fechaInicio);
    }

}
