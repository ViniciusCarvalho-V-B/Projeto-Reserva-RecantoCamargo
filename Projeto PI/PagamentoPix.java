import java.io.Serializable;

public class PagamentoPix extends Pagamento implements Serializable {
    @Override
    public void pagar(double valor) throws PagamentoFalhouException {
        if (valor <= 0) {
            throw new PagamentoFalhouException("Valor invalido para transacao Pix.");
        }
        System.out.println("Pagamento via PIX processado. Valor: R$ " + String.format("%.2f", valor));
    }

    @Override
    public String getTipo() {
        return "Pix";
    }
}
