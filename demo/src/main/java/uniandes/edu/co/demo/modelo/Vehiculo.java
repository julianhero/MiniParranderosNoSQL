package uniandes.edu.co.demo.modelo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehiculos")
public class Vehiculo {
    @Id
    private String id;
    private Usuario conductor_id;
    private String tipo;
    private String marca;
    private String modelo;
    private String color;
    private String placa;
    private String ciudad;
    private Number capacidad;
    private String nivel;
    private List<Disponibilidad> disponibilidad;

    public Vehiculo() {
    }

    public Vehiculo(String id, Usuario conductor_id, String tipo, String marca, String modelo, String color,
            String placa, String ciudad, Number capacidad, String nivel, List<Disponibilidad> disponibilidad) {
        this.id = id;
        this.conductor_id = conductor_id;
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.placa = placa;
        this.ciudad = ciudad;
        this.capacidad = capacidad;
        this.nivel = nivel;
        this.disponibilidad = disponibilidad;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getConductor_id() {
        return conductor_id;
    }

    public void setConductor_id(Usuario conductor_id) {
        this.conductor_id = conductor_id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Number getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Number capacidad) {
        this.capacidad = capacidad;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public List<Disponibilidad> getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(List<Disponibilidad> disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

}
