import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SistemaImoveis {
    private ArrayList<Imovel> listaImoveis;
    private final String CAMINHO_ARQUIVO = "banco_imoveis.dat";

    public SistemaImoveis() {
        this.listaImoveis = new ArrayList<>();
        carregarDados();
    }

    public void cadastrarImovel(String nome, String descricao, String endereco, double precoBase, Proprietario dono) {
        Imovel novo = new Imovel(nome, descricao, endereco, precoBase, dono);
        
        listaImoveis.add(novo);
        salvarDados();
        System.out.println("Imovel cadastrado com sucesso!");
    }

    public ArrayList<Imovel> getListaImoveis() {
        return listaImoveis;
    }

    public Imovel buscarImovel(int indice) {
        if (indice >= 0 && indice < listaImoveis.size()) {
            return listaImoveis.get(indice);
        }
        return null;
    }

    public boolean removerImovel(int indice) {
        if (indice >= 0 && indice < listaImoveis.size()) {
            listaImoveis.remove(indice);
            salvarDados();
            return true;
        }
        return false;
    }

    public void salvarDados() {
        try (ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream(CAMINHO_ARQUIVO))) {
            escritor.writeObject(listaImoveis);
        } catch (IOException erro) {
            System.out.println("Erro ao salvar sistema: " + erro.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) return;

        try (ObjectInputStream leitor = new ObjectInputStream(new FileInputStream(arquivo))) {
            listaImoveis = (ArrayList<Imovel>) leitor.readObject();
        } catch (IOException | ClassNotFoundException erro) {
            System.out.println("Erro ao carregar sistema: " + erro.getMessage());
        }
    }
}
