package leason.wayout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectActivity extends AppCompatActivity {
    List<Map<String, Object>> list;
    BluetoothAdapter BA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);


        BA = BluetoothAdapter.getDefaultAdapter();
        if (BA.isDiscovering()) {
            BA.cancelDiscovery();
        }
        BA.startDiscovery();





        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                if (action.equals(BluetoothDevice.ACTION_FOUND)) {

                    BluetoothDevice Bdevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    if (Bdevice.getName().equals("HC-06")) {
                        new ConnectThread(Bdevice, BA).run();
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                }
            }


        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
    }




}
