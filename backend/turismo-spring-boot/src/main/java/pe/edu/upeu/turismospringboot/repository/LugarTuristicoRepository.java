package pe.edu.upeu.turismospringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.turismospringboot.model.entity.LugarTuristico;

import java.util.Optional;

@Repository
public interface LugarTuristicoRepository extends JpaRepository<LugarTuristico, Long> {
    Optional<LugarTuristico> findByNombre(String nombre);
}