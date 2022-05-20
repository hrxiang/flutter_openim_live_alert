package io.openim.live.alert.flutter_openim_live_alert.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import io.openim.live.alert.flutter_openim_live_alert.FlutterOpenimLiveAlertPlugin;
import io.openim.live.alert.flutter_openim_live_alert.R;

public class CallService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;
    private TextView mTipsView;
    private final Point szWindow = new Point();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // init WindowManager
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        getWindowManagerDefaultDisplay();

        // Init LayoutInflater
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the floating view layout we created
        mFloatingView = inflater.inflate(R.layout.call, null);

        mTipsView = mFloatingView.findViewById(R.id.tv_tips);
        final TextView rejectButton = mFloatingView.findViewById(R.id.btn_reject);
        final TextView acceptButton = mFloatingView.findViewById(R.id.btn_accept);
        rejectButton.setOnClickListener(view -> {
            FlutterOpenimLiveAlertPlugin.channel.invokeMethod("reject", null);
        });
        acceptButton.setOnClickListener(view -> {
            FlutterOpenimLiveAlertPlugin.channel.invokeMethod("accept", null);
        });
        // Add the view to the window.
        WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        // Specify the view position
        params.gravity = Gravity.TOP | Gravity.CENTER;

        // Initially view will be added to top-left corner, you change x-y coordinates according to your need
        params.x = 0;
//        params.y = 300;

        // Add the view to the window
        mWindowManager.addView(mFloatingView, params);
    }

    private void getWindowManagerDefaultDisplay() {
        mWindowManager.getDefaultDisplay().getSize(szWindow);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) {
            mWindowManager.removeView(mFloatingView);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String tips = intent.getStringExtra("title");
        if (null != tips) {
            mTipsView.setText(tips);
        }
        return START_STICKY;
    }

    public static void startService(Context activity, String title) {
//        Intent i = new Intent();
//        i.setAction(Intent.ACTION_MAIN);
//        i.addCategory(Intent.CATEGORY_HOME);
//        activity.startActivity(i);
        Intent intent = new Intent(activity, CallService.class);
        intent.putExtra("title", title);
        activity.startService(intent);
    }

    public static void stopService(Context activity) {
        Intent intent = new Intent(activity, CallService.class);
        activity.stopService(intent);
    }
}
