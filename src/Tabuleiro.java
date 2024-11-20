class Casa {
    String nome;
    String tipo;
    Imovel imovel;
    Casa proxima;

    Casa(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public String getNome() {
        return nome;
    }

}

public class Tabuleiro {
    private Casa inicio;

    public void adicionarCasa(String nome, String tipo, Imovel imovel) {
        Casa novaCasa = new Casa(nome, tipo);
        novaCasa.imovel = imovel;

        if (inicio == null) {
            inicio = novaCasa;
            inicio.proxima = inicio;
        } else {
            Casa atual = inicio;
            while (atual.proxima != inicio) {
                atual = atual.proxima;
            }
            atual.proxima = novaCasa;
            novaCasa.proxima = inicio;
        }
    }

    public Casa getInicio() {
        return inicio;
    }

    public Casa getCasaNaPosicao(int posicao) {
        Casa atual = inicio;
        for (int i = 0; i < posicao; i++) {
            atual = atual.proxima;
        }
        return atual;
    }

    public int calcularTamanho() {
        Casa atual = inicio;
        int tamanho = 1;
        while (atual.proxima != inicio) {
            atual = atual.proxima;
            tamanho++;
        }
        return tamanho;
    }

    public void exibirTodasAsCasas() {

        Casa atual = inicio;
        int contador = 0;

        do {
            System.out.printf("Casa %d: Tipo=%s", contador, atual.tipo);
            if (atual.imovel != null) {
                System.out.printf(", Nome=%s, Preço=R$%.2f, Aluguel=R$%.2f",
                        atual.imovel.getNome(),
                        atual.imovel.getPrecoCompra(),
                        atual.imovel.getPrecoAluguel());
            }
            System.out.println();
            atual = atual.proxima;
            contador++;
        } while (atual != inicio);
    }

}
