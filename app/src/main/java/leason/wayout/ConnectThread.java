package leason.wayout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by leason on 2016/12/6.
 */

class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothAdapter mmAdapter;

    public ConnectThread(BluetoothDevice device, BluetoothAdapter adapter) {
        BluetoothSocket tmp = null;

        mmAdapter = adapter;
        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) {
        }

        mmSocket = tmp;

    }

    public void run() {

        mmAdapter.cancelDiscovery();
        try {
            mmSocket.connect();

            ManageConnectThread manageConnectThread = new ManageConnectThread();

            manageConnectThread.run();
            manageConnectThread.sendData(mmSocket, 97);

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