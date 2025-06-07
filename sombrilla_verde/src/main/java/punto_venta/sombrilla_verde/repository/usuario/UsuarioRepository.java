package punto_venta.sombrilla_verde.repository.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import punto_venta.sombrilla_verde.model.entity.usuario.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
}
