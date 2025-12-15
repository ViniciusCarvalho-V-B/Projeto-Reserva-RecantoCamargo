import java.io.Serializable;

public class Proprietario extends Pessoa implements Serializable {
    private String senha;

    public Proprietario(String nome, String email, String senha) {
        super(nome, email);
        this.senha = senha;
    }

    public boolean validarSenha(String senhaInformada) {
        return this.senha.equals(senhaInformada);
    }
}
