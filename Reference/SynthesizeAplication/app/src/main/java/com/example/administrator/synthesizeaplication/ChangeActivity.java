package com.example.administrator.synthesizeaplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by S.B Hwang on 2017-06-29.
 */

public class ChangeActivity extends Activity implements AdapterView.OnItemClickListener {

    private ArrayList<ApplicationInfo> dataLists;
    private PackageManager packageManager;
    private String dayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        dayTime = getIntent().getStringExtra("EXTRA_CHANGED");

        ListView applicationListView = (ListView) findViewById(R.id.application_listview);
        applicationListView.setOnItemClickListener(this);
        dataLists = new ArrayList<>();

        packageManager = getPackageManager();
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES | PackageManager.MATCH_UNINSTALLED_PACKAGES);
        for(ApplicationInfo appInfo : applications) {
            // Installed by User
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                dataLists.add(appInfo);
            }
        }
        CustomAdapter adapter = new CustomAdapter(this, dataLists);
        applicationListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FileManager fileManager = new FileManager(Environment.getExternalStorageDirectory().getAbsolutePath() + "/INDIGO/AutoLauncher/", "AutoLaunchAppInfo");
        fileManager.category(dayTime).tag("AppName").write(dataLists.get(position).loadLabel(packageManager).toString());
        fileManager.category(dayTime).tag("PackageName").write(dataLists.get(position).packageName);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("EXTRA_CHANGED", dayTime);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private class CustomAdapter extends ArrayAdapter<ApplicationInfo>{
        private ArrayList<ApplicationInfo> items;

        CustomAdapter(Context context, ArrayList<ApplicationInfo> listDatas) {
            super(context, 0, listDatas);
            items = listDatas;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if(view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.listview_item_application, null);
            }

            ImageView icon = (ImageView)view.findViewById(R.id.image_icon);
            TextView name = (TextView)view.findViewById(R.id.text_app_name);

            icon.setImageDrawable(items.get(position).loadIcon(packageManager));
            name.setText(items.get(position).loadLabel(packageManager).toString());
            return view;
        }
    }
}