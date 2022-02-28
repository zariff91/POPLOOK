package com.tiseno.poplook;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.StringUtils;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONObject;

import java.util.EnumMap;
import java.util.Map;

public class MyMemberIDFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    TextView topTextView,aboveBarcodeText,bottomTV,barcodeText, refreshBarcodeTV;

    RelativeLayout barcodeRL;

    ImageView barcodeImg;

    private int brightness;
    private ContentResolver contentResolver;
    private Window window;

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        contentResolver = getActivity().getApplicationContext().getContentResolver();
        window = getActivity().getWindow();

        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.screenBrightness = 1;
        getActivity().getWindow().setAttributes(lp);

        View view = inflater.inflate(R.layout.my_member_id_layout, container, false);

        ((MainActivity) getActivity()).changeToolBarText("My Member ID");
        ((MainActivity) getActivity()).changeBtnBackView(true);


        topTextView = (TextView)view.findViewById(R.id.topTV);
        topTextView.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        aboveBarcodeText = (TextView)view.findViewById(R.id.aboveBarcodeTextView);
        topTextView.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        barcodeText = (TextView)view.findViewById(R.id.barcodeTextView);
        barcodeText.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));


        bottomTV = (TextView)view.findViewById(R.id.bottomLayoutTV);
        bottomTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));

        bottomTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new LoyaltyDashboardFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });

        refreshBarcodeTV = (TextView)view.findViewById(R.id.refreshBarcode);
        refreshBarcodeTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));



        barcodeImg = (ImageView)view.findViewById(R.id.barcodeImage);

        barcodeRL = (RelativeLayout) view.findViewById(R.id.barcodeLayout);


        // Barcode Implementation////////////////////////////////////////////////////////////

       final SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);

        // barcode data
//        String barcode_data = "";
//        String barcode_data = "2133121 Jamie Lee Testing Jamie Lee Testing Jamie Lee Testing";
        String barcode_data = pref.getString("entity_id", "");
        String barcode_text;

        System.out.println("get Barcode data here = " + barcode_data);


        if(barcode_data.length() == 0 || barcode_data.equals("null") || barcode_data == null)
        {

            barcodeRL.setVisibility(View.GONE);
            barcode_text = "";

        }

       else if(barcode_data.length() > 25)
        {
            barcode_text = barcode_data.substring(0,24);
            refreshBarcodeTV.setVisibility(View.GONE);
            barcodeRL.setVisibility(View.VISIBLE);
        }

        else

            {

                barcode_text = barcode_data;
                refreshBarcodeTV.setVisibility(View.GONE);
                barcodeRL.setVisibility(View.VISIBLE);


            }

        // barcode image
        Bitmap bitmap = null;

        try {

            bitmap = encodeAsBitmap(barcode_text, BarcodeFormat.CODE_128, 3500, 700);
            barcodeImg.setImageBitmap(bitmap);
            barcodeText.setText(barcode_data);

        } catch (WriterException e) {
            e.printStackTrace();
        }



        // Barcode Implementation////////////////////////////////////////////////////////////

        refreshBarcodeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);

                String custID = pref.getString("UserID","");
                String shopID = pref.getString("SelectedShopID","");

                getBarcode(custID,shopID);

            }

        });



        return view;
    }

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    private void getBarcode(String customerID, String shopID){

        System.out.println("dapat customer"+customerID+" dengan shop ID"+shopID+"");

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String apikey =pref.getString("apikey","");
        String action="UserAuth/entityID?id_customer="+customerID+"&id_shop="+shopID+"&apikey="+apikey+"";
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(),this);
        callws.execute(action);

    }

    @Override
    public void onTaskComplete(JSONObject result) {

        System.out.println("Get JSON Response : " + result);

        if(result != null)
        {
            try {

                if(result.getBoolean("status"))
                {

                    JSONObject data = result.getJSONObject("data");


                    String barcode_data = data.getString("id_entity");
                    String barcode_text;

                    if(barcode_data.length() > 25)
                    {
                        barcode_text = barcode_data.substring(0,24);
                    }

                    else {barcode_text = barcode_data;}

                    // barcode image
                    Bitmap bitmap = null;

                    try {

                        bitmap = encodeAsBitmap(barcode_text, BarcodeFormat.CODE_128, 3500, 700);
                        barcodeImg.setImageBitmap(bitmap);
                        barcodeText.setText(barcode_data);

                        barcodeRL.setVisibility(View.VISIBLE);

                        refreshBarcodeTV.setVisibility(View.GONE);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("entity_id", barcode_data);
                    editor.apply();



                }

                else
                {

                    Toast toast = Toast.makeText(getActivity(),
                            "Unable to retrieve customer ID. Please try again in a few minute.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 50);
                    toast.show();

                }

            }
             catch (Exception e){};

        }

        else
        {

            Toast toast = Toast.makeText(getActivity(),
                    "Unable to retrieve customer ID. Please try again in a few minute.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            toast.show();

        }

    }
}
