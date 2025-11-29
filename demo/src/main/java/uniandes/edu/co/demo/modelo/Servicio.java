package uniandes.edu.co.demo.modelo;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonAlias;

@Document(collection = "servicios")
public class Servicio {
    @Id
    private String id;
    private String usuario_id;
    private String conductor_id;
    private String vehiculo_id;
    private String tipoServicio;
    private String nivel;
    private Punto origen;
    @JsonAlias({"destino","destinos"})
    private List<Punto> destino;
    @JsonAlias({"distancia","distancia_km"})
    private Number distancia_km;
    private Number costo;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;

    public Servicio() {
    }

    public Servicio(String id, String usuario_id, String conductor_id, String vehiculo_id, String tipoServicio,
            String nivel, Punto origen, List<Punto> destino, Number distancia_km, Number costo, LocalDateTime horaInicio,
            LocalDateTime horaFin) {
        this.id = id;
        this.usuario_id = usuario_id;
        this.conductor_id = conductor_id;
        this.vehiculo_id = vehiculo_id;
        this.tipoServicio = tipoServicio;
        this.nivel = nivel;
        this.origen = origen;
        this.destino = destino;
        this.distancia_km = distancia_km;
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

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String cliente_id) {
        this.usuario_id = cliente_id;
    }

    public String getConductor_id() {
        return conductor_id;
    }

    public void setConductor_id(String conductor_id) {
        this.conductor_id = conductor_id;
    }

    public String getVehiculo_id() {
        return vehiculo_id;
    }

    public void setVehiculo_id(String vehiculo_id) {
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

    public Number getDistancia_km() {
        return distancia_km;
    }

    public void setDistancia_km(Number distancia) {
        this.distancia_km = distancia;
    }

    public Number getCosto() {
        return costo;
    }

    public void setCosto(Number costo) {
        this.costo = costo;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalDateTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalDateTime horaFin) {
        this.horaFin = horaFin;
    }

}
