package oop.tp2_2;

import oop.tp2_2.models.Partido;
import oop.tp2_2.utils.CalculadoraDHondt;
import oop.tp2_2.utils.AnalisadorEleicao;
import oop.tp2_2.utils.ValidadorEleicao;
import java.util.*;

/**
 * Classe principal da aplicação para a simulação eleitoral do Parlamento da República Portuguesa.
 * Orquestra todo o processo de cálculo do método de distribuição proporcional D'Hondt e demonstra os
 * princípios eleitorais chave.
 * Esta classe é utilizada como ponto de entrada na aplicação
 * e coordena as operações entre todos os outros componentes.
 * @author Luis Matos
 */
public class Main
{
    // Constantes eleitorais - Baseadas nos paramêtros específicos do Parlamento Português
    private static final int TOTAL_ASSENTOS = 230; // Número total de assentos no Parlamento
    private static final int TOTAL_VOTOS = 6000000; // Número total de votos tendo por base uma partipação de 60%
    
    /**
     * Método principal - ponto de entrada na aplicação.
     * Executa uma análise comparativa detalhada por defeito.
     * @param args Argumentos para a linha de comandos (não utilizado)
     */
    public static void main(String[] args) 
    {
        System.out.println("\n=== SIMULAÇÃO DA ELEIÇÃO PARA O PARLAMENTO DA RÉPUBLICA PORTUGUESA ===");
        System.out.println("Método D'Hondt com Análise Comparativa de Coligações\n (Inclui Restrições Aplicadas ao Mundo Real)");
        System.out.println("=".repeat(70));
        
        // Executa a análise comparativa compreensiva
        executaAnaliseComparativa();
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("SIMULAÇÃO DE ELEIÇÃO E ANÁLISE COMPARATIVA CONCLUÍDAS");
        System.out.println("=".repeat(70));
    }
    
    /**
     * Inicializa os partidos participantes com uma coligação formada para efeitos demonstrativos.
     * Cria os partidos individuais e forma a coligação AD (PSD + CDS)
     * @return Retorna a lista de objetos Partido incluindo a coligação 
     */
    private static List<Partido> inicializaPartidosComColigacao()
    {
        // Criação dos partidos individualmente, em primeiro lugar
        // Nota: Os partidos PSD e CDS são criados como objetos individuais para seguidamente criar a coligação
        Partido ps = new Partido("PS", 1850000);
        Partido psd = new Partido("PSD", 1820000);
        Partido ch = new Partido("CH", 850000);
        Partido il = new Partido("IL", 420000);
        Partido be = new Partido("BE", 380000);
        Partido pcp = new Partido("PCP", 350000);
        Partido l = new Partido("L", 320000);
        Partido pan = new Partido("PAN", 280000);
        Partido cds = new Partido("CDS", 150000);
        
        // Criação da coligação recorrendo os objetos individuais previamente definidos
        List<Partido> membrosColigacao = new ArrayList<>();
        membrosColigacao.add(psd);
        membrosColigacao.add(cds);
        Partido coligacaoAD = new Partido("AD", membrosColigacao);
        
        // Debug: Verificação da criação da coligação 
        System.out.println("=== INICIALIZAÇÃO DE PARTIDO ===");
        System.out.printf("Coligação '%s' criada com os seguintes membros: %s\n", 
            coligacaoAD.getNome(), String.join(" + ", coligacaoAD.getMembrosColigacao()));
        System.out.printf("Total de votos da coligação: %,d\n", (int) coligacaoAD.getVotos());
        System.out.println();
        
        // Adicionar todos os partidos à lista final (incluindo a coligação, mas excluíndo os membros individuais
        // que a compõem)
        List<Partido> partidos = new ArrayList<>();
        partidos.add(ps);
        partidos.add(coligacaoAD); // Adicionamos a coligação AD (Aliança Democrática) em vez dos partidos individuais
        partidos.add(ch);
        partidos.add(il);
        partidos.add(be);
        partidos.add(pcp);
        partidos.add(l);
        partidos.add(pan);
        // Nota: Os partidos membros da coligação não são adicionados individualmente pois já se encontram
        // presentes no objeto coligação
               
        return partidos;
    }
    
    /**
     * Inicializa os partidos participantes sem coligações para efeitos demonstrativos.
     * Todos os partidos concorrem a título individual para providenciar uma base para comparação.
     * @return Lista dos objetos Partido sem coligações criadas
     */
    private static List<Partido> inicializaPartidosSemColigacao()
    {
        List<Partido> partidos = new ArrayList();
        
        // Criação dos partidos individualmente, em primeiro lugar 
        // Utilizamos a mesma alocação de votos presente no cenário de coligação para uma comparação justa.
        partidos.add(new Partido("PS", 1850000)); // Partido Socialista
        partidos.add(new Partido("PSD", 1820000)); // Partido Social Democrata
        partidos.add(new Partido("CH", 850000)); // Chega
        partidos.add(new Partido("IL", 420000)); // Iniciativa Liberal
        partidos.add(new Partido("BE", 380000)); // Bloco de Esquerda
        partidos.add(new Partido("PCP", 350000)); // Partido Comunista Português
        partidos.add(new Partido("L", 320000)); // Livre
        partidos.add(new Partido("PAN", 280000)); // Pessoas-Animais-Natureza
        partidos.add(new Partido("CDS", 150000)); // CDS - Partido Popular
        
        System.out.println("=== INICIALIZAÇÃO DE PARTIDOS SEM COLIGAÇÃO ===");
        System.out.println("Todos os partidos concorrem individualmente.");
        System.out.printf("Total de partidos: %d%n", partidos.size());
        System.out.println();
        
        
        return partidos;
    }
    
    /**
     * Executa uma análise comparativa compreensiva entre os cenários com e sem coligações.
     * Executa duas simulações em separado e providencia uma comparação detalhada dos resultados.
     * Este método demonstra a vantagem estratégica da formação de coligações.
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
