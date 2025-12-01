package uniandes.edu.co.demo.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

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

import uniandes.edu.co.demo.modelo.Disponibilidad;

import uniandes.edu.co.demo.modelo.Vehiculo;
import uniandes.edu.co.demo.repository.VehiculoRepository;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {
    @Autowired
    private VehiculoRepository vehiculoRepository;

    @PostMapping("new/save")
    public ResponseEntity<String> createVehiculo(@RequestBody Vehiculo vehiculo) {
        try {
            // validar y computar nivel
            String nivel = computeNivel(vehiculo);
            vehiculo.setNivel(nivel);
            String validationError = validateDisponibilidades(vehiculo, null);
            if (validationError != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationError);
            }
            vehiculoRepository.insertarVehiculo(vehiculo);
            return ResponseEntity.ok("Vehiculo creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/{id}/edit/save")
    public ResponseEntity<String> updateVehiculo(@PathVariable("id") String id, @RequestBody Vehiculo vehiculo) {
        try {
            // computar nivel y validar disponibilidades (excluir este vehículo)
            String nivel = computeNivel(vehiculo);
            vehiculo.setNivel(nivel);
            String validationError = validateDisponibilidades(vehiculo, id);
            if (validationError != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationError);
            }

            vehiculoRepository.actualizarVehiculo(
                    id,
                    vehiculo.getConductor_id(),
                    vehiculo.getTipo(),
                    vehiculo.getMarca(),
                    vehiculo.getModelo(),
                    vehiculo.getColor(),
                    vehiculo.getPlaca(),
                    vehiculo.getCiudad(),
                    vehiculo.getCapacidad(),
                    vehiculo.getNivel(),
                    vehiculo.getDisponibilidad());
            return ResponseEntity.ok("Vehiculo actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // computar nivel basado en capacidad y tipo de servicio
    private String computeNivel(Vehiculo v) {
        if (v == null) return null;
        // Determinar si hay disponibilidad que indica el pasajero
        String tipo = null;
        if (v.getDisponibilidad() != null) {
            for (Disponibilidad dis : v.getDisponibilidad()) {
                if (dis != null && dis.getTipoServicio() != null) {
                    String t = dis.getTipoServicio().toLowerCase();
                    if (t.contains("transporte") || t.contains("pasajero") || t.contains("pasajeros")) {
                        tipo = t;
                        break;
                    }
                }
            }
        }

        Integer capacidad = v.getCapacidad() == null ? 0 : v.getCapacidad().intValue();
        String modelo = v.getModelo() == null ? "" : v.getModelo().toLowerCase();

        // Only determine level for passenger transport; otherwise keep provided
        if (tipo != null) {
            // simple rules (can be tuned):
            // - Large: capacidad > 6 OR modelo contains 'van' or 'sprinter' or 'minibus'
            // - Confort: capacidad 5-6 OR modelo contains 'suv' or 'xl'
            // - Estándar: capacidad <=4 otherwise
            if (capacidad > 6 || modelo.contains("van") || modelo.contains("sprinter") || modelo.contains("minibus"))
                return "Large";
            if (capacidad >= 5 || modelo.contains("suv") || modelo.contains("xl") || modelo.contains("prestige"))
                return "Confort";
            return "Estándar";
        }

        // Not passenger transport: return existing nivel or default empty
        return v.getNivel() == null ? "" : v.getNivel();
    }

    // Validate that no availability overlaps for the conductor across vehicles
    // excludeVehicleId: when updating, exclude the vehicle being updated
    private String validateDisponibilidades(Vehiculo vehiculo, String excludeVehicleId) {
        if (vehiculo == null) return null;
        String conductorId = vehiculo.getConductor_id();
        List<Disponibilidad> nuevas = vehiculo.getDisponibilidad() == null ? new ArrayList<>() : vehiculo.getDisponibilidad();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("H:mm");

        // Validate intra-vehicle (no overlaps inside the same vehicle)
        for (int i = 0; i < nuevas.size(); i++) {
            Disponibilidad a = nuevas.get(i);
            LocalTime aStart;
            LocalTime aEnd;
            try {
                aStart = LocalTime.parse(a.getHoraInicio(), fmt);
                aEnd = LocalTime.parse(a.getHoraFin(), fmt);
            } catch (Exception ex) {
                return "Formato de hora inválido en disponibilidad: use HH:mm";
            }
            if (!aStart.isBefore(aEnd)) return "Hora inicio debe ser anterior a hora fin en una disponibilidad";
            for (int j = i + 1; j < nuevas.size(); j++) {
                Disponibilidad b = nuevas.get(j);
                if (a.getDia() == null || b.getDia() == null) continue;
                if (!a.getDia().equalsIgnoreCase(b.getDia())) continue;
                LocalTime bStart = LocalTime.parse(b.getHoraInicio(), fmt);
                LocalTime bEnd = LocalTime.parse(b.getHoraFin(), fmt);
                if (timesOverlap(aStart, aEnd, bStart, bEnd))
                    return "Hay disponibilidades solapadas dentro del mismo vehículo para el día " + a.getDia();
            }
        }

        // Validate against other vehicles of same conductor
        if (conductorId != null && !conductorId.isEmpty()) {
            List<Vehiculo> otros = vehiculoRepository.findByConductorId(conductorId);
            if (otros != null) {
                for (Vehiculo otro : otros) {
                    if (excludeVehicleId != null && excludeVehicleId.equals(otro.getId())) continue;
                    List<Disponibilidad> exist = otro.getDisponibilidad() == null ? new ArrayList<>() : otro.getDisponibilidad();
                    for (Disponibilidad a : nuevas) {
                        for (Disponibilidad b : exist) {
                            if (a.getDia() == null || b.getDia() == null) continue;
                            if (!a.getDia().equalsIgnoreCase(b.getDia())) continue;
                            LocalTime aStart;
                            LocalTime aEnd;
                            LocalTime bStart;
                            LocalTime bEnd;
                            try {
                                aStart = LocalTime.parse(a.getHoraInicio(), fmt);
                                aEnd = LocalTime.parse(a.getHoraFin(), fmt);
                                bStart = LocalTime.parse(b.getHoraInicio(), fmt);
                                bEnd = LocalTime.parse(b.getHoraFin(), fmt);
                            } catch (Exception ex) {
                                return "Formato de hora inválido en disponibilidades existentes o nuevas: use HH:mm";
                            }
                            if (timesOverlap(aStart, aEnd, bStart, bEnd))
                                return "Disponibilidad solapada con otro vehículo (id=" + otro.getId() + ") para el día " + a.getDia();
                        }
                    }
                }
            }
        }

        return null;
    }

    private boolean timesOverlap(LocalTime s1, LocalTime e1, LocalTime s2, LocalTime e2) {
        return s1.isBefore(e2) && s2.isBefore(e1);
    }
    @GetMapping("")
    public ResponseEntity<?> getAllVehiculo() {
        try {
            List<Vehiculo> vehiculos = vehiculoRepository.buscarTodosVehiculos();
            return ResponseEntity.ok(vehiculos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> getVehiculoId(@PathVariable("id") String id) {
        try {
            java.util.Optional<Vehiculo> vehiculo = vehiculoRepository.findById(id);
            if (vehiculo.isPresent()) {
                return ResponseEntity.ok(vehiculo.get());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteVehiculo(@PathVariable("id") String id) {
        try {
            vehiculoRepository.eliminarVehiculoId(id);
            return ResponseEntity.ok("Vehiculo eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

    


