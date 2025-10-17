package oop.tp2_2.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um partido ou coligação no sistema eleitoral.
 * Guarda o nome, contagem de votos, alocação de assentos, e informação da coligação.
 * @author Luis Matos
 */
public class Partido
{
    private String nome;
    private int votos;
    private int assentos;
    private boolean eColigacao;
    private List<String> membrosColigacao;
    
    /**
     * Constructor de partidos individuais.
     * @param nome O nome do partido.
     * @param votos O número de votos obtidos pelo partido.
     */
    public Partido(String nome, int votos)
    {
        this.nome = nome;
        this.votos = votos;
        this.assentos = 0;
        this.eColigacao = false;
        this.membrosColigacao = new ArrayList<>();
        this.membrosColigacao.add(nome);
    }
    
    /**
     * Constructor para coligações, formadas a partir de partidos individuais.
     * @param nomeColigacao O nome da coligação.
     * @param membros Lista dos partidos individuais que formam a coligação.
     */
    public Partido(String nomeColigacao, List<Partido> membros)
    {
        this.nome = nomeColigacao;
        this.eColigacao = true;
        this.membrosColigacao = new ArrayList<>();
        this.votos = 0;
        this.assentos = 0;
        
        // Agregação dos votos obtidos por todos os membros da coligação.
        for (Partido membro : membros)
        {
            this.votos += membro.getVotos();
            this.membrosColigacao.add(membro.getNome());
        }
    }
    
    // Getters
    public String getNome()
    {
        return nome;
    }
    
    public int getVotos()
    {
        return votos;
    }
    
    public int getAssentos()
    {
        return assentos;
    }
    
    public List<String> getMembrosColigacao()
    {
        return membrosColigacao;
    }
    
    public boolean eColigacao()
    {
        return eColigacao;
    }
    
    // Setter(s)
    public void setAssentos(int assentos)
    {
        this.assentos = assentos;
    }
    
    /**
     * Incrementa a contagem do número de assentos para um dado partido por um.
     * Utilizada durante a alocação de assentos de acordo com o método D'Hondt.
     */
    public void adicionaAssento()
    {
        this.assentos++;
    }
    
    /**
     * Retorna a cadeia formatada que representa um partido.
     * Para coligações, inclui os nomes dos partido membros da coligação.
     * @return Cadeia formatada para exibir os nomes do partidos/coligações.
     */
    @Override
    public String toString()
    {
        if (eColigacao)
        {
            return String.format("%s (Coligação: %s)", nome, String.join("+", membrosColigacao));
        }
        
        return nome;
    }
}
