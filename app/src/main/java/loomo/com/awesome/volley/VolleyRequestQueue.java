package loomo.com.awesome.volley;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by iQBAL on 12/2/2016.
 */
public class VolleyRequestQueue {

    private static final String TAG = "JSONRequestQueue";

    private static final int VOLLEY_TIMEOUT = 20 * 1000;
    private static final int VOLLEY_NUMBER_OF_TRIES = 3;

    private static VolleyRequestQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private VolleyRequestQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * return singleton instance
     * @return RequestQueue instance
     */
    public static synchronized VolleyRequestQueue getInstance(Context context) {

        if(mInstance == null) {
            mInstance = new VolleyRequestQueue(context);
        }
        return mInstance;
    }

    /***
     * Pull the request queue
     * @return
     */
    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    /***
     * Add request to global queue
     * @param request
     * @param tag
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> request, String tag) {

        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        // set timeout
        request.setRetryPolicy(new DefaultRetryPolicy(VOLLEY_TIMEOUT, VOLLEY_NUMBER_OF_TRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue().add(request);
    }

    /***
     * add request with DEFAULT tag
     * @param request
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    /***
     * Cancel pending request
     * @param tag
     */
    public void cancelPendingRequest(Object tag) {
        if(mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
