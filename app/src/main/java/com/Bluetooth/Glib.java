package com.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.Main.DataExporter;
import com.Main.UIDatabaseInterface2019;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Oombliocarius on 1/26/16.
 */
public class Glib implements Runnable {


    BluetoothSocket bluetoothSocket, temp;
    OutputStream os;
    PrintStream haha;

    BluetoothDevice bD;
    private UUID ui;
    int failed = 2;
    Object[] toWrite;
    SQLiteDatabase db;
    Cursor c;
    private Handler handler;


    public Glib(UUID u, BluetoothDevice bd, Handler handler) {
        ui  = u;
        bD = bd;
        this.handler = handler;
    }


    public void run() {
        ArrayList<String> l = new ArrayList<String>(10);

        BluetoothAdapter bluetoothAdapter
                = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            int o = 0;
            for (BluetoothDevice device : pairedDevices) {
                String deviceBTAdd = device.getAddress();
                Log.v("Mac Address", "PAIRED DEVICE: " + device.getAddress());
                l.add(o, deviceBTAdd);
                o++;
            }
        }


        if(l.contains("18:3B:D2:E1:88:59")) {
            try {
                temp = bD.createRfcommSocketToServiceRecord(ui);
                bluetoothSocket = temp;

                Thread connectionThread  = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // Make a connection to the BluetoothSocket
                        try {
                            // This is a blocking call and will only return on a
                            // successful connection or an exception
                            sendHandlerMessage("Trying to connect...");
                            bluetoothSocket.connect();
                            sendHandlerMessage("Successfully connected");
                        } catch (IOException e) {
                            //connection to device failed so close the socket
                            Log.v("Mac Address", "Failure :(");
                            sendHandlerMessage("Failed to connect");
                            failed = 1;
                            try {
                                bluetoothSocket.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        if(failed == 1) {

                        }
                        else {
                            Log.v("Mac Address", "Success :)?");
                            failed = 0;
                        }


                    }
                });

                connectionThread.start();


                Thread communicationThread  = new Thread(new Runnable() {
                    @Override
                    public void run(){
                        int l = 3;
                        try {
                            while(l < 4) {
                                if(failed == 0) {

                                    sendHandlerMessage("Exporting data...");
                                    String fileContents = DataExporter.exportNow(false);
                                    os = bluetoothSocket.getOutputStream();
                                    os.write(fileContents.getBytes());
                                    os.flush();

                                    sendHandlerMessage("Closing connection...");
                                    bluetoothSocket.close();
                                    sendHandlerMessage("Connection closed.  Data uploaded.");
                                    break;
                                    /*
                                    //c = UIDatabaseInterface2019.getDatabase().selectFromTable("Performance", "*");

                                    //ConfigEntry con = new ConfigEntry("yo", "lol", 3);

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    ObjectOutput out = null;
                                    byte[] yourBytes = null;
                                    try {
                                        out = new ObjectOutputStream(bos);

                                        if(c != null) {
                                            Log.v("Mac Address", "I was here");
                                            out.writeObject(c);
                                        }
                                        else {
                                            Log.v("Mac Address", "nullo");
                                        }
                                        yourBytes = bos.toByteArray();
                                    }
                                    catch(Exception e) {

                                        Log.e("Mac Address", "error was " + e.toString());
                                    }
                                    Log.v("Mac Address", Arrays.toString(yourBytes));
                                    try {

                                         // File is not on the disk, test.txt indicates
                                         // only the file name to be put into the zip

                                        ZipOutputStream zos = new ZipOutputStream(baos);
                                        ZipEntry entry = new ZipEntry("test.txt");
                                        //  ObjectOutputStream obs = new ObjectOutputStream(zos);

                                        zos.putNextEntry(entry);

                                        String test = "This is a test hopefully it works";
                                        yourBytes = test.getBytes();
                                        zos.write(yourBytes);
                                        //obs.writeObject();
                                        //obs.close();
                                        zos.closeEntry();
                                        zos.close();

                                        // use more Entries to add more files
                                     // and use closeEntry() to close each file entry

                                    } catch(IOException ioe) {
                                        ioe.printStackTrace();
                                    }

                                    Log.v("Mac Address", "Highway to Heaven");
                                    os = bluetoothSocket.getOutputStream();
                                    byte[] ly = baos.toByteArray();
                                    String test = new String(ly, "UTF-8");
                                    //  Log.v("Mac Address", test);
                                    Log.v("Mac Address", Arrays.toString(ly));
                                    try {
                                        Log.v("Mac Address", "WRITING CHA BOI");
                                        ObjectOutputStream oout = new ObjectOutputStream(os);
                                        oout.writeObject(ly);
                                        oout.flush();
                                        //os.write(bytes);
                                        //os.flush();
                                    } catch (IOException e) {

                                    }

                                    //  haha = new PrintStream(haha, true);
                                    //   haha.println("LOL");
                                    l++;
                                    */

                                }

                            }

                        } catch (IOException e) {
                            //connection to device failed so close the socket

                        }
                    }
                });

                communicationThread.start();

            }
            catch (Exception e) {


            }
        } //if statement ending


    }

    /* Call this from the main activity to send data to the remote device */
    /*
    public void write(byte[] bytes) {
        try {
            os.write(bytes);
        } catch (IOException e) { }
    }
    */

    private void sendHandlerMessage(String message) {
        Message msg = Message.obtain(handler, 0, message);
        handler.sendMessage(msg);
    }

}
