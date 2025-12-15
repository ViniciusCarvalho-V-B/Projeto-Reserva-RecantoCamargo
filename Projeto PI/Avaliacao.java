import java.io.Serializable;

public class Avaliacao implements Serializable {
    private double nota;
    private String comentario;

    public Avaliacao(double nota, String comentario) {
        this.nota = nota;
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "Nota: " + nota + " | Comentario: " + comentario;
    }
}
