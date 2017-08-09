package gjj.rxjava.whell.onsubscrib;

import gjj.rxjava.whell.subscriber.Subscriber;

/**
 * 作者：l on 2017/8/9 10:31
 * 邮箱：gujj512@163.com
 */
public interface OnSubscrib<T>{
    void call(Subscriber<T> subscriber);
}
