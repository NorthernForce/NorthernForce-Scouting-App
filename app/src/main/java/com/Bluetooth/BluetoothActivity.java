package com.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.Main.R;

import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {


    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT;

    private UUID uuid = UUID.fromString("e720951a-a29e-4772-b32e-7c60264d5c9b");
    private BroadcastReceiver mReceiver;

    private final Handler btMessageHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            updateStatus(msg.obj.toString());
        }
    };

    public BluetoothActivity() {
        this.mReceiver = new CustomBroadcastReceiver(btMessageHandler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bluetooth_activity);

        this.setUpButtons();
    }

    private void setUpButtons(){
        Context context = this.getBaseContext();

        Button connect = (Button) findViewById(R.id.bluetoothConnect);
        connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    startBluetoothDiscover();
                }
            });
    }

    public void startBluetoothDiscover(){
        boolean isMaster = (new BlueConnect(btMessageHandler).run(uuid, this));

        if(!isMaster) {
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter == null){
                updateStatus("Device Does Not Support Bluetooth");
                return;
            }
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                updateStatus("REQUEST_ENABLE_BT: " + (RESULT_OK == REQUEST_ENABLE_BT));
            }

            mBluetoothAdapter.startDiscovery();
            updateStatus("Started discovery");
        }
        else {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(discoverableIntent);
            updateStatus("Is master, started activity discoverableIntent");
        }

    }

    public void updateStatus(String newStatus){
        TextView status = (TextView) findViewById(R.id.bluetoothStatus);
        status.setText(newStatus);
    }

    private class CustomBroadcastReceiver extends BroadcastReceiver {

        private Handler handler;

        public CustomBroadcastReceiver(Handler handler) {
            this.handler = handler;
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                updateStatus(device.getName());
                //     Log.v("Mac Address", device.getName());
                if(device.getAddress().equalsIgnoreCase("18:3b:d2:e1:88:59")) {
                    Log.v("Mac Address", device.getName() + "\n" + device.getAddress());
                    Aggro ag = new Aggro(uuid, device, handler);
                    Thread t = new Thread(ag);
                    t.start();
                    mBluetoothAdapter.cancelDiscovery();
                    unregisterReceiver(mReceiver);
                }
            }
        }
    }
}
