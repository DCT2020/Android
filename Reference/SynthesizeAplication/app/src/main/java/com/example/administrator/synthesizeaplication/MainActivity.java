package com.example.administrator.synthesizeaplication;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import FunctionClasses.NfcHelper;
import Indigo.BlackBoxModel;

import static com.example.administrator.synthesizeaplication.UICreateHelper.*;

public class MainActivity extends AppCompatActivity{

    ListView FunctionListView;
    NfcAdapter nfcAdapter;
    String[] items;
    BlackBoxPresenterImpl  blackBoxPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(NfcHelper.getInstance().init(this) == false){
            Toast.makeText(this,"NFC를 지원하지 않는 단말기입니다.",Toast.LENGTH_LONG).show();
        }
        else
        {
            NfcHelper.getInstance().enable(this);
            NfcHelper.getInstance().setNfcActionCallBack(new NfcAdapter.ReaderCallback() {
                @Override
                public void onTagDiscovered(Tag tag) {
                    StringBuffer string = new StringBuffer();
                    string.append(NfcHelper.getInstance().byteArrayToHex(tag.getId()));
                    Log.i("Tag",string.toString());
                }
            });
        }

       // blackBoxPresenter = new BlackBoxPresenterImpl(this);

        ///////////////////////////////////////////////////////////////
        /*AppOpsManager appOpsManager = (AppOpsManager)getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        if(mode != AppOpsManager.MODE_ALLOWED) {
            startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), 2);
        }

        //setLayout();*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        NfcHelper.getInstance().enable(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        NfcHelper.getInstance().disable(this);
    }

/*
    @Override
    public void onClick(View view) {
        Intent changeIntent = new Intent(this, ChangeActivity.class);
        switch(view.getId()) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case CHANGED_AUTO_LAUNCH_APPLICATION_REQUEST:
                    FileManager fileManager = new FileManager(Environment.getExternalStorageDirectory().getAbsolutePath() + "/INDIGO/AutoLauncher/", "AutoLaunchAppInfo");
                    String changedTime = data.getStringExtra("EXTRA_CHANGED");
                    if(changedTime.equals("AM")) {
                        String appName = fileManager.category("AM").tag("AppName").read();
                        String packageName = fileManager.category("AM").tag("PackageName").read();
                        try {
                            amAppNameTextView.setText(appName);
                            amAppIconImageView.setImageDrawable(getPackageManager().getApplicationIcon(packageName));
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (changedTime.equals("PM")) {
                        String appName = fileManager.category("PM").tag("AppName").read();
                        String packageName = fileManager.category("PM").tag("PackageName").read();
                        try {
                            pmAppNameTextView.setText(appName);
                            pmAppIconImageView.setImageDrawable(getPackageManager().getApplicationIcon(packageName));
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    Intent intent = new Intent("ACTION_APP_CHANGED");
                    sendBroadcast(intent);
                    break;
                case 2:
                    Log.d("Permission Check: ", "Access Denied");
                    break;
            }
        }
    }

    private void setLayout() {
        setContentView(R.layout.activity_main);

        amChangeButton = (Button)findViewById(R.id.am_change_button);
        amChangeButton.setOnClickListener(this);
        amAppNameTextView = (TextView)findViewById(R.id.am_auto_launch_app_text);
        amAppIconImageView = (ImageView)findViewById(R.id.am_auto_launch_app_icon);

        pmChangeButton = (Button)findViewById(R.id.pm_change_button);
        pmChangeButton.setOnClickListener(this);
        pmAppNameTextView = (TextView)findViewById(R.id.pm_auto_launch_app_text);
        pmAppIconImageView = (ImageView)findViewById(R.id.pm_auto_launch_app_icon);

        FileManager fileManager = new FileManager(Environment.getExternalStorageDirectory().getAbsolutePath() + "/INDIGO/AutoLauncher/", "AutoLaunchAppInfo");
        String appName;
        String packageName;
        try {
            appName = fileManager.category("AM").tag("AppName").read();
            packageName = fileManager.category("AM").tag("PackageName").read();
            amAppNameTextView.setText(appName);
            amAppIconImageView.setImageDrawable(getPackageManager().getApplicationIcon(packageName));

            appName = fileManager.category("PM").tag("AppName").read();
            packageName = fileManager.category("PM").tag("PackageName").read();
            pmAppNameTextView.setText(appName);
            pmAppIconImageView.setImageDrawable(getPackageManager().getApplicationIcon(packageName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////
    private static final int CHANGED_AUTO_LAUNCH_APPLICATION_REQUEST = 1;

    private Button amChangeButton;
    private TextView amAppNameTextView;
    private ImageView amAppIconImageView;
    private Button pmChangeButton;
    private TextView pmAppNameTextView;
    private ImageView pmAppIconImageView;

    */
}
