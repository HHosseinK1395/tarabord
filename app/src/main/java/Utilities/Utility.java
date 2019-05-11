package Utilities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;

import ir.iro.passenger.R;

/**
 * Created by N550J on 5/18/2017.
 */
public class Utility {

    public static Dialog dialog = null;
    public static int PERMISSION_LOCATION = 1001;

    public static String MAP_INTENT_WARDENS = "wardens_payment";
    public static String MAP_INTENT_HANDY_TOLL = "handy_toll_payment";
    public static String MAP_INTENT_AUTOMATIC_TOLL = "automatic_toll_payment";
    public static String MAP_INTENT_TRAVE_PLAN = "travel_plan_payment";

    public static boolean isNetworkAvailable(Context appContext) {

        return NetworkUtils.hasInternetAccess(appContext);
    }

    public static void showProgress(Context context){

        dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_loading);
        dialog.show();
    }

    public static void hideProgress(){
        if(dialog != null)
            dialog.dismiss();
    }

    @TargetApi(12)
    public static void ShowSnakMessage(Context context, View view, int strSnakString, int iDuration) {

        Snackbar snackbar = Snackbar.make(view, context.getString(strSnakString), iDuration);
        snackbar.dismiss();
        View vSnackbar = snackbar.getView();
        snackbar.show();
    }


}
