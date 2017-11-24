package Indigo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by S.B Hwang on 2017-08-31.
 */

public class SelectBlackboxActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private String[] blackboxNameList = {"1st Black Box", "2nd Black Box"/*, "3rd Black Box"*/};
    private ListView blackboxListView;
    private Button addButton;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_blackbox);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, blackboxNameList);

        blackboxListView = (ListView)findViewById(R.id.blackbox_list);
        blackboxListView.setAdapter(adapter);
        blackboxListView.setOnItemClickListener(this);
        addButton = (Button)findViewById(R.id.add_button);
        addButton.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("blackbox_name", blackboxNameList[position]);
        switch(position) {
            case 0:
                intent.putExtra("start_sensor_a"    , "60:64:05:D1:30:00");
                intent.putExtra("start_sensor_b"    , "A8:1B:6A:AE:63:BD");
                intent.putExtra("height_sensor_a"   , "A8:1B:6A:AE:5F:E1");
                intent.putExtra("height_sensor_b"   , "60:64:05:D1:34:03");
                intent.putExtra("rotation_sensor_a" , "60:64:05:D1:3A:E9");
                intent.putExtra("rotation_sensor_b" , "60:64:05:D1:2B:50");
                intent.putExtra("reset_sensor_a"    , "60:64:05:D1:29:76");
                intent.putExtra("reset_sensor_b"    , "A8:1B:6A:AE:63:99");
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case 1:
                intent.putExtra("start_sensor_a"    , "60:64:05:D1:30:00");
                intent.putExtra("start_sensor_b"    , "A8:1B:6A:AE:63:BD");
                intent.putExtra("height_sensor_a"   , "A8:1B:6A:AE:46:46");
                intent.putExtra("height_sensor_b"   , "A8:1B:6A:AE:67:D4");
                intent.putExtra("rotation_sensor_a" , "A8:1B:6A:AE:63:D9");
                intent.putExtra("rotation_sensor_b" , "60:64:05:D1:53:C0");
                intent.putExtra("reset_sensor_a"    , "60:64:05:D1:32:57");
                intent.putExtra("reset_sensor_b"    , "A8:1B:6A:AE:3F:35");
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            /*case 2:
                intent.putExtra("start_sensor_a", "");
                intent.putExtra("start_sensor_b", "");
                intent.putExtra("height_sensor_a", "");
                intent.putExtra("height_sensor_b", "");
                intent.putExtra("rotation_sensor_a", "");
                intent.putExtra("rotation_sensor_b", "");
                intent.putExtra("reset_sensor_a", "");
                intent.putExtra("reset_sensor_b", "");
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;*/
        }
    }

    @Override
    public void onClick(View v) {
        // button click event
    }
}
