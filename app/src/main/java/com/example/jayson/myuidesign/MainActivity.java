package com.example.jayson.myuidesign;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.jayson.myuidesign.Modal.Person;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ServiceConnection {
    private static final String TAG = "jys";
    private EditText searchText;
    private Handler handler = new Handler();
    private Disposable mDisposable;

    HashMap<String,String> aaa = new HashMap<>();
    HashMap<String,Person> bbb = new HashMap<>(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aaa.put("a","b");

        Logger.addLogAdapter(new AndroidLogAdapter());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchText  = findViewById(R.id.editText);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Person per1 = new Person("jayson",30);
        Person per2 = new Person("jayson",30);

        if (per1.equals(per2)){
            Log.i(TAG,"per1 和 per2 相等");
        }else {
            Log.i(TAG,"per1 和 per2 不相等");
        }

        Log.i(TAG,"per1 hashcode:" + per1.hashCode());
        Log.i(TAG,"per2 hashcode:" + per2.hashCode());

        Intent intent = new Intent(this,RemoteService.class);

        bindService(intent,this, Context.BIND_AUTO_CREATE);

    }


    //1
    private Observable<String> createButtonObservable(){
        //2
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                //3
                final TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        emitter.onNext(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };


                //5
                searchText.addTextChangedListener(watcher);

                //6
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        searchText.removeTextChangedListener(watcher);
                    }
                });
            }
        }).filter(new Predicate<String>() {
            @Override
            public boolean test(String query) throws Exception {
                return query.length() >= 2;
            }
        }).debounce(1000, TimeUnit.MILLISECONDS);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onStart() {
        super.onStart();

//        Observable<String> stringObservable = createButtonObservable();

//        mDisposable = stringObservable
//            .observeOn(Schedulers.io())
//            .doOnNext(new Consumer<String>() {
//                @Override
//                public void accept(String s) throws Exception {
//                    Log.i("jys","doOnNext:" + s);
//                }
//            })
//            .map(new Function<String, String>() {
//                @Override
//                public String apply(String s) {
//                    return s+"LLLLLLLL";
//                }
//            })
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(new Consumer<String>() {
//                @Override
//                public void accept(String s) {
//                    Log.i("jys","接受消息：" + s);
//                }
//            });

//        testFlowable();
//        testFlowable2();

    }


    @SuppressLint("CheckResult")
    private void testBackPressure(){
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ;i++){
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("A");
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }


    @SuppressLint("CheckResult")
    private void testRx(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0 ;; i++){
                    emitter.onNext(i);
                    Thread.sleep(2000);// 每次发送完时间后延时2秒
                }
            }
        }).subscribeOn(Schedulers.io())
                .sample(2,TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                    }
                });
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0 ;; i++){
                    emitter.onNext(i);
                    Thread.sleep(2000);// 每次发送完时间后延时2秒
                }
            }
        }).subscribeOn(Schedulers.io())
                .sample(2,TimeUnit.SECONDS)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void testFlowable(){
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);

        Subscriber<Integer> downstream = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "onSubscribe");
                s.request(3);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.w(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };
        upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downstream);
    }

    void testFlowable2(){

        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                for (int i=0; ; i++){
                    Log.d(TAG,"emit:" + i);
                    e.onNext(i);
                }
            }
        },BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG,"onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG,"onNext" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG,"onError:",t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,"onComplete");
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    class LeakThread extends Thread{
        @Override
        public void run() {
            super.run();
        }
    }
}
