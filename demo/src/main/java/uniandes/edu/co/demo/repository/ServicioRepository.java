package uniandes.edu.co.demo.repository;


import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import uniandes.edu.co.demo.modelo.Servicio;
import uniandes.edu.co.demo.modelo.Punto;


public interface ServicioRepository extends MongoRepository<Servicio, String> {

     @Query(value = "{}")
    List<Servicio> buscarTodosServicios();

    @Query("{_id=?0}")
    List<Servicio> buscarServicioId(String id);

    default void insertarServicio(Servicio servicio) {
        save(servicio);
    }

    @Query("{_id:?0}")
    @Update("{$set:{usuario_id:?1, conductor_id:?2, vehiculo_id:?3, tipoServicio:?4, nivel:?5, origen:?6, destino:?7, distancia_km:?8, costo:?9, horaInicio:?10, horaFin:?11}}")
    void actualizarServicio(String id, String usuario_id, String conductor_id, String vehiculo_id,
            String tipoServicio, String nivel, Punto origen, List<Punto> destino, Number distancia_km, Number costo,
            LocalDateTime horaInicio, LocalDateTime horaFin);

    @Query(value = "{_id: ?0}", delete = true)
    void eliminarServicioId(String id);

    @Query(value = "{_id: ?0}", fields = "{'destino':1}")
    List<Punto> obtenerDestinoPorServicio(String id);
}
