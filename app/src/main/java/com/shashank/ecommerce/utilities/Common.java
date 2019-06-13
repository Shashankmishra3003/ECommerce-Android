package com.shashank.ecommerce.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.shashank.ecommerce.R;

public class Common {
    public static boolean checkConnectivity(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager !=null)
        {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null)
            {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED)
                    return true;
            }
        }

        return false;
    }

    public static void showLogInAlertDialog(final Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle("No Internet Connection")
                .setMessage("Please Check your Internet Connection and Try Again")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity)context).finish();
                    }
                }).setNegativeButton("Cancel",null).show();
    }


}
