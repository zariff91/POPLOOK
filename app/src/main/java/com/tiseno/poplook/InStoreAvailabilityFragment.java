package com.tiseno.poplook;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.functions.InStoreAdapter;
import com.tiseno.poplook.functions.StoreListItem;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tumblr.bookends.Bookends;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InStoreAvailabilityFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    Bookends<RecyclerView.Adapter> mBookends;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter RVAdapter;

    ChipCloud chipCloud;

    String[] sizeArray;
    String sizeForEmail;


    List<String> sizeList = new ArrayList<String>();
    List<String> sizeIDList = new ArrayList<String>();

    String productName;
    String productPrice;
    String productID;
    String img;


    TextView closeBtn,emailTo,orText,whatsappText,firstTV;

    TextView belowText,aboveText;

    TextView randomTV,randomTV2;


    ArrayList<StoreListItem> listArray_store = new ArrayList<StoreListItem>();

    ImageLoader imageLoader= ImageLoader.getInstance();


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.in_store_check, container, false);

        Bundle bundle = this.getArguments();

        sizeList = bundle.getStringArrayList("sizes");
        sizeIDList = bundle.getStringArrayList("attID");

        productName = bundle.getString("Product_Name");
        productPrice = bundle.getString("Product_Price");
        productID =  bundle.getString("Product_ID");
        img =  bundle.getString("image_url");

        System.out.println("get size 1 = " + sizeList);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.storeListRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        closeBtn = (TextView)view.findViewById(R.id.close_instore);
        closeBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        closeBtn.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View v) {


                                              getActivity().getFragmentManager().popBackStack();


                                          }

                                      }

        );



        //Create Table View

        RVAdapter = new InStoreAdapter(getActivity(),listArray_store);
        // Make Bookends
        mBookends= new Bookends<>(RVAdapter);

        LayoutInflater inflater1 = LayoutInflater.from(getActivity());
        final LinearLayout header = (LinearLayout) inflater1.inflate(R.layout.instore_header_view, mRecyclerView, false);
        final LinearLayout header2 = (LinearLayout) inflater1.inflate(R.layout.instore_header_below, mRecyclerView, false);

        ImageView prodImage = (ImageView) header.findViewById(R.id.product_Image);
        TextView prodName = (TextView) header.findViewById(R.id.productTitle);
        TextView prodPrice = (TextView) header.findViewById(R.id.productPrice);
        belowText = (TextView) header2.findViewById(R.id.below_TV);
        aboveText = (TextView) header2.findViewById(R.id.above_TV);

        emailTo = (TextView)header2.findViewById(R.id.emailto);
        emailTo.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        firstTV = (TextView)header2.findViewById(R.id.firstTV);
        firstTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        orText = (TextView)header2.findViewById(R.id.orTV);
        orText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        whatsappText = (TextView)header2.findViewById(R.id.whatsapp);
        whatsappText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        randomTV = (TextView)header2.findViewById(R.id.textTv);
        randomTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        SpannableString content = new SpannableString("Email here");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        emailTo.setText(content);
        emailTo.setVisibility(View.GONE);

        SpannableString content2 = new SpannableString("Whatsapp");
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        whatsappText.setText(content2);
        whatsappText.setVisibility(View.GONE);
        orText.setVisibility(View.GONE);
        randomTV.setVisibility(View.GONE);

        randomTV2 = (TextView)header2.findViewById(R.id.textTv2);
        randomTV2.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        randomTV2.setVisibility(View.GONE);
        firstTV.setVisibility(View.GONE);


        prodName.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        prodPrice.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        belowText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        aboveText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        emailTo.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View v) {

                                           Fragment fragment = new ContactUsFragment();
                                           Bundle bundle = new Bundle();
                                           bundle.putString("fromInstore", "1");
                                           bundle.putString("productName", productName);
                                           bundle.putString("productSize", sizeForEmail);


                                           fragment.setArguments(bundle);
                                           FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                           fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                           fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                           fragmentTransaction.addToBackStack(null);
                                           fragmentTransaction.commit();

                                       }

                                   }

        );

        whatsappText.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View v) {

                                           String phoneNumberWithCountryCode = "+60196933958";
                                           String message = "Hi Poplook I'm interested in "+productName+" Size "+sizeForEmail;

                                           String convertMessage = message.replace("&","%26");

                                           startActivity(new Intent(Intent.ACTION_VIEW,
                                                   Uri.parse(
                                                           String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                                                   phoneNumberWithCountryCode, convertMessage))));

                                       }

                                   }

        );


        prodName.setText(productName);
        prodPrice.setText("RM " + productPrice + " ");

        if(sizeList.size() != 0)
        {

            belowText.setVisibility(View.VISIBLE);

            sizeArray = new String[sizeList.size()];

            for (int x = 0; x <sizeList.size(); x++)
            {

                String sizes;
                sizes = sizeList.get(x);
                sizeArray[x] = sizes;

            }


            chipCloud = (ChipCloud) header.findViewById(R.id.select_size_tv_view_instore);

            chipCloud.setMode(ChipCloud.Mode.SINGLE);

            new ChipCloud.Configure()
                    .chipCloud(chipCloud)
                    .selectedColor(Color.parseColor("#000000"))
                    .selectedFontColor(Color.parseColor("#ffffff"))
                    .deselectedColor(Color.parseColor("#e1e1e1"))
                    .deselectedFontColor(Color.parseColor("#333333"))
                    .labels(sizeArray)
                    .gravity(ChipCloud.Gravity.CENTER)
                    .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin))
                    .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin))
                    .chipListener(new ChipListener() {
                        @Override
                        public void chipSelected(int index) {

                            listArray_store.clear();

                            String selected = sizeList.get(index);
                            sizeForEmail = selected;

                            System.out.println("get size 2 = " + selected);


                            updateStoreAvailability(selected);


                        }
                        @Override
                        public void chipDeselected(int index) {

                        }
                    })
                    .build();

        }


        else {

            updateStoreAvailability("Free%20Size");
        }

        mBookends.addHeader(header);
        mBookends.addHeader(header2);
        mRecyclerView.setAdapter(mBookends);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .build();
        display(prodImage, img,options);

        ((MainActivity) getActivity()).getSupportActionBar().hide();
        ((MainActivity) getActivity()).showBottomBar(false);

        if(listArray_store.size() !=0)
        {
            emailTo.setVisibility(View.VISIBLE);
            orText.setVisibility(View.VISIBLE);
            whatsappText.setVisibility(View.VISIBLE);
            randomTV.setVisibility(View.VISIBLE);
            randomTV2.setVisibility(View.VISIBLE);
            firstTV.setVisibility(View.VISIBLE);
        }

        return view;

    }

    private void updateStoreAvailability(String size) {

        System.out.println("get prod ID = " + productID);

        String action;
        action = "ProductsQuantity/store_detail/"+productID+"/"+size+"?apikey=PL:@KrAk!fA9RpGDcnIfDKzljGkEqW48yU4M6Y2GckgawSVbEg62FHKHBU7awnidFZ4wxVxUdcTAvkxT1GrlVhuZ1dKlqzl9zlsedD66G";
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    private void refreshHeaderText() {

        if(listArray_store.size() == 0)
        {

            belowText.setText("Not available in-stores");
            aboveText.setVisibility(View.GONE);
            emailTo.setVisibility(View.GONE);
            orText.setVisibility(View.GONE);
            whatsappText.setVisibility(View.GONE);
            randomTV.setVisibility(View.GONE);
            randomTV2.setVisibility(View.GONE);
            firstTV.setVisibility(View.GONE);


            belowText.setVisibility(View.VISIBLE);

        }


        else {

            emailTo.setVisibility(View.VISIBLE);
            orText.setVisibility(View.VISIBLE);
            whatsappText.setVisibility(View.VISIBLE);
            randomTV.setVisibility(View.VISIBLE);
            randomTV2.setVisibility(View.VISIBLE);
            firstTV.setVisibility(View.VISIBLE);



            belowText.setText("Available at:");
            aboveText.setVisibility(View.VISIBLE);
            belowText.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public void onTaskComplete(JSONObject result) {

        System.out.println("datata = " + result);


        if(result!=null) {

            try {
                if (result.getBoolean("status")) {

                    JSONArray data = result.getJSONArray("data");

                    for(int x = 0; x<data.length();x++)
                    {

                        JSONObject data1 = data.getJSONObject(x);

                        String title = data1.getString("store");
                        String address = data1.getString("store_address");

                        int quantity = data1.getInt("quantity");

                        if(quantity > 0)

                        {

                            listArray_store.add(new StoreListItem(title,address));

                        }

                    }

                    mBookends.notifyDataSetChanged();
                    refreshHeaderText();

                }
            } catch (Exception e) {
                System.out.println("Error " + e);
            }
        }

        else
        {

        }
    }

    public void display(final ImageView img, String url, DisplayImageOptions options)
    {

        System.out.println("get image url" + url);

        imageLoader.displayImage(url, img,options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                img.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                img.setVisibility(View.VISIBLE);


            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                img.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }

        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//
//        ((MainActivity) getActivity()).getSupportActionBar().hide();
//        ((MainActivity) getActivity()).showBottomBar(true);
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        ((MainActivity) getActivity()).getSupportActionBar().show();
//        ((MainActivity) getActivity()).showBottomBar(true);
//
//    }
}
