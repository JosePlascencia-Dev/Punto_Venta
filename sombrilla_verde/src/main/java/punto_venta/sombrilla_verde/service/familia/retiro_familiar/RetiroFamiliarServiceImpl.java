package punto_venta.sombrilla_verde.service.familia.retiro_familiar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroFamiliarEntity;
import punto_venta.sombrilla_verde.repository.familia.RetiroFamiliarRepository;

import java.util.List;

@Service
public class RetiroFamiliarServiceImpl implements RetiroFamiliarService {
    @Autowired
    private RetiroFamiliarRepository retiroFamiliarRepository;

    @Override
    public RetiroFamiliarEntity save(RetiroFamiliarEntity retiroEfectivo) {
        return retiroFamiliarRepository.save(retiroEfectivo);
    }

    @Override
    public List<RetiroFamiliarEntity> findAll() {
        return retiroFamiliarRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        retiroFamiliarRepository.deleteById(id);
    }

    @Override
    public RetiroFamiliarEntity findById(Integer id) {
        return retiroFamiliarRepository.findById(id).orElse(null);
    }
}
