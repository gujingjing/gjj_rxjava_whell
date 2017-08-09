package gjj.rxjava.whell.onsubscrib;

import gjj.rxjava.whell.operator.Transformer;
import gjj.rxjava.whell.observable.Observable;
import gjj.rxjava.whell.subscriber.MapSubscriber;
import gjj.rxjava.whell.subscriber.Subscriber;

/**
 * 作者：l on 2017/8/9 18:08
 * 邮箱：gujj512@163.com
 */
public class MapOnSubscriber<T,R> implements OnSubscrib<R> {

    private Observable<T> observableSource;//上层的observable
    private Transformer<? super T,? extends R> transformer;//转换的

    public MapOnSubscriber(Observable<T>observableSource,Transformer<? super T,? extends R>transformer){
        this.observableSource=observableSource;
        this.transformer=transformer;
    }

    @Override
    public void call(Subscriber<R> subscriber) {
        observableSource.subscrib(new MapSubscriber<R,T>(subscriber,transformer));
    }
}
