package punto_venta.sombrilla_verde.model.entity.compra;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;

import java.math.BigDecimal;

@Entity(name = "detalle_compra")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetallesCompraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_compra", nullable = false)
    private CompraEntity compra;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @Column(name = "cantidad", nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidad;

    @Column(name = "costo_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoUnitario;

    @Column(name = "total_linea", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalLinea;
}
