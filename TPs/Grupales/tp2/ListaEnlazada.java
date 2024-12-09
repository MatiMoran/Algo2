package aed;

/*
    Inv Representacion

    - 0 <= length

    - para todo 0 < i < length => Nodo[i] != null y Nodo[i].elem esta definido

    - Para todo 0 < i < length - 1 => Nodo[i] tiene un siguiente != null

    - Para todo 1 < i < length => Nodo[i] tiene un prev != null

    - 0 < length => Nodo[0].prev = null

    - 0 < length => Nodo[length - 1].siguiente = null

*/


public class ListaEnlazada<T> {

    private Nodo primero;
    private Nodo ultimo;
    private int length;

    private class Nodo {
        private T elem;
        private Nodo siguiente;
        private Nodo prev;
    }

    public ListaEnlazada() { // O(1)
        this.primero = null;
        this.ultimo = null;
        this.length = 0;
    }

    public int longitud() { // O(1)
        return length;
    }

    public void agregarAdelante(T elem) { // O(1)
        Nodo nuevo = new Nodo();
        nuevo.elem = elem;
        nuevo.siguiente = this.primero;
        nuevo.prev = null;

        if (this.primero != null) {
            this.primero.prev = nuevo;
        }
        this.primero = nuevo;

        if (this.ultimo == null) {
            this.ultimo = nuevo;
        }
        this.length = this.length + 1;
    }

    public void agregarAtras(T elem) { // O(1)
        Nodo nuevo = new Nodo();
        nuevo.elem = elem;
        nuevo.siguiente = null;
        nuevo.prev = this.ultimo;

        if (this.ultimo != null) {
            this.ultimo.siguiente = nuevo;
        }
        this.ultimo = nuevo;

        if (this.primero == null) {
            this.primero = nuevo;
        }

        this.length = this.length + 1;
    }

    public T obtener(int i) {  // O(this.length) en el peor caso
        Nodo node = nodeAt(i);
        return node.elem;
    }

    public void eliminar(int i) {  // O(this.length) en el peor caso
        Nodo node = nodeAt(i);
        node.elem = null;

        if (i == 0) {
            this.primero = node.siguiente;
        }

        if (i == this.length - 1) {
            this.ultimo = node.prev;
        }

        if (node.prev != null) {
            node.prev.siguiente = node.siguiente;
        }

        if (node.siguiente != null) {
            node.siguiente.prev = node.prev;
        }

        this.length = this.length - 1;
    }

    public void modificarPosicion(int indice, T elem) {  // O(this.length) en el peor caso
        Nodo node = nodeAt(indice); // O(this.length) en el peor caso
        node.elem = elem;
    }

    public ListaEnlazada<T> copiar() {
        ListaEnlazada newList = new ListaEnlazada();
        for (int i = 0; i < this.length; i++) {
            newList.agregarAtras(this.obtener(i));
        }
        return newList;
    }

    public ListaEnlazada(ListaEnlazada<T> lista) {
        ListaEnlazada<T> listaTemp = lista.copiar();
        this.primero = listaTemp.primero;
        this.ultimo = listaTemp.ultimo;
        this.length = listaTemp.length;
    }

    private Nodo nodeAt(int i) { // O(this.length) en el peor caso
        if (i < 0 || i >= this.length) {
            throw new IndexOutOfBoundsException();
        }

        Nodo n = this.primero;
        for (int j = 0; j < i; j++) {
            n = n.siguiente;
        }

        return n;
    }
    
    @Override
    public String toString() {

        StringBuilder ret = new StringBuilder();
        
        ret.append("[");

        for (int i = 0; i < this.length; i++) {
            if (i > 0)
                ret.append(" ");

            ret.append(this.obtener(i).toString());

            if (i < this.length - 1)
                ret.append(",");
        }
        
        ret.append("]");

        return ret.toString();
    }
}
