import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {

    private int niter;  // 반복 횟수
    public ArrayList<Double> hist;

    public SimulatedAnnealing(int niter) {
        this.niter = niter;
        hist = new ArrayList<>();
    }

    public double solve(Problem p, double t, double a, double lower, double upper) {

        Random r = new Random();
        double x0 = r.nextDouble() * (upper - lower) + lower;
        double f0 = p.fit(x0);    // 후보해의 적합도
        hist.add(f0);

//        double y0 = r.nextDouble() * (upper - lower) + lower;
//        double f0 = p.fit(x0, y0);

        for (int i = 0; i < niter; i++) {  // 반복
            int kt = (int) t;
            for (int j = 0; j < kt; j++) {
                double x1 = r.nextDouble() * (upper - lower) + lower;
                double f1 = p.fit(x1);     // 이웃해의 적합도

//                double y1 = r.nextDouble()* (upper - lower) + lower;
//                double f1 = p.fit(x1, y1);

                if (p.isNeighborBetter(f0, f1)) {  // 이웃해가 더 적합하다면
                    x0 = x1;
                    f0 = f1;            // 이웃해를 후보해로 설정
                    hist.add(f0);
                } else {
                    double d = Math.abs(f1 - f0);
                    double p0 = Math.exp(-d / t);
                    if (r.nextDouble() < p0) {
                        x0 = x1;
                        f0 = f1;
                        hist.add(f0);
                    }
                }
            }
            t += a;
        }
        return x0;
    }
}
