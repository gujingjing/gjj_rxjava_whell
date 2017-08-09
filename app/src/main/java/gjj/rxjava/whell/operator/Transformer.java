package gjj.rxjava.whell.operator;

/**
 * 作者：l on 2017/8/9 10:56
 * 邮箱：gujj512@163.com
 */
public interface Transformer<T,R> {
    R call(T from);
}
