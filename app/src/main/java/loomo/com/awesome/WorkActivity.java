package loomo.com.awesome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.locomotion.sbv.Base;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import loomo.com.awesome.volley.VolleyErrorHelper;
import loomo.com.awesome.volley.VolleyRequestQueue;

public class WorkActivity extends AppCompatActivity {

    private static final int RUN_TIME = 1000;

    private final static String TAG = "WORKACTIVITY";

    private String triggeredPatientID;

    // looma
    Base mBase;
    boolean isBind = false;
    Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initBase();

        triggeredPatientID = getIntent().getStringExtra("id");
        if(triggeredPatientID.isEmpty()) {
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        initBase();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (isBind){
            mBase.unbindService();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (isBind){
            mBase.unbindService();
        }
    }

    // start moving
    private void moveTowardsLocation() {

        if(!isBind) {
            return;
        }

        mBase.setLinearVelocity(0.5f);

        // let the robot run for 2 seconds
        try {
            Thread.sleep(RUN_TIME);
        } catch (InterruptedException e) {
        }

        mBase.setLinearVelocity(0.5f);

        // let the robot run for 2 seconds
        try {
            Thread.sleep(RUN_TIME);
        } catch (InterruptedException e) {
        }
        mBase.setLinearVelocity(0.5f);

//        mBase.setAngularVelocity(-0.5f);
//
//        // let the robot run for 2 seconds
//        try {
//            Thread.sleep(RUN_TIME);
//        } catch (InterruptedException e) {
//        }
//
//        mBase.setLinearVelocity(0.5f);
//
//        // let the robot run for 2 seconds
//        try {
//            Thread.sleep(RUN_TIME);
//        } catch (InterruptedException e) {
//        }

        // stop
        mBase.setLinearVelocity(0);
        updateRecord();

        finish();
    }

    private void initBase() {
        // get Base Instance
        mBase = Base.getInstance();
        // bindService, if not, all Base api will not work.
        mBase.bindService(getApplicationContext(), new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {
                isBind = true;
                mTimer = new Timer();
                mTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
//                        final AngularVelocity av = mBase.getAngularVelocity();
//                        final LinearVelocity lv = mBase.getLinearVelocity();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mLeftTicks.setText("AngularVelocity:" + av.getSpeed());
//                                mRightTicks.setText("LinearVelocity:" + lv.getSpeed());
//                                mTicksTime.setText("Timestamp:" + av.getTimestamp());
//                            }
//                        });
                    }
                }, 50, 200);

                moveTowardsLocation();
            }

            @Override
            public void onUnbind(String reason) {
                isBind = false;
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                }
            }
        });
    }

    private void updateRecord() {

        if(triggeredPatientID.isEmpty()) {
            return;
        }

        // get data
        final String uri = "http://jboss.rivil.co:8082/api/singapore/" + triggeredPatientID ;

        final HashMap<String, String> params = new HashMap<>();
        params.put("Attended", "true");

        // VERIFY USER
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.isEmpty()) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrorHelper.displayError(error, getApplicationContext());
                    }
                }
        ) {
            @Override
            public HashMap<String, String> getParams() {
                return params;
            }
        };

        VolleyRequestQueue.getInstance(this).addToRequestQueue(stringRequest, TAG);
    }

}
