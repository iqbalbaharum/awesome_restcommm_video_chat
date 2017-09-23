package loomo.com.awesome.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;

/**
 * Created by iQBAL on 12/2/2016.
 */
public class VolleyErrorHelper {


    public static void displayError(VolleyError error, Context context) {
        // TODO
    }

    /***
     * Get corresponding message about the error
     * @param error
     * @param context
     * @return
     */
    private static String getMessage(Object error, Context context) {

        /*if(error instanceof TimeoutError) {
            return context.getResources().getString(R.string.err_server_down);
        } else if( isServerProblem(error)) {
            return handleServerError(error, context);
        } else if( isNetworkProblem(error)) {
            return context.getResources().getString(R.string.err_no_internet);
        }*/

        return "Error on VOLLEY";
    }

    /***
     * Determine if the error is network-related error
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return ((error instanceof NetworkError) || (error instanceof NoConnectionError));
    }

    /***
     * Determine if the error is server-related error
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return ((error instanceof ServerError) || (error instanceof AuthFailureError));
    }

    /***
     * Handle message received from server error
     * @param error
     * @param context
     * @return
     */
    private static String handleServerError(Object error, Context context) {

        VolleyError volleyError = (VolleyError) error;

        NetworkResponse response = volleyError.networkResponse;

        if(response != null) {
            switch(response.statusCode) {
                default:{
                    //return context.getResources().getString(R.string.err_server_down);
                }
            }
        }

        return "VOLLEY - generic error";
    }
}
