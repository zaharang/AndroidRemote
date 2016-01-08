package com.zaharang.mylgremote;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ConsumerIrManager IRManager;
    Map<Integer,String> IR_Table;
    String TAG="ZAHA_REMOTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tableInit();
        irInit();
    }

    public void tableInit(){
        IR_Table = new HashMap<Integer,String>();

        IR_Table.put(R.id.button_ch_up,  "0000 006D 0022 0002 0157 00AC 0015 0016 0015 0016 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0016 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0689 0157 0056 0015 0E94");
        IR_Table.put(R.id.button_pwr_off, "0000 006d 0026 0000 0155 00aa 0016 0015 0016 0015 0016 0040 0016 0015 0016 0015 0016 0014 0016 0015 0016 0015 0016 0040 0016 0040 0016 0015 0016 0040 0016 0040 0016 0040 0016 0040 0016 0040 0016 0040 0016 0014 0016 0040 0016 0015 0016 0015 0016 0014 0016 0040 0016 0040 0016 0014 0016 0040 0016 0015 0016 0040 0016 0040 0016 0040 0016 0014 0016 0015 0016 060b 0155 0055 0016 0e58 0155 0055 0016 00aa");
        IR_Table.put(R.id.button_pwr_on,   "0000 006d 0026 0000 0155 00aa 0016 0015 0016 0015 0016 0040 0016 0015 0016 0015 0016 0015 0016 0014 0016 0015 0016 0040 0016 0040 0016 0015 0016 0040 0016 0040 0016 0040 0016 0040 0016 0040 0016 0015 0016 0015 0016 0040 0016 0015 0016 0015 0016 0015 0016 0040 0016 0040 0016 0040 0016 0040 0016 0015 0016 0040 0016 0040 0016 0040 0016 0014 0016 0015 0016 060b 0155 0055 0016 0e58 0155 0055 0016 00aa");
        IR_Table.put(R.id.button_ch_down, "0000 006D 0022 0002 0156 00AC 0015 0015 0015 0015 0015 0041 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0041 0015 0041 0015 0015 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0619 0156 0056 0015 0E6F");
        IR_Table.put(R.id.button_vol_up,  "0000 006D 0022 0002 0157 00AC 0015 0016 0015 0016 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0016 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0016 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0016 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0689 0157 0056 0015 0E94");
        IR_Table.put(R.id.button_vol_down,"0000 006D 0022 0002 0157 00AC 0015 0016 0015 0016 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0016 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0016 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0041 0015 0689 0157 0056 0015 0E94");
    }

    public void irInit() {
        IRManager=(ConsumerIrManager)this.getSystemService(Context.CONSUMER_IR_SERVICE);
    }

    public void onClickButton(View v){
        int duration;
        int frequency;
        int pulse;

        String data = IR_Table.get(v.getId());
        String[] items = data.split(" ");
        int[] results = new int[items.length - 4];  //skip 4 sequences for dummies

        frequency = Integer.parseInt(items[1],16); //hex frequency,
        frequency = (int) (1000000 / (frequency * 0.241246));  //convert frequency
        pulse = 1000000 / frequency;

        for (int i = 0; i < results.length ; i++) {
            try {
                duration = Integer.parseInt(items[i+4], 16);
                results[i]  = (int) (duration * pulse);
            } catch (NumberFormatException nfe) {};
        }

        if (data != null) {
            try {
                IRManager.transmit(frequency,results);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG,"IR Transmit failed");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
