package com.example.ourgraduationproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdpter  extends ArrayAdapter<User> {
    private static final String LOG_TAG=User.class.getSimpleName();

    public UserAdpter(Activity context, ArrayList<User> androidFlavors) {
        super(context, 0, androidFlavors);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.userview, parent, false);
        }

        User currentproudect = getItem(position);

        TextView Uname = (TextView) listItemView.findViewById(R.id.uname);

        Uname.setText(currentproudect.name);

        TextView Addres = (TextView) listItemView.findViewById(R.id.UAddres);

        Addres.setText(currentproudect.adress);

        TextView phone = (TextView) listItemView.findViewById(R.id.UPhone);

        phone.setText(currentproudect.phone);

        TextView Emai = (TextView) listItemView.findViewById(R.id.UEmail);

        Emai.setText(currentproudect.Email);
        return listItemView;
    }
}
