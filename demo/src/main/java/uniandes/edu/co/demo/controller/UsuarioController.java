package uniandes.edu.co.demo.controller;

import java.util.List;

import org.bson.Document;
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

import uniandes.edu.co.demo.modelo.Usuario;
import uniandes.edu.co.demo.repository.UsuarioRepository;
import uniandes.edu.co.demo.repository.UsuarioServicioCustom;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioServicioCustom usuarioServicioCustom;

    @PostMapping("new/save")
    public ResponseEntity<String> createUsuario(@RequestBody Usuario usuario) {
        try {
            usuarioRepository.insertarUsuario(usuario);
            return ResponseEntity.ok("Usuario creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/{id}/edit/save")
    public ResponseEntity<String> updateUsuario(@PathVariable("id") String id, @RequestBody Usuario usuario) {
        try {
            usuarioRepository.actualizarUsuario(
                    id,
                    usuario.getNombre(),
                    usuario.getTipo(),
                    usuario.getCorreo(),
                    usuario.getCelular(),
                    usuario.getCedula(),
                    usuario.getTarjetas());
            return ResponseEntity.ok("Usuario actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsuarios() {
        try {
            List<Usuario> usuarios = usuarioRepository.buscarTodosUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable("id") String id) {
        try {
            java.util.Optional<Usuario> usuario = usuarioRepository.findById(id);
            if (usuario.isPresent()) {
                return ResponseEntity.ok(usuario.get());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteUsuario(@PathVariable("id") String id) {
        try {
            usuarioRepository.eliminarUsuarioId(id);
            return ResponseEntity.ok("Usuario eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/conductores/top20")
public ResponseEntity<List<Document>> obtenerTop20Conductores() {
    try {
        List<Document> resultado = usuarioServicioCustom.obtenerTop20Conductores();
        return ResponseEntity.ok(resultado);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

}