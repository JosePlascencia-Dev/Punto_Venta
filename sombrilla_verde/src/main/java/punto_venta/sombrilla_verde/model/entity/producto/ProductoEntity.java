package punto_venta.sombrilla_verde.model.entity.producto;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import punto_venta.sombrilla_verde.model.entity.proveedor.ProveedorEntity;

import java.math.BigDecimal;

@Entity(name = "productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaEntity categoria;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private ProveedorEntity proveedor;

    @Column(name = "precio_compra", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioCompra;

    @Column(name = "precio_venta", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "existencia", nullable = false, precision = 10, scale = 3, columnDefinition = "DECIMAL(10,3) UNSIGNED DEFAULT 0")
    private BigDecimal existencia = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida", nullable = false, columnDefinition = "ENUM('PIEZA', 'KILO', 'LITRO', 'PAQUETE', 'CAJA') DEFAULT 'PIEZA'")
    private UnidadMedida unidadMedida = UnidadMedida.PIEZA;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;
}
