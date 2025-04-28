package pe.edu.upeu.turismospringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upeu.turismospringboot.model.dto.LugarTuristicoDto;
import pe.edu.upeu.turismospringboot.model.entity.LugarTuristico;
import pe.edu.upeu.turismospringboot.service.LugarTuristicoService;

import java.util.List;

@RestController
@RequestMapping("/admin/lugarTuristico")
public class LugarTuristicoController {
    @Autowired
    private LugarTuristicoService lugarTuristicoService;

    @GetMapping
    public ResponseEntity<List<LugarTuristico>> listarLugarTuristico() {
        return ResponseEntity.ok().body(lugarTuristicoService.getLugaresTuristicos());
    }

    @GetMapping("/{idLugarTuristico}")
    public ResponseEntity<LugarTuristico> listarLugarturisticoPorId(@PathVariable Long idLugarTuristico) {
        return ResponseEntity.ok().body(lugarTuristicoService.getLugarTuristicoById(idLugarTuristico));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<LugarTuristico> crearLugarTuristico(
            @RequestPart("lugarTuristico") String lugarTuristicoJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // Deserializamos el JSON recibido
            ObjectMapper objectMapper = new ObjectMapper();
            LugarTuristicoDto lugarTuristicoDto = objectMapper.readValue(lugarTuristicoJson, LugarTuristicoDto.class);

            // Llamamos al servicio para crear el lugar turístico
            LugarTuristico lugarTuristicoCreado = lugarTuristicoService.postLugarTuristico(lugarTuristicoDto, file);

            return ResponseEntity.status(HttpStatus.CREATED).body(lugarTuristicoCreado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(consumes = {"multipart/form-data"}, value = "/{idLugarTuristico}")
    public ResponseEntity<LugarTuristico> actualizarLugarTuristico(
            @PathVariable Long idLugarTuristico,
            @RequestPart("lugarTuristico") String lugarTuristicoJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // Deserializamos el JSON recibido
            ObjectMapper objectMapper = new ObjectMapper();
            LugarTuristicoDto lugarTuristicoDto = objectMapper.readValue(lugarTuristicoJson, LugarTuristicoDto.class);

            // Llamamos al servicio para actualizar el lugar turístico
            LugarTuristico lugarTuristicoActualizado = lugarTuristicoService.putLugarTuristico(idLugarTuristico, lugarTuristicoDto, file);

            return ResponseEntity.ok().body(lugarTuristicoActualizado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{idLugarTuristico}")
    public ResponseEntity<String> eliminarLugarTuristico(@PathVariable Long idLugarTuristico) {
        try{
            lugarTuristicoService.deleteLugarTuristico(idLugarTuristico);
            return ResponseEntity.ok().body("LugarTuristico eliminado");
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el lugarTuristico");
        }
    }
}
