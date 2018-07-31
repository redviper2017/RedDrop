package app.clairvoyant.reddrop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import app.clairvoyant.reddrop.model.Location;
import app.clairvoyant.reddrop.network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonorRequestActivity extends AppCompatActivity {

    private EditText name, number, location;
    private Spinner units, group;
    private Button donorSearchButton;

    private static final String TAG = "Donor Request Activity";

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
        List<String> bloodGroup = new ArrayList<String>();
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

        group.setAdapter(groupsDataAdapter);

        // On click of Search Button
        donorSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = location.getText().toString().trim();
                Log.d(TAG,"requested donation's address: "+address);

                // Create handle for the RetrofitInstance interface
                GetLocationDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetLocationDataService.class);


                // Call the method with parameter in the interface to get the notice data
                Call<Location> call = service.getLocationData(address);

                // Log of the URL called
                Log.d(TAG,"URL called: " + call.request().url());

                call.enqueue(new Callback<Location>() {
                    @Override
                    public void onResponse(Call<Location> call, Response<Location> response) {
                        Log.d(TAG,"recieved response from url is: "+response);
                        String lat = response.body().getLat().toString();
                        Log.d(TAG,"latitude is: "+lat);
                    }

                    @Override
                    public void onFailure(Call<Location> call, Throwable t) {

                    }
                });
            }
        });
    }
}
