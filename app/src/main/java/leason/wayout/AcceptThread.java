package leason.wayout;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by leason on 2016/12/6.
 */

 class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;

    private final BluetoothAdapter mmAdapter;
    InputStream inputStream;
    Activity activity;
    public AcceptThread(BluetoothAdapter adapter,Activity activity ) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;

        this.activity=activity;
      mmAdapter=adapter;
        try {
            // MY_UUID is the app's UUID string, also used by the client code

            tmp = mmAdapter.listenUsingRfcommWithServiceRecord("test",UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) { }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;

        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                socket = mmServerSocket.accept();

            } catch (IOException e) {
                break;
            }


            final BluetoothSocket finalSocket = socket;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    byte[][] tmp=null;
                    try {
                     tmp =  ManageConnectThread.receiveData(finalSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(activity,"connect  :"+ String.valueOf(tmp[0]),Toast.LENGTH_SHORT).show();
                }
            });

                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

        }


    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }
}