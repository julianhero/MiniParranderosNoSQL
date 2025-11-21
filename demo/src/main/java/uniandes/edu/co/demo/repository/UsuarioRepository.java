package uniandes.edu.co.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import uniandes.edu.co.demo.modelo.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    
    @Query("{ 'correo' : ?0 }")

}
