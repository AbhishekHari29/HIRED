package com.droidevils.hired.Helper;

import android.app.Activity;
import android.util.Log;

import com.droidevils.hired.Common.LoadingDialog;

public class ProcessManager {

    private Activity activity;
    private int processCount;
    private LoadingDialog loadingDialog;

    public ProcessManager(Activity activity) {
        this.activity = activity;
        this.processCount = 0;
        this.loadingDialog = new LoadingDialog(this.activity);
    }

    public void incrementProcessCount() {
        processCount++;
        Log.i("PROCESS MANAGER", "Incremented|Process Count = "+processCount);
        if (processCount == 1)
            startLoading();
    }

    public void decrementProcessCount() {
        processCount--;
        Log.i("PROCESS MANAGER", "Decremented|Process Count = "+processCount);
        if (processCount < 1)
            stopLoading();

    }

    public void startLoading() {
        Log.i("PROCESS MANAGER", "Start Loading|Process Count = "+processCount);
        loadingDialog.startLoadingDialog();
    }

    public void stopLoading() {
        Log.i("PROCESS MANAGER", "Stop Loading|Process Count = "+processCount);
        loadingDialog.stopLoadingDialog();
    }

}
