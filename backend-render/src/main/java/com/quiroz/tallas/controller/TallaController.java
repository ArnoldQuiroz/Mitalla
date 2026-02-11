package com.quiroz.tallas.controller;

import com.quiroz.tallas.model.Talla;
import com.quiroz.tallas.service.TallaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tallas")
@CrossOrigin(origins = "*")
public class TallaController {
    @Autowired
    private TallaService tallaService;

    @GetMapping
    public ResponseEntity<List<Talla>> listar() {
        return ResponseEntity.ok(tallaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Talla> detalle(@PathVariable Integer id) {
        Optional<Talla> talla = tallaService.buscar(id);
        return talla.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Talla> crear(@RequestBody Talla talla) {
        talla.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(tallaService.guardar(talla));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Talla> editar(@PathVariable Integer id, @RequestBody Talla talla) {
        if (!tallaService.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        talla.setId(id);
        return ResponseEntity.ok(tallaService.guardar(talla));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!tallaService.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        tallaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
