package com.fcc.trego;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class FailureDialog
{
    public void showDialog(Activity activity, String msg)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.failure_dialog);

        TextView textView = dialog.findViewById(R.id.text_failure_dialog);
        textView.setText(msg);

        Button backDialogButton = dialog.findViewById(R.id.btn_failure_dialog_back);
        backDialogButton.setOnClickListener(v -> dialog.dismiss());

        Button retryDialogButton = dialog.findViewById(R.id.btn_failure_dialog_retry);
        retryDialogButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
