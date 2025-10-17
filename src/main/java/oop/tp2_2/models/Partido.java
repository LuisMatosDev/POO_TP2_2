package oop.tp2_2.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luis Matos
 */
public class Partido
{
    private String nome;
    private int votos;
    private int assentos;
    private boolean eColigacao;
    private List<String> membrosColigacao;
    
    public Partido(String nome, int votos)
    {
        this.nome = nome;
        this.votos = votos;
        this.assentos = 0;
        this.eColigacao = false;
        this.membrosColigacao = new ArrayList<>();
        this.membrosColigacao.add(nome);
    }
    
    public Partido(String nomeColigacao, List<Partido> membros)
    {
        this.nome = nomeColigacao;
        this.eColigacao = true;
        this.membrosColigacao = new ArrayList<>();
        this.votos = 0;
        this.assentos = 0;
        
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
    
    public void adicionaAssento()
    {
        this.assentos++;
    }
    
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
