package uniandes.edu.co.demo.modelo;

public class UsuarioServicio {

    private String _id;
    private int total;

    public UsuarioServicio() {
    }
    public UsuarioServicio(String _id, int total) {
        this._id = _id;
        this.total = total;
    }
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    
    @Override
    public String toString() {
        return "UsuarioServicio {"+ "_id=" + _id + ", total=" + total + "}";
    }
    
}
