package uniandes.edu.co.demo.modelo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;
    private String tipo;
    private String nombre;
    private String correo;
    private String celular;
    private String cedula;
    private List<TarjetaCredito> tarjeta;

    public Usuario() {
    }

    public Usuario(String id, String tipo, String nombre, String correo, String celular, String cedula,
            List<TarjetaCredito> tarjeta) {
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.correo = correo;
        this.celular = celular;
        this.cedula = cedula;
        this.tarjeta = tarjeta;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public List<TarjetaCredito> getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(List<TarjetaCredito> tarjeta) {
        this.tarjeta = tarjeta;
    }

}
