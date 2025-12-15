import java.io.Serializable;

public abstract class Pagamento implements Serializable {
    public abstract void pagar(double valor) throws PagamentoFalhouException;
    public abstract String getTipo();
}
