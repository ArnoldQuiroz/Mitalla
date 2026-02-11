package com.quiroz.tallas.repository;

import com.quiroz.tallas.model.Talla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TallaRepository extends JpaRepository<Talla, Integer> {
}
