package oop.tp2_2.utils;

import oop.tp2_2.models.Partido;
import oop.tp2_2.models.Quociente;
import java.util.*;

/**
 *
 * @author Luis Matos
 */
public class CalculadoraDHondt
{
   /**
    * 
    */
   private static final class ComparadorQuociente implements Comparator<Quociente>
   {
       @Override
       public int compare(Quociente qc1, Quociente qc2)
       {
           //
           int comparaQuociente = Double.compare(qc2.getQuociente(), qc1.getQuociente());
           if (comparaQuociente != 0)
           {
               return comparaQuociente;
           }
           //
           //
           return Integer.compare(qc1.getVotosPartido(), qc2.getVotosPartido());
       }
   }
   
   /**
    * 
    */
   public static Map<Partido, Integer> calculaDistribuicaoAssentos(List<Partido> partidos,
           int assentosTotal, int votosMargemMin)
   {
       //
       PriorityQueue<Quociente> filaQuociente = new PriorityQueue<>(new ComparadorQuociente());
       Map<Partido, Integer> alocacaoAssentos = new HashMap<>();
       
       //
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
