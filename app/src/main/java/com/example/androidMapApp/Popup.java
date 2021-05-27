package com.example.androidMapApp;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.googlemapapi.R;

public class Popup extends Dialog {
    private String title, positiveButtonText, negativeButtonText, sitenameHint, descritpionHint ;
    private TextView titleView;
    private EditText sitenameEditText, descriptionEditText;
    private Button cancelButton, saveButton;

    public Popup(Activity activity){
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.insert_location_popup_template);

        titleView = findViewById(R.id.title);
        sitenameEditText = findViewById(R.id.sitename);
        descriptionEditText = findViewById(R.id.location_description);
        cancelButton = findViewById(R.id.cancel_btn);
        saveButton = findViewById(R.id.save_btn);
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescritpionHint(String descritpionHint) {
        this.descritpionHint = descritpionHint;
    }

    public void setSitenameHint(String usernameHint) {
        this.sitenameHint = usernameHint;
    }

    public void setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
    }

    public void setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public EditText getSitenameEditText() {
        return sitenameEditText;
    }

    public EditText getDescriptionEditText() {
        return descriptionEditText;
    }

    public void build(){
        titleView.setText(title);

        sitenameEditText.setHint(sitenameHint);
        descriptionEditText.setHint(descritpionHint);

        cancelButton.setText(negativeButtonText);
        saveButton.setText(positiveButtonText);

        show();
    }
}
