package com.grupo07.preventedstores.popupWindows;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.grupo07.preventedstores.R;
import com.grupo07.preventedstores.objects.SanitaryMeasure;
import com.grupo07.preventedstores.objects.SanitaryMeasuresFactory;
import com.grupo07.preventedstores.objects.Store;
import com.grupo07.preventedstores.activities.MapsActivity;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * Popup windows that shows the sanitary information of a store.
 * Can lead to an edit store option
 */
public class ShowStoreWindow extends AppCompatDialogFragment {

    // Parent activity used as application context for the widgets
    private MapsActivity context;

    // Store whose information is shown
    private Store store;

    /**
     * The class constructor requires an application context to be passed
     * @param context application context
     */
    public ShowStoreWindow(MapsActivity context) {
        this.context = context;
        context.updateStores();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View windowView = layoutInflater.inflate(R.layout.show_store, null);
        dialogBuilder.setView(windowView);

        TextView storeNameView = (TextView) windowView.findViewById(R.id.storeName);
        storeNameView.setText(store.getName() + " ⬤");
        ((TextView) windowView.findViewById(R.id.author)).setText("Added by " + store.getAuthor());
        ((TextView) windowView.findViewById(R.id.category)).setText(store.getCategory());

        TextView optionViews[] = new TextView[5];
        optionViews[0] = (TextView)windowView.findViewById(R.id.option1);
        optionViews[1] = (TextView) windowView.findViewById(R.id.option2);
        optionViews[2] = (TextView) windowView.findViewById(R.id.option3);
        optionViews[3] = (TextView) windowView.findViewById(R.id.option4);
        optionViews[4] = (TextView) windowView.findViewById(R.id.option5);

        // unique category checkbox options
        SanitaryMeasure measures = SanitaryMeasuresFactory.makeMeasures(store.getCategory());
        optionViews[optionViews.length-2].setText("✓ " + measures.getMeasures()[0]);
        optionViews[optionViews.length-1].setText("✓ " + measures.getMeasures()[1]);

        String sanitaryOptions = store.getSanitaryOptions();
        displaySanitaryOptions(optionViews, sanitaryOptions);

        setIndicatorColor(storeNameView, sanitaryOptions);

        final Dialog dialog = dialogBuilder.create();

        Button editButton = (Button) windowView.findViewById(R.id.editButton);
        setupEditStoreButton(dialog, editButton);

        // to achieve rounded corners
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    /**
     * Display the given sanitary options in text views
     * @param optionViews text views to display the sanitary options
     * @param sanitaryOptions sanitary options as a string
     */
    private void displaySanitaryOptions(TextView optionViews[], String sanitaryOptions) {
        for (int i = 0; i < optionViews.length; i++) {
            if (sanitaryOptions.charAt(i) == '0') {
                optionViews[i].setText(optionViews[i].getText().toString().replace("✓", "X"));
                optionViews[i].setTextColor(Color.parseColor("#ff3333"));
            }
        }
    }

    /**
     * Sets up the edit button to open the store in an edit window
     * @param window current window shown
     * @param editButton button linked to the edit store option
     */
    private void setupEditStoreButton(final Dialog window, Button editButton) {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                StoreFormWindow formDialog = new StoreFormWindow(context);
                formDialog.setStoreToEdit(store);
                formDialog.show(context.getSupportFragmentManager(), "");
            }
        });
    }

    /**
     * Sets the color of the rating circle indicator
     *
     * @param indicatorView text view that displays the indicator
     * @param sanitaryOptions sanitary options used to get the indicator color
     */
    private void setIndicatorColor(TextView indicatorView, String sanitaryOptions) {
        int totalOptions = sanitaryOptions.length();

        int fulfilledOptions = 0;
        for (int i = 0; i < totalOptions; i++)
            if (sanitaryOptions.charAt(i) == '1')
                fulfilledOptions += 1;

        int rating = (int) (((float) fulfilledOptions / totalOptions) * 100);

        int indicatorColor;
        if (rating == 100) {
            indicatorColor = Color.parseColor("#53c553");
        } else if (rating > 40) {
            indicatorColor = Color.parseColor("#cfcf07");
        } else if (rating > 0) {
            indicatorColor = Color.parseColor("#ff944d");
        } else {
            indicatorColor = Color.parseColor("#ff3333");
        }

        indicatorView.setTextColor(indicatorColor);
    }

    public void setStore(Store store) {
        this.store = store;
    }

}
