package gjj.rxjava.whell.scheduler;

import java.util.concurrent.Executor;

/**
 * 作者：l on 2017/8/9 18:57
 * 邮箱：gujj512@163.com
 */
public class Worker {

    Executor executor;
    public Worker(Executor executor){
        this.executor=executor;
    }
    //执行操作
    public void schedule(Runnable runnable){
        executor.execute(runnable);
    }
}
