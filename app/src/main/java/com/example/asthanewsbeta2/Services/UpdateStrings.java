package com.example.asthanewsbeta2.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.asthanewsbeta2.Modules.MngData;

public class UpdateStrings extends Service {
    public UpdateStrings() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"Service Created!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service Started!", Toast.LENGTH_SHORT).show();
        MngData.setData(this, "test", "name", "jay");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Service Destroy!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
