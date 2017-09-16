package com.Bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.Main.MainActivity;

import java.util.UUID;

import java.util.ArrayList;

/**
 * Created by Oombliocarius on 10/22/15.
 */

public class BlueConnect {
    int q = 0;
    UUID j = null;
    ArrayList<BluetoothSocket> connections = new ArrayList<BluetoothSocket>(7);
    String android_id;
    public BlueConnect() {

    }

    public BlueConnect(MainActivity the, UUID uuid, Context leggo) {


    }
    String thisAddress = null;
    public boolean run(UUID uuid, Context leggo) {
        boolean isMaster = false;

        Utils.getMACAddress(null);
        j = uuid;
        android_id = Settings.Secure.getString(leggo.getContentResolver(), Settings.Secure.ANDROID_ID);
        String thisAddress = Utils.getMACAddress(null);
        BluetoothAdapter bl = BluetoothAdapter.getDefaultAdapter();

        Log.v("Mac Address", "B" + thisAddress);

        if (android_id.equalsIgnoreCase("ce5798be02b59464")) {
            Log.v("Mac Address", "MASTER");
            isMaster = true;

            Listener listen = new Listener();
            listen.setUUID(j);
            Thread t = new Thread(listen);
            t.start();
            // NetworkScanner scanner = new NetworkScanner();
        }
        else {

            Log.v("Mac Address", "servant");

            // Log.v("Mac Address", Integer.toString(bl.getScanMode()));
            // Aggro agr = new Aggro(j, mA);
            // Thread t = new Thread(agr);
            // t.start();

        }

        return isMaster;
    }


}



