package uniandes.edu.co.demo.modelo;

import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TarjetaCredito {
    private String numero;
    @Field("nombreEnTarjeta")
    @JsonProperty("nombreEnTarjeta")
    private String nombreTarjeta;
    private String vencimiento;
    @Field("cvv")
    @JsonProperty("cvv")
    private String codigo;

    public TarjetaCredito() {
    }

    public TarjetaCredito(String numero, String nombreTarjeta, String vencimiento, String codigo) {
        this.numero = numero;
        this.nombreTarjeta = nombreTarjeta;
        this.vencimiento = vencimiento;
        this.codigo = codigo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombreTarjeta() {
        return nombreTarjeta;
    }

    public void setNombreTarjeta(String nombreTarjeta) {
        this.nombreTarjeta = nombreTarjeta;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
