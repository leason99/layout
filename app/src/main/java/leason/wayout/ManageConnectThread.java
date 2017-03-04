package leason.wayout;

import android.bluetooth.BluetoothSocket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by leason on 2016/12/8.
 */

public class ManageConnectThread  {

   static Thread thread;

    public ManageConnectThread() {


    }

    public static void sendData(final BluetoothSocket socket, final int data) throws IOException {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    output.write(data);
                    OutputStream outputStream = socket.getOutputStream();

                    outputStream.write(output.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    static public byte[][] receiveData(final BluetoothSocket socket) throws IOException {

        final byte[][] result = new byte[1][4];

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[4];

                try {
                    InputStream inputStream = socket.getInputStream();

                    inputStream.read(buffer);
                    result[0] = buffer;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        return result;
    }
}