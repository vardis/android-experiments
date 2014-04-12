package com.giorgos.fragments.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by giorgos on 12/04/14.
 */
public class CarsArrayAdapter extends ArrayAdapter<Car> {

    public CarsArrayAdapter(Context context, List<Car> cars) {
        super(context, R.layout.cars_list, cars);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cars_list, parent, false);
        Car car = getItem(position);

        TextView title = (TextView) view.findViewById(R.id.carList_title);
        title.setText(car.getBrand() + " - " + car.getModel());

        ImageView brandIcon = (ImageView) view.findViewById(R.id.carList_brandIcon);
        brandIcon.setImageResource(car.getBrandIcon());
        return view;
    }
}
