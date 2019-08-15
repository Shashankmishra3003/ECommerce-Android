package com.shashank.ecommerce.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.shashank.ecommerce.MainActivity;
import com.shashank.ecommerce.model.Request;

public class Common extends AppCompatActivity {
    Context context;

    //for displaying the order detail by using the model from order status;
    public static Request currentRequest;

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
                }).setCancelable(false).show();
    }

    public static void showEmptyCartAlertDialog(final Context context)
    {
        final Intent intent = new Intent(context, MainActivity.class);
        new AlertDialog.Builder(context)
                .setTitle("Your Cart is Empty")
                .setMessage("Please add Products to your Shopping Cart")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity)context).finish();
                        context.startActivity(intent);
                    }

        }).setCancelable(false).show();

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

    public void orderPlacedSnackbar(View view)
    {
        Snackbar snackbar = Snackbar
                .make(view,"Thank you, your Order is Placed",Snackbar.LENGTH_LONG)
                .setAction("",null);
        snackbar.setActionTextColor(Color.WHITE);
        View sbview = snackbar.getView();
        sbview.setBackgroundColor(Color.parseColor("#0394F4"));
        TextView textView = sbview.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
