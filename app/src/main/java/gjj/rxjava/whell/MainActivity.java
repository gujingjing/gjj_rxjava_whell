package gjj.rxjava.whell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import gjj.rxjava.whell.observable.Observable;
import gjj.rxjava.whell.onsubscrib.OnSubscrib;
import gjj.rxjava.whell.subscriber.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static String TAG="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TAG=getClass().getSimpleName();

        Observable<String> observable=Observable.create(new OnSubscrib<String>() {
            @Override
            public void call(Subscriber<String> subscriber) {
                for (int i=0;i<10;i++){
                    subscriber.onNext(i+"");
                }
            }
        });

        observable.subscrib(new Subscriber<String>() {
            @Override
            public void onNext(String value) {
                Log.e(TAG,value);
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
