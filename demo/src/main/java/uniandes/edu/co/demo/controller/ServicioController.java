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
import uniandes.edu.co.demo.modelo.Punto;
import uniandes.edu.co.demo.modelo.Vehiculo;
import uniandes.edu.co.demo.repository.VehiculoRepository;
import uniandes.edu.co.demo.repository.UsuarioRepository;
import uniandes.edu.co.demo.modelo.Usuario;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/servicios")

public class ServicioController {
    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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
    public ResponseEntity<String> updateServicio(@PathVariable("id") String id, @RequestBody Servicio servicio) {
        try {
            servicioRepository.actualizarServicio(
                    id,
                    servicio.getUsuario_id(),
                    servicio.getConductor_id(),
                    servicio.getVehiculo_id(),
                    servicio.getTipoServicio(),
                    servicio.getNivel(),
                    servicio.getOrigen(),
                    servicio.getDestino(),
                    servicio.getDistancia_km(),
                    servicio.getCosto(),
                    servicio.getHoraInicio(),
                    servicio.getHoraFin());
            return ResponseEntity.ok("Servicio actualizado exitosamente");
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

    @PostMapping("/request")
    public ResponseEntity<?> requestServicio(@RequestBody java.util.Map<String, Object> req) {
        try {
            // Conseguimos datos necesarios
            String usuarioId = (String) req.get("usuario_id");
            String tipoServicio = (String) req.get("tipoServicio");
            if (tipoServicio == null) tipoServicio = "pasajero";
            String nivel = (String) req.get("nivel");

            // Punto origen
            java.util.Map<String, Object> origenMap = (java.util.Map<String, Object>) req.get("origen");
            if (origenMap == null)
                return ResponseEntity.badRequest().body("origen requerido");
            Punto origen = parsePoint(origenMap);

            // Puntos destino ES UN ARRAY
            java.util.List<Punto> destinos = new java.util.ArrayList<>();
            Object destMap = req.get("destino");
            if (destMap != null && destMap instanceof java.util.List) {
                for (Object d : (java.util.List<?>) destMap) {
                    if (d instanceof java.util.Map)
                        destinos.add(parsePoint((java.util.Map<String, Object>) d));
                }
            }
            if (destinos.isEmpty())
                destinos.add(origen); // default destino = origen if not provided

            // Calcular distancia
            double km = haversine(origen.getLatitud(), origen.getLongitud(),
                    destinos.get(0).getLatitud(), destinos.get(0).getLongitud());

            // Calcular costo
            Number costo = calcularCosto(km, nivel);

            // Buscar vehiculo disponible
            List<Vehiculo> vehiculos = vehiculoRepository.buscarTodosVehiculos();
            Vehiculo asignado = null;
            for (Vehiculo v : vehiculos) {
                if (v.getCiudad() == null || !v.getCiudad().equalsIgnoreCase(origen.getCiudad()))
                    continue;
                if (!hasAvailability(v, tipoServicio))
                    continue;
                // Mirar si tiene servicios activos
                List<Servicio> activos = servicioRepository.findAll().stream()
                        .filter(s -> s.getConductor_id() != null && s.getConductor_id().equals(v.getConductor_id())
                                && s.getHoraFin() == null)
                        .toList();
                if (activos.isEmpty()) {
                    asignado = v;
                    break;
                }
            }

            if (asignado == null)
                return ResponseEntity.status(HttpStatus.CONFLICT).body("No hay conductores disponibles");

            // Validar nivel de servicio
            if (nivel != null && !nivel.equalsIgnoreCase(asignado.getNivel())) {
                return ResponseEntity.badRequest().body("Nivel solicitado no disponible en este vehículo (disponible: " + asignado.getNivel() + ")");
            }

            // Crear servicio
            Servicio s = new Servicio();
            s.setUsuario_id(usuarioId);
            s.setConductor_id(asignado.getConductor_id());
            s.setVehiculo_id(asignado.getId());
            s.setTipoServicio(tipoServicio);
            s.setNivel(asignado.getNivel());
            s.setOrigen(origen);
            s.setDestino(destinos);
            s.setDistancia_km(km);
            s.setCosto(costo);
            s.setHoraInicio(java.time.LocalDateTime.now());

            Servicio saved = servicioRepository.save(s);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private Punto parsePoint(java.util.Map<String, Object> m) {
        Punto p = new Punto();
        if (m.get("direccion") != null)
            p.setDireccion((String) m.get("direccion"));
        double lat = m.get("lat") != null ? ((Number) m.get("lat")).doubleValue()
                : (m.get("latitud") != null ? ((Number) m.get("latitud")).doubleValue() : 0);
        double lng = m.get("lng") != null ? ((Number) m.get("lng")).doubleValue()
                : (m.get("longitud") != null ? ((Number) m.get("longitud")).doubleValue() : 0);
        p.setLatitud(lat);
        p.setLongitud(lng);
        if (m.get("ciudad") != null)
            p.setCiudad((String) m.get("ciudad"));
        return p;
    }

    private boolean hasAvailability(Vehiculo v, String tipoServicio) {
        if (v.getDisponibilidad() == null)
            return false;
        return v.getDisponibilidad().stream()
                .anyMatch(d -> d.getTipoServicio() != null && d.getTipoServicio().equalsIgnoreCase(tipoServicio));
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371;
        double latDist = Math.toRadians(lat2 - lat1);
        double lonDist = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDist / 2) * Math.sin(latDist / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDist / 2)
                        * Math.sin(lonDist / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private Number calcularCosto(double km, String nivel) {
        double rate = 1000.0;
        if (nivel != null) {
            if (nivel.toLowerCase().contains("confort"))
                rate = 1500.0;
            else if (nivel.toLowerCase().contains("large") || nivel.toLowerCase().contains("grande"))
                rate = 2000.0;
        }
        return Math.round(rate * km);
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<?> finishServicio(@PathVariable("id") String id) {
        try {
            java.util.Optional<Servicio> opt = servicioRepository.findById(id);
            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(java.util.Map.of("error", "Servicio no encontrado"));
            }

            Servicio s = opt.get();
            if (s.getHoraFin() != null) {
                return ResponseEntity.badRequest().body("Servicio ya finalizado");
            }

            // Poner hora fin
            s.setHoraFin(java.time.LocalDateTime.now());

            // Si la distancia no está seteada, calcularla
            if (s.getDistancia_km() == null || s.getDistancia_km().doubleValue() == 0) {
                if (s.getOrigen() != null && s.getDestino() != null && !s.getDestino().isEmpty()) {
                    double km = haversine(s.getOrigen().getLatitud(), s.getOrigen().getLongitud(),
                            s.getDestino().get(0).getLatitud(), s.getDestino().get(0).getLongitud());
                    s.setDistancia_km(km);
                }
            }

            // Si el costo no está seteado, calcularlo
            if (s.getCosto() == null || s.getCosto().doubleValue() == 0) {
                double kmVal = s.getDistancia_km() != null ? s.getDistancia_km().doubleValue() : 0.0;
                Number costo = calcularCosto(kmVal, s.getNivel());
                s.setCosto(costo);
            }

            Servicio saved = servicioRepository.save(s);

            // marking conductor/vehiculo available is implicit: active services are those with horaFin == null
            // since we updated horaFin, the conductor will show as available for future requests.

            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/historico/usuario/{usuarioId}")
    public ResponseEntity<?> getHistoricoPorUsuario(@PathVariable("usuarioId") String usuarioId) {
        try {
            List<Servicio> list = servicioRepository.findAll().stream()
                    .filter(s -> s.getUsuario_id() != null && s.getUsuario_id().equals(usuarioId))
                    .sorted((a, b) -> java.time.LocalDateTime.now().compareTo(java.time.LocalDateTime.now())) // no-op placeholder
                    .toList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/historico/conductor/{conductorId}")
    public ResponseEntity<?> getHistoricoPorConductor(@PathVariable("conductorId") String conductorId) {
        try {
            List<Servicio> list = servicioRepository.findAll().stream()
                    .filter(s -> s.getConductor_id() != null && s.getConductor_id().equals(conductorId))
                    .toList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

