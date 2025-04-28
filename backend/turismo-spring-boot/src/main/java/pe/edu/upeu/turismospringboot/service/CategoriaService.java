package pe.edu.upeu.turismospringboot.service;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.CategoriaDto;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;

import java.util.List;

public interface CategoriaService {
    public List<Categoria> getCategorias();
    public Categoria getCategoriaById(Long id);
    public Categoria postCategoria(CategoriaDto categoria, MultipartFile multipartFile);
    public Categoria putCategoria(Long idCategoria, CategoriaDto categoria, MultipartFile multipartFile);
    public void deleteCategoria(Long idCategoria);
}
