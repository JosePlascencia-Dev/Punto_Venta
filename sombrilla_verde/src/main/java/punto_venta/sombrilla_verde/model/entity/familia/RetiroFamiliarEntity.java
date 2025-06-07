package punto_venta.sombrilla_verde.model.entity.familia;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import punto_venta.sombrilla_verde.model.entity.usuario.UsuarioEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "retiros_familiares")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetiroFamiliarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_retiro")
    private Integer id;

    @CreationTimestamp
    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "motivo", nullable = false)
    private String motivo;

    @Column(name = "total_costo", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCosto;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @OneToMany(mappedBy = "retiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleRetiroFamiliarEntity> detalles;
}
