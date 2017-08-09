package gjj.rxjava.whell.subscriber;

/**
 * 作者：l on 2017/8/9 10:27
 * 邮箱：gujj512@163.com
 */
//观察者的基层抽象类
public interface Observer<T> {
    void onNext(T value);
    void onError(Throwable throwable);
    void onComplete();
}
