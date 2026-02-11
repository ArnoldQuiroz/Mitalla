package com.quiroz.tallas.service;

import com.quiroz.tallas.model.Talla;
import com.quiroz.tallas.repository.TallaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TallaService {
    @Autowired
    private TallaRepository tallaRepository;

    public List<Talla> listar() {
        return tallaRepository.findAll();
    }

    public Optional<Talla> buscar(Integer id) {
        return tallaRepository.findById(id);
    }

    public Talla guardar(Talla talla) {
        if (talla.getActivo() == null) {
            talla.setActivo(true);
        }
        return tallaRepository.save(talla);
    }

    public void eliminar(Integer id) {
        tallaRepository.deleteById(id);
    }

    public boolean existe(Integer id) {
        return tallaRepository.existsById(id);
    }
}
