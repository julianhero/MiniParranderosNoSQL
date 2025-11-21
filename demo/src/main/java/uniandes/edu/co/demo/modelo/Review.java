package uniandes.edu.co.demo.modelo;

import java.sql.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    private String servicio_id;
    private String autor_id;
    private String destinatario_id;
    private Number calificacion;
    private String comentario;
    private Date fecha;

    public Review() {
    }

    public Review(String id, String servicio_id, String autor_id, String destinatario_id, Number calificacion,
            String comentario, Date fecha) {
        this.id = id;
        this.servicio_id = servicio_id;
        this.autor_id = autor_id;
        this.destinatario_id = destinatario_id;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServicio_id() {
        return servicio_id;
    }

    public void setServicio_id(String servicio_id) {
        this.servicio_id = servicio_id;
    }

    public String getAutor_id() {
        return autor_id;
    }

    public void setAutor_id(String autor_id) {
        this.autor_id = autor_id;
    }

    public String getDestinatario_id() {
        return destinatario_id;
    }

    public void setDestinatario_id(String destinatario_id) {
        this.destinatario_id = destinatario_id;
    }

    public Number getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Number calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}
