package punto_venta.sombrilla_verde.service.usuario;

import punto_venta.sombrilla_verde.model.entity.usuario.UsuarioEntity;

import java.util.List;

public interface UsuarioService {
    UsuarioEntity save(UsuarioEntity usuario);
    List<UsuarioEntity> findAll();
    void deleteById(Integer id);
    UsuarioEntity findById(Integer id);
}
