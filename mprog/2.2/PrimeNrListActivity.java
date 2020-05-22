package com.example.sqlitedb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PrimeNrListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PrimeNrAdapter primeNrAdapter;
    private PrimeNrDatabase primeNrDatabase;

    // OnCreate, sets the recycler view to display all the prime numbers.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime_nr_list);


        primeNrDatabase = PrimeNrDatabase.getInstance(getApplicationContext());
        recyclerView = findViewById(R.id.primenr_recycler_view);

        List<PrimeNr> primeNrs = primeNrDatabase.getAllPrimeNrs();
        primeNrAdapter = new PrimeNrAdapter(primeNrs);
        recyclerView.setAdapter(primeNrAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(PrimeNrListActivity.this));
    }

    // View holder for the recycler view.
    private class PrimeNrHolder extends RecyclerView.ViewHolder {

        private TextView txtPrimeNr;
        private TextView txtFoundOn;

        // Sets up the text views for the holder.
        public PrimeNrHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_prime_nr, parent, false));

            txtPrimeNr = itemView.findViewById(R.id.list_item_prime_nr);
            txtFoundOn = itemView.findViewById(R.id.list_item_date_found);
        }

        // Binds a prime number to the holder.
        public void bind(PrimeNr primeNr) {
            txtPrimeNr.setText(Long.toString(primeNr.getPrimeNr()));
            txtFoundOn.setText(primeNr.getFoundOn());
        }
    }

    // Adapter for the recycler view.
    private class PrimeNrAdapter extends RecyclerView.Adapter<PrimeNrHolder> {

        private List<PrimeNr> primeNrs;

        // Constructor that takes a list of prime numbers.
        public PrimeNrAdapter(List<PrimeNr> primeNrs) {
            this.primeNrs = primeNrs;
        }

        // Create a prime number holder.
        @Override
        public PrimeNrHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(PrimeNrListActivity.this);
            return new PrimeNrHolder(layoutInflater, parent);
        }

        // Calls on the holde to bind the primer number.
        @Override
        public void onBindViewHolder(PrimeNrHolder holder, int position) {
            PrimeNr primeNr = primeNrs.get(position);
            holder.bind(primeNr);
        }

        // Get the size of the prime number list.
        @Override
        public int getItemCount() {
            return primeNrs.size();
        }

    }
}
