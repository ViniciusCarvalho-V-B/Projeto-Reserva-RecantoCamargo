/*
 * Projeto P.I Recanto Camargo - Reservas - 
 * 
 * Grupo - Vinicius Carvalho, Vinicius Andrade, Felipe Carvalho, José Vitor, Rafael, Julia
 * */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static Scanner leia = new Scanner(System.in);
    
    static SistemaImoveis sistemaImoveis = new SistemaImoveis();
    static SistemaProprietarios sistemaUsuarios = new SistemaProprietarios();

    public static void main(String[] args) {
        int perfil = -1;
        while (perfil != 0) {
            System.out.println("\n======================================");
            System.out.println("   BEM-VINDO AO RECANTO CAMARGO ");
            System.out.println("======================================");
            System.out.println("1 - Sou PROPRIETARIO");
            System.out.println("2 - Sou HOSPEDE");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");

            try {
                String entrada = leia.nextLine().trim();
                perfil = Integer.parseInt(entrada);

                if (perfil == 1) menuAcessoProprietario();
                else if (perfil == 2) areaDoHospede();
                else if (perfil == 0) System.out.println("Encerrando...");
                else System.out.println("Opcao invalida.");
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas numeros.");
            }
        }
    }

    private static void menuAcessoProprietario() {
        System.out.println("\n--- AREA DO PROPRIETARIO ---");
        System.out.println("1 - Fazer Login");
        System.out.println("2 - Criar Nova Conta");
        System.out.println("0 - Voltar");
        System.out.print("Opcao: ");
        try {
            int op = Integer.parseInt(leia.nextLine());
            if (op == 1) fazerLogin();
            else if (op == 2) criarContaProprietario();
        } catch (NumberFormatException e) {
            System.out.println("Opcao invalida.");
        }
    }

    private static void areaDoHospede() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- MENU HOSPEDE ---");
            System.out.println("1 - Ver Imoveis");
            System.out.println("2 - Selecionar Imovel para Reserva");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");
            try {
                opcao = Integer.parseInt(leia.nextLine());
                switch (opcao) {
                    case 1: listarTodosImoveis(); break;
                    case 2: menuSelecionarImovel(); break;
                    case 0: break;
                    default: System.out.println("Opcao invalida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro de digitacao.");
            }
        }
    }

    private static void criarContaProprietario() {
        System.out.println("\n--- NOVO CADASTRO ---");
        System.out.print("Nome: ");
        String nome = leia.nextLine();
        System.out.print("Email: ");
        String email = leia.nextLine();
        System.out.print("Senha: ");
        String senha = leia.nextLine();
        try {
            sistemaUsuarios.cadastrarProprietario(nome, email, senha);
            System.out.println("Conta criada!");
        } catch (DadosInvalidosException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void fazerLogin() {
        System.out.print("Email: ");
        String email = leia.nextLine();
        System.out.print("Senha: ");
        String senha = leia.nextLine();
        Proprietario dono = sistemaUsuarios.fazerLogin(email, senha);
        if (dono != null) {
            System.out.println("Ola, " + dono.nome + "!");
            menuLogadoProprietario(dono);
        } else {
            System.out.println("Login invalido.");
        }
    }

    private static void menuLogadoProprietario(Proprietario dono) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- PAINEL PROPRIETARIO ---");
            System.out.println("1 - Cadastrar Imovel");
            System.out.println("2 - Listar Meus Imoveis");
            System.out.println("3 - Relatorio de Reservas");
            System.out.println("4 - Excluir Imovel");
            System.out.println("5 - Aprovar Reservas Pendentes");
            System.out.println("0 - Logout");
            System.out.print("Escolha: ");
            try {
                opcao = Integer.parseInt(leia.nextLine());
                switch (opcao) {
                    case 1: menuCadastrarImovel(dono); break;
                    case 2: listarMeusImoveis(dono); break;
                    case 3: verRelatorioGeral(dono); break;
                    case 4: excluirImovel(dono); break;
                    case 5: aprovarReservasPendentes(dono); break;
                    case 0: break;
                    default: System.out.println("Opcao invalida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro de digitacao.");
            }
        }
    }

    private static void menuCadastrarImovel(Proprietario dono) {
        System.out.println("\n--- NOVO IMOVEL ---");
        System.out.print("Titulo: ");
        String nome = leia.nextLine();
        System.out.print("Descricao: ");
        String desc = leia.nextLine();
        System.out.print("Endereco: ");
        String end = leia.nextLine();
        System.out.print("Preco Diario: ");
        try {
            double preco = Double.parseDouble(leia.nextLine());
            sistemaImoveis.cadastrarImovel(nome, desc, end, preco, dono);
        } catch (NumberFormatException e) {
            System.out.println("Preco invalido.");
        }
    }

    private static void aprovarReservasPendentes(Proprietario dono) {
        System.out.println("\n--- APROVACAO DE RESERVAS ---");
        var lista = sistemaImoveis.getListaImoveis();
        boolean achou = false;

        for (Imovel imovel : lista) {
            if (imovel.getProprietario().email.equals(dono.email)) {
                if (imovel.temReservas()) {
                    System.out.println("Imovel: " + imovel.getNome());
                    imovel.listarReservas();
                    
                    System.out.print("Deseja aprovar alguma reserva deste imovel? (S/N): ");
                    if (leia.nextLine().equalsIgnoreCase("S")) {
                        System.out.print("Digite o ID da reserva: ");
                        try {
                            int id = Integer.parseInt(leia.nextLine());
                            imovel.aprovarReserva(id); // A mensagem de sucesso esta dentro deste metodo
                        } catch (Exception e) {
                            System.out.println("Erro: " + e.getMessage());
                        }
                    }
                    achou = true;
                }
            }
        }
        if (!achou) System.out.println("Voce nao tem reservas pendentes.");
        sistemaImoveis.salvarDados();
    }

    private static void listarTodosImoveis() {
        var lista = sistemaImoveis.getListaImoveis();
        if (lista.isEmpty()) System.out.println("Nenhum imovel.");
        else for (int i=0; i<lista.size(); i++) System.out.println("["+i+"] "+lista.get(i));
    }

    private static void listarMeusImoveis(Proprietario dono) {
        var lista = sistemaImoveis.getListaImoveis();
        for (int i=0; i<lista.size(); i++) {
            if (lista.get(i).getProprietario().email.equals(dono.email))
                System.out.println("["+i+"] "+lista.get(i));
        }
    }

    private static void excluirImovel(Proprietario dono) {
        listarMeusImoveis(dono);
        System.out.print("ID para excluir: ");
        try {
            int id = Integer.parseInt(leia.nextLine());
            Imovel alvo = sistemaImoveis.buscarImovel(id);
            if (alvo != null && alvo.getProprietario().email.equals(dono.email)) {
                sistemaImoveis.removerImovel(id);
                System.out.println("Excluido.");
            } else System.out.println("Erro na exclusao.");
        } catch (Exception e) { System.out.println("Erro."); }
    }

    private static void verRelatorioGeral(Proprietario dono) {
        for (Imovel im : sistemaImoveis.getListaImoveis()) {
            if (im.getProprietario().email.equals(dono.email)) {
                System.out.println("Imovel: " + im.getNome());
                im.listarReservas();
            }
        }
    }

    private static void menuSelecionarImovel() {
        listarTodosImoveis();
        System.out.print("Digite ID do imovel: ");
        try {
            int id = Integer.parseInt(leia.nextLine());
            Imovel im = sistemaImoveis.buscarImovel(id);
            if (im != null) menuDoImovel(im);
            else System.out.println("Nao encontrado.");
        } catch (Exception e) { System.out.println("Erro."); }
    }

    private static void menuDoImovel(Imovel imovel) {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- " + imovel.getNome() + " ---");
            System.out.println("1 - Reservar");
            System.out.println("2 - Ver Avaliacoes");
            System.out.println("3 - Avaliar Estadia");
            System.out.println("4 - Cancelar Reserva");
            System.out.println("0 - Voltar");
            System.out.print("Opcao: ");
            try {
                op = Integer.parseInt(leia.nextLine());
                switch (op) {
                    case 1: realizarReserva(imovel); break;
                    case 2: imovel.listarAvaliacoes(); break;
                    case 3: realizarAvaliacao(imovel); break;
                    case 4: realizarCancelamento(imovel); break;
                    case 0: break;
                }
            } catch (Exception e) { System.out.println("Erro."); }
        }
        sistemaImoveis.salvarDados();
    }

    private static void realizarReserva(Imovel imovel) {
        try {
            System.out.print("Nome: "); String nome = leia.nextLine();
            System.out.print("Email: "); String email = leia.nextLine();
            System.out.print("WhatsApp: "); String zap = leia.nextLine();
            Hospede h = new Hospede(nome, email, zap);

            LocalDate ent = lerData("Entrada (dd/MM/yyyy): ");
            LocalDate sai = lerData("Saida (dd/MM/yyyy): ");
            System.out.println("1- Pix | 2- Cartao");
            Pagamento pag = leia.nextLine().equals("1") ? new PagamentoPix() : new PagamentoCartao();

            double valor = imovel.calcularValorTotal(ent, sai);
            System.out.println("Total: R$ " + String.format("%.2f", valor));
            System.out.print("Pagar e Solicitar? (S/N): ");
            if (leia.nextLine().equalsIgnoreCase("S")) {
                Reserva r = imovel.criarReserva(h, ent, sai, pag);
                System.out.println("Solicitacao enviada! Aguarde aprovacao do dono.");
            }
        } catch (Exception e) { System.out.println("Erro: " + e.getMessage()); }
    }

    private static void realizarAvaliacao(Imovel imovel) {
        if (!imovel.temReservas()) { System.out.println("Sem reservas."); return; }
        imovel.listarReservas();
        System.out.print("ID Reserva: ");
        try {
            int id = Integer.parseInt(leia.nextLine());
            System.out.print("Nota: "); double nota = Double.parseDouble(leia.nextLine());
            System.out.print("Comentario: "); String txt = leia.nextLine();
            imovel.adicionarAvaliacao(id, nota, txt);
            System.out.println("Avaliado!");
        } catch (Exception e) { System.out.println("Erro: " + e.getMessage()); }
    }

    private static void realizarCancelamento(Imovel imovel) {
        if (!imovel.temReservas()) { System.out.println("Sem reservas."); return; }
        imovel.listarReservas();
        System.out.print("ID Reserva: ");
        try {
            int id = Integer.parseInt(leia.nextLine());
            imovel.cancelarReserva(id);
        } catch (Exception e) { System.out.println("Erro: " + e.getMessage()); }
    }

    private static LocalDate lerData(String msg) throws DadosInvalidosException {
        System.out.print(msg);
        try { return LocalDate.parse(leia.nextLine(), FORMATO_DATA); }
        catch (DateTimeParseException e) { throw new DadosInvalidosException("Data invalida."); }
    }
}
