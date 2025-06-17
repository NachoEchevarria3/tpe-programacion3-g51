import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Servicio servicio = new Servicio();
        try {
            servicio.inicializar("./src/Maquinas.txt");
            servicio.greedy();
            servicio.backtracking();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}