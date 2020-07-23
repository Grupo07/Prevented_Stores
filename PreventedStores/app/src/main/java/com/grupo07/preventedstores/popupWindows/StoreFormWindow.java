package com.grupo07.preventedstores.popupWindows;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.grupo07.preventedstores.R;
import com.grupo07.preventedstores.objects.Store;
import com.grupo07.preventedstores.activities.MapsActivity;
import com.grupo07.preventedstores.database.StoreDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * Store form window to create or edit a store
 */
public class StoreFormWindow extends AppCompatDialogFragment {

    // Parent activity used as application context for the widgets
    private MapsActivity context;

    // store name input
    private EditText storeName;

    // sanitary options inputs
    private CheckBox[] options = new CheckBox[2];

    // register or update button
    private Button actionButton;

    // store location
    private LatLng location;

    // store category
    private String category = "Groceries";

    // store to update in edit mode
    private Store storeToEdit = null;

    /**
     * The class constructor requires an application context to be passed
     * @param context application context
     */
    public StoreFormWindow(MapsActivity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final boolean inEditMode = (storeToEdit != null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.store_form, null);
        builder.setView(view);

        storeName = (EditText) view.findViewById(R.id.storeNameEditText);
        options[0] = (CheckBox) view.findViewById(R.id.checkBox1);
        options[1] = (CheckBox) view.findViewById(R.id.checkBox2);
        actionButton = (Button) view.findViewById(R.id.okButton);

        Button deleteButton = (Button) view.findViewById(R.id.deleteButton);


        if(inEditMode) {
            ((TextView) view.findViewById(R.id.title)).setText("Update Store");
            deleteButton.setVisibility(View.VISIBLE);
            setEditModeForm();
        }

        Spinner categoryDropdown = (Spinner) view.findViewById(R.id.categoryDropdown);
        setupCategoryDropdown(categoryDropdown);

        final Dialog dialog = builder.create();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreDatabase.deleteStore(storeToEdit, context);
                dialog.dismiss();
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataIsValid()) {
                    String name = storeName.getText().toString();
                    String option1Value = (options[0].isChecked()) ? "1" : "0";
                    String option2Value = (options[1].isChecked()) ? "1" : "0";
                    String sanitaryOptions = option1Value + option2Value;

                    String author;
                    double latitude, longitude;

                    if (inEditMode) {
                        latitude = storeToEdit.getLatitude();
                        longitude = storeToEdit.getLongitude();
                        author = storeToEdit.getAuthor();
                    } else {
                        latitude = location.latitude;
                        longitude = location.longitude;
                        author = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace("@user.com", "");
                    }

                    Store newStore = new Store(name, category, latitude, longitude, sanitaryOptions, author);

                    StoreDatabase.saveStore(newStore);

                    context.updateStores();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Store name can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return dialog;
    }

    /**
     * Indicates that the current store form data is valid
     * @return form data validity
     */
    private boolean dataIsValid() {
        String storeName = this.storeName.getText().toString();
        return !storeName.isEmpty();
    }

    /**
     * Changes form look and functionality to edit store mode
     */
    private void setEditModeForm() {
        storeName.setText(storeToEdit.name);

        String sanitaryOptions = storeToEdit.getSanitaryOptions();
        for (int i = 0; i < options.length; i++) {
            if (sanitaryOptions.charAt(i) == '1') {
                options[i].setChecked(true);
            }
        }

        actionButton.setText("Update");
    }

    /**
     * Set ups the store category dropdown
     * @param categoryDropdown spinner to display the dropdown
     */
    private void setupCategoryDropdown(Spinner categoryDropdown) {
        final String[] categories = {"Groceries", "Food", "Bank", "Hardware Store", "Gas Station", "Pharmacy", "Hospital", "Hair Salon"};

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryDropdown.setAdapter(categoriesAdapter);

        categoryDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setStoreToEdit(Store store) {
        this.storeToEdit = store;
    }

}
