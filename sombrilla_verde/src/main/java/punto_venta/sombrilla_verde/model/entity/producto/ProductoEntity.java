package punto_venta.sombrilla_verde.model.entity.producto;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @NotBlank(message = "El codigo es obligatorio")
    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaEntity categoria;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private ProveedorEntity proveedor;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de compra debe ser mayor que cero")
    @NotNull(message = "La existencia no puede ser nula")
    @Column(name = "precio_compra", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioCompra;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor que cero")
    @NotNull(message = "La existencia no puede ser nula")
    @Column(name = "precio_venta", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;

    @DecimalMin(value = "0.0", inclusive = true, message = "La existencia no puede ser negativa")
    @NotNull(message = "La existencia no puede ser nula")
    @Column(name = "existencia", nullable = false, precision = 10, scale = 3, columnDefinition = "DECIMAL(10,3) UNSIGNED DEFAULT 0")
    private BigDecimal existencia = BigDecimal.ZERO;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida", nullable = false, columnDefinition = "ENUM('PIEZA', 'KILO', 'LITRO', 'PAQUETE', 'CAJA') DEFAULT 'PIEZA'")
    private UnidadMedida unidadMedida = UnidadMedida.PIEZA;

    @NotNull
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;
}
