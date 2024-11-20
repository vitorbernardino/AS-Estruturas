import java.util.*;

public class RegrasJogo {
    private Tabuleiro tabuleiro = new Tabuleiro();
    private List<Jogador> jogadores = new ArrayList<>();
    private double salario;
    private int rodadasMax;
    private int tamanhoTabuleiro;

    public void configurarJogo(double saldoInicial, double salario, int rodadasMax) {
        this.salario = salario;
        this.rodadasMax = rodadasMax;

        gerarCasasAleatorias();

        tabuleiro.exibirTodasAsCasas();

        cadastrarJogadores(saldoInicial);

        this.tamanhoTabuleiro = tabuleiro.calcularTamanho();
    }

    private void gerarCasasAleatorias() {
        Random random = new Random();
        List<Imovel> imoveis = gerarListaDeImoveis();

        int totalCasas = 20;
        int maxCasasEspeciais = totalCasas / 3;
        int maxImoveis = totalCasas - maxCasasEspeciais;

        int contadorCasasEspeciais = 0;
        int contadorImoveis = 0;

        tabuleiro.adicionarCasa("Início", "INICIO", null);

        for (int i = 1; i < totalCasas; i++) {
            boolean adicionarImovel = random.nextBoolean() && contadorImoveis < maxImoveis;
            if (adicionarImovel) {
                Imovel imovel = imoveis.get(contadorImoveis % imoveis.size());
                contadorImoveis++;
                tabuleiro.adicionarCasa(imovel.nome, "IMOVEL", imovel);
            } else if (contadorCasasEspeciais < maxCasasEspeciais) {
                String tipoCasaEspecial = gerarTipoCasaEspecial(random);
                tabuleiro.adicionarCasa(tipoCasaEspecial, tipoCasaEspecial, null);
                contadorCasasEspeciais++;
            }
        }
    }

    private List<Imovel> gerarListaDeImoveis() {
        return Arrays.asList(
                new Imovel("Casa do Bosque", 200000, 1100),
                new Imovel("Apartamento Central", 350000, 1800),
                new Imovel("Vila das Flores", 400000, 2200),
                new Imovel("Pousada da Praia", 500000, 2700),
                new Imovel("Mansão da Colina", 600000, 3300),
                new Imovel("Residência do Lago", 450000, 2500),
                new Imovel("Cobertura Diamante", 700000, 3700),
                new Imovel("Edifício Horizonte", 550000, 2900),
                new Imovel("Chácara do Sol", 300000, 1600),
                new Imovel("Fazenda Boa Vista", 250000, 1300)
        );
    }

    private String gerarTipoCasaEspecial(Random random) {
        String[] tiposEspeciais = {"IMPOSTO", "RESTITUICAO", "PRISAO", "SORTE", "REVES"};
        return tiposEspeciais[random.nextInt(tiposEspeciais.length)];
    }

    private void cadastrarJogadores(double saldoInicial) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Número de jogadores (2 a 6): ");
        int numeroJogadores = scanner.nextInt();

        for (int i = 0; i < numeroJogadores; i++) {
            System.out.print("Nome do jogador " + (i + 1) + ": ");
            String nome = scanner.next();
            jogadores.add(new Jogador(nome, saldoInicial));
        }
    }

    public void iniciarJogo() {
        Random dado = new Random();
        int rodadaAtual = 0;

        while (rodadaAtual < rodadasMax && jogadores.size() > 1) {
            System.out.println("\nRodada " + (rodadaAtual + 1));
            for (Jogador jogador : jogadores) {
                if (jogador.estaFalido()) {
                    System.out.println(jogador.nome + " está falido e foi removido do jogo.");
                    continue;
                }


                if (jogador.estaNaPrisao()) {
                    System.out.println(jogador.nome + " está preso e não pode jogar esta rodada.");
                    jogador.reduzirRodadaPrisao();
                    continue;
                }

                int resultadoDado = dado.nextInt(6) + 1;
                System.out.println(jogador.nome + " jogou o dado: " + resultadoDado);
                jogador.posicaoAtual = (jogador.posicaoAtual + resultadoDado) % tamanhoTabuleiro;

                Casa casaAtual = tabuleiro.getCasaNaPosicao(jogador.posicaoAtual);
                interagirComCasa(jogador, casaAtual);
            }
            rodadaAtual++;
        }

        declararVencedor();
    }

    private void interagirComCasa(Jogador jogador, Casa casa) {
        switch (casa.tipo) {
            case "INICIO":
                jogador.receber(salario);
                break;
            case "IMOVEL":
                if (casa.getImovel().getProprietario() == null) {
                    if (jogador.getSaldo() >= casa.getImovel().getPrecoCompra()) {
                        jogador.setSaldo(jogador.getSaldo() - casa.getImovel().getPrecoCompra());
                        casa.getImovel().setProprietario(jogador);
                        System.out.println(jogador.getNome() + " comprou " + casa.getNome());
                    } else {
                        System.out.println(jogador.getNome() + " não tem saldo suficiente para comprar " + casa.getNome());
                    }
                } else if (!casa.getImovel().getProprietario().equals(jogador)) {
                    double aluguel = casa.getImovel().getPrecoAluguel();
                    if (jogador.getSaldo() >= aluguel) {
                        jogador.setSaldo(jogador.getSaldo() - aluguel);
                        casa.getImovel().getProprietario().setSaldo(
                                casa.getImovel().getProprietario().getSaldo() + aluguel
                        );
                        System.out.println(jogador.getNome() + " pagou aluguel para " + casa.getImovel().getProprietario().getNome());
                    } else {
                        System.out.println(jogador.getNome() + " está falido e não pode pagar o aluguel.");
                    }
                }
                break;
            case "IMPOSTO":
                double imposto = jogador.saldo * 0.05;
                jogador.pagar(imposto);
                System.out.println(jogador.nome + " pagou imposto de R$" + imposto);
                break;
            case "RESTITUICAO":
                double restituição = salario * 0.10;
                jogador.receber(restituição);
                System.out.println(jogador.nome + " recebeu restituição de R$" + restituição);
                break;
            case "PRISAO":
                jogador.rodadasNaPrisao = 1;
                System.out.println(jogador.nome + " foi para a prisão.");
                break;
            case "SORTE":
                jogador.receber(500);
                System.out.println(jogador.nome + " ganhou R$500 na sorte!");
                break;
            case "REVES":
                jogador.pagar(300);
                System.out.println(jogador.nome + " perdeu R$300 no revés.");
                break;
        }
    }

    private void declararVencedor() {
        Jogador vencedor = jogadores.stream()
                .filter(j -> !j.estaFalido())
                .max(Comparator.comparingDouble(j -> j.saldo))
                .orElse(null);

        if (vencedor == null) {
            System.out.println("Todos os jogadores foram à falência. Não há vencedor.");
        } else {
            System.out.println("O vencedor é " + vencedor.nome + " com saldo de R$" + vencedor.saldo);
        }
    }
}
