package com.Bluetooth;

import android.util.Log;

import java.net.*;
import java.util.*;

public class Utils {


    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            Log.v("macAddressTag", interfaces.toString());
            Log.e("macAddressTag", interfaces.get(6).toString());
            StringBuilder sb = new StringBuilder();

            byte[] mac = interfaces.get(6).getHardwareAddress();
            for (int idx = 0; idx < mac.length; idx++) {
                sb.append(String.format("%02X:", mac[idx]));
            }

            return sb.toString();



        } catch (Exception ex) {

        }
        return null;

    }
}