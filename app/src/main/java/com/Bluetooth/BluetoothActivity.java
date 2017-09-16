package com.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.Main.R;

import java.util.UUID;

public class BluetoothActivity extends ActionBarActivity {


    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT;

    private UUID uuid = UUID.fromString("e720951a-a29e-4772-b32e-7c60264d5c9b");
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            updateStatus("Recived Something");
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
                    Aggro ag = new Aggro(uuid, device);
                    Thread t = new Thread(ag);
                    t.start();
                    mBluetoothAdapter.cancelDiscovery();
                    unregisterReceiver(mReceiver);
                }
            }
        }
    };
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
        boolean isMaster = (new BlueConnect().run(uuid, this));

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

    private void updateStatus(String newStatus){
        TextView status = (TextView) findViewById(R.id.bluetoothStatus);
        status.setText(newStatus);
    }
}
