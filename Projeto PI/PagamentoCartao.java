import java.io.Serializable;

public class PagamentoCartao extends Pagamento implements Serializable {
    @Override
    public void pagar(double valor) throws PagamentoFalhouException {
        if (valor <= 0) {
            throw new PagamentoFalhouException("Cartao recusado: valor invalido.");
        }
        System.out.println("Pagamento via CARTAO processado. Valor: R$ " + String.format("%.2f", valor));
    }

    @Override
    public String getTipo() {
        return "Cartao de Credito";
    }
}
