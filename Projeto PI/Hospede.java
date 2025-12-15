import java.io.Serializable;

public class Hospede extends Pessoa implements Serializable {
    private String whatsapp;

    public Hospede(String nome, String email, String whatsapp) {
        super(nome, email);
        this.whatsapp = whatsapp;
    }

    public String getNome() {
        return nome;
    }

    public String getWhatsapp() {
        return whatsapp;
    }
}
