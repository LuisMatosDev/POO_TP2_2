package oop.tp2_2.utils;

import oop.tp2_2.models.Partido;
import java.util.List;

/**
 * Trata da validação e aplica restrições e limites eleitorais.
 * Assegura que os partidos cumprem os requisitos minímos e aplica limites de votação.
 * @author Luis Matos
 */
public class ValidadorEleicao
{
    // Constantes para limites eleitorais (em percentagens)
    private static final double PERCENTAGEM_LIMITE_MIN = 1.75;
    private static final double PERCENTAGEM_LIMITE_MAX = 36.25;
    private static final double PERCENTAGEM_DIFERENCA_MAX = 1.75;
    
    /**
     * Método para validar e aplicar restrições eleitorais à lista de partidos.
     * Mostra a informação sobre as restrições e aplica o limite máximo de votos.
     * @param partidos Lista de todos os partidos participantes.
     * @param totalVotos Número total de votos válidos lançados na eleição.
     */
    
    public static void validaEAplicaRestricoes(List<Partido> partidos, int totalVotos)
    {
        System.out.println("=== A APLICAR RESTRIÇÕES ELEITORAIS ===");
        
        // Calcula o valor absoluto de votos a partir das percentagens.
        int limiteVotosMin = calculaLimiteVotosMin(totalVotos);
        int limiteVotosMax = calculaLimiteVotosMax(totalVotos);
        int diferencaMaxVotos = calculaDiferencaMaxVotos(totalVotos);
        
        // Mostra a informação sobre as restrições ao utilizador.
        mostraInfoRestricoes(limiteVotosMin, limiteVotosMax, diferencaMaxVotos, totalVotos);
        
        // Aplica o limite máximo de votos aos partidos no topo.
        aplicaLimiteVotosMax(partidos, limiteVotosMax, diferencaMaxVotos);
    }
    
    /**
     * Calcula o valor minímo de votos necessários para a alocação de assentos parlamentares.
     * @param totalVotos Número total de votos válidos lançados na eleição.
     * @return Limite minímo de votos em números absolutos a partir das percentagens.
     */
    public static int calculaLimiteVotosMin(int totalVotos)
    {
        return (int) (totalVotos * PERCENTAGEM_LIMITE_MIN / 100);
    }
    
    /**
     * Aplica um filtro à lista de partidos e retorna aqueles que cumprem com o limite minímo para alocação
     * de assentos parlamentares.
     * @param partidos Lista de todos os partidos.
     * @param limiteVotosMin Limite minímo de votos necessário para alocação de assentos.
     * @return Retorna a lista de partidos elegíveis.
     */
    public static List<Partido> getPartidosElegiveis(List<Partido> partidos, int limiteVotosMin)
    {
        return partidos.stream().filter(partido -> partido.getVotos() >= limiteVotosMin).toList();
    }
    
    /**
     * Calcula o limite máximo de votos permitidos a cada partido (30% do total de votos lançados).
     * @param totalVotos Número total de votos lançados na eleição
     * @return Retorna o valor máximo de votos em números absolutos a partir das percentagens.
     */
    private static int calculaLimiteVotosMax(int totalVotos)
    {
        return (int) (totalVotos * PERCENTAGEM_LIMITE_MAX / 100);
    }
    
    /**
     * Calcula a diferença máxima de votos entre os dois partidos mais votados.
     * @param totalVotos Número total de votos lançados na eleição
     * @return Retorna o valor da diferença máxima de votos em números absolutos a partir das percentagens. 
     */
    private static int calculaDiferencaMaxVotos(int totalVotos)
    {
        return (int) (totalVotos * PERCENTAGEM_DIFERENCA_MAX / 100);
    }
    
    /**
     * Mostra a informação sobre as restrições e limites a aplicar na eleição.
     * @param limiteVotosMin Limite minímo de votos para elegibilidade.
     * @param limiteVotosMax Limite máximo de votos entre os dois primeiros partidos.
     * @param diferencaMaxVotos Diferença máxima de votos permitida entre os dois primeiros partidos.
     * @param totalVotos Total de votos lançados para efeitos de cálculo das percentagens.
     */
    private static void mostraInfoRestricoes(int limiteVotosMin, int limiteVotosMax,
            int diferencaMaxVotos, int totalVotos)
    {
        System.out.printf("Limite minímo: %.2f%% (%d votos)%n", PERCENTAGEM_LIMITE_MIN, limiteVotosMin);
        System.out.printf("Limite máximo: %.2f%% (%d votos)%n", PERCENTAGEM_LIMITE_MAX, limiteVotosMax);
        System.out.printf("Diferença máxima (dois primeiros): %.2f%% (%d votos)%n", PERCENTAGEM_DIFERENCA_MAX, diferencaMaxVotos);
    }
    
    /**
     * Aplica os limites máximos de votos e restrições de diferenças de votos entre os dois primeiros partidos.
     * Numa implementação real, aqui iriam ser ajustadas as contagens de votos para cada partido.
     * @param partidos Lista de todos os partidos participantes.
     * @param limiteVotosMax Número máximo de votos permitido para cada partido.
     * @param diferencaMaxVotos Diferença máxima de votos permitida aos dois primeiros partidos.
     */
    private static void aplicaLimiteVotosMax(List<Partido> partidos, int limiteVotosMax, int diferencaMaxVotos)
    {
        //
        List<Partido> ordenaPorVotos = partidos.stream()
                .sorted((p1,p2) -> Integer.compare(p2.getVotos(), p1.getVotos())).toList();
        
        // Ordena os partidos por número de votos em ordem descendente
        // de forma a identificar os dois maiores partidos 
        if (ordenaPorVotos.size() >= 2)
        {
            Partido primeiro = ordenaPorVotos.get(0);
            Partido segundo = ordenaPorVotos.get(1);
            
            // Verifica a existência de pelo menos dois partidos para aplicar as restrições
            System.out.printf("\nDois primeiros partidos antes de restrições:%n");
            System.out.printf("1. %s: %,d votos%n", primeiro.getNome(), primeiro.getVotos());
            System.out.printf("2. %s: %,d votos%n", segundo.getNome(), segundo.getVotos());
            
            // Verifica e mostra as violações de limites máximos de votos, se existirem
            if (primeiro.getVotos() > limiteVotosMax || segundo.getVotos() > limiteVotosMax)
            {
                System.out.println("Limite máximo a ser aplicado aos 2 primeiros partidos");
            }
            
            // Verifica e mostra as violações na diferença máxima de votos, se existirem
            int diferencaVotos = Math.abs(primeiro.getVotos() - segundo.getVotos());
            if (diferencaVotos > diferencaMaxVotos)
            {
                System.out.printf("Restrição de diferença máxima de votos a ser aplicada (atual: %,d, máxima: %,d)%n",
                        diferencaVotos, diferencaMaxVotos);
            }
        }
    }
}
