import java.util.*;

class exercicio2{

    private double factorial(int n) {
        if (n == 0 || n == 1) return 1.0;
        return n * factorial(n - 1);
    }

    private double calcCurrent(int k){
        return (factorial(2 * k)
                / (Math.pow(4, k) * Math.pow(factorial(k), 2) * (2 * k + 1)))
                * Math.pow(0.5, 2 * k + 1);
    }

    private void caclS(double eps) {
        double S = 0.0;
        int k = 1;
        double current;
        current = calcCurrent(0);
        S += current;
        while (Math.abs(current) > eps) {
            current = calcCurrent(k);
            S += current;
            k++;
        }
        S *= 6;

        System.out.println("epsilon: " + eps);
        System.out.println("S: " + S);
    }

    public static void main(String[] args) {
        exercicio2 obj = new exercicio2();
        double[] epsilons = {1e-5, 1e-10, 1e-15};
        for (double eps : epsilons) {
            obj.caclS(eps);
        }
    }

}