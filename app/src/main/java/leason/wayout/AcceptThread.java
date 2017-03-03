package leason.wayout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
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
    public AcceptThread(BluetoothAdapter adapter) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
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
            // If a connection was accepted
            final ManageConnectThread manageConnectThread=new ManageConnectThread();
            manageConnectThread.run();


            final BluetoothSocket finalSocket = socket;
            blueToothActivity.instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int tmp=0;
                    try {
                     tmp =  manageConnectThread.receiveData(finalSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(blueToothActivity.instance,"connect  :"+ String.valueOf(tmp),Toast.LENGTH_SHORT).show();



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