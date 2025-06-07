package punto_venta.sombrilla_verde.model.entity.proveedor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "proveedores")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProveedorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer id;

    @Column(name = "nombre_empresa", nullable = false, unique = true)
    private String nombreEmpresa;

    @Column(name = "contacto_principal")
    private String contactoPrincipal;

    @Column(name = "telefono", unique = true)
    private String telefono;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "activo", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
}
