public class Imovel {
    String nome;
    double precoCompra;
    double precoAluguel;
    Jogador proprietario;

    Imovel(String nome, double precoCompra, double precoAluguel) {
        this.nome = nome;
        this.precoCompra = precoCompra;
        this.precoAluguel = precoAluguel;
        this.proprietario = null;
    }

    public Jogador getProprietario() {
        return proprietario;
    }

    public void setProprietario(Jogador proprietario) {
        this.proprietario = proprietario;
    }

    public double getPrecoCompra() {
        return precoCompra;
    }

    public double getPrecoAluguel() {
        return precoAluguel;
    }

    public String getNome() {
        return nome;
    }

}
