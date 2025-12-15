import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reserva implements Serializable {
    private Hospede hospede;
    private LocalDate entrada;
    private LocalDate saida;
    private StatusReserva status;
    private Avaliacao avaliacao;
    private Pagamento pagamento; 

    public Reserva(Hospede hospede, LocalDate entrada, LocalDate saida, Pagamento pagamento) {
        this.hospede = hospede;
        this.entrada = entrada;
        this.saida = saida;
        this.pagamento = pagamento;
        this.status = StatusReserva.AGUARDANDO_APROVACAO;
        this.avaliacao = null;
    }

    public LocalDate getEntrada() {
        return entrada;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void confirmar() {
        this.status = StatusReserva.CONFIRMADA;
    }
    
    public void cancelar() {
        this.status = StatusReserva.CANCELADA;
    }
    
    public void finalizar() {
        this.status = StatusReserva.FINALIZADA;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public Hospede getHospede() {
        return hospede;
    }

    public long getNoites() {
        return ChronoUnit.DAYS.between(entrada, saida);
    }

    public boolean sobrepoe(LocalDate dataEntrada, LocalDate dataSaida) {
        return dataEntrada.isBefore(saida) && dataSaida.isAfter(entrada);
    }

    @Override
    public String toString() {
        String texto = "Hospede: " + hospede.getNome() + 
                       " | Periodo: " + entrada + " a " + saida + 
                       " | Status: " + status;
        
        if (avaliacao != null) {
            texto += "\n   [Avaliacao]: " + avaliacao.toString();
        }
        return texto;
    }
}
