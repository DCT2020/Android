package com.example.administrator.synthesizeaplication;

import android.content.Intent;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;

import FunctionClasses.NfcHelper;

import static com.example.administrator.synthesizeaplication.UICreateHelper.*;

public class MainActivity extends AppCompatActivity {

    ListView FunctionListView;
    NfcAdapter nfcAdapter;
    String[] items;

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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        NfcHelper.getInstance().enable(this);
    }

    protected void onPause() {
        super.onPause();

        NfcHelper.getInstance().disable(this);
    }
}
