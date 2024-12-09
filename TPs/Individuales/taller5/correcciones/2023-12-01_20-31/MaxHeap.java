package aed;

public class MaxHeap {
    private Router[] heap;
    private int SiguienteIndiceVacio;

    public MaxHeap(Router[] routers) { // algoritmo de floyd O(n)
        this.heap = routers.clone();
        this.SiguienteIndiceVacio = heap.length;

        int ultimoNodo = this.SiguienteIndiceVacio - 1;
        int ultimoPadre = ultimoNodo % 2 == 0 ? ultimoNodo / 2 - 1 : ultimoNodo / 2;

        for (int i = ultimoPadre; i >= 0; i--) {
            AcomodarNodoAbajo(i);
        }
    }

    public Router ObtenerMaximo() {
        return this.heap[0];
    }

    public void BorrarMaximo() {
        if (this.SiguienteIndiceVacio == 0) {
            return;
        }

        PermutarNodos(0, this.SiguienteIndiceVacio - 1);
        BorrarUltimoNodo();
        AcomodarNodoAbajo(0);
    }

    public void AgregarElemento(Router routers) {
        if (this.SiguienteIndiceVacio == this.heap.length) {
            return;
        }

        heap[this.SiguienteIndiceVacio++] = routers;
        AcomodarNodoArriba(this.SiguienteIndiceVacio - 1);
    }

    public Boolean EsVacio() {
        return this.SiguienteIndiceVacio == 0;
    }

    private void AcomodarNodoArriba(int indiceNodo) {
        int indiceActual = indiceNodo;
        int indicePadre = indiceActual % 2 == 0 ? indiceActual / 2 - 1 : indiceActual / 2;

        while (indiceActual > 0 && heap[indiceActual].getTrafico() > heap[indicePadre].getTrafico()) {
            PermutarNodos(indiceActual, indicePadre);
            indiceActual = indicePadre;
            indicePadre = indiceActual % 2 == 0 ? indiceActual / 2 - 1 : indiceActual / 2;
        }
    }

    private int AcomodarNodoAbajo(int indiceNodo) {
        int indiceAcutal = indiceNodo;

        while (TieneHijos(indiceAcutal) && !esMayorAHijos(indiceAcutal)) {
            int hijo1 = indiceAcutal * 2 + 1;
            int hijo2 = indiceAcutal * 2 + 2;
            int indiceDelMayor;

            if (hijo2 >= this.SiguienteIndiceVacio) {
                indiceDelMayor = hijo1;
            } else {
                indiceDelMayor = heap[hijo1].getTrafico() > heap[hijo2].getTrafico() ? hijo1 : hijo2;
            }

            PermutarNodos(indiceDelMayor, indiceAcutal);
            indiceAcutal = indiceDelMayor;

        }

        return indiceAcutal;
    }

    private void PermutarNodos(int firstIndex, int secondIndex) {
        Router temp = heap[firstIndex];
        heap[firstIndex] = heap[secondIndex];
        heap[secondIndex] = temp;
    }

    private void BorrarUltimoNodo() {
        heap[--this.SiguienteIndiceVacio] = null;
    }

    private boolean TieneHijos(int indice) {
        return (indice * 2 + 1) < this.SiguienteIndiceVacio;
    }

    private boolean esMayorAHijos(int indice) {

        if (!TieneHijos(indice)) {
            return true;
        }

        int indiceHijo1 = indice * 2 + 1;
        int indiceHijo2 = indice * 2 + 2;

        if (indiceHijo1 < this.SiguienteIndiceVacio && indiceHijo2 < this.SiguienteIndiceVacio) {
            return heap[indice].getTrafico() > heap[indiceHijo1].getTrafico()
                    && heap[indice].getTrafico() > heap[indiceHijo2].getTrafico();
        }

        return heap[indice].getTrafico() > heap[indiceHijo1].getTrafico();
    }
}
