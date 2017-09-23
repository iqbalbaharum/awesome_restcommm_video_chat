package loomo.com.awesome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.locomotion.sbv.Base;

import loomo.com.awesome.service.TriggerService;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MAINACTIVITY";

    public final static int READ_FREQUENCY = 120000;

    private boolean isBind = false;

    private Base mBase;

    private Button mBtnStart;
    private Button mBtnEnd;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start service
                init();
            }
        });

        mBtnEnd = (Button) findViewById(R.id.btn_stop);
        mBtnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // stop service
                stop();
            }
        });

        mHandler = new Handler();
//        Intent intent = new Intent(getApplicationContext(), TriggerService.class);
//        startService(intent);

        mCheckTrigger.run();
    }

    Runnable mCheckTrigger = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), TriggerService.class);
            startService(intent);

            mHandler.postDelayed(this, READ_FREQUENCY);
        }
    };

    @Override
    protected void onDestroy() {

        stop();

        super.onDestroy();
    }

    private void init() {
        // get Base Instance
        mBase = Base.getInstance();

        mBase.bindService(getApplicationContext(), new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {
                isBind = true;
            }

            @Override
            public void onUnbind(String reason) {
                stop();
                isBind = false;
            }
        });
    }

    private void stop() {

        if(isBind){
            mBase.unbindService();
        }

        mBase = null;
    }

    public void goToWork(View view) {
    }
}
