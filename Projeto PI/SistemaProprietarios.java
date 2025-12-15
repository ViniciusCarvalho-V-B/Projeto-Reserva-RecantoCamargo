import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SistemaProprietarios {
    private ArrayList<Proprietario> listaProprietarios;
    private final String CAMINHO_ARQUIVO = "banco_proprietarios.dat";

    public SistemaProprietarios() {
        this.listaProprietarios = new ArrayList<>();
        carregarDados();
    }

    public void cadastrarProprietario(String nome, String email, String senha) throws DadosInvalidosException {
    
        for (Proprietario p : listaProprietarios) {
            if (p.email.equals(email)) {
                throw new DadosInvalidosException("Este email ja esta cadastrado.");
            }
        }
        
        Proprietario novo = new Proprietario(nome, email, senha);
        listaProprietarios.add(novo);
        salvarDados();
    }

    public Proprietario fazerLogin(String email, String senha) {
        for (Proprietario p : listaProprietarios) {
            if (p.email.equals(email) && p.validarSenha(senha)) {
                return p;
            }
        }
        return null;
    }

    private void salvarDados() {
        try (ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream(CAMINHO_ARQUIVO))) {
            escritor.writeObject(listaProprietarios);
        } catch (IOException erro) {
            System.out.println("Erro ao salvar proprietarios: " + erro.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) return;

        try (ObjectInputStream leitor = new ObjectInputStream(new FileInputStream(arquivo))) {
            listaProprietarios = (ArrayList<Proprietario>) leitor.readObject();
        } catch (IOException | ClassNotFoundException erro) {
            System.out.println("Erro ao carregar proprietarios: " + erro.getMessage());
        }
    }
}
