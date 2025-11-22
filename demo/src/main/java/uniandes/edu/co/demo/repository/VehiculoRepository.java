package uniandes.edu.co.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import uniandes.edu.co.demo.modelo.Disponibilidad;
import uniandes.edu.co.demo.modelo.Vehiculo;

public interface VehiculoRepository extends MongoRepository<Vehiculo, String> {

    @Query(value = "{}")
    List<Vehiculo> buscarTodosVehiculos();

    @Query("{_id=?0}")
    List<Vehiculo> buscarVehiculoId(String id);

    @Query("{'conductor_id': ?0}")
    List<Vehiculo> findByConductorId(String conductorId);

    default void insertarVehiculo(Vehiculo vehiculo) {
        save(vehiculo);
    }

    @Query("{_id:?0}")
    @Update("{$set:{conductor_id:?1, tipo:?2, marca:?3, modelo:?4, color:?5, placa:?6, ciudad:?7, capacidad:?8, nivel:?9, disponibilidad:?10}}")
    void actualizarVehiculo(String id, String conductor_id, String tipo, String marca, String modelo, String color,
            String placa, String ciudad, Number capacidad, String nivel, List<Disponibilidad> disponibilidad);

    @Query(value = "{_id: ?0}", delete = true)
    void eliminarVehiculoId(String id);

    @Query(value = "{_id: ?0}", fields = "{'disponibilidad':1}")
    List<Disponibilidad> obtenerDisponibilidadporVehiculo(String id);
}
