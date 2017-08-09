package gjj.rxjava.whell.observable;

import gjj.rxjava.whell.onsubscrib.MapOnSubscriber;
import gjj.rxjava.whell.onsubscrib.OnSubscrib;
import gjj.rxjava.whell.operator.Transformer;
import gjj.rxjava.whell.subscriber.Subscriber;

/**
 * 作者：l on 2017/8/9 10:22
 * 邮箱：gujj512@163.com
 */
//被观察者
public class Observable<T>{

    OnSubscrib<T> onSubscrib;

    //私有构造
    private Observable(OnSubscrib<T>onSubscrib){
        this.onSubscrib=onSubscrib;
    }
    //创造被观察者对象
    public static <T> Observable<T> create(OnSubscrib<T>onSubscrib){
        return new Observable<T>(onSubscrib);
    }

    //开始观察，观察者和被观察者建议联系
    public void subscrib(Subscriber<T> subscriber){
        subscriber.start();
        //被观察者拿到观察者对象
        onSubscrib.call(subscriber);
    }

    //实现map操作符
    public <R> Observable<R> map(final Transformer<? super T,? extends R> transformer){

        //生成新的observable和onsubscrib
        return create(new MapOnSubscriber<T,R>(Observable.this,transformer));
//        return create(new OnSubscrib<R>() {
//            @Override
//            public void call(final Subscriber<? super R> subscriber) {
//                //监听上层的observable
//                Observable.this.subscrib(new Subscriber<T>() {
//                    @Override
//                    public void onNext(T value) {
//                        //将上层发过来的消息转化发送出去
//                        subscriber.onNext(transformer.call(value));
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        subscriber.onError(throwable);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        subscriber.onComplete();
//                    }
//                });
//            }
//        });
    }
}
