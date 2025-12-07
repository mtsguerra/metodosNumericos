// polinomio interpolador utilizando o método de Lagrange
class polinomioInterpolador {
    // coordenadas X e Y dos pontos
    private double[] meses;
    private double[] evaporacao;
    private int nPontos;

    public polinomioInterpolador(double[] x, double[] y) {
        this.meses = x.clone();
        this.evaporacao = y.clone();
        this.nPontos = x.length;
    }

    public double avaliar(double mes) {
        double resultado = 0.0;
        for (int i = 0; i < nPontos; i++) {
            double produto = evaporacao[i];
            for (int j = 0; j < nPontos; j++) {
                if (i != j) {
                    double numerador = (mes - meses[j]);
                    double denominador = (meses[i] - meses[j]);
                    produto *= numerador / denominador;
                }
            }
            resultado += produto;
        }
        return resultado;
    }
}

// spline cúbico natural
class splineCubicoNatural {
    private double[] meses;  // coordenadas X dos pontos
    private double[] evaporacao;  // coordenadas Y dos pontos
    private double[] a;  // coeficientes constantes
    private double[] b;  // coeficientes lineares
    private double[] c;  // coeficientes quadráticos
    private double[] d;  // coeficientes cúbicos
    private int nPontos;

    public splineCubicoNatural(double[] x, double[] y) {
        this.meses = x.clone();
        this.evaporacao = y.clone();
        this.nPontos = x.length;
        this.a = new double[nPontos];
        this.b = new double[nPontos];
        this.c = new double[nPontos];
        this.d = new double[nPontos];
        calcularSpline();
    }

    private void calcularSpline() {

        // Calc diferenças entre pontos em sequencia
        double[] h = new double[nPontos - 1];
        for (int i = 0; i < nPontos - 1; i++){
            h[i] = meses[i + 1] - meses[i];
        }

        // Coeficientes a são os valores y nos nós
        for (int i = 0; i < nPontos; i++){
            a[i] = evaporacao[i];
        }

        // Calcular alpha para o sistema tridiagonal
        double[] alpha = new double[nPontos - 1];
        for (int i = 1; i < nPontos - 1; i++){
            alpha[i] = 3.0 / h[i] * (a[i + 1] - a[i])
                    - 3.0 / h[i - 1] * (a[i] - a[i - 1]);
        }

        // Resolvendo para encontrar C usando decomposição LU
        double[] l = new double[nPontos];
        double[] mu = new double[nPontos];
        double[] z = new double[nPontos];

        l[0] = 1.0;
        mu[0] = 0.0;
        z[0] = 0.0;

        // Decomposição LU
        for (int i = 1; i < nPontos - 1; i++) {
            l[i] = 2.0 * (h[i - 1] + h[i]) - h[i - 1] * mu[i - 1];
            mu[i] = h[i] / l[i];
            z[i] = (alpha[i] - h[i - 1] * z[i - 1]) / l[i];
        }

        l[nPontos - 1] = 1.0;
        z[nPontos - 1] = 0.0;
        c[nPontos - 1] = 0.0;

        for (int j = nPontos - 2; j >= 0; j--) {
            c[j] = z[j] - mu[j] * c[j + 1];
            b[j] = (a[j + 1] - a[j]) / h[j]
                    - h[j] * (c[j + 1] + 2.0 * c[j]) / 3.0;
            d[j] = (c[j + 1] - c[j]) / (3.0 * h[j]);
        }
    }

    public double avaliar(double mes) {
        // Encontra o intervalo certo
        int indiceIntervalo = 0;
        boolean encontrou = false;
        for (int i = 0; i < nPontos - 1; i++) {
            if (mes >= meses[i] && mes <= meses[i + 1]) {
                indiceIntervalo = i;
                encontrou = true;
                break;
            }
        }

        // ao nao encontrar, usa o intervalo mais próximo
        if (!encontrou) {
            if (mes < meses[0]) {
                indiceIntervalo = 0;
            } else if (mes > meses[nPontos - 1]) {
                indiceIntervalo = nPontos - 2;
            }
        }

        // Avaliar o polinômio cúbico no intervalo encontrado
        double dx = mes - meses[indiceIntervalo];
        double resultado = a[indiceIntervalo] + b[indiceIntervalo] * dx
                + c[indiceIntervalo] * dx * dx
                + d[indiceIntervalo] * dx * dx * dx;
        return resultado;
    }
}

/**
 * Classe principal para teste dos construtores
 */
public class ex2 {
    public static void main(String[] args) {
        // Dados meteorológicos de Adelaide, Australia ao decorrer de 23 anos
        // em um espaco de 12 meses do ano
        double[] meses = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        double[] evaporacao = {8.6, 7.0, 6.4, 4.0, 2.8, 1.8, 1.8, 2.1, 3.2, 4.7,
                6.2, 7.6};

        polinomioInterpolador poliInter = new polinomioInterpolador(meses,
                evaporacao);
        splineCubicoNatural splineCubico = new splineCubicoNatural(meses,
                evaporacao);

        System.out.println("========== Evaporacao em Adelaide, Australia ==========");

        System.out.printf("%-10s %-20s %-20s%n", "x", "p(x)", "s(x)");
        System. out.println("───────────────────────────────────────────────────────");

        // de 1 ate 12 com alteracao de 0.5
        for (double mes = 1.0; mes <= 12.0; mes += 0.5) {
            double polinomioValor = poliInter.avaliar(mes);
            double splineValor = splineCubico.avaliar(mes);
            System.out.printf("%-10.1f %-20.6f %-20.6f%n", mes, polinomioValor, splineValor);
        }
        System.out.println();
        System.out.println("========== comparacao em ponto especifico ==========");
        System.out.println();
        System.out.println("mes = 5.5:");
        System.out.println("p(5.5) = " + poliInter.avaliar(5.5));
        System.out.println("s(5.5) = " + splineCubico.avaliar(5.5));
    }
}