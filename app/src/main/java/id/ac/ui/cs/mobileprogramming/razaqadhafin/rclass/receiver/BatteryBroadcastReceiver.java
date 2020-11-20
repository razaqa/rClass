package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryBroadcastReceiver extends BroadcastReceiver {


    private String lowBatteryTextTitle;
    private String lowBatteryWarningText;

    public BatteryBroadcastReceiver() {}

    public BatteryBroadcastReceiver(String lowBatteryTextTitle, String lowBatteryWarningText) {
        this.lowBatteryTextTitle = lowBatteryTextTitle;
        this.lowBatteryWarningText = lowBatteryWarningText;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(lowBatteryTextTitle)
                .setMessage(lowBatteryWarningText)
                .setPositiveButton("OK", null)
                .show();
    }
}
