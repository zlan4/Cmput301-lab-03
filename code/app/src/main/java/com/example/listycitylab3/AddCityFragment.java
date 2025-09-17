package com.example.listycitylab3;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void refreshCityList();
    }

    private AddCityDialogListener listener;
    private City cityToEdit;
    public AddCityFragment() {
    }

    // Constructor for editing a city
    public AddCityFragment(City city) {
        this.cityToEdit = city;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        if (cityToEdit != null) {
            editCityName.setText(cityToEdit.getName());
            editProvinceName.setText(cityToEdit.getProvince());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add/edit city")
                .setCancelable(false)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    if (cityName.isEmpty() || provinceName.isEmpty()) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Please enter a city and province!")
                                .setPositiveButton("OK", null)
                                .show();
                        return;
                    }
                    boolean isDuplicate = false;
                    for (City city : ((MainActivity) getActivity()).getDataList()) {
                        if (cityToEdit != null && city == cityToEdit) {
                            continue;
                        }

                        if (city.getName().equalsIgnoreCase(cityName) &&
                                city.getProvince().equalsIgnoreCase(provinceName)) {
                            isDuplicate = true;
                            break;
                        }
                    }

                    if (isDuplicate) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("The city and province are already in the list!")
                                .setPositiveButton("OK", null)
                                .show();
                        return;
                    }
                    if (cityToEdit != null) {
                        cityToEdit.setName(cityName);
                        cityToEdit.setProvince(provinceName);
                        listener.refreshCityList();
                    } else {
                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }
}
