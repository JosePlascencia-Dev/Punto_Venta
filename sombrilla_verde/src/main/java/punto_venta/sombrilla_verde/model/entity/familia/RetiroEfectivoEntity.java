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

@Entity(name = "retiros_efectivo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetiroEfectivoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_retiro")
    private Integer id;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "motivo", nullable = false)
    private String motivo;

    @CreationTimestamp
    @Column(name = "fecha", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;
}
