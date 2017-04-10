package leason.wayout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Message;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by leason on 2016/12/6.
 */

class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothAdapter mmAdapter;

    public ConnectThread(BluetoothSocket socket, BluetoothAdapter adapter) {
        mmAdapter = adapter;
        mmSocket = socket;
    }

    public void run() {

        mmAdapter.cancelDiscovery();
        try {
           if(!mmSocket.isConnected()) {
               mmSocket.connect();
           }
               // ManageConnectThread manageConnectThread = new ManageConnectThread();
          //  ManageConnectThread.sendData(mmSocket, 97);
            Message msg=new Message();
            msg.what=1;
            MainService.mainService.handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Will cancel an in-progress connection, and close the socket
     */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }
}