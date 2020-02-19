package com.fcc.trego;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class SuccessDialog
{
    public void showDialog(Activity activity, String msg)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.success_dialog);

        TextView textView = dialog.findViewById(R.id.text_success_dialog);
        textView.setText(msg);

        Button dialogButton = dialog.findViewById(R.id.btn_success_dialog_ok);
        dialogButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
