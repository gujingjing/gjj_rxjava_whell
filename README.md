#RXJava 造轮子

###什么是rxjava?
	rxjava 是一个响应式编程，为了更好更优雅的处理异步时间，内部实现原理是观察者模式
	
####自己造轮子实现Rxjava
>参考文档
>http://blog.csdn.net/tellh/article/details/71534704

观察者模式核心有两个，一个观察者一个被观察者

1. 先创建一个被观察者observer(基层抽象类)

		//观察者的基层抽象类
		public interface Observer<T> {
		    void onNext(T value);
		    void onError(Throwable throwable);
		    void onComplete();
		}
观察者内部实现了三个方法，完成，错误，下一步

2. 创建一个observable(观察者)
	
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
		  }
		  
	* 观察者的构造函数中传入了一个onSubscrib对象
		- onsubscrib实际就是观察者发生（发起通知）实现的对象
		- onsubscrib 中的call()方法,就是建立观察者和被观察者之间关系的桥梁
	
	onSubscrib类如下
	
			public interface OnSubscrib<T>{
		    	void call(Subscriber<T> subscriber);
			}
	
	###例子实现
	总体流程是这样的:
	
		1. 创建一个被观察者对象observable，传入了一个onsubscrib对象，用来设置通知发生的时机
		2. 创建一个观察者subscriber,依次实现抽象方法
		3. 通过被观察者observable调用subscrib(),传入了subscriber
			- 其实是observable中的onsubscrib拿到了subscriber
			- 拿到subscriber之后,根据出发条件通知观察者
		4. subscriber被调用抽象方法，观察者被通知了

	### 添加map操作符
	添加map操作符原理其实是这样的:
	
	1. 观察者模式先创建了observable对象，传入了onsubscrib对象
	2. 调用map操作符，生成了一对新的观察者和被观察者的observable 和subscriber
	3. 生成的subscriber 直接观察上层的observable
	4. 这样通知流程就完成了

如下图：
![map操作符](https://github.com/gujingjing/gjj_rxjava_whell/blob/master/images/map%E6%93%8D%E4%BD%9C%E7%AC%A6.jpeg?raw=true)


###线程切换实现
1.Worker是实现代码的runnable,会在指定的线程中工作
例如，代码中是一个单线程工作

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

2. Scheduler 提供了生成Worker的方法
3. 定义一个Schedule作为scheduler的工厂类,内部提供了各种线程，以供在worker中使用

###具体实现
1. 定义一个subscribOn()指定被观察者的工作线程


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


内部实现和map操作符类似
- 生成一个新的observeble和subscriber
- 监听上层observable,并且将onsubscrib.call()方法出发通知放在设置的线程中发送通知

2. 定义一个observeOn()指定观察者的线程

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

原理和上面一样
- 生成一个新的observeble和subscriber
- 监听上层observable,生成新的subscriber
- 在subscriber中，将next（）方法在指定的线程中运行

###测试例子

			Observable.create(new OnSubscrib<String>() {
			            @Override
			            public void call(Subscriber<String> subscriber) {
			                Log.e(TAGObservable, Thread.currentThread().getName());
			                subscriber.onNext("1");
			            }
			        })
			                .subscribOn(Schedulers.io())
			                .observeOn(Schedulers.io())
			                .subscrib(new Subscriber<String>() {
			                    @Override
			                    public void onNext(String value) {
			                        Log.e(TAGSubscriber, value);
			                        Log.e(TAGSubscriber, Thread.currentThread().getName());
			                    }

			                    @Override
			                    public void onError(Throwable throwable) {

			                    }

			                    @Override
			                    public void onComplete() {

			                    }
			                });

线程调度流程如下图：

![线程调度.jpeg](http://upload-images.jianshu.io/upload_images/1387450-6e4da78c2ca3201d.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


rxJava简单的轮子实现就先告一段落了，下面开始各种操作符的使用体验了啊