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
