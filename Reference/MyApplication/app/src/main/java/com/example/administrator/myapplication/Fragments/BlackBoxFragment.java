package com.example.administrator.myapplication.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.myapplication.IndigoFragment;
import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;

import java.util.ArrayList;

public class BlackBoxFragment extends IndigoFragment {

    private View view = null;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void setLayoutId() {
        layoutId = R.layout.fragment_blackbox;
    }

    @Override
    protected void onCreateView(View rootView) {
        view = rootView;

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("[설정]>[권한]에서 권한을 허용할 수 있습니다")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

        Button btn = (Button)(rootView.findViewById(R.id.button));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"이야후",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
