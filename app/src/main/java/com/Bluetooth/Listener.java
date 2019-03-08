package com.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.Main.UIDatabaseInterface2019;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.UUID;

/**
 * Created by Oombliocarius on 11/4/15.
 */
public class Listener implements Runnable {
    UUID uuid = null;
    BluetoothServerSocket bluetoothServerSocket;
    BluetoothSocket bluetoothSocket;
    InputStream inputStream;
    boolean  stopWorker;
    byte[] readBuffer;
    Thread workerThread;
    private Handler handler;

    public Listener(Handler handler) {
        this.handler = handler;
    }

    public void run() {

        BluetoothAdapter bluetoothAdapter  = BluetoothAdapter.getDefaultAdapter();
        try {
            bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Server", uuid);
            Log.v("Mac Address", "Listening");
            sendHandlerMessage("Waiting for connection...");
            bluetoothSocket = bluetoothServerSocket.accept();
            Log.v("Mac Address", "Connection accepted");
            sendHandlerMessage("Connected");
            // bluetoothSocket.connect();
            inputStream =   bluetoothSocket.getInputStream();
            Log.v("Mac Address", "Sauron's Land");
            beginListenForData();
            // Updater up = new Updater(inputStream);
            // Thread t = new Thread(up);
            // t.start();
            workerThread.join(); // wait for this thread to finish
            sendHandlerMessage("Closing connection");
            bluetoothSocket.close();
            sendHandlerMessage("Connection closed");

        }
        catch(Exception e) {

        }

    }

    public void setUUID(UUID id) {
        uuid = id;
    }










    void beginListenForData()
    {
        Log.v("Mac Address", "Devil's Door");
        stopWorker = false;

        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                readBuffer = new byte[1024];
                int numBytes; // bytes returned from read()
                // Keep listening to the InputStream until an exception occurs.
                StringBuilder buffer = new StringBuilder();
                while (true) {
                    sendHandlerMessage("Reading data...");
                    try {
                        // Read from the InputStream.
                        numBytes = inputStream.read(readBuffer);
                        byte[] readBytes = new byte[numBytes];
                        System.arraycopy(readBuffer, 0, readBytes, 0, numBytes );
                        buffer.append(new String(readBytes));
                    } catch (IOException e) {
                        Log.d("Error", "Input stream was disconnected", e);
                        break;
                    }
                }

                String dataReceived = buffer.toString();
                System.out.println("Received: " + dataReceived);

                try {
                    StringReader reader = new StringReader(dataReceived);
                    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
                    sendHandlerMessage("Inserting into database...");
                    UIDatabaseInterface2019.submitDataEntries(csvParser.getRecords());
                    sendHandlerMessage("Data inserted");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                /*
                Log.v("Mac Address", "STARTED");
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = inputStream.available();
                     //   Log.v("Mac Address", "BYTES: " + bytesAvailable);
                        if(bytesAvailable > 0)
                        {
                            Log.v("Mac Address", "WE'RE UP IN THIS");
                            byte[] packetBytes = new byte[inputStream.available()];
                          //
                           // String data = new String(packetBytes, "UTF-8");
                            Log.v("Mac Address", "AVAILABLE: " + inputStream.available());
                            Log.v("Mac Address", Arrays.toString(packetBytes));
                         //   inputStream.read(packetBytes);
                            ObjectInputStream obin = new ObjectInputStream(inputStream);
                            Object obo = null;
                            try {
                                obo = obin.readObject();
                            }
                            catch(Exception e) {

                            }
                            byte[] hope = (byte[]) obo;
                            Log.v("Mac Address", Arrays.toString(hope));


                         //   Log.v("Mac Address", String.valueOf(baos.size()));
                            ByteArrayInputStream bi = new ByteArrayInputStream(hope);
                            ZipInputStream zis = new ZipInputStream(bi);
                            Log.v("Mac Address", "HOPE: " + zis.available());
                      //      zis.getNextEntry();
                       //     byte[] results = null;
                       //     zis.read(results, 0, 0);
                       //     String please = new String(results, "UTF-8");
                       //     Log.v("Mac Address", please);

                            ZipEntry entry;
                            try {
                                while ((entry = zis.getNextEntry()) != null) {
                                    String s = String.format("Entry: %s len %d added %TD",
                                            entry.getName(), entry.getSize(),
                                            new Date(entry.getTime()));
                                    Log.v("Mac Address", "THIS FAR");
                                   // ZipFile zipFile = new ZipFile("text.zip");
                                    Log.v("Mac Address", "THIS FAR 1");
                                     ObjectInputStream ino = new ObjectInputStream(zis);
                                    Object ob = ino.readObject();

                                    Log.v("Mac Address", ob.toString());
                                    if(ob instanceof ConfigEntry) {
                                        ConfigEntry con = (ConfigEntry) ob;
                                        Log.v("Mac Address", con.getText());
                                    }
                                    if(ob instanceof SQLiteDatabase) {
                                        SQLiteDatabase con = (SQLiteDatabase) ob;
                                        Log.v("Mac Address", con.toString());
                                    }
                                    if(ob instanceof Cursor) {
                                        Cursor con = (Cursor) ob;
                                        Log.v("Mac Address", con.toString());
                                    }
                                    if(ob instanceof String) {
                                        String str = (String) ob;
                                        Log.v("Mac Address", str);
                                    }



                                }
                            }
                            catch(Exception e) {
                            Log.v("Mac Address", "OH BOI");
                                StringWriter errors = new StringWriter();
                                e.printStackTrace(new PrintWriter(errors));
                                Log.v("Mac Address", errors.toString());
                            }
                            /*
                            ObjectInputStream obin = new ObjectInputStream(inputStream);
                            Object obo = null;
                            try {
                                 obo = obin.readObject();
                            }
                            catch(Exception e) {

                            }
                            byte[] hope = obo

                            ByteArrayInputStream bis = new ByteArrayInputStream(packetBytes);
                            ObjectInput inputStream = null;
                            try {
                                inputStream = new ObjectInputStream(bis);
                                Object o = inputStream.readObject();
                                ZipEntry e = (ZipEntry) o;
                                Log.v("Mac Address", o.toString());
                                bis.close();
                            }
                            catch (NullPointerException ex) {
                                    // ignore close exception
                                Log.v("Mac Address", "SIGH");
                                //ex.printStackTrace();
                                }
                            catch (ClassNotFoundException eo) {
                                // ignore close exception
                                Log.v("Mac Address", "Weird");
                                //ex.printStackTrace();
                            }
                            Log.v("Mac Address", "SIGH2");


                           //         System.arraycopy(readBuffer, 0, packetBytes, 0, packetBytes.length);
                        //    ByteArrayOutputStream obj = (ByteArrayOutputStream) deserialize(packetBytes);


                     //               Log.v("Mac Address", obj.toString());






                        }
                    }
                    catch (IOException ex)
                    {
                        StringWriter errors = new StringWriter();
                        ex.printStackTrace(new PrintWriter(errors));
                        Log.v("Mac Address", errors.toString());
                        Log.v("Mac Address", "Uprising Failed");
                        stopWorker = true;
                    }
                } */
            }
        });

        workerThread.start();
    }

    /*
    public static Object deserialize(byte[] data) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();
        }
        catch (Exception e) {

        }
        return null;
    }
    */

    private void sendHandlerMessage(String message) {
        Message msg = Message.obtain(handler, 0, message);
        handler.sendMessage(msg);
    }

}