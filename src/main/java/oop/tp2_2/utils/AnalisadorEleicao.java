package oop.tp2_2.utils;

import oop.tp2_2.models.Partido;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Providencia os métodos de análise e demonstração de forma a verificar a veracidade das afirmações-chave
 * sobre o método D'Hondt.
 * Contem métodos para demonstrar vantagem das coligações e favoritismo de partidos grandes.
 * @author Luis Matos
 */
public class AnalisadorEleicao
{
    // Constantes referentes a dados das eleições para efetuar os cálculos.
    private static final int TOTAL_ASSENTOS = 230;
    private static final int TOTAL_VOTOS = 6000000;
    
    /**
     * Método para demonstrar a afirmação #1:
     * "'É mais vantajoso para 2 partidos concorrerem coligados do que separados.'"
     * Mostra como partidos coligados adquirem mais assentos, do que se concorrem separados.
     * @param partidos Lista de todos os partidos, inclusive coligações.
     */
    public static void demonstraVantagemColigacao(List<Partido> partidos)
    {
        System.out.println("\nAfirmaçao #1: 'É mais vantajoso para 2 partidos concorrerem coligados do que separados.'");
        
        // Encontra partidos coligados na lista
        Partido coligacao = null;
        for (Partido partido : partidos)
        {
            if(partido.eColigacao())
            {
                coligacao = partido;
                break;
            }
        }
        
        if (coligacao != null)
        {
            System.out.println("Exemplo: " + coligacao.getNome());
            System.out.println("Membros de coligação: " + String.join(" + ", coligacao.getMembrosColigacao()));
            
            System.out.printf("Total de votos da coligação: %,d%n", coligacao.getVotos());
            System.out.printf("Assentos alocados à coligação: %d%n", coligacao.getAssentos());
            
            // Estima quantos assentos os membros da coligação obteriam se concorressem em separado.
            int estimaAssentosIndividual = estimativaAssentosIndividual(coligacao);
            System.out.printf("Número de assentos estimado, se concorrem em separado: ~%d%n", estimaAssentosIndividual);
            System.out.printf("Vantagem da coligação: +%d assentos%n", coligacao.getAssentos() - estimaAssentosIndividual);
        }
    }
    
    /**
     * Método para demonstrar a afirmação #2:
     * "'O método D'Hondt favorece os partidos maiores'"
     * Analisa a eficiência dos votos (votos por assento) para mostrar que partidos grandes têm melhor representação.
     * @param partidos Lista de todos os partidos ordenada por número de votos.
     */
    public static void demonstraVantagemPartidoGrande(List<Partido> partidos)
    {
        System.out.println("\nAfirmação #2: 'O método D'Hondt favorece os partidos maiores'");
        System.out.println("Análise da eficiência de votos por assento:");
        
        // Ordena partidos por votos em ordem descendente para analisar os partidos grandes primeiro.
        List<Partido> ordenaPorVotos = partidos.stream().sorted((p1,p2) -> 
                Integer.compare(p2.getVotos(), p1.getVotos())).toList();
        
        // Calcula e mostra métricas de eficiência para cada partido com assentos.
        for (Partido partido : ordenaPorVotos)
        {
            if (partido.getAssentos() > 0)
            {
                double votosPorAssento = (double) partido.getVotos() / partido.getAssentos();
                double eficiencia = calculaEficiencia(partido);
                
                System.out.printf("%-20s: %,d votos/assento (Taxa de eficiência: %.3f)%n",
                        partido.getNome(), (int) votosPorAssento, eficiencia);
            }
        }
        
        // Explica as métricas de eficiência ao utilizador.
        System.out.println("\nInterpretação:");
        System.out.println("- Menos votos/assento = maior eficiência de representação.");
        System.out.println("- Taxa de eficiência > 1.0 = Tratamento mais favorável.");
        System.out.println("- Partidos maiores tendem a ter uma taxa de eficiência mais alta.");
    }
    
    /**
     * Método para mostrar resultados eleitorais compreensivos numa tabela formatada.
     * Mostra votos, percentagens, assentos, e métricas de eficiência para todos os partidos.
     * @param partidos Lista de todos os partidos participantes.
     * @param totalAssentos Total de assentos parlamentares.
     * @param totalVotos Total de votos válidos lançados.
     */
    public static void mostraResultadosEleicao(List<Partido> partidos, int totalAssentos, int totalVotos)
    {
        System.out.println("\n=== RESULTADOS FINAIS DA ELEIÇÃO ===");
        System.out.printf("Parlamento da República Portuguesa - %d Assentos%n", totalAssentos);
        System.out.printf("Total de votos lançados: %,d%n", totalVotos);
        System.out.printf("Taxa de Abstenção: 40.16%% (4,016,000 de eleitores)%n%n");
        
        // Ordena os partidos pelo número de assentos ganhos em ordem descendente para mostrar os resultados.
        List<Partido> ordenaPorAssentos = partidos.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getAssentos(), p1.getAssentos())).toList();
        
        // Cabeçalho da tabela para mostrar os resultados.
        System.out.println("Partido   |    Votos    |    %    |  Assentos  |  % Assentos  |  Votos/Assento  |");
        System.out.println("----------|-------------|---------|------------|--------------|-----------------|");
        
        // Mostra os resultados de cada partido numa tabela.
        for (Partido partido : ordenaPorAssentos)
        {
            if (partido.getAssentos() > 0)
            {
                // Apenas mostra partidos que ganharam assentos.
                double percentagemVoto = (partido.getVotos() * 100.0 / totalVotos);
                double percentagemAssento = (partido.getAssentos() * 100.0 / totalAssentos);
                double votosPorAssento = partido.getAssentos() > 0 ?
                        (double) partido.getVotos() / partido.getAssentos() : 0;
                
                System.out.printf("%-9s | %,11d | %7.2f | %10d | %12.2f | %,15.0f |%n",
                        partido.getNome(), partido.getVotos(), percentagemVoto,
                        partido.getAssentos(), percentagemAssento, votosPorAssento);
            }
        }
    }
    
    /**
     * Método para estimar o número de assentos que os membros da coligação
     * obteriam se concorressem individualmente.
     * Método de estimativa simplificado para efeitos da demonstração.
     * @param coligacao Coligacao a analisar
     * @return O número de assentos estimado, se os membros da coligacao concorrem individualmente.
     */
    private static int estimativaAssentosIndividual(Partido coligacao)
    {
        // Estimativa simplificada - numa análise real, utilizaríamos cálculos do método D'Hondt separados. 
        // Utilizamos uma heurística baseada num padrão típico de distribuição de assentos.
        int assentosEstimados = 0;
        for (String membro : coligacao.getMembrosColigacao())
        {
            // Estimativa: assume que partidos pequenos necessitam de mais votos por assento
            // e contabiliza efeitos das restrições.
            assentosEstimados += (int) (coligacao.getVotos() * 0.4 / coligacao.getMembrosColigacao()
                    .size() / 25000);
        }
        return Math.max(1, assentosEstimados); // Assegura pelo menos um lugar para efeitos da demonstração.
    }
    
    /**
     * Método para calcular o rácio de eficiência para um dado partido
     * (percentagem de assentos / percentagem de votos)
     * Valores acima de 1.0 indicam tratamento favorável por parte do sistema eleitoral.
     * @param partido O partido para o qual iremos calcular a eficiência
     * @return Rácio de eficiência (maior = tratamento mais favorável)
     */
    private static double calculaEficiencia(Partido partido)
    {
        double percentagemAssentos = (partido.getAssentos() * 100.0 / TOTAL_ASSENTOS);
        double percentagemVotos = (partido.getVotos() * 100.0 / TOTAL_VOTOS);
        return percentagemAssentos / percentagemVotos; //
    }
    
    /**
     * 
     */
    public static void realizaAnaliseComparativa(List<Partido> comPartidosColigados,
            List<Partido> semPartidosColigados, int totalAssentos, int totalVotos)
    {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("ANÁLISE COMPARATIVA: CENÁRIO COM COLIGAÇÃO VS. SEM COLIGAÇÃO");
        System.out.println("=".repeat(80));
        
        //
        System.out.println("\nCOMPARAÇÃO DE RESULTADOS:");
        System.out.println("Partido   |    Com Coligação    |    Sem Coligação    |  Diferença  |  Vantagem  |");
        System.out.println("----------|---------------------|---------------------|-------------|------------|");
        
        //
        Map<String, Integer> assentosColigacao = criaMapaAssentos(comPartidosColigados);
        Map<String, Integer> semAssentosColigacao = criaMapaAssentos(semPartidosColigados);
        
        //
        int totalAssentosColigacao = 0;
        int totalAssentosSemColigacao = 0;
        
        //
        for (Partido partido : comPartidosColigados)
        {
            String nomePartido = partido.getNome();
            int contagemAssentosColigacao = assentosColigacao.getOrDefault(nomePartido, 0);
            int contagemAssentosSemColigacao = semAssentosColigacao.getOrDefault(nomePartido, 0);
            int diferenca = contagemAssentosColigacao - contagemAssentosSemColigacao;
            
            totalAssentosColigacao += contagemAssentosColigacao;
            totalAssentosSemColigacao += contagemAssentosSemColigacao;
            
            String vantagem = calculaIndicadorVantagem(diferenca);
            
            System.out.printf("%-9s | %,21d | %,21d | %13d | %s%n", nomePartido,
                    contagemAssentosColigacao, contagemAssentosSemColigacao, diferenca, vantagem);
        }
        
        //
        mostraSumarioComparativo(totalAssentosColigacao, totalAssentosSemColigacao, comPartidosColigados,
                                    semPartidosColigados);
        
        //
        mostraPercepcaoComparativa(comPartidosColigados, semPartidosColigados, assentosColigacao,
                                        semAssentosColigacao);
    }
    
    /**
     * 
     */
    private static Map<String, Integer> criaMapaAssentos(List<Partido> partidos)
    {
        Map<String, Integer> mapaAssentos = new HashMap<>();
        for (Partido partido : partidos)
        {
            mapaAssentos.put(partido.getNome(), partido.getAssentos());
        }
        return mapaAssentos;
    }
    
    /**
     * 
     */
    private static String calculaIndicadorVantagem(int diferenca)
    {
        if (diferenca > 0)
        {
            return "↑ " + diferenca + " vantagem";
        }
        else if (diferenca < 0)
        {
            return "↓ " + diferenca + " desvantagem";
        }
        else
        {
            return "= sem alteração";
        }
    }
    
    /**
     * 
     */
    private static void mostraSumarioComparativo(int totalColigacao, int totalSemColigacao,
                                                    List<Partido> comPartidosColigados,
                                                    List<Partido> semPartidosColigados)
    {
        System.out.println("\n" + "-".repeat(80));
        System.out.println("RESUMO ESTATÍSTICO DA ANÁLISE COMPARATIVA:");
        System.out.println("-".repeat(80));
        
        System.out.printf("Total de assentos (com coligação): %d%n", totalColigacao);
        System.out.printf("Total de assentos (sem coligação): %d%n", totalSemColigacao);
        System.out.printf("Número de partidos (com coligação): %d%n", comPartidosColigados.size());
        System.out.printf("Número de partidos (sem coligação): %d%n", semPartidosColigados.size());
        
        //
        double eficienciaComColigacao = calculaMediaEficiencia(comPartidosColigados);
        double eficienciaSemColigacao = calculaMediaEficiencia(semPartidosColigados);
        
        System.out.printf("Eficiência média (com coligação): %.3f%n", eficienciaComColigacao);
        System.out.printf("Eficiência média (sem coligação): %.3f%n", eficienciaSemColigacao);
    }
    
    /**
     * 
     */
    private static double calculaMediaEficiencia(List<Partido> partidos)
    {
        double totalEficiencia = 0;
        int contagem = 0;
        
        for (Partido partido : partidos)
        {
            if (partido.getAssentos() > 0)
            {
                double percentagemAssentos = (partido.getAssentos() * 100.00 / TOTAL_ASSENTOS);
                double percentagemVotos = (partido.getVotos() * 100.00 / TOTAL_VOTOS);
                
                if (percentagemVotos > 0) //
                {
                    totalEficiencia += (percentagemAssentos / percentagemVotos);
                    contagem++;
                }
            }
        }
        
        return contagem > 0 ? totalEficiencia / contagem : 0;
    }
    
    /**
     * 
     */
    private static void mostraPercepcaoComparativa(List<Partido> partidosComColigacao,
            List<Partido> partidosSemColigacao, Map<String, Integer> assentosColigacao,
            Map<String, Integer> assentosSemColigacao)
    {
        System.out.println("\n" + "~".repeat(80));
        System.out.println("OBSERVAÇÕES ESTRATÉGICAS:");
        System.out.println("~".repeat(80));
        
        //
        Partido coligacao = encontraPartidoColigacao(partidosComColigacao);
        if (coligacao != null)
        {
            String nomeColigacao = coligacao.getNome();
            int contagemAssentosColigacao = assentosColigacao.getOrDefault(nomeColigacao, 0);
            
            //
            int totalAssentosSeparados = 0;
            for (String membro : coligacao.getMembrosColigacao())
            {
                totalAssentosSeparados += assentosSemColigacao.getOrDefault(membro, 0);
            }
            
            int vantagemColigacao = contagemAssentosColigacao + totalAssentosSeparados;
            
            System.out.println("1. IMPACTO DA COLIGAÇÃO:");
            System.out.printf("  - %s obteve %d assentos como coligação%n", nomeColigacao,
                    contagemAssentosColigacao);
            System.out.printf("  - Os membros individuais teriam obtido %d assentos parlamentares separadamente%n",
                    totalAssentosSeparados);
            System.out.printf("  - Vantagem líquida da coligação: +%d assentos%n", vantagemColigacao);
            
            if (vantagemColigacao > 0)
            {
                System.out.println("  → CONCLUSÃO: A coligação foi estrategicamente vantajosa.");
            }
            else
            {
                System.out.println("  → CONCLUSÃO: A coligação não trouxe vantagem significativa.");
            }
        }
        
        //
        analisaEfeitoRestricoes(partidosComColigacao, partidosSemColigacao);
        
        //
        analisaVantagemPartidosMaiores(partidosComColigacao, partidosSemColigacao);
    }
    
    /**
     * 
     */
    private static Partido encontraPartidoColigacao(List<Partido> partidos)
    {
        for (Partido partido : partidos)
        {
            if (partido.eColigacao())
            {
                return partido;
            }
        }
        return null;
    }
    
    /**
     * 
     */
    private static void analisaEfeitoRestricoes(List<Partido> partidosComColigacao,
            List<Partido> partidosSemColigacao)
    {
        int limiteVotosMin = (int) (TOTAL_VOTOS * 1.75 / 100);
        
        System.out.println("\n2. EFEITO DO LIMITE ELEITORAL (1.75%):");
        
        //
        long abaixoLimiteComColigacao = partidosComColigacao.stream()
                .filter(p -> p.getVotos() < limiteVotosMin && p.getAssentos() == 0).count();
        
        long abaixoLimiteSemColigacao = partidosSemColigacao.stream()
                .filter(p -> p.getVotos() < limiteVotosMin && p.getAssentos() == 0).count();
        
        System.out.printf("  - Partidos abaixo do limite (com coligação): %d%n", abaixoLimiteComColigacao);
    
        System.out.printf("  - Partidos abaixo do limite (sem coligação): %d%n", abaixoLimiteSemColigacao);
        
        if (abaixoLimiteComColigacao != abaixoLimiteSemColigacao)
        {
            System.out.println("  → As coligações podem ajudar partidos pequenos a superar o limite");
        }
    }
    
    /**
     * 
     */
    private static void analisaVantagemPartidosMaiores(List<Partido> partidosComColigacao,
            List<Partido> partidosSemColigacao)
    {
        System.out.println("\n3. FAVORECIMENTO DE PARTIDOS GRANDES:");
        
        //
        double[] eficienciaColigacao = calculaEficienciaTopoVsFundo(partidosComColigacao);
        double[] eficienciaSemColigacao = calculaEficienciaTopoVsFundo(partidosSemColigacao);
        
        System.out.printf("  - Eficiência dos 2 primeiros partidos (com coligação): %.3f%n",
                eficienciaColigacao[0]);
        System.out.printf("  - Eficiência dos outros partidos (com coligação): %.3f%n",
                eficienciaColigacao[1]);
        System.out.printf("  - Eficiência dos 2 primeiros partidos (sem coligação): %.3f%n",
                eficienciaSemColigacao[0]);
        System.out.printf("  - Eficiência dos outros partidos (sem coligação): %.3f%n",
                eficienciaSemColigacao[1]);
        
        if (eficienciaColigacao[0] > eficienciaColigacao[1] ||
                eficienciaSemColigacao[0] > eficienciaSemColigacao[1])
        {
            System.out.println("  → O método D'Hondt favorece consistentemente os partidos maiores.");
        }
    }
    
    /**
     * 
     */
    private static double[] calculaEficienciaTopoVsFundo(List<Partido> partidos)
    {
        //
        List<Partido> ordenado = partidos.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getVotos(), p1.getVotos())).toList();
        
        double eficienciaTopo = 0;
        double eficienciaFundo = 0;
        int contagemTopo = 0;
        int contagemFundo = 0;
        
        for (int i = 0; i < ordenado.size(); i++)
        {
            Partido partido = ordenado.get(i);
            if (partido.getAssentos() > 0)
            {
                double percentagemAssentos = (partido.getAssentos() * 100.0 / TOTAL_ASSENTOS);
                double percentagemVotos = (partido.getVotos() * 100.0 / TOTAL_VOTOS);
                double eficiencia = (percentagemVotos > 0) ? percentagemAssentos / percentagemVotos : 0;
                
                if(i < 2) //
                {
                    eficienciaTopo += eficiencia;
                    contagemTopo++;
                }
                else //
                {
                    eficienciaFundo += eficiencia;
                    contagemFundo++;
                }
            }
        }
        return new double[]
        {
            contagemTopo > 0 ? eficienciaTopo / contagemTopo : 0,
            contagemFundo > 0 ? eficienciaFundo / contagemFundo : 0
        };
    }   
}
