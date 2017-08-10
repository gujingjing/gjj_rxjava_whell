package gjj.rxjava.whell.scheduler;

import java.util.concurrent.Executor;

/**
 * 作者：l on 2017/8/9 18:56
 * 邮箱：gujj512@163.com
 */
public class Scheduler {

    Executor executor;

    public Scheduler(Executor executor){
        this.executor=executor;
    }
    //创建work工作
    public Worker createWorker(){
        return new Worker(executor);
    }
}
