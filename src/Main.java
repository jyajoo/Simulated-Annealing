public class Main {

    public static void main(String[] args) {

//        double[] value = {30, 27, 24, 20, 17, 14, 11, 9, 5, 2};
        SimulatedAnnealing sa = new SimulatedAnnealing(1000);
        Problem p = new Problem() {

            @Override
            public double fit(double x) {
//                return x * x * x * x - x * x * x - 5 * x * x - 5;
                return -0.1083*x + 29.479;
            }

//            @Override
//            public double fit(double x, double y) {
//                double avg = 0;
//                double sum =0;
//                for (int i = 0; i < value.length; i++) {
//                    sum += Math.abs(value[i] - (x * i + y));
//                }
//                avg = sum/value.length;
//                return avg;
//            }

            @Override
            public boolean isNeighborBetter(double f0, double f1) {
                return f0 > f1;
            }
        };

        double x = sa.solve(p, 100, 0.99, 0, 3);
        System.out.println("x 좌표 : " + x);
        System.out.println("y 좌표 : " + p.fit(x));
//        System.out.println(sa.hist);
    }
}

