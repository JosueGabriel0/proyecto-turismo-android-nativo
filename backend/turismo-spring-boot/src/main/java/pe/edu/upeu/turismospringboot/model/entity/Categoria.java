package pe.edu.upeu.turismospringboot.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria")
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    @Column(nullable = false, unique = true)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_lugar_turistico", nullable = false)
    @JsonBackReference
    private LugarTuristico lugarTuristico;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Emprendimiento> emprendimientos = new ArrayList<>();

    private LocalDateTime fechaCreacionCategoria;
    private LocalDateTime fechaModificacionCategoria;

    @PrePersist
    public void onCreate(){
        fechaCreacionCategoria = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        fechaModificacionCategoria = LocalDateTime.now();
    }
}