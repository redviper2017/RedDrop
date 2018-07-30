package app.clairvoyant.reddrop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class DonorRequestActivity extends AppCompatActivity {

    private EditText name, number, location;
    private Spinner units, group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_request);

        name = findViewById(R.id.contact_name);
        number = findViewById(R.id.contact_number);
        location = findViewById(R.id.contact_location);

        units = findViewById(R.id.spinner_unitsRequired);
        group = findViewById(R.id.spinner_bloodGroup);

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
    }
}
