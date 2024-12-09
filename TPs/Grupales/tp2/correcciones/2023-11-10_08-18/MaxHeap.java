package aed;

/*
    Inv Representacion

    - 0 <= SiguienteIndiceVacio <= heap.size()

    - Toda posicion con indice menor a "SiguienteIndiceVacio" tiene un valor de la clase VotosDistrito valido (indice y votos validos)

    - Para todo indice X, La cantidad de votos de una posicion X de "heap" es mayor o igual 
      a la cantidad de votos de las posiciones 2*X+1 y 2*X+2 siempre que estas sean menores a "SiguienteIndiceVacio", es decir que sean posiciones validas.
      Otra forma de decirlo es que para toda posicion X que tenga hijos, la cantidad de votos de la posicion X es mayor a la de sus hijos

*/

public class MaxHeap {
    private VotosDistrito[] heap;
    private int SiguienteIndiceVacio;

    public MaxHeap(int size) { // O(size)
        this.heap = new VotosDistrito[size]; // O(size)
        this.SiguienteIndiceVacio = 0; // O(1)
    }

    public MaxHeap(VotosDistrito[] votosDistrito) { // O(votosDistrito.length) algotirtmo de Floyd
        this.heap = votosDistrito.clone(); // O(votosDistrito.length)
        this.SiguienteIndiceVacio = heap.length;

        int ultimoNodo = this.SiguienteIndiceVacio - 1;
        int ultimoPadre = ultimoNodo % 2 == 0 ? ultimoNodo / 2 - 1 : ultimoNodo / 2;

        // O(votosDistrito.length) algoritmo de Floyd
        for (int i = ultimoPadre; i >= 0; i--) {
            AcomodarNodoAbajo(i);
        }
    }

    public MaxHeap(MaxHeap maxheap) { // O(maxheap.heap.length), es decir el tamano de heap que queremos duplicar
        this.heap = new VotosDistrito[maxheap.heap.length]; // O(heap.length)

        // heap.length * O(1) = O(heap.length)
        for (int i = 0; i < maxheap.heap.length; i++) {
            this.heap[i] = new VotosDistrito(maxheap.heap[i].indice(), maxheap.heap[i].votos()); // O(1)
        }

        this.SiguienteIndiceVacio = maxheap.SiguienteIndiceVacio;

        // la complejidad total es O(heap.length)
    }

    public MaxHeap clone() { // O(this.heap.length)
        return new MaxHeap(this.heap);  // O(this.heap.length) algotirtmo de Floyd
    }

    public VotosDistrito ObtenerMaximo() { // O(1)
        return this.heap[0];
    }

    public void BorrarMaximo() { // O(log this.heap.size)
        if (this.SiguienteIndiceVacio == 0) {
            return;
        }

        PermutarNodos(0, this.SiguienteIndiceVacio - 1);  // O(1)
        BorrarUltimoNodo(); // O(1)
        AcomodarNodoAbajo(0); // O(log this.heap.size)
    }

    public void AgregarElemento(VotosDistrito votosDistrito) { // O(log size)
        if (this.SiguienteIndiceVacio == this.heap.length) {
            return;
        }

        heap[this.SiguienteIndiceVacio++] = votosDistrito;
        AcomodarNodoArriba(this.SiguienteIndiceVacio - 1); // O(log size)
    }

    public void EditarMaximo(int votos) { // log(this.heap.size)
        VotosDistrito votosDistrito = this.ObtenerMaximo(); // O(1)
        votosDistrito.votos(votos); // O(1)
        AcomodarNodoAbajo(0); // log(this.heap.size)
    }

    private void AcomodarNodoArriba(int indiceNodo) {  // log(this.heap.size)
        int indiceActual = indiceNodo;
        int indicePadre = indiceActual % 2 == 0 ? indiceActual / 2 - 1 : indiceActual / 2;

        // la complejidad de esta funcion solo depende la cantidad de iteraciones
        // que realiza este bucle, y este bucle se va a seguir ejecutando mientras
        // el indice tenga un padre, asi que depende de la altura del heap
        // con lo cual se ejecuta log(this.heap.size) veces
        while (indiceActual > 0 && heap[indiceActual].votos() > heap[indicePadre].votos()) {
            PermutarNodos(indiceActual, indicePadre); // O(1)
            indiceActual = indicePadre;
            indicePadre = indiceActual % 2 == 0 ? indiceActual / 2 - 1 : indiceActual / 2;
        }
    }

    private int AcomodarNodoAbajo(int indiceNodo) { // log(this.heap.size)
        int indiceAcutal = indiceNodo;

        // la complejidad de esta funcion solo depende la cantidad de iteraciones
        // que realiza este bucle, y este bucle se va a seguir ejecutando mientras
        // el indice tenga un hijo, asi que depende de la altura del heap
        // con lo cual se ejecuta log(this.heap.size) veces
        while (TieneHijos(indiceAcutal) && !esMayorAHijos(indiceAcutal)) {
            int hijo1 = indiceAcutal * 2 + 1;
            int hijo2 = indiceAcutal * 2 + 2;
            int indiceDelMayor;

            if (hijo2 >= this.SiguienteIndiceVacio) {
                indiceDelMayor = hijo1;
            } else {
                indiceDelMayor = heap[hijo1].votos() > heap[hijo2].votos() ? hijo1 : hijo2;
            }

            PermutarNodos(indiceDelMayor, indiceAcutal); // O(1)
            indiceAcutal = indiceDelMayor;

            // cada bucle es O(1)
        }

        return indiceAcutal;
    }

    private void PermutarNodos(int firstIndex, int secondIndex) { // O(1)
        VotosDistrito temp = heap[firstIndex];
        heap[firstIndex] = heap[secondIndex];
        heap[secondIndex] = temp;
    }

    private void BorrarUltimoNodo() { // O(1)
        heap[--this.SiguienteIndiceVacio] = null;
    }

    private boolean TieneHijos(int indice) { // O(1)
        return (indice * 2 + 1) < this.SiguienteIndiceVacio;
    }

    private boolean esMayorAHijos(int indice) { // O(1)

        if (!TieneHijos(indice)) { // O(1)
            return true;
        }

        int indiceHijo1 = indice * 2 + 1;
        int indiceHijo2 = indice * 2 + 2;

        if (indiceHijo1 < this.SiguienteIndiceVacio && indiceHijo2 < this.SiguienteIndiceVacio) {
            return heap[indice].votos() > heap[indiceHijo1].votos()
                    && heap[indice].votos() > heap[indiceHijo2].votos();
        }

        return heap[indice].votos() > heap[indiceHijo1].votos();
    }
}
