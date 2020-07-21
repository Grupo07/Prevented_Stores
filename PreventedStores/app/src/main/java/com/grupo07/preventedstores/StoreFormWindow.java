package com.grupo07.preventedstores;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class StoreFormWindow extends AppCompatDialogFragment {

    private EditText storeName;
    private CheckBox[] options = new CheckBox[2];
    private Button actionButton;
    private Button deleteButton;
    private MapsActivity context;
    private LatLng location;

    private Store storeToEdit = null;

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
        deleteButton = (Button) view.findViewById(R.id.deleteButton);

        if(inEditMode) {
            ((TextView) view.findViewById(R.id.title)).setText("Update Store");
            deleteButton.setVisibility(View.VISIBLE);
            setEditModeForm();
        }

        final Dialog dialog = builder.create();


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataIsValid()) {
                    String name = storeName.getText().toString();
                    String option1Value = (options[0].isChecked()) ? "1" : "0";
                    String option2Value = (options[1].isChecked()) ? "1" : "0";
                    String sanitaryOptions = option1Value + option2Value;

                    double latitude, longitude;
                    if (inEditMode) {
                        latitude = storeToEdit.getLatitude();
                        longitude = storeToEdit.getLongitude();
                    } else {
                        latitude = location.latitude;
                        longitude = location.longitude;
                    }
                    Store newStore = new Store(name, "market", latitude, longitude, sanitaryOptions, "no author");

                    StoreDatabase.saveStore(newStore);

                    context.updateStores();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Store name can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreDatabase.deleteStore(storeToEdit, context);
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return dialog;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    private boolean dataIsValid() {
        String storeName = this.storeName.getText().toString();

        return !storeName.isEmpty();
    }

    public void setEditMode(Store store) {
        this.storeToEdit = store;
    }

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
}
