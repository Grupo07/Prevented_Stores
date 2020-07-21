package com.grupo07.preventedstores;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ShowStoreWindow extends AppCompatDialogFragment {


    private MapsActivity context;
    private Store store;

    public ShowStoreWindow(MapsActivity context) {
        this.context = context;
        context.updateStores();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.show_store, null);
        builder.setView(view);

        TextView storeName = (TextView) view.findViewById(R.id.storeName);
        TextView options[] = new TextView[2];
        options[0] = (TextView)view.findViewById(R.id.option1);
        options[1] = (TextView) view.findViewById(R.id.option2);

        storeName.setText(store.name);
        String sanitaryOptions = store.getSanitaryOptions();
        for (int i = 0; i < options.length; i++) {
            if (sanitaryOptions.charAt(i) == '0') {
                options[i].setText(options[i].getText().toString().replace("✓", "X"));
                options[i].setTextColor(Color.RED);
            }
        }

        final Dialog dialog = builder.create();

        Button editButton = (Button) view.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                StoreFormWindow formDialog = new StoreFormWindow(context);
                formDialog.setEditMode(store);
                formDialog.show(context.getSupportFragmentManager(), "");
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    public void setStore(Store store) {
        this.store = store;
    }

}
