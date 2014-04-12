package com.giorgos.fragments.app;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by giorgos on 12/04/14.
 */
public class CarDetailViewBinder {
    private TextView modelName;
    private ImageView brandIcon;
    private ImageView previewIcon;
    private TextView year;
    private Car car;

    public CarDetailViewBinder(View parent, Car car) {
        this.car = car;
        modelName = (TextView) parent.findViewById(R.id.modelNameText);
        brandIcon = (ImageView) parent.findViewById(R.id.brandIcon);
        previewIcon = (ImageView) parent.findViewById(R.id.previewIcon);
        year = (TextView) parent.findViewById(R.id.yearText);
    }

    public void bindToView() {
        modelName.setText(car.getBrand() + " - " + car.getModel());
        year.setText(Integer.toString(car.getYear()));
        brandIcon.setImageResource(R.drawable.honda);
        previewIcon.setImageResource(car.getImageRes());
    }

    public TextView getModelName() {
        return modelName;
    }

    public ImageView getBrandIcon() {
        return brandIcon;
    }

    public TextView getYear() {
        return year;
    }
}
