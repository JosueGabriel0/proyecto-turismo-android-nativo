package pe.edu.upeu.turismospringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.CategoriaDto;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.service.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/admin/categoria")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerCategorias() {
        return ResponseEntity.ok().body(categoriaService.getCategorias());
    }

    @GetMapping("/{idCategoria}")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long idCategoria) {
        return ResponseEntity.ok().body(categoriaService.getCategoriaById(idCategoria));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Categoria> guardarCategoria(@RequestPart("categoria") String categoriaJson,
                                                      @RequestPart(value = "file", required = false) MultipartFile file) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            CategoriaDto categoriaDto = objectMapper.readValue(categoriaJson, CategoriaDto.class);

            Categoria categoriaCreada = categoriaService.postCategoria(categoriaDto, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCreada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(consumes = {"multipart/form-data"}, value = "/{idCategoria}")
    public ResponseEntity<Categoria> actualizarCategoria(
            @PathVariable Long idCategoria,
            @RequestPart("categoria") String categoriaJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            CategoriaDto categoriaDto = objectMapper.readValue(categoriaJson, CategoriaDto.class);

            Categoria categoriaCreada = categoriaService.putCategoria(idCategoria, categoriaDto, file);

            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCreada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{idCategoria}")
    public ResponseEntity<String> eliminarCategoria(@PathVariable Long idCategoria) {
        try{
            categoriaService.deleteCategoria(idCategoria);
            return ResponseEntity.ok().body("Categoria eliminada exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar categoria");
        }
    }
}