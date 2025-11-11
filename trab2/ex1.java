package trab2;

class ex1 {

    //f(x) = sen(x^2) + 1.1 - e^(-x)
    public double funcaoFx(double x) {
        return Math.sin(x * x) + 1.1 - Math.exp(-x);
    }

    //f'(x) = 2x*cos(x^2) + e^(-x)
    public double derivadaFx(double x) {
        return 2 * x * Math.cos(x * x) + Math.exp(-x);
    }

    //f''(x) = 2*cos(x^2) - 4x²*sin(x^2) - e^(-x)
    public double derivadaDuplaFx(double x) {
        return 2 * Math.cos(x * x) - 4 * x * x * Math.sin(x * x) - Math.exp(-x);
    }

    //----------------------------------------------------------//

    /**
     * exA, parte gráfica realizada com o geogebra.
     *
     * @return retorna intervalo [left, right] de amplitude 0.1,
     * contendo a menor raiz de F(x) = 0
     */
    public double[] encontrarIntervaloMenorRaiz() {
        // Parâmetros de busca
        // tendo sen(x^2) em [-1, 1], podemos dizer que e^-x deve estar
        // entre 0.1 e 2.1 para que haja raiz (sen(x^2) + 1.1 - e^-x = 0),
        // portanto -0.74 <= x <= 2.3, aproximadamente.
        double xMin = -0.74;
        double xMax = 2.3;
        double step = 0.0001;
        double amplitude = 0.1;

        double xAnterior = xMin;
        double fAnterior = funcaoFx(xMin);

        for (double x = xMin + step; x <= xMax; x += step) {
            double fx = funcaoFx(x);
            if (fAnterior * fx < 0) {
                // primeira raiz encontrada entre xAnterior e x
                double raizAprox = (xAnterior + x) / 2.0;
                double left = raizAprox - amplitude / 2.0;
                double right = left + amplitude;

                // Garante mudança de sinal no intervalo
                while (funcaoFx(left) * funcaoFx(right) > 0 && left > xMin) {
                    left -= 0.001;
                    right = left + amplitude;
                }
                return new double[]{left, right};
            }
            xAnterior = x;
            fAnterior = fx;
        }
        return null; // não será o caso para a função dada
    }

    //----------------------------------------------------------//

    public static class ansBissecao {
        double raiz;
        int nIteracoes;

        public ansBissecao(double raiz, int nIteracoes) {
            this.raiz = raiz;
            this.nIteracoes = nIteracoes;
        }
    }

    /**
     * Basicamente um Binary Search para encontrar a raiz de F(x) = 0, onde
     * durante a busca se verifica os intervalos onde há mudança de sinal.
     *
     * @param left começo do intervalo
     * @param right fim do intervalo
     * @param epsilon erro máximo desejado
     * @return retorna a raiz aproximada e o número de iterações,
     * atualizando na classe ansBissecao
     */
    public ansBissecao metodoBissecao(double left, double right, double epsilon) {
        int i = 0;
        double fLeft = funcaoFx(left);
        double fRight = funcaoFx(right);

        // verifica a aplicabilidade do metodo
        // neste caso f(x) também é contínua, pois é composta por funções
        // continuas (seno, exp e polinômios), logo não é preciso verificar a
        // continuidade
        if (fLeft * fRight > 0) {
            throw new RuntimeException("não é possível aplicar o método da " +
                    "bisseção neste intervalo");
        }

        while ((right - left) / 2.0 > epsilon) {
            i++;
            double mid = (left + right) / 2.0;
            double fMid = funcaoFx(mid);

            System.out.printf("Iteração %d: left=%.10f, right=%.10f, middle=%.10f, F(middle)=%.10e, amplitude=%.10e%n",
                    i, left, right, mid, fMid, (right - left) / 2.0);

            if (Math.abs(fMid) < epsilon) {
                break;
            }
            if (fLeft * fMid < 0) right = mid;
            else left = mid;
        }

        double lastMid = (left + right) / 2.0;
        return new ansBissecao(lastMid, i);
    }

    //----------------------------------------------------------//

    public class ansNewton {
        double raiz;
        int nIteracoes;

        public ansNewton(double raiz, int nIteracoes) {
            this.raiz = raiz;
            this.nIteracoes = nIteracoes;
        }
    }

    /**
     * Partindo de uma aproximação inicial x0 e iterando a fórmula:
     * x_n = x_(n-1) - F(x_(n-1)) / F'(x_(n-1)), até que o erro absoluto
     * seja menor que epsilon ou então ultrapasse o número máximo de
     * iterações permitidas.
     *
     * @param x0 aproximação inicial da raiz
     * @param epsilon erro absoluto máximo desejado (majorado)
     * @param maxIteracoes número máximo de iterações permitidas
     * @return retorna a raiz aproximada e o número de iterações na classe ansNewton
     */
    public ansNewton metodoNewton(double x0, double epsilon, int maxIteracoes) {
        double lastX;
        double x = x0;
        int i = 0;
        // Verifica se a derivada a aplicabilidade do metodo de newton,
        // utilizamos 1e-15 para evitar erros de precisão numérica que
        // poderia ser causado ao utilizar o 0. Além disso, também podemos
        // afirmar que a função é contínua, pois é composta por funções
        // contínuas (seno, exp e polinômios).
        if (Math.abs(derivadaFx(x)) < 1e-15) {
            throw new RuntimeException("Derivada muito perto de zero na " +
                    "iteração " + i);
        }
        do {
            i++;
            double fx = funcaoFx(x);
            double fLinhaX = derivadaFx(x);

            if (Math.abs(fLinhaX) < 1e-15) {
                throw new RuntimeException("Derivada muito perto de zero na " +
                        "iteração " + i);
            }

            lastX = x;
            x -= fx / fLinhaX;
            System.out.printf("Iteração %d: x=%.15f, F(x)=%.10e, F'(x)=%.10e, |x_n - x_{n-1}|=%.10e%n",
                    i, x, funcaoFx(x), fLinhaX, Math.abs(x - lastX));

            if (i >= maxIteracoes) {
                throw new RuntimeException("Número máximo de iterações ultrapassado");
            }

        } while (Math.abs(x - lastX) > epsilon && Math.abs(funcaoFx(x)) > epsilon);
        return new ansNewton(x, i);
    }

    public static void main(String[] args) {
        ex1 myobj = new ex1();

        System.out.println("\n--------------------------------------------------------");
        System.out.println("         EXERCÍCIO 1 - MÉTODOS NUMÉRICOS");
        System.out.println("         f(x) = sen(x^2) + 1.1 - e^(-x)");
        System.out.println("--------------------------------------------------------\n");

        // ===== ITEM (a) =====
        System.out.println("--------------------------------------------------------");
        System.out.println("ITEM (a) - Separação de raízes e intervalo I");
        System.out.println("--------------------------------------------------------");

        double[] intervaloI = myobj.encontrarIntervaloMenorRaiz();
        if (intervaloI == null) {
            System.err.println("Erro: não é possível encontrar o intervalo I " +
                    "com a função dada.");
            return;
        }

        double left = intervaloI[0];
        double right = intervaloI[1];
        double epsilon = 5e-9;

        System.out.printf("Intervalo I = [%.10f, %.10f]\n", left, right);
        System.out.printf("Amplitude = %.10f (10^-1)\n", right - left);
        System.out.printf("f(%.10f) = %+.10e\n", left, myobj.funcaoFx(left));
        System.out.printf("f(%.10f) = %+.10e\n", right, myobj.funcaoFx(right));
        System.out.printf("f(left) * f(right) = %.10e, satisfazendo a " +
                        "aplicabilidade de f(left)*f(right) < 0\n",
                myobj.funcaoFx(left) * myobj.funcaoFx(right));
        // ===== ITEM (b.i) - MOSTRAR CONDIÇÕES =====
        //myobj.mostrarCondicoesBissecao(left, right);
        //myobj.mostrarCondicoesNewton(left, right);

        System.out.println("\n--------------------------------------------------------");
        System.out.println("ITEM (b.ii) - utilizando epsilon = 5×10^-9");
        System.out.println(
                "--------------------------------------------------------\n");
        System.out.println("─── MÉTODO DA BISSEÇÃO ───\n");
        ansBissecao resBis = myobj.metodoBissecao(left, right, epsilon);
        System.out.printf("\nRaiz: %.15f\n", resBis.raiz);
        System.out.printf("Iterações: %d\n", resBis.nIteracoes);

        System.out.println("\n─── MÉTODO DE NEWTON ───\n");
        double x0 = left;
        ansNewton resNewt = myobj.metodoNewton(x0, epsilon, 100);
        System.out.printf("\nRaiz: %.15f\n", resNewt.raiz);
        System.out.printf("Iterações: %d\n", resNewt.nIteracoes);
    }
}