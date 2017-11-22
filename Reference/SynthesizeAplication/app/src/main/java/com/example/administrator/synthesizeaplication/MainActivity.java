package com.example.administrator.synthesizeaplication;

import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import BehaviorClasses.IndigoNfc;

public class MainActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(IndigoNfc.getInstance().init(this) == false){
            Toast.makeText(this,"Nfc initialization failed, please check nfc is it on",Toast.LENGTH_LONG).show();
        }
        else
        {
            IndigoNfc.getInstance().enable(this);
        }
    }


}
