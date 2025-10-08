import java.util.*;

class exercicio1{

    public double calculateEps(){
        double epsFinal = 1.0;
        while (true){
            if (1.0 + epsFinal / 2.0 == 1.0) break;
            epsFinal /= 2.0;
        }
        return epsFinal;
    }

    public static void main(String[] args) {
        exercicio1 ex1 = new exercicio1();
        double eps = ex1.calculateEps();
        System.out.println("O valor de eps é: " + eps);
    }
}