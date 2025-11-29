package uniandes.edu.co.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uniandes.edu.co.demo.modelo.Servicio;
import uniandes.edu.co.demo.repository.ServicioRepository;

@RestController
@RequestMapping("/servicios")

public class ServicioController {
    @Autowired
    private ServicioRepository servicioRepository;

    
    @PostMapping("new/save")
    public ResponseEntity<Servicio> createServicio(@RequestBody Servicio servicio) {
        try {
            Servicio saved = servicioRepository.save(servicio);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/{id}/edit/save")
    public ResponseEntity<Servicio> updateServicio(@PathVariable("id") String id, @RequestBody Servicio servicio) {
        try {
            java.util.Optional<Servicio> existingOpt = servicioRepository.findById(id);
            if (!existingOpt.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Servicio existing = existingOpt.get();
            // merge non-null fields from incoming servicio into existing
            if (servicio.getUsuario_id() != null)
                existing.setUsuario_id(servicio.getUsuario_id());
            if (servicio.getConductor_id() != null)
                existing.setConductor_id(servicio.getConductor_id());
            if (servicio.getVehiculo_id() != null)
                existing.setVehiculo_id(servicio.getVehiculo_id());
            if (servicio.getTipoServicio() != null)
                existing.setTipoServicio(servicio.getTipoServicio());
            if (servicio.getNivel() != null)
                existing.setNivel(servicio.getNivel());
            if (servicio.getOrigen() != null)
                existing.setOrigen(servicio.getOrigen());
            if (servicio.getDestino() != null)
                existing.setDestino(servicio.getDestino());
            if (servicio.getDistancia_km() != null)
                existing.setDistancia_km(servicio.getDistancia_km());
            if (servicio.getCosto() != null)
                existing.setCosto(servicio.getCosto());
            if (servicio.getHoraInicio() != null)
                existing.setHoraInicio(servicio.getHoraInicio());
            if (servicio.getHoraFin() != null)
                existing.setHoraFin(servicio.getHoraFin());

            Servicio saved = servicioRepository.save(existing);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    @GetMapping("")
    public ResponseEntity<?> getAllServicios() {
        try {
            List<Servicio> servicios = servicioRepository.buscarTodosServicios();
            return ResponseEntity.ok(servicios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> getServicioById(@PathVariable("id") String id) {
        try {
            java.util.Optional<Servicio> servicio = servicioRepository.findById(id);
            if (servicio.isPresent()) {
                return ResponseEntity.ok(servicio.get());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteServicio(@PathVariable("id") String id) {
        try {
            servicioRepository.eliminarServicioId(id);
            return ResponseEntity.ok("Servicio eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
