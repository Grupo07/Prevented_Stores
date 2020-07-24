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
import com.grupo07.preventedstores.objects.SanitaryMeasure;
import com.grupo07.preventedstores.objects.SanitaryMeasuresFactory;
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
    private CheckBox[] options = new CheckBox[5];

    // register or update button
    private Button actionButton;

    // store location
    private LatLng location;

    // store category
    private String category = "grocery";

    // store to update in edit mode
    private Store storeToEdit = null;

    // store categories
    private final String[] categories = {"grocery", "food", "shop", "services"};

    // store category dropdown
    private Spinner categoryDropdown;

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
        options[2] = (CheckBox) view.findViewById(R.id.checkBox3);
        options[3] = (CheckBox) view.findViewById(R.id.categoryCheckBox1);
        options[4] = (CheckBox) view.findViewById(R.id.categoryCheckBox2);
        actionButton = (Button) view.findViewById(R.id.okButton);
        categoryDropdown = (Spinner) view.findViewById(R.id.categoryDropdown);
        Button deleteButton = (Button) view.findViewById(R.id.deleteButton);

        if(inEditMode) {
            ((TextView) view.findViewById(R.id.title)).setText("Update Store");
            deleteButton.setVisibility(View.VISIBLE);
            setEditModeForm();
        }

        updateCategoryOptions();
        setupCategoryDropdown();

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
                    String sanitaryOptions = "";
                    for(CheckBox option: options)
                        sanitaryOptions += (option.isChecked()) ? "1" : "0";

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

        // transparent dialog background to achieve round corners
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
        this.storeName.setText(storeToEdit.getName());

        this.category = storeToEdit.getCategory();

        // restore dropdown category
        for (int i = 0; i < categories.length; i++)
            if (categories[i] == category)
                categoryDropdown.setSelection(i);

        // restore options checks
        String sanitaryOptions = storeToEdit.getSanitaryOptions();
        for (int i = 0; i < options.length; i++)
            if (sanitaryOptions.charAt(i) == '1')
                options[i].setChecked(true);

        this.actionButton.setText("Update");
    }

    /**
     * Set ups the store category dropdown
     */
    private void setupCategoryDropdown() {
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.categoryDropdown.setAdapter(categoriesAdapter);
        this.categoryDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // update selected category
                category = (String) parent.getItemAtPosition(position);

                updateCategoryOptions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Updates the available options with the current store category
     */
    private void updateCategoryOptions() {
        SanitaryMeasure measures = SanitaryMeasuresFactory.makeMeasures(category);
        options[options.length-2].setText(measures.getMeasures()[0]);
        options[options.length-1].setText(measures.getMeasures()[1]);
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setStoreToEdit(Store store) {
        this.storeToEdit = store;
    }

}
