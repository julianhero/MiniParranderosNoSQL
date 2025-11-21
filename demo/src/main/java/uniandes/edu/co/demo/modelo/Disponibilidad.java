package uniandes.edu.co.demo.modelo;

public class Disponibilidad {
    private String dia;
    private String horaInicio;
    private String horaFin;
    private String tipoServicio;

    public Disponibilidad() {
    }

    public Disponibilidad(String dia, String horaInicio, String horaFin, String tipoServicio) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.tipoServicio = tipoServicio;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

}
