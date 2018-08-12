package app.clairvoyant.reddrop;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DonorRequestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText name, number, location;
    private Spinner units, group;
    private Button donorSearchButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private static final String TAG = "Donor Request Activity";

    private String noOfUnits, bloodGroupSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_request);

        name = findViewById(R.id.contact_name);
        number = findViewById(R.id.contact_number);
        location = findViewById(R.id.contact_location);

        units = findViewById(R.id.spinner_unitsRequired);
        group = findViewById(R.id.spinner_bloodGroup);

        donorSearchButton = findViewById(R.id.btn_search_donor);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("requests");

        // units Spinner Drop down elements
        List<String> unitsRequired = new ArrayList<String>();
        unitsRequired.add("Units Required");
        unitsRequired.add("1");
        unitsRequired.add("2");
        unitsRequired.add("3");
        unitsRequired.add("4");
        unitsRequired.add("5");
        unitsRequired.add("6");

        // groups Spinner Drop down elements
        final List<String> bloodGroup = new ArrayList<String>();
        bloodGroup.add("Blood Group");
        bloodGroup.add("A+");
        bloodGroup.add("B+");
        bloodGroup.add("O+");
        bloodGroup.add("AB+");
        bloodGroup.add("A-");
        bloodGroup.add("B-");
        bloodGroup.add("O-");
        bloodGroup.add("AB-");

        // Creating adapter for units spinner
        ArrayAdapter<String> unitsDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitsRequired);

        // Creating adapter for groups spinner
        ArrayAdapter<String> groupsDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bloodGroup);

        unitsDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        groupsDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner

        units.setAdapter(unitsDataAdapter);
        units.setOnItemSelectedListener(this);

        group.setAdapter(groupsDataAdapter);
        group.setOnItemSelectedListener(this);





        // On click of Search Button
        donorSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Address> addressList = null;
                Geocoder geocoder = new Geocoder(DonorRequestActivity.this);
                try {
                    addressList = geocoder.getFromLocationName(location.getText().toString().trim(),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = null;
                if (addressList != null) {
                    address = addressList.get(0);
                }
                LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                Log.d(TAG,"latitude and longitude is: "+latLng);

                if (!noOfUnits.equals("Units Required") && !bloodGroup.equals("Blood Group")) {
                    final RequestForm requestForm = new RequestForm(name.getText().toString(), number.getText().toString(), location.getText().toString(),bloodGroupSelected,noOfUnits);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int numberOfChildNode = (int) dataSnapshot.getChildrenCount();
                            int currentChildNumber = numberOfChildNode+1;
                            Log.d(TAG,"child number: "+currentChildNumber);
                            databaseReference.child(String.valueOf(currentChildNumber)).setValue(requestForm);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        adapterView.getItemAtPosition(i);
        switch (adapterView.getId()){
            case R.id.spinner_bloodGroup:
                bloodGroupSelected = group.getItemAtPosition(i).toString();
                Log.d(TAG,"blood group needed: "+bloodGroupSelected);
                break;
            case R.id.spinner_unitsRequired:
                noOfUnits = units.getItemAtPosition(i).toString();
                Log.d(TAG,"number of units needed: "+noOfUnits);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
