public class Maquina {
    private String nombre;
    private int capacidad;

    public Maquina(String nombre, int capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    @Override
    public String toString() {
        return "(" + this.nombre + ", " + this.capacidad + ")";
    }
}
