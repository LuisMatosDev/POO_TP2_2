package oop.tp2_2.utils;

import oop.tp2_2.models.Partido;
import oop.tp2_2.models.Quociente;
import java.util.*;

/**
 * Implementa o método D'Hondt para alocação proporcional de assentos parlamentares.
 * Utiliza uma fila de prioridade para gerir de forma eficiente os cálculos de quocientes e alocação de assentos.
 * @author Luis Matos
 */
public class CalculadoraDHondt
{
   /**
    * Comparador customizado para a PriorityQueue que implementa as regras D'Hondt com desempate.
    * Ordenação primária: Quocientes mais altos primeiro (comportamento max-heap).
    * Desempate: Quando os quocientes são iguais, favorece os partidos com menor número total de votos.
    */
   private static final class ComparadorQuociente implements Comparator<Quociente>
   {
       @Override
       public int compare(Quociente qc1, Quociente qc2)
       {
           // Comparação primária: quocientes mais altos devem surgir primeiro.
           int comparaQuociente = Double.compare(qc2.getQuociente(), qc1.getQuociente());
           if (comparaQuociente != 0)
           {
               return comparaQuociente;
           }
           // Comparação secundária (desempate): favorece partidos com menos votos
           // Implementa a regra para desempate pedida no enunciado
           return Integer.compare(qc1.getVotosPartido(), qc2.getVotosPartido());
       }
   }
   
   /**
    * Calcula a distribuição de assentos de acordo com o método D'Hondt.
    * @param partidos Lista dos partidos participantes.
    * @param assentosTotal Número total de assentos para alocar.
    * @param votosMargemMin Limite minímo de votos para elegibilidade de alocação de assentos.
    * @return Retorna o mapa que contém a alocação final de assentos para cada partido.
    */
   public static Map<Partido, Integer> calculaDistribuicaoAssentos(List<Partido> partidos,
           int assentosTotal, int votosMargemMin)
   {
       // A fila de prioridade lida automaticamente com a ordenação de quocientes de acordo com o comparador.
       PriorityQueue<Quociente> filaQuociente = new PriorityQueue<>(new ComparadorQuociente());
       Map<Partido, Integer> alocacaoAssentos = new HashMap<>();
       
       // Inicializa a alocação de assentos e filtra os partidos elegíveis 
       List<Partido> partidosElegiveis = new ArrayList<>();
       for (Partido partido : partidos)
       {
           alocacaoAssentos.put(partido, 0); //
           if (partido.getVotos() >= votosMargemMin)
           {
               partidosElegiveis.add(partido); //
           }
       }
       
       //
       
       for (Partido partido : partidosElegiveis)
       {
           double quociente = (double) partido.getVotos() / 1; //
           filaQuociente.offer(new Quociente(partido, quociente, 1));
       }
       
       //
       for (int assento = 1; assento <= assentosTotal; assento++)
       {
           Quociente maior = filaQuociente.poll();
           if (maior == null)
           {
               break; //
           }
           
           Partido partidoVencedor = maior.getPartido();
           //
           alocacaoAssentos.put(partidoVencedor, alocacaoAssentos.get(partidoVencedor) + 1);
           
           //
           int divisorSeguinte = maior.getDivisor() + 1;
           double quocienteSeguinte = (double) partidoVencedor.getVotos() / divisorSeguinte;
           filaQuociente.offer(new Quociente(partidoVencedor, quocienteSeguinte, divisorSeguinte));
       }
       
       return alocacaoAssentos;
   }
   
   /**
    * 
    */
   public static void aplicaAlocacaoAssentos(Map<Partido, Integer> alocacaoAssentos)
   {
       for (Map.Entry<Partido, Integer> entrada : alocacaoAssentos.entrySet())
       {
           entrada.getKey().setAssentos(entrada.getValue());
       }
   }
}
