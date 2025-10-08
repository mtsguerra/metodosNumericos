import java.util.*;

public class exercicio3 {

    public double calcS (double eps) {
        double S = 0.0;
        int k = 0;
        double current;

        while (true) {
            current = (k % 2 == 0 ? 1.0 : -1.0) / (2 * k + 1);
            if (Math.abs(current) < eps) break;
            S += current;
            k++;
        }
        S *= 4;
        System.out.printf("eps = %.0e, termos = %d, S ≈ %.16f%n", eps, k, S);
        return S;
    }

    public static void main(String[] args) {
        exercicio3 obj = new exercicio3();
        double[] epsilons = {1e-5, 1e-10, 1e-15};
        for (double eps : epsilons) {
            obj.calcS(eps);
        }
    }
}