package pe.edu.upeu.turismospringboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.CategoriaDto;
import pe.edu.upeu.turismospringboot.model.entity.Categoria;
import pe.edu.upeu.turismospringboot.model.entity.LugarTuristico;
import pe.edu.upeu.turismospringboot.repository.CategoriaRepository;
import pe.edu.upeu.turismospringboot.repository.LugarTuristicoRepository;
import pe.edu.upeu.turismospringboot.service.CategoriaService;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private LugarTuristicoRepository lugarTuristicoRepository;

    @Override
    public List<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria getCategoriaById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("No existe la categoria con el id: " + id));
    }

    @Override
    public Categoria postCategoria(CategoriaDto categoria, MultipartFile file) {
        LugarTuristico lugarTuristico = lugarTuristicoRepository.findByNombre(categoria.getNombreLugar()).orElseThrow(() -> new RuntimeException("No se encontro el Lugar con nombre: "+categoria.getNombreLugar()));
        Categoria categoriaEntity = new Categoria();
        categoriaEntity.setNombre(categoria.getNombre());
        categoriaEntity.setLugarTuristico(lugarTuristico);

        // Si hay un archivo, guardarlo y actualizar la URL de la imagen
        if (file != null && !file.isEmpty()) {
            String fileName = saveFile(file); // Método para guardar el archivo
            categoriaEntity.setImagenUrl(fileName); // Asumimos que el archivo es una imagen
        }

        return categoriaRepository.save(categoriaEntity);
    }

    @Override
    public Categoria putCategoria(Long idCategoria, CategoriaDto categoria, MultipartFile file) {
        LugarTuristico lugarTuristico = lugarTuristicoRepository.findByNombre(categoria.getNombreLugar()).orElseThrow(() -> new RuntimeException("No se encontro el Lugar con nombre: "+categoria.getNombreLugar()));
        Categoria categoriaEncontrada = categoriaRepository.findById(idCategoria).orElseThrow(() -> new RuntimeException("No existe la categoria con el id: " + idCategoria));
        System.out.println("ID de la Categoria encontrada: " + categoriaEncontrada.getIdCategoria());
        System.out.println("ID del LugarTuristico asociado: " + lugarTuristico.getIdLugar());
        categoriaEncontrada.setNombre(categoria.getNombre());
        categoriaEncontrada.setLugarTuristico(lugarTuristico);
        // Si hay un archivo, guardarlo y actualizar la URL de la imagen
        if (file != null && !file.isEmpty()) {
            String fileName = saveFile(file); // Método para guardar el archivo
            categoriaEncontrada.setImagenUrl(fileName); // Asumimos que el archivo es una imagen
        }
        return categoriaRepository.save(categoriaEncontrada);
    }

    @Override
    public void deleteCategoria(Long idCategoria) {
        categoriaRepository.deleteById(idCategoria);
    }

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/upload/";
    private String saveFile(MultipartFile file) {
        try {
            File uploadPath = new File(UPLOAD_DIR);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destinationFile = new File(uploadPath, fileName);
            file.transferTo(destinationFile);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }
}
