package gjj.rxjava.whell.scheduler;

import java.util.concurrent.Executors;

/**
 * 作者：l on 2017/8/9 18:55
 * 邮箱：gujj512@163.com
 */
//这个类似管理线程的一个工厂类
public class Schedulers {

    //工厂类中提供了很多的线程池
    //单线程
    private static final Scheduler ioScheduler=new Scheduler(Executors.newSingleThreadExecutor());

    public static Scheduler io(){
        return ioScheduler;
    }
}
