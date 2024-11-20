import java.util.ArrayList;
import java.util.List;

public class Jogador {
    String nome;
    double saldo;
    int posicaoAtual;
    List<Imovel> propriedades = new ArrayList<>();
    int rodadasNaPrisao = 0;

    Jogador(String nome, double saldoInicial) {
        this.nome = nome;
        this.saldo = saldoInicial;
        this.posicaoAtual = 0;
    }

    public void adicionarPropriedade(Imovel imovel) {
        propriedades.add(imovel);
        imovel.proprietario = this;
    }

    public void pagar(double valor) {
        saldo -= valor;
        System.out.println(nome + " pagou R$" + valor + ". Saldo atual: R$" + saldo);
    }

    public void receber(double valor) {
        saldo += valor;
        System.out.println(nome + " recebeu R$" + valor + ". Saldo atual: R$" + saldo);
    }

    public boolean estaFalido() {
        return saldo < 0;
    }

    public String getNome() {
        return nome;
    }


    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public boolean estaNaPrisao() {
        return rodadasNaPrisao > 0;
    }

    public void reduzirRodadaPrisao() {
        if (rodadasNaPrisao > 0) {
            rodadasNaPrisao--;
        }
    }

}
