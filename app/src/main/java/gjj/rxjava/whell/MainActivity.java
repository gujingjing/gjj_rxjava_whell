package gjj.rxjava.whell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import gjj.rxjava.whell.observable.Observable;
import gjj.rxjava.whell.onsubscrib.OnSubscrib;
import gjj.rxjava.whell.scheduler.Schedulers;
import gjj.rxjava.whell.subscriber.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "";
    private static String TAGObservable = "";
    private static String TAGSubscriber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TAG = getClass().getSimpleName();
        TAGObservable = TAG + "-observable-";
        TAGSubscriber = TAG + "-subscriber-";

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
    }
}
