package uniandes.edu.co.demo.modelo;

public class TarjetaCredito {
    private String numero;
    private String nombreTarjeta;
    private String vencimiento;
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
