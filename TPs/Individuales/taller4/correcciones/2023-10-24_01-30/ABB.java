package aed;

// Todos los tipos de datos "Comparables" tienen el método compareTo()
// elem1.compareTo(elem2) devuelve un entero. Si es mayor a 0, entonces elem1 > elem2
public class ABB<T extends Comparable<T>> implements Conjunto<T> {
    // Agregar atributos privados del Conjunto

    private Nodo raiz;
    private int cardinal;

    private class Nodo {
        T elem;
        Nodo left;
        Nodo right;
        Nodo parent;

        public Nodo(T elem, Nodo parent) {
            this.elem = elem;
            this.left = null;
            this.right = null;
            this.parent = parent;
        }

        public boolean parentIsGreater() {
            return parent != null ? parent.elem.compareTo(elem) > 0 : true;
        }
    }

    public ABB() {
        this.raiz = null;
        this.cardinal = 0;
    }

    public int cardinal() {
        return this.cardinal;
    }

    public T minimo() {
        return minimo(this.raiz);
    }

    public T maximo() {
        Nodo node = this.raiz;
        while (node.right != null) {
            node = node.right;
        }
        return node.elem;
    }

    public void insertar(T elem) {
        Nodo node = this.raiz;
        if (node == null) {
            this.raiz = new Nodo(elem, null);
            this.cardinal++;
            return;
        }

        do {
            int compare = node.elem.compareTo(elem);
            if (compare == 0) {
                return;
            }

            if (compare > 0) {
                if (node.left == null) {
                    node.left = new Nodo(elem, node);
                    this.cardinal++;
                    return;
                }
                node = node.left;
                continue;
            }

            if (node.right == null) {
                node.right = new Nodo(elem, node);
                this.cardinal++;
                return;
            }
            node = node.right;
        } while (true);
    }

    public boolean pertenece(T elem) {
        return obtenerNodo(elem) != null;
    }

    public void eliminar(T elem) {
        Nodo node = this.obtenerNodo(elem);
        if (node == null) {
            return;
        }

        Nodo parentNode = node.parent;
        Nodo nextNode;
        this.cardinal--;

        if (node.right == null && node.left == null) {
            nextNode = null;
        } else if (node.right == null) {
            nextNode = node.left;
            nextNode.parent = parentNode;
        } else if (node.left == null) {
            nextNode = node.right;
            nextNode.parent = parentNode;
        } else {
            nextNode = node.right;
            nextNode.parent = parentNode;
            Nodo smallNodeOnRight = obtenerNodo(minimo(nextNode));
            smallNodeOnRight.left = node.left;
            node.left.parent = smallNodeOnRight;
        }

        if (parentNode == null) {
            this.raiz = nextNode;
            return;
        }

        if (node.parentIsGreater()) {
            parentNode.left = nextNode;
        } else {
            parentNode.right = nextNode;
        }
    }

    private Nodo obtenerNodo(T elem) {
        Nodo node = this.raiz;
        if (node == null) {
            return null;
        }

        do {
            int compare = node.elem.compareTo(elem);
            if (compare == 0) {
                return node;
            }

            if (compare > 0) {
                if (node.left == null) {
                    return null;
                }
                node = node.left;
                continue;
            }

            if (node.right == null) {
                return null;
            }
            node = node.right;
        } while (true);
    }

    private T minimo(Nodo node) {
        while (node.left != null) {
            node = node.left;/*  */
        }
        return node.elem;
    }

    public String toString() {

        StringBuilder ret = new StringBuilder();

        ret.append("{");

        Iterador<T> iter = this.iterador();

        while (iter.haySiguiente()) {
            ret.append(",");
            ret.append(iter.siguiente().toString());
        }

        ret.append("}");

        if (ret.charAt(1) == ',')
            ret.deleteCharAt(1);

        return ret.toString();
    }

    private class ABB_Iterador implements Iterador<T> {
        private Nodo current;

        public ABB_Iterador(ABB<T> abb) {
            this.current = obtenerNodo(minimo(abb.raiz));
        }

        public boolean haySiguiente() {
            return current != null;
        }

        public T siguiente() {
            T ret = current.elem;
            current = obtenerNodoSiguiente();
            return ret;
        }

        private Nodo obtenerNodoSiguiente() {
            Nodo nodo = current;
            if (nodo.right != null) {
                return obtenerNodo(minimo(nodo.right));
            }

            while (nodo.parent != null) {
                if (nodo.parentIsGreater()) {
                    return nodo.parent;
                }
                nodo = nodo.parent;
            }

            return null;
        }
    }

    public Iterador<T> iterador() {
        return new ABB_Iterador(this);
    }

}
