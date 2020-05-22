package com.mprog.ringaochtaemotsamtal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// App that shows call history and allows the user to click on a call and call the number.
public class CallLogActivity extends AppCompatActivity {

    private static final int CALL_PERMISSIONS = 200;
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.CALL_PHONE
    };

    private static final String SORT_ORDER = CallLog.Calls.DATE + " DESC";

    private RecyclerView recyclerView;
    private CallAdapter callAdapter;

    // Sets up the recycler view and asks for permission to Read call log.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }
    }

    // Gets the call log on resume.
    @Override
    protected void onResume() {
        super.onResume();
        getCallLog();
    }

    // Gets ti call log form the CallLog.Calls in the device.
    private void getCallLog() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            List<Call> calls = new ArrayList<>();
            Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, SORT_ORDER);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String number = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                    long millis = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));

                    calls.add(new Call(name, number, new Date(millis)));
                    c.moveToNext();
                }
            }
            c.close();
            setCallsViewList(calls);
        }
    }

    // Sets the list of the recycler view, notifies the adapter if changes has been made.
    private void setCallsViewList(List<Call> calls) {
        if (callAdapter == null) {
            callAdapter = new CallAdapter(calls);
            recyclerView.setAdapter(callAdapter);
        } else {
            callAdapter.setCalls(calls);
            callAdapter.notifyDataSetChanged();
        }
    }

    // Request permission to read call log and call phone.
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMISSIONS, CALL_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, CALL_PERMISSIONS);
        }
    }

    // Handles the permission request.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_PERMISSIONS && checkGrantResults(grantResults)) {
            getCallLog();
        } else {
            Toast.makeText(this, "PERMISSION NEEDED!", Toast.LENGTH_SHORT).show();
        }
    }

    // Checks the result of the permission request.
    private boolean checkGrantResults(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    // View holder for recycler view.
    private class CallHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Call call;
        private TextView displayTextView;
        private TextView dateTextView;

        // Sets up the holders text views.
        public CallHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.call_list_item, parent, false));
            itemView.setOnClickListener(this);

            displayTextView = itemView.findViewById(R.id.display_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
        }

        // Binds a call to the holder.
        public void bind(Call call) {
            this.call = call;
            displayTextView.setText(call.getName() != null ? call.getName() : call.getNumber());

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
            dateTextView.setText(sdf.format(call.getDate()));
        }

        // Creates the intent to call the number and starts the activity.
        @Override
        public void onClick(View v) {
            String telString = "tel:" + call.getNumber();

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(telString));

            if (ContextCompat.checkSelfPermission(CallLogActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            }
        }
    }

    // Adapter for the recycler view.
    private class CallAdapter extends RecyclerView.Adapter<CallHolder> {
        private List<Call> calls;

        // Constructor that takes a list of calls.
        public CallAdapter(List<Call> calls) {
            this.calls = calls;
        }

        //Creates a CallHolder.
        @NonNull
        @Override
        public CallHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(CallLogActivity.this);

            return new CallHolder(inflater, parent);
        }

        // Calls on the holder to bind the call.
        @Override
        public void onBindViewHolder(@NonNull CallHolder holder, int position) {
            holder.bind(calls.get(position));
        }

        // Returns the size of the calls list.
        @Override
        public int getItemCount() {
            return calls.size();
        }

        // Sets the list of calls.
        public void setCalls(List<Call> calls) {
            this.calls = calls;
        }
    }
}
