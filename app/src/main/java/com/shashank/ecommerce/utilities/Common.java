package com.shashank.ecommerce.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.shashank.ecommerce.R;

public class Common extends AppCompatActivity {
    Context context;

    public Common(Context context)
    {
        this.context = context;
    }

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

    public void addedToCartSnackBar(View view)
    {
        Snackbar snackbar = Snackbar
                .make(view,"Product added to Cart",Snackbar.LENGTH_LONG)
                .setAction("",null);
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.parseColor("#0394F4"));
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
