package punto_venta.sombrilla_verde.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import punto_venta.sombrilla_verde.model.entity.usuario.UsuarioEntity;
import punto_venta.sombrilla_verde.repository.usuario.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UsuarioEntity save(UsuarioEntity usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<UsuarioEntity> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioEntity findById(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<UsuarioEntity> findByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }
}
