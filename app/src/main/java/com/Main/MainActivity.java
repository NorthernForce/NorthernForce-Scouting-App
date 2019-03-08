package com.Main;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.Bluetooth.Aggro;
import com.Bluetooth.BluetoothActivity;
import com.DataEntry.EnterDataActivity2019;
import com.DataView.ViewDataActivity;
import com.DataView.ViewDataActivity2019;
import com.Settings.SettingsActivity;
import com.easyphotopicker.EasyImage;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * The main activity where the app starts, has buttons to enter data, view data, or sync between devices
 */
public class MainActivity extends AppCompatActivity {


    public static UIDatabaseInterface2019 uiDatabaseInterface;
    private Context baseContext;
    private BluetoothAdapter bl;
    private ShareActionProvider shareActionProvider;
    private static SimpleDateFormat exportSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH_mm_ss");

    private UUID uuid = UUID.fromString("e720951a-a29e-4772-b32e-7c60264d5c9b");

    /*
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismiss progress dialog

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


                //     Log.v("Mac Address", device.getName());
                if(device.getAddress().equalsIgnoreCase("18:3b:d2:e1:88:59")) {
                    Log.v("Mac Address", device.getName() + "\n" + device.getAddress());
                    Aggro ag = new Aggro(uuid, device, handler);
                    Thread t = new Thread(ag);
                    t.start();
                    bl.cancelDiscovery();
                    unregisterReceiver(mReceiver);
                }
            }
        }
    };
    */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiDatabaseInterface = new UIDatabaseInterface2019(this.getBaseContext());

        setContentView(R.layout.activity_main);

        this.baseContext = this.getBaseContext();


        EasyImage.configuration(this)
                .setImagesFolderName("ScoutingPics")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(true);


        Button enterData = findViewById(R.id.titleScreenEnterData);
        enterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = baseContext;
                Intent i = new Intent(context, EnterDataActivity2019.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(i);
            }
        });
        //enterData.setTextColor(Color.GREEN);

        Button viewData = findViewById(R.id.titleScreenViewData);
        viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = baseContext;
                Intent i = new Intent(context, ViewDataActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(i);
            }
        });

        Button viewDataTable = findViewById(R.id.titleScreenViewDataTable);
        viewDataTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = baseContext;
                Intent i = new Intent(context, ViewDataActivity2019.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(i);
            }
        });


        Button bluetooth = findViewById(R.id.titleScreenBluetooth);
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = baseContext;
                Intent i = new Intent(context, BluetoothActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(i);
            }
        });

        Button export = findViewById(R.id.titleScreenExportData);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new DataExporter().execute();

                String fileName = "scout-" + exportSdf.format(new Date()) + ".csv";
                String fileContents = DataExporter.exportNow(true);

                try {
                    File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    /*
                    for (String s : downloadDir.list()) {
                        System.out.println("download file: " + s);
                    }
                    */
                    String reportsDir = getResources().getString(R.string.scouting_reports_dir);
                    File scoutingReportsDir = new File(downloadDir, reportsDir);
                    boolean dirExists = scoutingReportsDir.exists();
                    //System.out.println("reports directory exists: " + dirExists);
                    if (! dirExists) {
                        boolean success = scoutingReportsDir.mkdir();
                        //System.out.println("Successfully created reports dir: " + success);
                    }
                    File exportFile = new File(scoutingReportsDir, fileName);
                    //System.out.println("file location: " + exportFile.getAbsolutePath());

                    FileOutputStream fos = new FileOutputStream(exportFile);
                    fos.write(fileContents.getBytes());
                    fos.close();

                    // Doesn't always show up on laptop, so force a rescan
                    Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri fileContentUri = Uri.fromFile(exportFile);
                    mediaScannerIntent.setData(fileContentUri);
                    sendBroadcast(mediaScannerIntent);

                    Snackbar mySnackbar = Snackbar.make(view, "Exported csv to Downloads/"
                            + reportsDir + "/" + fileName, Snackbar.LENGTH_LONG);
                    mySnackbar.show();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                // Code below will prompt to upload to google drive
                // or some other sharing mechanism
                /*
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, fileContents);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, fileName);
                sendIntent.setType("text/csv");
                setShareIntent(sendIntent);
                startActivity(Intent.createChooser(sendIntent, "Export results"));
                */
            }
        });

        EasyImage.configuration(this)
                .setImagesFolderName("Pictures");
        Button camera = findViewById(R.id.titleScreenCamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openCamera(MainActivity.this, 0);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        /*
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, DataExporter.exportNow());
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, exportSdf.format(new Date()) + ".csv");
                sendIntent.setType("text/csv");
                setShareIntent(sendIntent);
                startActivity(Intent.createChooser(sendIntent, "Export results"));
                return false;
            }
        });

*/

        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Context context = this.getBaseContext();
            Intent i = new Intent(context, SettingsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }

        if (id == R.id.action_enterData){
            Context context = this.getBaseContext();
            Intent i = new Intent(context, EnterDataActivity2019.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }

        if (id == R.id.action_viewData){
            Context context = this.getBaseContext();
            Intent i = new Intent(context, ViewDataActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }

        if (id == R.id.action_viewDataTable){
            Context context = this.getBaseContext();
            Intent i = new Intent(context, ViewDataActivity2019.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
