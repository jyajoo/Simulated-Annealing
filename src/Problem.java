public interface Problem {

    double fit(double x);
//    double fit(double x, double y);

    boolean isNeighborBetter(double f0, double f1);  // f0는 후보해의 적합도, f1은 이웃해의 적합도
}
