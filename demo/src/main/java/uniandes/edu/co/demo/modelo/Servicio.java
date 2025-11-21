package uniandes.edu.co.demo.modelo;

import java.sql.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "servicios")
public class Servicio {
    @Id
    private String id;
    private Usuario cliente_id;
    private Usuario conductor_id;
    private Vehiculo vehiculo_id;
    private String tipoServicio;
    private String nivel;
    private Punto origen;
    private List<Punto> destino;
    private Number distancia;
    private Number costo;
    private Date horaInicio;
    private Date horaFin;

    public Servicio() {
    }

    public Servicio(String id, Usuario cliente_id, Usuario conductor_id, Vehiculo vehiculo_id, String tipoServicio,
            String nivel, Punto origen, List<Punto> destino, Number distancia, Number costo, Date horaInicio,
            Date horaFin) {
        this.id = id;
        this.cliente_id = cliente_id;
        this.conductor_id = conductor_id;
        this.vehiculo_id = vehiculo_id;
        this.tipoServicio = tipoServicio;
        this.nivel = nivel;
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.costo = costo;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(Usuario cliente_id) {
        this.cliente_id = cliente_id;
    }

    public Usuario getConductor_id() {
        return conductor_id;
    }

    public void setConductor_id(Usuario conductor_id) {
        this.conductor_id = conductor_id;
    }

    public Vehiculo getVehiculo_id() {
        return vehiculo_id;
    }

    public void setVehiculo_id(Vehiculo vehiculo_id) {
        this.vehiculo_id = vehiculo_id;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Punto getOrigen() {
        return origen;
    }

    public void setOrigen(Punto origen) {
        this.origen = origen;
    }

    public List<Punto> getDestino() {
        return destino;
    }

    public void setDestino(List<Punto> destino) {
        this.destino = destino;
    }

    public Number getDistancia() {
        return distancia;
    }

    public void setDistancia(Number distancia) {
        this.distancia = distancia;
    }

    public Number getCosto() {
        return costo;
    }

    public void setCosto(Number costo) {
        this.costo = costo;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

}
