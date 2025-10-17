package oop.tp2_2.models;

/**
 * Representa a entrada de um quociente no método de cálculo D'Hondt.
 * Utilizado para rastrear os quocientes de um partido durante as operações da fila de prioridade
 * durante a alocação de assentos.
 * @author Luis Matos
 */
public class Quociente
{
    private Partido partido;
    private double quociente;
    private int divisor;
    private int votosPartido;
    
    /**
     * Constructor para entrada de um quociente.
     * @param partido O partido ao qual este quociente pertence.
     * @param quociente O valor calculado do quociente (votos / divisor).
     * @param divisor O divisor atual, em utilização no método de cálculo D'Hondt.
     */
    public Quociente(Partido partido, double quociente, int divisor)
    {
        this.partido = partido;
        this.quociente = quociente;
        this.divisor = divisor;
        this.votosPartido = partido.getVotos();
    }
    
    // Getters
    public Partido getPartido()
    {
        return partido;
    }
    
    public double getQuociente()
    {
        return quociente;
    }
    
    public int getDivisor()
    {
        return divisor;
    }
    
    public int getVotosPartido()
    {
        return votosPartido;
    }
    
    /**
     * Retorna uma cadeia formatada que mostra o cálculo do quociente.
     * Útil para fazer debug e compreender o processo do método D'Hondt.
     * @return Cadeia formatada para exibir o cálculo do quociente.
     */
    @Override
    public String toString()
    {
        return String.format("%s: %d / %d = %.2f", partido.getNome(), votosPartido, divisor, quociente);
    }
}
