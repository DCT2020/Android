package com.example.administrator.myapplication.Fragments;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.IndigoFragment;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.module.Car;
import com.example.administrator.myapplication.module.NfcHelper;
import com.example.administrator.myapplication.module.XMLManager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-27.
 */

public class NFCFragment extends IndigoFragment {

    private View view;
    private StringBuffer currentNfcId = new StringBuffer();
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

                String carNumberText = carNumber.getText().toString();
                if(carNumberText.equals("") || Integer.parseInt(carNumberText) < 0){
                    Toast.makeText(activity.getApplicationContext(),"유효한 차량 번호가 아닙니다.",Toast.LENGTH_LONG).show();
                }
                else if(!currentNfcId.toString().equals(getString(R.string.nfc_address_default))){
                    Car car = new Car(Integer.parseInt(carNumber.getText().toString()));
                    car.addTag(new StringBuffer(nfcAddress.getText()));

                    if(removingDuplication(car)) {
                        xmlManager.writeToXmlFile(view, carContainer);
                    }
                }
                else{
                    Toast.makeText(activity.getApplicationContext(),"유효하지 않은 Nfc주소 입니다.",Toast.LENGTH_LONG).show();
                }
            }

        });

        xmlManager.Init(activity);
       carContainer = xmlManager.readToXmlFile();
    }

    private Boolean removingDuplication(Car car){

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
                c.addTag(car.getTags().get(0));
                return true;
            }
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
