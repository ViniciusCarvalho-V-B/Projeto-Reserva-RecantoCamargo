import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Imovel implements Serializable {
    private String nome;
    private String descricao;
    private String endereco;
    private double precoBase;
    private Proprietario proprietario;
    private ArrayList<Reserva> reservas;
    private DataDinamica calculoData;

    public Imovel(String nome, String descricao, String endereco, double precoBase, Proprietario proprietario) {
        this.nome = nome;
        this.descricao = descricao;
        this.endereco = endereco;
        this.precoBase = precoBase;
        this.proprietario = proprietario;
        this.reservas = new ArrayList<>();
        this.calculoData = new DataDinamica();
    }

    public boolean validarAnuncio() {
        return descricao != null && !descricao.isEmpty() && 
               endereco != null && !endereco.isEmpty();
    }

    public String getNome() { return nome; }
    public Proprietario getProprietario() { return proprietario; }

    public boolean temReservas() {
        return !reservas.isEmpty();
    }

    public double calcularValorTotal(LocalDate dataEntrada, LocalDate dataSaida) throws DadosInvalidosException {
        if (dataEntrada.isBefore(LocalDate.now())) {
            throw new DadosInvalidosException("A data de entrada nao pode ser no passado.");
        }
        long totalDias = ChronoUnit.DAYS.between(dataEntrada, dataSaida);
        if (totalDias <= 0) {
            throw new DadosInvalidosException("A data de saida deve ser posterior a entrada.");
        }

        double valorTotal = 0;
        LocalDate dataAtual = dataEntrada;

        System.out.println("\n--- Extrato Detalhado ---");
        while (dataAtual.isBefore(dataSaida)) {
            double valorDoDia = calculoData.aplicarSazonalidade(dataAtual, precoBase);
            valorTotal += valorDoDia;
            dataAtual = dataAtual.plusDays(1);
        }
        System.out.println("-------------------------");
        return valorTotal;
    }

    public boolean estaDisponivel(LocalDate dataEntrada, LocalDate dataSaida) {
        for (Reserva reserva : reservas) {
            boolean reservaAtiva = (reserva.getStatus() == StatusReserva.CONFIRMADA || 
                                    reserva.getStatus() == StatusReserva.FINALIZADA ||
                                    reserva.getStatus() == StatusReserva.AGUARDANDO_APROVACAO);
            
            if (reservaAtiva && reserva.sobrepoe(dataEntrada, dataSaida)) {
                return false;
            }
        }
        return true;
    }

    public Reserva criarReserva(Hospede hospede, LocalDate dataEntrada, LocalDate dataSaida, Pagamento pagamento) 
            throws DadosInvalidosException, DataIndisponivelException, PagamentoFalhouException {
        
        if (!validarAnuncio()) {
            throw new DataIndisponivelException("Anuncio invalido (falta descricao ou endereco).");
        }

        double valor = calcularValorTotal(dataEntrada, dataSaida);

        if (!estaDisponivel(dataEntrada, dataSaida)) {
            throw new DataIndisponivelException("Imovel indisponivel neste periodo.");
        }

        pagamento.pagar(valor);

        Reserva novaReserva = new Reserva(hospede, dataEntrada, dataSaida, pagamento);
        reservas.add(novaReserva);

        return novaReserva;
    }

    public void aprovarReserva(int indice) throws DadosInvalidosException {
        if (indice < 0 || indice >= reservas.size()) throw new DadosInvalidosException("Reserva nao encontrada.");
        
        Reserva r = reservas.get(indice);
        if (r.getStatus() != StatusReserva.AGUARDANDO_APROVACAO) {
            throw new DadosInvalidosException("Esta reserva nao esta pendente de aprovacao.");
        }
        r.confirmar();
        System.out.println(">> Reserva APROVADA pelo proprietario! O comprovante ja esta liberado para o hospede.");
    }

    public void cancelarReserva(int indice) throws DadosInvalidosException {
        if (indice < 0 || indice >= reservas.size()) {
            throw new DadosInvalidosException("Reserva nao encontrada.");
        }
        Reserva r = reservas.get(indice);
        
        long diasParaEntrada = ChronoUnit.DAYS.between(LocalDate.now(), r.getEntrada());
        if (diasParaEntrada < 7) {
            throw new DadosInvalidosException("Cancelamento recusado: Faltam menos de 7 dias para o check-in.");
        }

        if (r.getStatus() == StatusReserva.CANCELADA) throw new DadosInvalidosException("Ja cancelada.");
        if (r.getStatus() == StatusReserva.FINALIZADA) throw new DadosInvalidosException("Ja finalizada.");

        r.cancelar();
        System.out.println(">> Processando cancelamento... Politica de reembolso aplicada");
    }

    public void adicionarAvaliacao(int indice, double nota, String comentario) throws DadosInvalidosException {
        if (indice < 0 || indice >= reservas.size()) throw new DadosInvalidosException("Reserva nao encontrada.");
        Reserva r = reservas.get(indice);
        
        if (r.getStatus() == StatusReserva.AGUARDANDO_APROVACAO) {
             throw new DadosInvalidosException("Reserva nao aprovada nao pode ser avaliada.");
        }
        if (r.getStatus() == StatusReserva.CANCELADA) {
            throw new DadosInvalidosException("Reserva cancelada nao pode ser avaliada.");
        }
        if (r.getAvaliacao() != null) {
            throw new DadosInvalidosException("Ja existe avaliacao.");
        }
        r.setAvaliacao(new Avaliacao(nota, comentario));
        r.finalizar();
    }

    public void listarReservas() {
        if (reservas.isEmpty()) System.out.println("Nenhuma reserva neste imovel.");
        else for (int i = 0; i < reservas.size(); i++) System.out.println("[" + i + "] " + reservas.get(i));
    }

    public void listarAvaliacoes() {
        System.out.println("Avaliacoes do imovel " + nome + ":");
        boolean tem = false;
        for (Reserva r : reservas) {
            if (r.getAvaliacao() != null) {
                System.out.println("- " + r.getAvaliacao());
                tem = true;
            }
        }
        if (!tem) System.out.println("Sem avaliacoes.");
    }

    @Override
    public String toString() {
        return "Imovel: " + nome + " | Desc: " + descricao + " | Dono: " + proprietario.nome + " | R$ " + precoBase;
    }
}
