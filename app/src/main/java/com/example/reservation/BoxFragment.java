package com.example.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

import com.example.reservation.classes.GlobalLists;
import com.example.reservation.classes.ReturnServiceClassHolder;

public class BoxFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_box, container, false);

        // Get a reference to the container layout where you want to add the boxes
        LinearLayout containerLayout = rootView.findViewById(R.id.containerLayout);

        // Loop to add the box layout multiple times
        int itmesInList= GlobalLists.getInstance().getMasterList().get(0).size();
        int listSize = GlobalLists.getInstance().getMasterList().size();
        for (int i = 0; i < listSize; i++) {

                // Inflate the box layout
                View boxView = inflater.inflate(R.layout.item_box_layout, containerLayout, false);

                TextView companyNameTextView = boxView.findViewById(R.id.companyDisplay);
                TextView serviceTextView = boxView.findViewById(R.id.serviceDisplay);
                TextView serviceTypeTextView = boxView.findViewById(R.id.serviceTypeDisplay);

                // Set data from the lists to the TextViews
                //String display = ReturnServiceClassHolder.getInstance().getReturnServices().getServiceList().get(i) + ReturnServiceClassHolder.getInstance().getReturnServices().getServiceTypeList().get(j);
                companyNameTextView.setText(GlobalLists.getInstance().getMasterList().get(i).get(0));
                serviceTextView.setText(GlobalLists.getInstance().getMasterList().get(i).get(1));
                serviceTypeTextView.setText(GlobalLists.getInstance().getMasterList().get(i).get(2));


                // Add the inflated box layout to the container
                containerLayout.addView(boxView);
            }


        ///dodano

       /* int numOfServices = ReturnServiceClassHolder.getInstance().getReturnServices().getServiceList().size();
           for(int serviceNum = 0; serviceNum < numOfServices; numOfServices++ ){
                    View boxView = inflater.inflate(R.layout.item_box_layout, containerLayout, false);

                    TextView serviceTextView = boxView.findViewById(R.id.serviceDisplay);


                    serviceTextView.setText(ReturnServiceClassHolder.getInstance().getReturnServices().getServiceTypeList().get(serviceNum));

                    // Add the inflated box layout to the container
                    containerLayout.addView(boxView);

        }*/

        return rootView;
    }
}
