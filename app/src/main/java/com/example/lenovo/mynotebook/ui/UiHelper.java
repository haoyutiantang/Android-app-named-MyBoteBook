package com.example.lenovo.mynotebook.ui;

import android.content.Context;
import android.widget.Toast;

public class UiHelper {
    public static void toastShowMessageShort(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
