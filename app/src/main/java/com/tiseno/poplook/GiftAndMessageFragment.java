package com.tiseno.poplook;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;

public class GiftAndMessageFragment extends Fragment {

    TextView title,doneButton,addressLbl;
    ImageButton backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).getSupportActionBar().hide();

        View contentView = inflater.inflate(R.layout.gift_message_layout, container, false);

        backButton = (ImageButton)contentView.findViewById(R.id.backButtonCustomOrder);

        String titlee=getArguments().getString("content");


        title = (TextView) contentView.findViewById(R.id.pageTitle);
        title.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        title.setText(titlee);

        doneButton = (TextView) contentView.findViewById(R.id.giftDoneButton);
        doneButton.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        backButton.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View v) {


                                              getActivity().onBackPressed();

                                          }

                                      }

        );

        doneButton.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View v) {


                                              getActivity().onBackPressed();

                                          }

                                      }

        );

        return contentView;
    }

}
