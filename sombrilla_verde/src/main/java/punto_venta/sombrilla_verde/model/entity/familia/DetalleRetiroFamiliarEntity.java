package punto_venta.sombrilla_verde.model.entity.familia;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;

import java.math.BigDecimal;

@Entity(name = "detalle_retiro_familiar")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleRetiroFamiliarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_retiro", nullable = false)
    private RetiroFamiliarEntity retiro;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @Column(name = "cantidad", nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidad;

    @Column(name = "costo_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoUnitario;

    @Column(name = "total_linea", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalLinea;
}
