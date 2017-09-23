package loomo.com.awesome.service;

import android.app.IntentService;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import loomo.com.awesome.WorkActivity;
import loomo.com.awesome.data.model.ATrigger;
import loomo.com.awesome.volley.VolleyErrorHelper;
import loomo.com.awesome.volley.VolleyRequestQueue;

/**
 * Created by MuhammadIqbal on 11/2/2017.
 */

public class TriggerService extends IntentService {

    private final static String TAG = "TRIGGERSERVICE";

    public TriggerService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    /***
     * Read trigger database
     */
    private void readTriggerAPI() {

        // get data
        final String uri = "http://jboss.rivil.co:8082/api/singapore";

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()) {
                            Gson gson = new Gson();
                            ATrigger[] triggers = gson.fromJson(response, ATrigger[].class);
                            for(ATrigger trigger: triggers) {
                                if(!trigger.isAttended()) {
                                    // initiate service
                                    Intent intent = new Intent(getApplicationContext(), WorkActivity.class);
                                    intent.putExtra("id", trigger.getId());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrorHelper.displayError(error, getApplicationContext());
                    }
                }
        );

        VolleyRequestQueue.getInstance(this).addToRequestQueue(stringRequest, TAG);
    }
}
