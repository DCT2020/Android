package com.example.administrator.myapplication.Fragments;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.IndigoFragment;
import com.example.administrator.myapplication.Module.AutoLauncherModule.ChangeActivity;
import com.example.administrator.myapplication.Module.AutoLauncherModule.FileManager;
import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 2017-11-27.
 */

public class AutoLauncherFragment extends IndigoFragment implements View.OnClickListener {

    private View view;
    private static final int CHANGED_AUTO_LAUNCH_APPLICATION_REQUEST = 1;

    private Button amChangeButton;
    private TextView amAppNameTextView;
    private ImageView amAppIconImageView;
    private Button pmChangeButton;
    private TextView pmAppNameTextView;
    private ImageView pmAppIconImageView;

    @Override
    protected void setLayoutId() {
        layoutId = R.layout.fragment_autolauncher;
    }

    @Override
    protected void onCreateView(View rootView) {

        view = rootView;

        AppOpsManager appOpsManager = (AppOpsManager)activity.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), activity.getPackageName());
        if(mode != AppOpsManager.MODE_ALLOWED) {
            startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), 2);
        }

        setLayout();
    }

    @Override
    public void onClick(View v) {
        Intent changeIntent = new Intent(activity, ChangeActivity.class);
        switch(v.getId()) {
            case R.id.am_change_button:
                changeIntent.putExtra("EXTRA_CHANGED", "AM");
                startActivityForResult(changeIntent, CHANGED_AUTO_LAUNCH_APPLICATION_REQUEST);
                break;
            case R.id.pm_change_button:
                changeIntent.putExtra("EXTRA_CHANGED", "PM");
                startActivityForResult(changeIntent, CHANGED_AUTO_LAUNCH_APPLICATION_REQUEST);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == activity.RESULT_OK) {
            switch(requestCode) {
                case CHANGED_AUTO_LAUNCH_APPLICATION_REQUEST:
                    FileManager fileManager = new FileManager(Environment.getExternalStorageDirectory().getAbsolutePath() + "/INDIGO/AutoLauncher/", "AutoLaunchAppInfo");
                    String changedTime = data.getStringExtra("EXTRA_CHANGED");
                    if(changedTime.equals("AM")) {
                        String appName = fileManager.category("AM").tag("AppName").read();
                        String packageName = fileManager.category("AM").tag("PackageName").read();
                        try {
                            amAppNameTextView.setText(appName);
                            amAppIconImageView.setImageDrawable(activity.getPackageManager().getApplicationIcon(packageName));
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (changedTime.equals("PM")) {
                        String appName = fileManager.category("PM").tag("AppName").read();
                        String packageName = fileManager.category("PM").tag("PackageName").read();
                        try {
                            pmAppNameTextView.setText(appName);
                            pmAppIconImageView.setImageDrawable(activity.getPackageManager().getApplicationIcon(packageName));
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    Intent intent = new Intent("ACTION_APP_CHANGED");
                    activity.sendBroadcast(intent);
                    break;
                case 2:
                    Log.d("Permission Check: ", "Access Denied");
                    break;
            }
        }
    }

    private void setLayout() {

        amChangeButton = (Button)view.findViewById(R.id.am_change_button);
        amChangeButton.setOnClickListener(this);
        amAppNameTextView = (TextView)view.findViewById(R.id.am_auto_launch_app_text);
        amAppIconImageView = (ImageView)view.findViewById(R.id.am_auto_launch_app_icon);

        pmChangeButton = (Button)view.findViewById(R.id.pm_change_button);
        pmChangeButton.setOnClickListener(this);
        pmAppNameTextView = (TextView)view.findViewById(R.id.pm_auto_launch_app_text);
        pmAppIconImageView = (ImageView)view.findViewById(R.id.pm_auto_launch_app_icon);

        FileManager fileManager = new FileManager(Environment.getExternalStorageDirectory().getAbsolutePath() + "/INDIGO/AutoLauncher/", "AutoLaunchAppInfo");
        String appName;
        String packageName;
        try {
            appName = fileManager.category("AM").tag("AppName").read();
            packageName = fileManager.category("AM").tag("PackageName").read();
            amAppNameTextView.setText(appName);
            amAppIconImageView.setImageDrawable(activity.getPackageManager().getApplicationIcon(packageName));

            appName = fileManager.category("PM").tag("AppName").read();
            packageName = fileManager.category("PM").tag("PackageName").read();
            pmAppNameTextView.setText(appName);
            pmAppIconImageView.setImageDrawable(activity.getPackageManager().getApplicationIcon(packageName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
