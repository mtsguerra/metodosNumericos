import java.util.*;

class exercicio4 {
    public static void main(String[] args) {
        exercicio2 e2 = new exercicio2();
        exercicio3 e3 = new exercicio3();
        double[] epsilons = {1e-5, 1e-10, 1e-15};
        for (double eps : epsilons) {
            double S2 = e2.calcS(eps);
            System.out.println("Erro absoluto: " + Math.abs(Math.PI - S2));
        }
        for (double eps : epsilons) {
            double S3 = e3.calcS(eps);
            System.out.println("Erro absoluto: " + Math.abs(Math.PI - S3));
        }

    }
}