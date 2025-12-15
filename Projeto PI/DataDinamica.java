import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class DataDinamica implements Serializable {
    
    public double aplicarSazonalidade(LocalDate data, double precoBase) {
        double precoFinal = precoBase;
        String motivo = "Tarifa Padrao";

        int dia = data.getDayOfMonth();
        Month mes = data.getMonth();
        DayOfWeek diaSemana = data.getDayOfWeek();

        boolean isAnoNovo = (dia == 1 && mes == Month.JANUARY);
        boolean isNatal = (dia == 25 && mes == Month.DECEMBER);
        boolean isPadroeira = (dia == 12 && mes == Month.OCTOBER);

        if (isAnoNovo || isNatal || isPadroeira) {
            precoFinal = precoBase * 2.0; 
            motivo = "FERIADO ESPECIAL (+100%)";
            
            imprimirExtrato(data, diaSemana, precoFinal, motivo);
            return precoFinal;
        }

        boolean isAltaTemporada = (mes == Month.JANUARY || 
                                   mes == Month.JULY || 
                                   mes == Month.OCTOBER || 
                                   mes == Month.DECEMBER);

        if (isAltaTemporada) {
            precoFinal = precoBase * 1.5;
            motivo = "Alta Temporada (+50%)";
        }

        boolean isFimDeSemana = (diaSemana == DayOfWeek.FRIDAY || 
                                 diaSemana == DayOfWeek.SATURDAY || 
                                 diaSemana == DayOfWeek.SUNDAY);

        if (isFimDeSemana && !isAltaTemporada) {
            precoFinal = precoBase * 1.3; 
            motivo = "Fim de Semana (+30%)";
        } 

        else if (isFimDeSemana && isAltaTemporada) {
             motivo = "Alta Temporada + FDS";
        }

        imprimirExtrato(data, diaSemana, precoFinal, motivo);
        return precoFinal;
    }

    private void imprimirExtrato(LocalDate data, DayOfWeek diaSemana, double valor, String motivo) {
        System.out.println("Data: " + data + " (" + diaSemana + ") | " + 
                           String.format("R$ %.2f", valor) + " | " + motivo);
    }
}
