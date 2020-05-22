package com.example.anvandakontakter;

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
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.provider.ContactsContract;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int READ_CONTACTS_REQUEST_CODE = 1;

    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private List<Contact> contacts;

    // OnCreate, sets up the recycler view.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contacts = new ArrayList<>();
        contactAdapter = new ContactAdapter(contacts);
        recyclerView.setAdapter(contactAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Takes the search text to find contacts.
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!checkPermission()) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUEST_CODE);
                } else {
                    findContact(query);
                }
                return false;
            }

            // Not used.
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    // Checks the permission to read contacts.
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        }
    }

    // Handles the permission request.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACTS_REQUEST_CODE && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission needed to serach for contacts", Toast.LENGTH_SHORT).show();
        }
    }

    // Finds contacts in the phones contacts that matches the search.
    private void findContact(String search) {
        contacts.clear();
        contactAdapter.notifyDataSetChanged();

        String selection = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
                ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
        String[] searchArgs = new String[]{"%" + search + "%"};
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, selection, searchArgs, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contacts.add(new Contact(name));
                cursor.moveToNext();
            }
        }
    }

    // View holder for the recycler view.
    private class ContactHolder extends RecyclerView.ViewHolder {

        private TextView nameTxt;
        private TextView phoneNrTxt;

        // Sets up the text views.
        public ContactHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.contacts_item, parent, false));

            nameTxt = itemView.findViewById(R.id.contact_name_txt);
            phoneNrTxt = itemView.findViewById(R.id.contact_nr_txt);
        }

        // Sets the text of the contact to the holders text views.
        public void bind(Contact contact) {
            nameTxt.setText(contact.getName());
            phoneNrTxt.setText(contact.getPhoneNr());
        }

    }

    // Adapter for the recycler view.
    private class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {

        private List<Contact> contacts;

        // Constructor for the recycler view, takes a list of contacts.
        public ContactAdapter(List<Contact> contacts) {
            this.contacts = contacts;
        }

        // Creates a contract holder.
        @NonNull
        @Override
        public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            return new ContactHolder(layoutInflater, parent);
        }

        // Calls on the holder to bind a contact.
        @Override
        public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
            holder.bind(contacts.get(position));
        }

        // Returns the size of the contacts list.
        @Override
        public int getItemCount() {
            return contacts.size();
        }
    }
}
