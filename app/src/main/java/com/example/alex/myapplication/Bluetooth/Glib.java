package com.example.alex.myapplication.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Set;
import android.bluetooth.BluetoothAdapter;

import com.example.alex.myapplication.ConfigParser.ConfigEntry;
import com.example.alex.myapplication.MainActivity;

import java.io.*;
import java.util.*;
import java.util.zip.ZipOutputStream;
import java.util.zip.*;

/**
 * Created by Oombliocarius on 1/26/16.
 */
public class Glib implements Runnable {


    BluetoothSocket bs, temp;
    OutputStream os;
    PrintStream haha;
    MainActivity ma;
    BluetoothDevice bD;
    private UUID ui;
    int failed = 2;

    public Glib(UUID u, MainActivity mA, BluetoothDevice bd) {


        ma = mA;
        ui  = u;
        bD = bd;


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
                bs = temp;
                Log.v("Mac Address", "Shouldn't have connected");

                Thread connectionThread  = new Thread(new Runnable() {

                    @Override
                    public void run() {


                        // Make a connection to the BluetoothSocket
                        try {
                            // This is a blocking call and will only return on a
                            // successful connection or an exception
                            bs.connect();
                        } catch (IOException e) {
                            //connection to device failed so close the socket
                            Log.v("Mac Address", "Failure :(");
                            failed = 1;
                            try {
                                bs.close();
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



                Log.v("Mac Address", "Should have connected");




                Thread communicationThread  = new Thread(new Runnable() {



                    public void write(byte[] bytes) {
                        try {
                            Log.v("Mac Address", "WRITING CHA BOI");
                            ObjectOutputStream oout = new ObjectOutputStream(os);
                            oout.writeObject(bytes);
                            oout.flush();
                            //os.write(bytes);
                            //os.flush();
                        } catch (IOException e) { }
                    }

                    @Override
                    public void run() {


                        int l = 3;
                        try {

                            while(l < 4) {

                                if(failed == 0) {




                                    String s = "Tired, Exhausted";
                                    ConfigEntry con = new ConfigEntry("yo", "lol", 3);

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    ObjectOutput out = null;
                                    byte[] yourBytes = null;
                                    try {
                                        out = new ObjectOutputStream(bos);
                                        out.writeObject(con);
                                        yourBytes =bos.toByteArray();
                                    }
                                    catch(Exception e) {

                                    }
                                    Log.v("Mac Address", Arrays.toString(yourBytes));
                                    try {

  /* File is not on the disk, test.txt indicates
     only the file name to be put into the zip */

                                        ZipOutputStream zos = new ZipOutputStream(baos);
                                        ZipEntry entry = new ZipEntry("test.txt");
                                        //  ObjectOutputStream obs = new ObjectOutputStream(zos);

                                        zos.putNextEntry(entry);

                                        zos.write(yourBytes);
                                        //obs.writeObject();
                                        //obs.close();
                                        zos.closeEntry();
                                        zos.close();

  /* use more Entries to add more files
     and use closeEntry() to close each file entry */

                                    } catch(IOException ioe) {
                                        ioe.printStackTrace();
                                    }




                                    Log.v("Mac Address", "Highway to Heaven");
                                    os = bs.getOutputStream();
                                    byte[] ly = baos.toByteArray();
                                    String test = new String(ly, "UTF-8");
                                    //  Log.v("Mac Address", test);
                                    Log.v("Mac Address", Arrays.toString(ly));
                                    write(ly);
                                    //  haha = new PrintStream(haha, true);
                                    //   haha.println("LOL");
                                    l++;

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

}