package com.antonio.clientudp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.ProgressBar;
import android.app.ActionBar.LayoutParams;

public class WaitingIcon extends Dialog {
    public Typeface font_roboto;

    public WaitingIcon show(Context context, CharSequence title,
                                 boolean indeterminate, boolean cancelable) {
        Log.d("MY MyProgressDialog ", "Constructor MyProgressDialog");
        WaitingIcon dialog = new WaitingIcon(context);

        dialog.setTitle(title);
        dialog.setCancelable(cancelable);

        //((TextView)dialog.findViewById(R.style.NewDialog)).setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/FONT"));
        /*For icon */
        LayoutParams layoutparam = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        //  layoutparam.height = 80;
        /* Add icon*/
        dialog.addContentView(new ProgressBar(context),layoutparam);
        dialog.show();

        return dialog;
    }

    public WaitingIcon(Context context) {
        super(context, R.style.NewDialog);
        //Typeface face=Typeface.createFromAsset(context.getAssets(),"fonts/FONT");
    }
}
