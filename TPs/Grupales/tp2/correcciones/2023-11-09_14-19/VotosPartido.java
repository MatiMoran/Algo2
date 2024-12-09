package aed;

public class VotosPartido {
    private int presidente;
    private int diputados;

    VotosPartido(int presidente, int diputados) { // O(1)
        this.presidente = presidente;
        this.diputados = diputados;
    }

    public int votosPresidente() { // O(1)
        return presidente;
    }

    public int votosDiputados() { // O(1)
        return diputados;
    }
}
