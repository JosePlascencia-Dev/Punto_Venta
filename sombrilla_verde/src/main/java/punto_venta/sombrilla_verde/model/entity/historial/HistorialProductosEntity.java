package punto_venta.sombrilla_verde.model.entity.historial;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;
import punto_venta.sombrilla_verde.model.entity.usuario.UsuarioEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "historial_productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialProductosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @CreationTimestamp
    @Column(name = "fecha_movimiento", nullable = false, updatable = false)
    private LocalDateTime fechaMovimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, columnDefinition = "ENUM('COMPRA', 'VENTA', 'AJUSTE', 'FAMILIAR')")
    private TipoMovimiento tipoMovimiento;

    @Column(name = "cantidad", nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidad;

    @Column(name = "existencia_actual", nullable = false, precision = 10, scale = 3)
    private BigDecimal existenciaActual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(columnDefinition = "TEXT")
    private String notas;

}
