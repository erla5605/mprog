package com.example.anvandakalender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.provider.CalendarContract.*;

// App that shows the events in the users calendar for the day.
public class MainActivity extends AppCompatActivity {

    private static final int READ_CALENDER_REQUEST_CODE= 1;
    private static final String DATE_FORMAT = "YYYY-MM-dd HH:mm";

    private static final String[] PROJECTION = new String[]{
            Events.TITLE,
            Events.DTSTART,
            Events.DTEND
    };
    private static final String selection = Events.DTSTART + " >= ? AND " + Events.DTSTART + " < ?";

    private Button showEventButton;
    private RecyclerView eventsRecyclerView;
    private EventAdapter eventAdapter;

    // Sets the button to show event and the recycler view to display the events.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showEventButton = findViewById(R.id.show_events_button);
        showEventButton.setOnClickListener(new View.OnClickListener() {
            /*
             Check permission to read calender then calls findEvents() to display the events.
             If permission is not granted request permission.
            */
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED)
                    findEvents();
                else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALENDAR}, READ_CALENDER_REQUEST_CODE);
                }
            }
        });

        eventsRecyclerView = findViewById(R.id.events_view);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(new ArrayList<Event>());
        eventsRecyclerView.setAdapter(eventAdapter);
    }

    /*    Checks and requests permission to the calendar.
    Gets the events of the day.
    Takes the events found, sorts them and sets them in the recycler view adapter to be displayed to the user.*/
    private void findEvents() {
        List<Event> events = queryEvents();
        Collections.sort(events, new EventComparator());
        eventAdapter.setEvents(events);
        eventAdapter.notifyDataSetChanged();

    }

    // Makes a query on the calendar for the events of the day.
    private List<Event> queryEvents() {
        List<Event> events = new ArrayList<>();
        String[] selectionArgs = new String[]{
                Long.toString(getStartOfToday()),
                Long.toString(getStartOfNextDay())
        };

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(Events.CONTENT_URI, PROJECTION, selection, selectionArgs, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String title = cursor.getString(cursor.getColumnIndex(Events.TITLE));
                Date start = new Date(cursor.getLong(cursor.getColumnIndex(Events.DTSTART)));
                Date end = new Date(cursor.getLong(cursor.getColumnIndex(Events.DTEND)));
                events.add(new Event(title, start, end));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return events;
    }

    // Gets the start of today in milliseconds.
    private long getStartOfToday() {
        return getStartOfDay(Calendar.getInstance());
    }

    // Gets the start of the day after today in milliseconds.
    private long getStartOfNextDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        return getStartOfDay(calendar);
    }

    // Handles request for permission to read calender.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == READ_CALENDER_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            findEvents();
        } else {
            Toast.makeText(this, "Need permission to read calender", Toast.LENGTH_SHORT).show();
        }
    }

    // Gets the start of a day in milliseconds.
    private long getStartOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    // Holder for the event items in the recycler view.
    private class EventHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView startTextView;
        private TextView endTextView;

        // Creates the view and set the text views.
        public EventHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_event, parent, false));

            titleTextView = itemView.findViewById(R.id.event_title);
            startTextView = itemView.findViewById(R.id.start_event);
            endTextView = itemView.findViewById(R.id.end_event);
        }

        // Binds the event title, start time and end time to the text views of the holder.
        public void bind(Event event) {
            titleTextView.setText(event.getTitle());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            String start = simpleDateFormat.format(event.getStart());
            String end = simpleDateFormat.format(event.getEnd());
            startTextView.setText(start);
            endTextView.setText(end);
        }
    }

    // Adapter for the recycler view.
    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {

        List<Event> events;

        // Sets the list of events to be displayed in the recycler view.
        public EventAdapter(List<Event> events) {
            this.events = events;
        }

        // Creates the Event holder for the event.
        @NonNull
        @Override
        public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

            return new EventHolder(layoutInflater, parent);
        }

        // Calls the holder to bind a events attributes.
        @Override
        public void onBindViewHolder(@NonNull EventHolder holder, int position) {
            Event event = events.get(position);
            holder.bind(event);
        }

        // Returns the size of the list of events.
        @Override
        public int getItemCount() {
            return events.size();
        }

        // Sets the list of events.
        public void setEvents(List<Event> events) {
            this.events = events;
        }
    }
}
