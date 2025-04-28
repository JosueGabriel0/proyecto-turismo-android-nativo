package pe.edu.upeu.turismospringboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.LugarTuristicoDto;
import pe.edu.upeu.turismospringboot.model.entity.LugarTuristico;
import pe.edu.upeu.turismospringboot.repository.LugarTuristicoRepository;
import pe.edu.upeu.turismospringboot.service.LugarTuristicoService;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class LugarTuristicoServiceImpl implements LugarTuristicoService {
    @Autowired
    private LugarTuristicoRepository lugarTuristicoRepository;

    @Override
    public List<LugarTuristico> getLugaresTuristicos() {
        return lugarTuristicoRepository.findAll();
    }

    @Override
    public LugarTuristico getLugarTuristicoById(Long idLugarTuristico) {
        return lugarTuristicoRepository.findById(idLugarTuristico).orElseThrow(() -> new RuntimeException("Lugar Turistico con id: "+idLugarTuristico+" no encontrado"));
    }

    @Override
    public LugarTuristico postLugarTuristico(LugarTuristicoDto lugarTuristicoDto, MultipartFile file) {
        LugarTuristico lugarTuristico = new LugarTuristico();
        lugarTuristico.setNombre(lugarTuristicoDto.getNombre());
        lugarTuristico.setDescripcion(lugarTuristicoDto.getDescripcion());
        lugarTuristico.setUbicacion(lugarTuristicoDto.getUbicacion());
        lugarTuristico.setImagenUrl(lugarTuristicoDto.getImagenUrl());

        // Si hay un archivo, guardarlo y actualizar la URL de la imagen
        if (file != null && !file.isEmpty()) {
            String fileName = saveFile(file); // Método para guardar el archivo
            lugarTuristico.setImagenUrl(fileName); // Asumimos que el archivo es una imagen
        }

        return lugarTuristicoRepository.save(lugarTuristico);
    }

    @Override
    public LugarTuristico putLugarTuristico(Long idLugar, LugarTuristicoDto lugarTuristicoDto, MultipartFile file) {
        // Buscar el lugar turístico existente por ID
        LugarTuristico lugarTuristicoExistente = lugarTuristicoRepository.findById(idLugar)
                .orElseThrow(() -> new RuntimeException("Lugar turístico no encontrado"));

        // Actualizar los campos del lugar turístico
        lugarTuristicoExistente.setNombre(lugarTuristicoDto.getNombre());
        lugarTuristicoExistente.setDescripcion(lugarTuristicoDto.getDescripcion());
        lugarTuristicoExistente.setUbicacion(lugarTuristicoDto.getUbicacion());

        // Si hay un archivo, guardarlo y actualizar la URL de la imagen
        if (file != null && !file.isEmpty()) {
            String fileName = saveFile(file); // Método para guardar el archivo
            lugarTuristicoExistente.setImagenUrl(fileName); // Asumimos que el archivo es una imagen
        }

        // Guardar los cambios en la base de datos
        return lugarTuristicoRepository.save(lugarTuristicoExistente);
    }


    @Override
    public void deleteLugarTuristico(Long idLugarTuristico) {
        lugarTuristicoRepository.deleteById(idLugarTuristico);
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
