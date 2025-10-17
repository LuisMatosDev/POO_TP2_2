package oop.tp2_2.models;

/**
 *
 * @author Luis Matos
 */
public class Quociente
{
    private Partido partido;
    private double quociente;
    private int divisor;
    private int votosPartido;
    
    
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
    
    @Override
    public String toString()
    {
        return String.format("%s: %d / %d = %.2f", partido.getNome(), votosPartido, divisor, quociente);
    }
}
