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

            Resources res = getResources();
            items = res.getStringArray(R.array.items);

            //어뎁터를 생성 및 초기화합니다.
            ItemAdapter itemAdapter = new ItemAdapter(this, R.layout.function_listview_detail);
            {
                itemAdapter.addElementInfo(new ItemAdapter.IElementInitializer() {
                    @Override
                    public void InitAction(View v, int _childViewId, Object[] _dataContainerForInitialize, int index) {
                        TextView textView = (TextView) (v.findViewById(_childViewId));
                        textView.setText(_dataContainerForInitialize[index].toString());
                    }
                }, R.id.functionName, res.getStringArray(R.array.items));
            }

            FunctionListView = (ListView) (findViewById(R.id.functionListView));
            FunctionListView.setAdapter(itemAdapter);
            FunctionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent doBehaviorActivity = new Intent(getApplicationContext(), .class);
                    doBehaviorActivity.putExtra("com.example.administrator.ITEM_INDEX", i);
                    startActivity(doBehaviorActivity);
                }
            });

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
