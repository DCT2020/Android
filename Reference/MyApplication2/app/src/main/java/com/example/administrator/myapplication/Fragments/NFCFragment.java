package com.example.administrator.myapplication.Fragments;

import android.content.ClipData;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.IndigoFragment;
import com.example.administrator.myapplication.ItemAdapter;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Module.NFCModule.*;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-27.
 */

public class NFCFragment extends IndigoFragment {

    private View view;
    private XMLManager xmlManager = new XMLManager();
    private ArrayList<Car> carContainer = null;

    public NFCFragment(){
            }

    @Override
    protected void setLayoutId() {
        layoutId = R.layout.fragment_nfc;
    }

    @Override
    protected void onCreateView(final View rootView) {

        view = rootView;

        NfcHelper.getInstance().setNfcActionCallBack(new NfcAdapter.ReaderCallback() {
            @Override
            public void onTagDiscovered(Tag tag) {
                final StringBuffer string = new StringBuffer();
                string.append(NfcHelper.getInstance().byteArrayToHex(tag.getId()));
                Log.i("Tag",string.toString());

                // Only the original thread that created a view hierarchy can touch its views.을 회피하기 위함.
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView text = (TextView)(view.findViewById(R.id.nfc_address_textView));
                        text.setText(string.toString());
                        text.setTextColor(getResources().getColor(R.color.colorBlack));
                    }
                });
            }
        });

        Button button = (Button)(rootView.findViewById(R.id.nfc_add_button));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {

                TextView carNumber = (TextView)(view.findViewById(R.id.car_number));
                TextView nfcAddress = (TextView)(view.findViewById(R.id.nfc_address_textView));

                String currentNfcId = new String();
                currentNfcId = nfcAddress.getText().toString();

                String carNumberText = carNumber.getText().toString();
                if(carNumberText.equals("") || Integer.parseInt(carNumberText) <= 0){
                    Toast.makeText(activity.getApplicationContext(),"유효한 차량 번호가 아닙니다.",Toast.LENGTH_LONG).show();
                }
                else if(!currentNfcId.equals(getString(R.string.nfc_address_default))
                        && !currentNfcId.equals("")){

                    Car car = new Car(Integer.parseInt(carNumber.getText().toString())
                    );
                    car.addTag(new StringBuffer(nfcAddress.getText()));

                    if(removingDuplication(car)) {
                        xmlManager.writeToXmlFile(view, carContainer);
                        Toast.makeText(activity.getApplicationContext(),"등록되었습니다.",Toast.LENGTH_LONG).show();
                        nfcAddress.setTextColor(getResources().getColor(R.color.colorGood));
                    }
                    else{
                        nfcAddress.setTextColor(getResources().getColor(R.color.colorNotGood));
                    }
                }
                else{
                    Toast.makeText(activity.getApplicationContext(),"유효하지 않은 Nfc주소 입니다.",Toast.LENGTH_LONG).show();
                    nfcAddress.setTextColor(getResources().getColor(R.color.colorNotGood));
                }
            }

        });

        xmlManager.Init(activity);
       carContainer = xmlManager.readToXmlFile();

        Button initButton  = (Button)(view.findViewById(R.id.init_button));
        initButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)(view.findViewById(R.id.init_approval_checkbox));
                if(checkBox.isChecked()){
                    checkBox.setChecked(false);
                    xmlManager.writeToXmlFile(view, new  ArrayList<Car>());
                }
            }
        });
    }

    private Boolean removingDuplication(Car car){

        Boolean isSameCarNumber = false;
        Car temp = null;
        for(Car c  : carContainer) {
            // tag 중복
            for(StringBuffer str : c.getTags()){
                if(str.toString().equals(car.getTags().get(0).toString())){
                    Toast.makeText(activity.getApplicationContext(),"중복된 Nfc카드 입니다.",Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            // 차량번호가 같고 Nfc 아이디가 다름.
            if(c.getNumber() == car.getNumber()){
                temp = c;
                isSameCarNumber = true;
            }
        }

        // carContainer 루프를 한번만 돌기위한 방책
        if(isSameCarNumber){
            temp.addTag(car.getTags().get(0));
            return true;
        }

        // 중복없음 -> 새로운 값 추가
        carContainer.add(car);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        NfcHelper.getInstance().enable(activity);
    }

    @Override
    public void onPause() {
        super.onPause();

        NfcHelper.getInstance().disable(activity);
    }

}
