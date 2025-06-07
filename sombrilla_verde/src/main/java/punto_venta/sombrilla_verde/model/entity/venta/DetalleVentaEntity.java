package punto_venta.sombrilla_verde.model.entity.venta;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;

import java.math.BigDecimal;

@Entity(name = "detalle_venta")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleVentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private VentaEntity venta;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @Column(name = "cantidad", nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "total_linea", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalLinea;
}
