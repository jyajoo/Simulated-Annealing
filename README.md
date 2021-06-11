# SimulatedAnnealing



## 모의 담금질 기법

높은 온도(T)에서 액체 상태인 물질이 있다. 온도가 점차 낮아지는 동안 이 물질이 결정체로 변화하는 과정을 모방한 해를 탐색하는 알고리즘이다.

용융 상태에서는 물질의 분자가 자유로이 움직이는데, 이를 모방하여 해를 탐색하는 과정도 특정한 패턴 없이 이루어진다.

온도가 점점 낮아지면 분자의 움직임이 점점 줄어들어 결정체가 되는데, 해 탐색 과정도 이와 유사하게 점점 더 규칙적인 방식으로 이루어진다.



> 임의의 후보해 s 선택
>
> 초기 T 설정
>
> repeat
>
> ​	for i = 1 to k(T) {     // k(T)는 T에서의 반복 횟수
>
> ​		  s의 이웃해 중에서 랜덤하게 하나의 해 s' 를 선택한다.
>
> ​		  d = (s' 의 값) - (s 의 값)
>
> ​		  if(d < 0)   s = s'           // 이웃해인 s'가 s보다 우수한 경우
>
> ​		  else
>
> ​                q : 0과 1 사이에서 랜덤하게 선택
>
> ​				if( q < p )   s = s'   }
>
> ​	1보다 작은 상수 a(냉각율, cooling ratio)를 곱하며 일정 비율로 T를 감소시킨다.
>
> return s

- 값의 차이가 큼에도 불구하고 p를 크게 하면 탐색되어온 결과가 무시될 수 있으므로
- 확률 p는 T와 비례하고 d와 반비례하여 변화할 수 있도록 해야 한다.
- 확률 p는 1 / e^(d/T) = e^(-d/T) 로 정의된다.



```java
public interface Problem {

    double fit(double x);

    boolean isNeighborBetter(double f0, double f1);  // f0는 후보해의 적합도, f1은 이웃해의 적합도
}
```


- `fit` : 해당 x좌표의 y 좌표를 구하는 함수
- `isNeighborBetter` : 후보해와 이웃해를 비교하며 무엇이 더 적합한지를 확인하는 함수



```java
public class Main {

    public static void main(String[] args) {

        SimulatedAnnealing sa = new SimulatedAnnealing(1000);
        Problem p = new Problem() {

            @Override
            public double fit(double x) {
                return x * x * x * x - x * x * x - 5 * x * x - 5;
            }

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
```


- `fit` 과 `isNeighborBetter`를 오버라이딩해준다.



```java
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

        for (int i = 0; i < niter; i++) {  // 반복
            int kt = (int) t;
            for (int j = 0; j < kt; j++) {
                double x1 = r.nextDouble() * (upper - lower) + lower;
                double f1 = p.fit(x1);     // 이웃해의 적합도

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
```

- t : 초기 온도, a : 냉각율, lower & uppder : x좌표의 범위 지정, nither : 반복 횟수
- k(T) 는 현재 T에서 for 루프가 수행되는 횟수이며, 초기 온도로 설정해주었다.
- 이웃해를 범위에 알맞게 랜덤한 수로 선택하고 이웃해의 결과값을 구해준다.
- 따라서 이웃해와 후보해 중 어느 것이 더 적합한 지를 비교해주며 가장 최소값이 될 때의 x좌표를 구한다.




## 4차 함수 전역 최적점

y = x^4 - x^3 - 5x^2 - 5

<img src="https://user-images.githubusercontent.com/73464584/121650175-42f28680-cad4-11eb-97ed-081a4399c885.png" alt="image-20210611163325378" style="zoom:70%;" />

해당 그래프를 그려보면 전역 최적점은 (2, -17)이다.

t = 1, a = 0.99로 설정


[결과값]

<img src="https://user-images.githubusercontent.com/73464584/121654180-6d464300-cad8-11eb-9141-7a27da967cad.png" alt="image-20210611171339520" style="zoom:150%;" />

최적해와 유사한 값임을 확인해볼수 있다.

p0의 값을 임의로 0.001로 해준 후 결과를 구해주면, 정확도가 매우 높은 최적해를 구할 수 있다.




## 데이터 값과 추정

#### 데이터

시간이 지날 수록 양초의 길이가 짧아진다. 양초의 길이는 30cm라고 가정하자.

- 독립 변수(x) : 소요 시간(분)

- 종속 변수(y) : 양초의 길이
- 선형 모델

| 소요 시간(분) | 1    | 26   | 53   | 83   | 108  | 138  | 167  | 189  | 230  | 259  |
| ------------- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- |
| 양초 길이     | 30   | 27   | 24   | 20   | 17   | 14   | 11   | 9    | 5    | 2    |




#### 추정

- 우리가 알고 싶은 방정식은 원래의 자료와의 오차(error)를 가장 적게 차이나도록 구성된 식이다.

- 이러한 선을 회귀선이라고 하며,

  따라서, 이래에 표현된 그림은 독립변수 x와 종속 변수 y 그리고 두 변수들간의 상관관계를 표현한 회귀선을 나타낸 그림이다.

![image-20210611181308764](https://user-images.githubusercontent.com/73464584/121698549-1311a600-cb09-11eb-86a7-edf3c356a886.png)

x축은 소요 시간을 y축은 양초의 길이이며, 점으로 해당 데이터들을 표현하였다.

해당 데이터들을 통해 추세선으로 표현을 해본다면 y = - 0.1083x + 29.479 라는 결과를 확인할 수 있다.

모의 담금질 기법을 이용하여 y = ax + b에서 a 는 -0.1083인 것을, b = 29.479인 것을 추정해낼 수 있다.



---

선형 모델에서 추정할 수 있는 값을 예측값이라고 하고, 실제 데이터에서 얻은 값을 관측값이라고 하면,

이 둘의 차이(오차, error)들을 구해주고, 절대값을 취해준 후의 모두 합해 주고 평균을 계산해주는 것을 `MAE(Mean Absolute Error)`라고 한다.

![image-20210611200828463](https://user-images.githubusercontent.com/73464584/121677428-ce7a1080-caf0-11eb-979e-3e783184d503.png)

혹은 이 차이를 제곱하여 평균을 계산해주는 것은 `MSE(Mean Squared Error)`라고 한다.

<img src="https://user-images.githubusercontent.com/73464584/121677347-b609f600-caf0-11eb-96cc-ee23de96ed73.png" alt="image-20210611200727758" style="zoom:67%;" />

이 것을 우리는 `비용 함수(cost function)`이라 한다.


```java
double[] value = {30, 27, 24, 20, 17, 14, 11, 9, 5, 2};
```

```java
@Override
public double fit(double x, double y) {
    double avg = 0;
    double sum =0;
    for (int i = 0; i < value.length; i++) {
        sum += Math.abs(value[i] - (x * i + y));
    }
    avg = sum/value.length;
    return avg;
}
```

- MSE 를 구해주므로써 비용 함수(cost funtion)을 구해준다.

`경사 하강법(Gradient Descent)`: 비용 함수를 미분하여, 기울기를 이용하여 x 값에 따라 비용 함수의 최소값이 어디인지를 찾아본다.

그럼 이 최소값이 되도록 하는 a와 b를 구하게 되면 회귀선을 추정해낼 수 있다.



## 마무리

모의 담금질 기법은 범위 설정, 초기 온도나 냉각율 설정 등에 따라 결과와 성능에 차이가 드러난다.

위에서 최적해를 찾아주면서 모의 담금질 기법이 항상 최적해를 찾을 수 있는 것이 아니다라는 사실을 확인할 수 있었다.

이웃해를 정의해주는 방법에는 총 3가지 방법이 있는데, 삽입, 교환, 반전의 방식으로 표현을 해주면 최적해를 찾을 가능성을 높일 수 있다.

구현해준 코드는 이웃해를 랜덤으로 해주는 방식을 택하였기 때문에 위에 말했듯이 이웃해를 정의해주는 방식을 사용하거나, 

초기 온도와 냉각율을 조절해주는 방식을 택하여 최적해를 찾을 가능성을 높일 수도 있다.

또, niter(반복 횟수)를 크게 해줄 수록 여러 번 비교하여 최적해와 가장 가까운 값을 찾아낼 수 있으므로, 시간적으로 효율을 굉장히 낮아질 수 있겠지만, 최적해를 찾을 가능성을 높일 수 있다.

