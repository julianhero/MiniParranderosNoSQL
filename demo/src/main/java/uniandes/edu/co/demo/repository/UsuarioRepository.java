package uniandes.edu.co.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import uniandes.edu.co.demo.modelo.TarjetaCredito;
import uniandes.edu.co.demo.modelo.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    @Query(value = "{}")
    List<Usuario> buscarTodosUsuarios();

    @Query("{_id=?0}")
    List<Usuario> buscarUsuarioId(String id);

    default void insertarUsuario(Usuario usuario) {
        save(usuario);
    }

    @Query("{_id:?0}")
    @Update("{$set:{nombre:?1, tipo:?2, correo:?3, celular:?4, cedula:?5, tarjetas:?6}}")
    void actualizarUsuario(String id, String nombre, String tipo, String correo, String celular, String cedula,
            List<TarjetaCredito> tarjeta);

    @Query(value = "{_id: ?0}", delete = true)
    void eliminarUsuarioId(String id);

    @Query(value = "{_id: ?0}", fields = "{'tarjetas':1}")
    List<TarjetaCredito> obtenerTarjetasUsuarioporUsuario(String id);
}
