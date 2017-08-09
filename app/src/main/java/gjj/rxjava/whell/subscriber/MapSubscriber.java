package gjj.rxjava.whell.subscriber;

import gjj.rxjava.whell.operator.Transformer;

/**
 * 作者：l on 2017/8/9 18:15
 * 邮箱：gujj512@163.com
 */
public class MapSubscriber<T, R> extends Subscriber<R> {

    private Subscriber<? super T>subscriber;
    private Transformer<? super R,? extends T> transformer;

    public MapSubscriber(Subscriber<? super T>subscriber,Transformer<? super R,? extends T>transformer){
        this.subscriber=subscriber;
        this.transformer=transformer;
    }
    @Override
    public void onNext(R value) {
        subscriber.onNext(transformer.call(value));
    }

    @Override
    public void onError(Throwable throwable) {
        subscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        subscriber.onComplete();
    }
}
