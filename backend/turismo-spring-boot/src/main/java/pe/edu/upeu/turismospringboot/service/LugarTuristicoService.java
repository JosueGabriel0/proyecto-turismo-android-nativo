package pe.edu.upeu.turismospringboot.service;

import org.springframework.web.multipart.MultipartFile;
import pe.edu.upeu.turismospringboot.model.dto.LugarTuristicoDto;
import pe.edu.upeu.turismospringboot.model.entity.LugarTuristico;

import java.util.List;

public interface LugarTuristicoService {
    public List<LugarTuristico> getLugaresTuristicos();
    public LugarTuristico getLugarTuristicoById(Long idLugarTuristico);
    public LugarTuristico postLugarTuristico(LugarTuristicoDto lugarTuristicoDto, MultipartFile multipartFile);
    public LugarTuristico putLugarTuristico(Long idLugarTuristico, LugarTuristicoDto lugarTuristicoDto, MultipartFile multipartFile);
    public void deleteLugarTuristico(Long idLugarTuristico);
}
