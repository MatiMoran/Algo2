package aed;

public class SistemaCNE {
    String[] nombresPartidos;
    String[] nombresDistrictos;
    int[] diputadosPorDistrito;
    int[] ultimasMesasDistritos;
    int[] votosPresidenciales;
    int[][] votosDiputadosPorDistricto;
    MaxHeap[] HeapsDiputadosPorDistricto; // cada MaxHeap corresponderá a un escrutinio en un distrito, tiene la misma
                                          // info que votosDiputadosPorDistricto

    boolean[] mesasRegistradas; // array de booleanos que indica la mesa ha sido registrada
    boolean hayBallotage; // booleano que indica si hay balotage

    public SistemaCNE(
            String[] nombresDistritos,
            int[] diputadosPorDistrito,
            String[] nombresPartidos,
            int[] ultimasMesasDistritos) { // O(P * D)

        this.nombresPartidos = nombresPartidos; // O(1)
        this.nombresDistrictos = nombresDistritos; // O(1)
        this.diputadosPorDistrito = diputadosPorDistrito; // O(1)
        this.ultimasMesasDistritos = ultimasMesasDistritos; // O(1)

        this.votosPresidenciales = new int[this.nombresPartidos.length]; // O(P)
        this.votosDiputadosPorDistricto = new int[this.nombresDistrictos.length][this.nombresPartidos.length]; // O(P *
                                                                                                               // D)

        this.HeapsDiputadosPorDistricto = new MaxHeap[this.nombresDistrictos.length]; // O(D)

        // D x O(P) = O(P * D)
        for (int i = 0; i < this.nombresDistrictos.length; i++) { // D iteraciones
            this.HeapsDiputadosPorDistricto[i] = new MaxHeap(this.nombresPartidos.length); // O(P) ya que este
                                                                                           // constructor recibe un
                                                                                           // array P
        }

        this.mesasRegistradas = new boolean[this.ultimasMesasDistritos[this.ultimasMesasDistritos.length - 1]]; // O(M)
                                                                                                                // ???

        this.hayBallotage = false; // O(1)

        // la complejidad total de este constructor es O(1) + O(P) + O(P * D) + O(D) +
        // O(M) ?? = O(P * D)
    }

    // en todo lo que queda de los comentarios de la complejidad vamos a evitar 
    // poner comentarios para las operaciones en las cuales es facil ver que su complejidad es O(1)
    // debido a que solo se hacen operaciones de acceso a arrays y operaciones aritmeticas basicas
    public String nombrePartido(int idPartido) { // O(1)
        return this.nombresPartidos[idPartido];
    }

    public String nombreDistrito(int idDistrito) { // O(1)
        return this.nombresDistrictos[idDistrito];
    }

    public int diputadosEnDisputa(int idDistrito) { // O(1)
        return this.diputadosPorDistrito[idDistrito];
    }

    private int indiceDistritoDeMesa(int idMesa) { // O(log d)
        int min_index = 0;
        int max_index = this.ultimasMesasDistritos.length - 1;
        int middle_index = (min_index + max_index) / 2;

        if (idMesa < this.ultimasMesasDistritos[0]) {
            return 0;
        }

        if (idMesa >= this.ultimasMesasDistritos[this.ultimasMesasDistritos.length - 1]) {
            return this.ultimasMesasDistritos.length - 1;
        }

        // la complejidad de este método depende de la cantidad de iteraciones que se realicen
        // en el bucle.
        // Como en el bucle hacemos una busqueda binaria ya que vamos tomando el punto medio
        // del intervalo de busqueda de la iteracion anterior, entonces
        // la complejidad total de este método es O(log d)
        while (min_index != max_index) {

            if (this.ultimasMesasDistritos[middle_index - 1] <= idMesa
                    && idMesa < this.ultimasMesasDistritos[middle_index]) {
                return middle_index;
            }

            if (this.ultimasMesasDistritos[middle_index] <= idMesa
                    && idMesa < this.ultimasMesasDistritos[middle_index + 1]) {
                return middle_index + 1;
            }

            if (idMesa < this.ultimasMesasDistritos[middle_index]) {
                max_index = middle_index;
            } else {
                min_index = middle_index;
            }

            middle_index = (min_index + max_index) / 2;
        }

        return middle_index;
    }

    public String distritoDeMesa(int idMesa) { // O(log d)
        int indiceDistritoDeMesa = indiceDistritoDeMesa(idMesa); // O(log d)
        return this.nombreDistrito(indiceDistritoDeMesa); // O(1)
    }

    private boolean PartidoSuperaUmbral(int votosPartido, int votosTotales, double umbral) { // O(1)
        return ((double) votosPartido / votosTotales) >= umbral;
    }

    private boolean esPartidoEnBlanco(int indice) { // O(1)
        return indice == (this.nombresPartidos.length - 1);
    }

    private boolean HayBallotagePresidencial(int votosTotalesPresidenciales) { // O(P)

        double porcentajeDelMaximo = 0.0;
        double porcentajeDelSubMaximo = 0.0;

        // P * O(1) = O(P)
        for (int i = 0; i < this.nombresPartidos.length; i++) { // P iteraciones

            // Todas las operaciones son O(1)

            if (esPartidoEnBlanco(i)) {
                break;
            }

            double porcentajesDePartido = (double) 100 * this.votosPresidenciales[i] / votosTotalesPresidenciales;

            if (porcentajesDePartido >= 45) {
                return false;
            }

            if (porcentajesDePartido > porcentajeDelMaximo) {
                porcentajeDelSubMaximo = porcentajeDelMaximo;
                porcentajeDelMaximo = porcentajesDePartido;
            } else if (porcentajesDePartido > porcentajeDelSubMaximo) {
                porcentajeDelSubMaximo = porcentajesDePartido;
            }
        }

        return porcentajeDelMaximo < 40 || porcentajeDelMaximo - porcentajeDelSubMaximo <= 10;
    }

    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) { // O(P + log D )

        this.mesasRegistradas[idMesa] = true;

        int idDistrito = indiceDistritoDeMesa(idMesa); // O(log d)

        // P * O(1) = O(P)
        for (int i = 0; i < actaMesa.length; i++) { // P iteraciones
            this.votosPresidenciales[i] += actaMesa[i].votosPresidente(); // O(1)
            this.votosDiputadosPorDistricto[idDistrito][i] += actaMesa[i].votosDiputados(); // O(1)
        }

        VotosDistrito[] votosDistrito = new VotosDistrito[this.votosDiputadosPorDistricto[idDistrito].length]; // O(1)

        int votosTotalesDistrito = 0;
        // P * O(1) = O(P)
        for (int i = 0; i < this.votosDiputadosPorDistricto[idDistrito].length; i++) { // P iteraciones
            votosTotalesDistrito += this.votosDiputadosPorDistricto[idDistrito][i];
        }

        int votosTotalesPresidenciales = 0;
        // P * O(1) = O(P)
        for (int i = 0; i < this.nombresPartidos.length; i++) { // P iteraciones
            votosTotalesPresidenciales += this.votosPresidenciales[i];
        }
        this.hayBallotage = HayBallotagePresidencial(votosTotalesPresidenciales); // O(P)

        // P * O(1) = O(P)
        for (int i = 0; i < this.votosDiputadosPorDistricto[idDistrito].length; i++) { // P iteraciones
            if (!PartidoSuperaUmbral(this.votosDiputadosPorDistricto[idDistrito][i], votosTotalesDistrito, 0.03)) { // O(1)
                votosDistrito[i] = new VotosDistrito(i, 0); // O(1)
            } else if (esPartidoEnBlanco(i)) {
                votosDistrito[i] = new VotosDistrito(i, 0); // O(1)
            } else {
                votosDistrito[i] = new VotosDistrito(i, this.votosDiputadosPorDistricto[idDistrito][i]); // O(1)
            }
        }

        this.HeapsDiputadosPorDistricto[idDistrito] = new MaxHeap(votosDistrito); // O(P) algoritmo de Floyd

        // la complejidad total es O(log D) + O(1) + O(P) = O(P + log D)
    }

    public int votosPresidenciales(int idPartido) { // O(1)
        return this.votosPresidenciales[idPartido];
    }

    public int votosDiputados(int idPartido, int idDistrito) { // O(1)
        return this.votosDiputadosPorDistricto[idDistrito][idPartido];
    }

    public int[] resultadosDiputados(int idDistrito) { // O(D_d * log p)

        // se inicia todo en 0, aca vamos a cargar las bancas ganadas por cada partido
        int[] bancasGanadasPorPartido = new int[this.nombresPartidos.length]; // O(P)

        int diputadosEnDisputa = diputadosEnDisputa(idDistrito); // O(1)
        MaxHeap heap = new MaxHeap(this.HeapsDiputadosPorDistricto[idDistrito]); // aca duplicamos el heap de un districto asi que es O(P)

        // D_d * O(log p) = O(D_d * log p)
        for (int i = 0; i < diputadosEnDisputa; i++) { // D_d ciclos

            VotosDistrito ganadorDeBanca = heap.ObtenerMaximo(); // si un Max Heap es O(1)

            bancasGanadasPorPartido[ganadorDeBanca.indice()] += 1; // O(1)

            int votosDePartido = this.votosDiputadosPorDistricto[idDistrito][ganadorDeBanca.indice()]; // O(1)

            int nuevosVotosDePartido = votosDePartido / (1 + bancasGanadasPorPartido[ganadorDeBanca.indice()]); // O(1)

            heap.BorrarMaximo(); // si es un Max Heap es O(log p)

            ganadorDeBanca.votos(nuevosVotosDePartido); // O(1)

            heap.AgregarElemento(ganadorDeBanca); // si es un Max Heap es (log p)

            // complejidad de cada ciclo es O(log p)
        }

        return bancasGanadasPorPartido;
    }

    public boolean hayBallotage() { // O(1)
        return this.hayBallotage;
    }
}
