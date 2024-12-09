package aed;

public class VotosDistrito {
    private int indicePartido;
    private int votos;

    public VotosDistrito(int indicePartido, int votos) { // O(1)
        this.indicePartido = indicePartido;
        this.votos = votos;
    }

    public int indice() { // O(1)
        return indicePartido;
    }

    public int votos() { // O(1)
        return votos;
    }

    public void votos(int votos) { // O(1)
        this.votos = votos;
    }
}
