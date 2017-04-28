package leason.wayout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Set;

import io.palaima.smoothbluetooth.SmoothBluetooth;


/**
 * Created by leason on 2017/4/24.
 */

public class MyApplication extends android.app.Application {


    static SmoothBluetooth mSmoothBluetooth;
    static Boolean ispaired=false;
    /**
     * In practise you will use some kind of dependency injection pattern.
     */

    @Override
    public void onCreate() {
        super.onCreate();
         mSmoothBluetooth=new SmoothBluetooth(this);
        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if(device.getName().equals("HC-06")){
                    ispaired=true;
                }
            }
        }

    }

     public boolean isConnected() {
        return mSmoothBluetooth.isConnected();
    }


}
