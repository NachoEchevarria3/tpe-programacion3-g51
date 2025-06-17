import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Servicio {
    private final List<Maquina> maquinas;
    private int piezas;

    private List<Maquina> solucion;
    private int arranques;
    private int estados;

    public Servicio() {
        maquinas = new ArrayList<>();
        piezas = 0;
        solucion = new ArrayList<>();
        arranques = Integer.MAX_VALUE;
        estados = 0;
    }

    public void inicializar(String path) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line = bufferedReader.readLine();
            this.piezas = Integer.parseInt(line);

            while ((line = bufferedReader.readLine()) != null) {
                String[] fileMachines = line.split(",");
                this.maquinas.add(new Maquina(fileMachines[0].trim(), Integer.parseInt(fileMachines[1].trim())));
            }
        }
    }

    /*
    * GREEDY:
    * Los candidatos a seleccionar serían las máquinas.
    * La estrategia greedy que elegimos fue ordenar la lista de máquinas de mayor a menor capacidad y así seleccionar
    * la máquina que más piezas puede producir siempre que sea posible (No supere la cantidad de piezas restantes),
    * reduciendo así la cantidad de arranques.
    * Este algoritmo puede que no resuelva el problema, porque toma una decisión con un criterio definido y en algunos casos
    * podría no llegar a una solución. Por ejemplo:
    *   - Máquinas: (M1 - 3), (M2 - 2), (M3 - 3). Piezas: 7
    *   - 1: Selecciona M1 --> Restan 4
    *   - 2: Selecciona M1 --> Restan 1
    *   - 3: Selecciona M1 --> No sirve (Excede cantidad de máquinas a producir), la elimina.
    *   - 4: Selecciona M3 --> No sirve (Excede cantidad de máquinas a producir), la elimina.
    *   - 5: Selecciona M2 --> No sirve (Excede cantidad de máquinas a producir), la elimina.
    *   - 6: No tengo más máquina y resta 1 pieza a producir, no hay solución.
    * */
    public void greedy() {
        if (this.piezas <= 0) {
            System.out.println("Greedy: No hay piezas que producir.\n");
            return;
        }

        List<Maquina> resultado = new ArrayList<>();

        List<Maquina> maquinasDisponibles = new ArrayList<>(this.maquinas);
        maquinasDisponibles.sort(Comparator.comparingInt(Maquina::getCapacidad).reversed());

        int piezasRestantes = this.piezas;
        int candidatos = 0;

        while (piezasRestantes > 0 && !maquinasDisponibles.isEmpty()) {
            Maquina maquina = maquinasDisponibles.getFirst();
            candidatos++;

            if (maquina.getCapacidad() <= piezasRestantes) {
                resultado.add(maquina);
                piezasRestantes -= maquina.getCapacidad();
            } else {
                maquinasDisponibles.remove(maquina);
            }
        }

        if (piezasRestantes > 0) {
            System.out.println("Greedy: No hay solución\n");
        } else {
            imprimirSolucionGreedy(resultado, candidatos);
        }
    }

    private void imprimirSolucionGreedy(List<Maquina> solucionGreedy, int candidates) {
        System.out.println("Greedy");
        System.out.println("Solución obtenida: " + solucionGreedy);
        System.out.println("Piezas producidas: " + this.piezas);
        System.out.println("Cantidad de puestas en funcionamiento: " + solucionGreedy.size());
        System.out.println("Cantidad de candidatos considerados: " + candidates + "\n");
    }

    /*
    * BACKTRACKING:
    * El árbol de exploración lo construimos a partir de la cantidad de piezas restantes por producir. En cada nodo del
    * árbol se elige una maquina que:
    *   - Sea a partir del índice actual, se considera desde el índice de la maquina elegida anteriormente hacia adelante
    *     para no generar estados repetidos.
    *   - Su capacidad no supere la cantidad de piezas restantes.
    * Se realiza la exploración hasta que la cantidad de piezas restantes sea 0, cuando se llega a esa cantidad se considera
    * la lista de máquinas actual como una posible solución y se evalúa si esa combinación de maquinas es mejor que la
    * solución optima.
    *
    * Podas:
    *   - Si la combinación actual supera la cantidad de arranques (tamaño de la lista) de la solución optima no se sigue explorando.
    *
    * */
    public void backtracking() {
        if (this.piezas <= 0) {
            System.out.println("Backtracking: No hay piezas que producir.\n");
            return;
        }

        this.solucion.clear();
        this.arranques = Integer.MAX_VALUE;
        this.estados = 0;

        List<Maquina> maquinasDisponibles = new ArrayList<>(this.maquinas);
        backtrack(0, this.piezas, new ArrayList<>(), maquinasDisponibles);

        if (solucion.isEmpty()) {
            System.out.println("Backtracking: No hay solucion\n");
        } else {
            imprimirSolucionBacktracking();
        }
    }

    private void backtrack(int index, int piezasRestantes, List<Maquina> solucionActual, List<Maquina> maquinasDisponibles) {
        this.estados++;

        if (piezasRestantes == 0) {
            if (solucionActual.size() < arranques) {
                arranques = solucionActual.size();
                solucion.clear();
                solucion.addAll(solucionActual);
            }

            return;
        }

        if (solucionActual.size() >= arranques) return;

        for (int i = index; i < maquinasDisponibles.size(); i++) {
            Maquina maquina = maquinasDisponibles.get(i);
            if (piezasRestantes - maquina.getCapacidad() < 0) continue;

            solucionActual.add(maquina);
            backtrack(i, piezasRestantes - maquina.getCapacidad(), solucionActual, maquinasDisponibles);
            solucionActual.remove(solucionActual.size() - 1);
        }
    }

    private void imprimirSolucionBacktracking() {
        System.out.println("Backtracking");
        System.out.println("Solución obtenida: " + solucion);
        System.out.println("Piezas producidas: " + this.piezas);
        System.out.println("Cantidad de puestas en funcionamiento: " + solucion.size());
        System.out.println("Cantidad de estados generados: " + estados + "\n");
    }
}
