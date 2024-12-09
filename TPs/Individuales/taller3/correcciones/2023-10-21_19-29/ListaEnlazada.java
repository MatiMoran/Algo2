package aed;

public class ListaEnlazada<T> implements Secuencia<T> {

    private Nodo firstItem;
    private Nodo lastItem;
    private int length;

    private class Nodo {
        private T elem;
        private Nodo next;
        private Nodo prev;
    }

    public ListaEnlazada() {
        this.firstItem = null;
        this.lastItem = null;
        this.length = 0;
    }

    public int longitud() {
        return length;
    }

    public void agregarAdelante(T elem) {
        Nodo newItem = new Nodo();
        newItem.elem = elem;
        newItem.next = this.firstItem;
        newItem.prev = null;

        if (this.firstItem != null) {
            this.firstItem.prev = newItem;
        }
        this.firstItem = newItem;

        if (this.lastItem == null) {
            this.lastItem = newItem;
        }
        this.length = this.length + 1;
    }

    public void agregarAtras(T elem) {
        Nodo newItem = new Nodo();
        newItem.elem = elem;
        newItem.next = null;
        newItem.prev = this.lastItem;

        if (this.lastItem != null) {
            this.lastItem.next = newItem;
        }
        this.lastItem = newItem;

        if (this.firstItem == null) {
            this.firstItem = newItem;
        }

        this.length = this.length + 1;
    }

    public T obtener(int i) {
        Nodo node = NodeAt(i);
        return node.elem;
    }

    public void eliminar(int i) {
        Nodo node = NodeAt(i);
        node.elem = null;

        if (i == 0) {
            this.firstItem = node.next;
        }

        if (i == this.length - 1) {
            this.lastItem = node.prev;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        }

        this.length = this.length - 1;
    }

    public void modificarPosicion(int indice, T elem) {
        Nodo node = NodeAt(indice);
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
        this.firstItem = listaTemp.firstItem;
        this.lastItem = listaTemp.lastItem;
        this.length = listaTemp.length;
    }

    private Nodo NodeAt(int i) {
        if (i < 0 || i >= this.length) {
            throw new IndexOutOfBoundsException();
        }

        Nodo n = this.firstItem;
        for (int j = 0; j < i; j++) {
            n = n.next;
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

    private class ListaIterador implements Iterador<T> {

        private ListaEnlazada<T> lista;
        private int index;
        private int length;

        public ListaIterador(ListaEnlazada<T> lista)
        {
            this.lista = lista;
            this.length = lista.longitud();
            this.index = 0;
        }

        public boolean haySiguiente() {
            return index <= this.length - 1;
        }

        public boolean hayAnterior() {
            return this.length > 0 && index > 0;
        }

        public T siguiente() {
            return lista.obtener(index++);
        }

        public T anterior() {
            return lista.obtener(--index);
        }
    }

    public Iterador<T> iterador() {
        return new ListaIterador(this);
    }
}
