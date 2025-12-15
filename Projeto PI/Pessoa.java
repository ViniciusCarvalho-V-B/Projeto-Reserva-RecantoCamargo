import java.io.Serializable;

public class Pessoa implements Serializable {
    protected String nome;
    protected String email;

    public Pessoa(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }
}
