package punto_venta.sombrilla_verde.service.usuario;

import punto_venta.sombrilla_verde.model.entity.usuario.UsuarioEntity;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    UsuarioEntity save(UsuarioEntity usuario);
    List<UsuarioEntity> findAll();
    void deleteById(Integer id);
    UsuarioEntity findById(Integer id);
    Optional<UsuarioEntity> findByNombreUsuario(String nombreUsuario);
}
