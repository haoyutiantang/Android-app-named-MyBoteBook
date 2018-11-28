package com.example.lenovo.mynotebook.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lenovo.mynotebook.R;
import com.example.lenovo.mynotebook.function.StringFunction;

public class MessageBox {

    private AlertDialog.Builder dialogBuilder = null;
    private Context context=null;


    public MessageBox(Context context) {
        dialogBuilder = new AlertDialog.Builder(context);
        this.context = context;
    }

    private void showTwoButtonDialog(String message, String title,
                                     String firstButtonText, String secondButtonText,
                                     MessageBox.IButtonClick okButtonListener,
                                     MessageBox.IButtonClick cancelButtonListener) {
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        final MessageBox.IButtonClick okListener = okButtonListener;
        dialogBuilder.setPositiveButton(firstButtonText,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (okListener != null)
                            okListener.doSomething();
                        dialog.dismiss();

                    }
                });
        final MessageBox.IButtonClick cancelListener = cancelButtonListener;
        dialogBuilder.setNegativeButton(secondButtonText,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (cancelListener != null)
                            cancelListener.doSomething();
                        dialog.dismiss();
                    }
                });
        dialogBuilder.create().show();
    }

    public void showOKOrCancelDialog(String message, String title,
                                     MessageBox.IButtonClick okButtonListener,
                                     MessageBox.IButtonClick cancelButtonListener) {
        showTwoButtonDialog(message, title, "确定", "取消", okButtonListener,
                cancelButtonListener);
    }

    public void showInputDialog(String dialogTitle, String inputDescription,
                                String defaultText,
                                final MessageBox.ISimpleInputDialogButtonClick okButtonListener,
                                final MessageBox.ISimpleInputDialogButtonClick cancelButtonListener) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.inputdialog, null);

        final TextView messageTextView = (TextView) dialogView
                .findViewById(R.id.inputDialog_tvMessage);
        final EditText userInputEditText = (EditText) dialogView
                .findViewById(R.id.inputDialog_edtUserInput);

        dialogBuilder.setCancelable(true);
        dialogBuilder.setTitle(dialogTitle);
        messageTextView.setText(inputDescription);

        if(StringFunction.isNotNullOrEmpty(defaultText)){
            userInputEditText.setText(defaultText);

        }

        dialogBuilder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (okButtonListener != null
                                && userInputEditText != null
                                && StringFunction
                                .isNotNullOrEmpty(userInputEditText
                                        .getText().toString())) {
                            okButtonListener.doSomething(userInputEditText
                                    .getText().toString());
                        }else{
                            UiHelper.toastShowMessageShort(context,"名字不能为空,请重新输入");
                        }

                    }
                });

        dialogBuilder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (cancelButtonListener != null) {
                            if (userInputEditText != null
                                    && StringFunction
                                    .isNotNullOrEmpty(userInputEditText
                                            .getText().toString())) {
                                cancelButtonListener
                                        .doSomething(userInputEditText
                                                .getText().toString());
                            } else {
                                cancelButtonListener.doSomething("");
                            }

                        }

                    }
                });
        dialogBuilder.setView(dialogView);
        dialogBuilder.create().show();
        //try {
        //    Looper.loop();}catch (Exception e) {}
    }


    public interface IButtonClick {
        void doSomething();
    }

    public interface ISimpleInputDialogButtonClick {
        void doSomething(String userInput);
    }
}
