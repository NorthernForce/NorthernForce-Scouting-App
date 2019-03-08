package com.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Oombliocarius on 12/17/15.
 */
public class Aggro implements Runnable {
    private UUID ui;
    BluetoothSocket bs;
    OutputStream os;
    PrintStream haha;
    BluetoothDevice bD;
    private Handler handler;


    public Aggro(UUID u, BluetoothDevice bd, Handler handler) {
        ui  = u;
        bD = bd;
        this.handler = handler;
    }

    public void run() {
        try {
            Log.v("Mac Address", "Aggro Started");
            //  bD.createBond();
            Log.v("Mac Address", pairDevice(bD) + "  PAIRING STATUS");
            Glib glib = new Glib(ui, bD, handler);
            Thread to = new Thread(glib);
            to.run();

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void Aggr() {

    }

    public boolean pairDevice(BluetoothDevice device) {
        try {
            Method createBond = BluetoothDevice.class.getMethod("createBond");
            Log.v("Mac Address", String.valueOf((boolean) createBond.invoke(device)) + "OOOOOOOOOO");
            //return (boolean) createBond.invoke(device);

        }
        catch (Exception e) {
            Log.v("Mac Address", "Catch Called");
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
