package com.tiseno.poplook;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.sizeSelectionAdapter;

import java.util.ArrayList;

public class SelectSizeAndFilterFragment extends Fragment {

    ArrayList<String> listArray_attribute = new ArrayList<String>();
    ArrayList<String> listArray_color = new ArrayList<String>();

    TextView tv1;

    String origin;

    Button doneBtn;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter RVAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();

        origin = bundle.getString("sizeOrcolour");

        View mainView = inflater.inflate(R.layout.select_size_filter_layout, container, false);

        tv1 = (TextView)mainView.findViewById(R.id.pleaseSelectTv);
        tv1.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        if(bundle.getString("sizeOrcolour").equals("size"))
        {

            tv1.setText("Please select size :");

        }
        else if (bundle.getString("sizeOrcolour").equals("colour"))
        {

            tv1.setText("Please select colour :");

        }
        else {}

        doneBtn = (Button)mainView.findViewById(R.id.donBtnn);
        doneBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        doneBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Fragment frag = new FilterAndSortFragment();

                Bundle bundle = new Bundle();

                bundle.putBoolean("isFiltered", true);
                bundle.putStringArrayList("listSelectedSize", listArray_attribute);
                bundle.putStringArrayList("listSelectedColor", listArray_color);

                frag.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                SelectSizeAndFilterFragment filterFragment = (SelectSizeAndFilterFragment)fragmentManager.findFragmentByTag("SelectSizeAndFilterFragment");

                ((MainActivity) getActivity()).getSupportActionBar().show();
//
                if (filterFragment != null && filterFragment.isVisible()) {
                    // add your code here
                    FragmentTransaction trans1 = fragmentManager.beginTransaction();
                    trans1.remove(filterFragment);
                    trans1.commit();
                    fragmentManager.popBackStack();

                }

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, frag, "FilterAndSortFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        mRecyclerView = (RecyclerView) mainView.findViewById(R.id.filtersortBtnRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        RVAdapter = new sizeSelectionAdapter(getActivity(), this, origin);

        mRecyclerView.setAdapter(RVAdapter);


        return mainView;

    }

    public void selectedData(String firstData)
    {
        System.out.println("getdatafromadapter = " + firstData);

        listArray_attribute.add(firstData);
    }

    public void selectedDataColor(String firstData)
    {
        System.out.println("getdatafromadapter = " + firstData);

        listArray_color.add(firstData);
    }

}
