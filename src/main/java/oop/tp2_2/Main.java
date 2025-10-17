package oop.tp2_2;

import oop.tp2_2.models.Partido;
import oop.tp2_2.utils.CalculadoraDHondt;
import oop.tp2_2.utils.AnalisadorEleicao;
import oop.tp2_2.utils.ValidadorEleicao;
import java.util.*;

/**
 *
 * @author Luis Matos
 */
public class Main
{
    //
    private static final int TOTAL_ASSENTOS = 230; //
    private static final int TOTAL_VOTOS = 6000000; //
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        System.out.println("=== SIMULAÇÃO DA ELEIÇÃO PARA O PARLAMENTO DA RÉPUBLICA PORTUGUESA ===");
        System.out.println("Método D'Hondt com Análise Comparativa de Coligações\n (Inclui Restrições Aplicadas ao Mundo Real)");
        System.out.println("=".repeat(70));
        
        // Executa a análise comparativa compreensiva
        executaAnaliseComparativa();
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("SIMULAÇÃO DE ELEIÇÃO E ANÁLISE COMPARATIVA CONCLUÍDAS");
        System.out.println("=".repeat(70));
    }
    
    /**
     * 
     */
    private static List<Partido> inicializaPartidosComColigacao()
    {
        List<Partido> partidos = new ArrayList();
        
        //
        //
        Partido ps = new Partido("PS", 1850000); //
        Partido psd = new Partido("PSD", 1820000); //
        Partido ch = new Partido("CH", 850000); //
        Partido il = new Partido("IL", 420000); //
        Partido be = new Partido("BE", 380000); //
        Partido pcp = new Partido("PCP", 350000); //
        Partido l = new Partido("L", 320000); //
        Partido pan = new Partido("PAN", 280000); //
        Partido cds = new Partido("CDS", 150000); //
        
        //
        //
        Partido coligacaoAD = new Partido("AD", Arrays.asList(psd, cds));
        
        //
        
        partidos.add(ps);
        partidos.add(coligacaoAD);
        partidos.add(ch);
        partidos.add(il);
        partidos.add(be);
        partidos.add(pcp);
        partidos.add(l);
        partidos.add(pan);
        //
        
        //
        System.out.println("=== INICIALIZAÇÃO DE PARTIDO ===");
        System.out.printf("Coligação '%s' criada com os seguintes membros: %s\n", 
            coligacaoAD.getNome(), String.join(" + ", coligacaoAD.getMembrosColigacao()));
        System.out.printf("Total de votos da coligação: %,d\n", (int) coligacaoAD.getVotos());
        System.out.println();
                
        return partidos;
    }
    
    /**
     * 
     */
    private static List<Partido> inicializaPartidosSemColigacao()
    {
        List<Partido> partidos = new ArrayList();
        
        //
        //
        partidos.add(new Partido("PS", 1850000));
        partidos.add(new Partido("PSD", 1820000));
        partidos.add(new Partido("CH", 850000));
        partidos.add(new Partido("IL", 420000));
        partidos.add(new Partido("BE", 380000));
        partidos.add(new Partido("PCP", 350000));
        partidos.add(new Partido("L", 320000));
        partidos.add(new Partido("PAN", 280000));
        partidos.add(new Partido("CDS", 150000));
        
        System.out.println("=== INICIALIZAÇÃO DE PARTIDOS SEM COLIGAÇÃO ===");
        System.out.println("Todos os partidos concorrem individualmente.");
        System.out.printf("Total de partidos: %d%n", partidos.size());
        System.out.println();
        
        
        return partidos;
    }
    
    /**
     * 
     */
    private static void executaAnaliseComparativa()
    {
        System.out.println("=".repeat(80));
        System.out.println("ANÁLISE COMPARATIVA COMPLETA");
        System.out.println("Demonstração do impacto estratégico das coligações eleitorais");
        System.out.println("=".repeat(80));
        
        // CENÁRIO #1: COM COLIGAÇÃO
        System.out.println("\n>>> CENÁRIO 1: COM COLIGAÇÃO AD (PSD + CDS)");
        System.out.println("-".repeat(50));
        
        List<Partido> comColigacao = inicializaPartidosComColigacao();
        ValidadorEleicao.validaEAplicaRestricoes(comColigacao, TOTAL_VOTOS);
        int limiteVotosMin = ValidadorEleicao.calculaLimiteVotosMin(TOTAL_VOTOS);
        var alocacaoAssentosComColigacao = CalculadoraDHondt.calculaDistribuicaoAssentos(comColigacao,
                TOTAL_ASSENTOS, limiteVotosMin);
        CalculadoraDHondt.aplicaAlocacaoAssentos(alocacaoAssentosComColigacao);
        AnalisadorEleicao.mostraResultadosEleicao(comColigacao, TOTAL_ASSENTOS, TOTAL_VOTOS);
        
        // CENÁRIO #2: SEM COLIGAÇÃO
        System.out.println("\n>>> CENÁRIO 2: SEM COLIGAÇÃO (TODOS OS PARTIDOS INDIVIDUAIS)");
        System.out.println("-".repeat(50));
        
        List<Partido> semColigacao = inicializaPartidosSemColigacao();
        ValidadorEleicao.validaEAplicaRestricoes(semColigacao, TOTAL_VOTOS);
        var alocacaoAssentosSemColigacao = CalculadoraDHondt.calculaDistribuicaoAssentos(semColigacao,
                TOTAL_ASSENTOS, limiteVotosMin);
        CalculadoraDHondt.aplicaAlocacaoAssentos(alocacaoAssentosSemColigacao);
        AnalisadorEleicao.mostraResultadosEleicao(semColigacao, TOTAL_ASSENTOS, TOTAL_VOTOS);
        
        // ANÁLISE COMPARATIVA
        AnalisadorEleicao.realizaAnaliseComparativa(comColigacao, semColigacao, TOTAL_ASSENTOS, TOTAL_VOTOS);

        // DEMONSTRA AFIRMAÇÕES-CHAVE PARA AMBOS OS CENÁRIOS
        System.out.println("\n>>> DEMONSTRAÇÃO DAS AFIRMAÇÕES-CHAVE:");
        System.out.println("=".repeat(50));
        
        System.out.println("\n--- CENÁRIO COM COLIGAÇÃO ---");
        AnalisadorEleicao.demonstraVantagemColigacao(comColigacao);
        AnalisadorEleicao.demonstraVantagemPartidoGrande(comColigacao);
        
        System.out.println("\n--- CENÁRIO SEM COLIGAÇÃO ---");
        AnalisadorEleicao.demonstraVantagemPartidoGrande(semColigacao);
        System.out.println("(Não existem coligações para demonstrar a Afirmação #1!)");
    }
}
