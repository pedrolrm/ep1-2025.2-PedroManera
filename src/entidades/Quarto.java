package entidades;

public class Quarto {
    private String numero;
    private TipoQuarto tipo;
    private boolean ocupado;

    public Quarto(String numero, TipoQuarto tipo){
        this.numero = numero;
        this.tipo = tipo;
    }
    
    public String getNumero(){
        return numero;
    }

    public TipoQuarto getTipo(){
        return tipo;
    }

    public boolean isOcupado(){
        return ocupado;
    }

    public void setOcupado(boolean ocupado){
        this.ocupado = ocupado;
    }

    public void setNumero(String novoNumero){
        this.numero = novoNumero;
    }


}
