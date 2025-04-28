package pe.edu.upeu.turismospringboot.model.dto;

import lombok.Data;

@Data
public class LugarTuristicoDto {
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private String imagenUrl;
}
