package gjj.rxjava.whell.observable;

import gjj.rxjava.whell.onsubscrib.MapOnSubscriber;
import gjj.rxjava.whell.onsubscrib.OnSubscrib;
import gjj.rxjava.whell.operator.Transformer;
import gjj.rxjava.whell.scheduler.Scheduler;
import gjj.rxjava.whell.scheduler.Worker;
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
    /**
     * subscribOn
     * 指定观察者的运行线程
     */
    public Observable<T> subscribOn(final Scheduler scheduler){

        return Observable.create(new OnSubscrib() {
            @Override
            public void call(final Subscriber subscriber) {
                //将线程发送的操作用在具体的线程中
                scheduler.createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        //订阅上层观察者
                        Observable.this.subscrib(subscriber);
                    }
                });
            }
        });
    }
    /**
     * observeOn
     * 用于指定观察者执行的线程
     */
    public Observable observeOn(final Scheduler scheduler){

        //需要将subscriber中的方法放置在对应线程中处理
        return Observable.create(new OnSubscrib() {
            @Override
            public void call(final Subscriber subscriber) {

                final Worker worker=scheduler.createWorker();
                Observable.this.subscrib(new Subscriber<T>() {
                    @Override
                    public void onNext(final T value) {
                        //下一步执行在指定线程中
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onNext(value);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        subscriber.onError(throwable);
                    }

                    @Override
                    public void onComplete() {
                        subscriber.onComplete();
                    }
                });
            }
        });
    }
}
