package trab1;

import java.util.*;

class exercicio2{

    public double calcS(double eps) {
        double current = 0.5;
        double acc = current;
        int k = 0;
        int n = 1;

        while (true) {
            double numerator   = (2.0 * k + 1.0) * (2.0 * k + 1.0) * 0.25;
            double denominator = 2.0 * (k + 1.0) * (2.0 * k + 3.0);
            double next_one = current * (numerator / denominator);

            if (8.0 * next_one <= eps) {
                break;
            }

            acc += next_one;
            current = next_one;
            k++;
            n++;
        }
        double S = 6.0 * acc;

        System.out.println("Número de termos somados: " + n);
        System.out.println("S: " + S);

        System.out.printf(Locale.US,
                "epsilon = %.1e | S ≈ %.17f | erro abs ≈ %.3e%n",
                eps, S, Math.abs(Math.PI - S));  // S = π teoricamente
        return S;
    }

    public static void main(String[] args) {
        exercicio2 obj = new exercicio2();
        double[] epsilons = {1e-5, 1e-10, 1e-15};
        for (double eps : epsilons) {
            obj.calcS(eps);
        }
    }

}