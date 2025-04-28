package pe.edu.upeu.turismospringboot.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "emprendimiento")
@Data
public class Emprendimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmprendimiento;

    private String nombre;
    private String descripcion;
    private String contacto;
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    @JsonBackReference
    private Categoria categoria;

    @OneToOne(mappedBy = "emprendimiento", cascade = CascadeType.ALL)
    @JsonBackReference
    private Hotel hotel;

    @OneToMany(mappedBy = "emprendimiento", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Resena> resenas = new ArrayList<>();

    private LocalDateTime fechaCreacionEmprendimiento;
    private LocalDateTime fechaModificacionEmprendimiento;

    @PrePersist
    public void onCreate(){
        fechaCreacionEmprendimiento = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        fechaModificacionEmprendimiento = LocalDateTime.now();
    }
}