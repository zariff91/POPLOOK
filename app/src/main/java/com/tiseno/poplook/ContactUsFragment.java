package com.tiseno.poplook;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ContactUsFragment extends Fragment implements AsyncTaskCompleteListener<JSONObject> {
    Spinner spinner,contactUs_orderRefSpinner;
    TextView contactUs_orderRefTV,contactUs_ByPhoneTV,contactUs_ByPhoneET,contactUs_ByWSTV,contactUs_ByPWSET,contactUs_ByEmailTV,contactUs_ByEmailET,contactUs_sendUsMessageTV,contactUs_subjectTV,contactUs_userEmailTV,contactUsMessageTV;
    EditText contactUsMessageET,contactUs_userEmailET;
    Button contactUs_submitBtn;
    LinearLayout OrderRefLL;
    View subjectDivider1;
    String Email,UserID,subject,SelectedShopID;
    String fromInStore;
    String prodName,prodSize;

    int orderRef;
    List<String> id_orderList = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_contact_us, container, false);

        ((MainActivity) getActivity()).changeToolBarText("Contact Us");
        ((MainActivity) getActivity()).changeToolBarTextView(true);
        ((MainActivity) getActivity()).changeBtnBackView(true);
        ((MainActivity) getActivity()).changeToolBarImageView(false);
        ((MainActivity) getActivity()).changeBtnSearchView(true);
        ((MainActivity) getActivity()).changeBtnBagView(true);
        ((MainActivity) getActivity()).changeBtnWishlistView(true);
        ((MainActivity) getActivity()).changeBtnCloseXView(false);
        ((MainActivity) getActivity()).setDrawerState(true);
        ((MainActivity) getActivity()).disableExpandToolbar(false);

        ((MainActivity) getActivity()).getSupportActionBar().show();
        ((MainActivity) getActivity()).showBottomBar(true);


        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        UserID = pref.getString("UserID","");
        //multishop
        SelectedShopID  = pref.getString("SelectedShopID", "1");
        //
        orderRef=0;
        OrderRefLL = (LinearLayout) view.findViewById(R.id.OrderRefLL);
        subjectDivider1 = (View) view.findViewById(R.id.subjectDivider1);

        contactUs_ByPhoneTV = (TextView) view.findViewById(R.id.contactUs_ByPhoneTV);
        contactUs_ByPhoneET = (TextView) view.findViewById(R.id.contactUs_ByPhoneET);
        contactUs_ByWSTV = (TextView) view.findViewById(R.id.contactUs_ByWS);
        contactUs_ByPWSET = (TextView) view.findViewById(R.id.contactUs_ByWSET2);
        contactUs_ByEmailTV = (TextView) view.findViewById(R.id.contactUs_ByEmailTV);
        contactUs_ByEmailET = (TextView) view.findViewById(R.id.contactUs_ByEmailET);
        contactUs_sendUsMessageTV = (TextView) view.findViewById(R.id.contactUs_sendUsMessageTV);
        contactUs_subjectTV = (TextView) view.findViewById(R.id.contactUs_subjectTV);
        contactUs_userEmailTV = (TextView) view.findViewById(R.id.contactUs_userEmailTV);
        contactUs_userEmailET = (EditText) view.findViewById(R.id.contactUs_userEmailET);
        contactUsMessageTV = (TextView) view.findViewById(R.id.contactUsMessageTV);
        contactUs_submitBtn = (Button) view.findViewById(R.id.contactUs_submitBtn);
        contactUsMessageET = (EditText) view.findViewById(R.id.contactUsMessageET);
        contactUs_orderRefTV = (TextView) view.findViewById(R.id.contactUs_orderRefTV);

        spinner=(Spinner)view.findViewById(R.id.contactUs_Spinner);
        contactUs_orderRefSpinner=(Spinner)view.findViewById(R.id.contactUs_orderRefSpinner);

        contactUs_ByPhoneTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        contactUs_ByPhoneET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        contactUs_ByWSTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        contactUs_ByPWSET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        contactUs_ByEmailTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        contactUs_ByEmailET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        contactUs_sendUsMessageTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        contactUs_subjectTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        contactUs_userEmailTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        contactUs_userEmailET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        contactUsMessageTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        contactUs_submitBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        contactUsMessageET.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        contactUs_orderRefTV.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        Bundle bundle = this.getArguments();
        fromInStore = bundle.getString("fromInstore","0");

        List<String> list = new ArrayList<String>();
        list.add("General Enquiry");
        list.add("Order Enquiry");
        list.add("Interest to Purchase");

        contactUs_ByPhoneET.setText("+606-253 2750");
        contactUs_ByPWSET.setText("Monday to Friday\n" + "10am - 6pm GMT +8");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_layout2, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        if(fromInStore.equals("1"))
        {
            prodName = bundle.getString("productName");
            prodSize = bundle.getString("productSize");


            contactUsMessageET.setText("Hi poplook i would like to ask about " + prodName+" " +prodSize+"\n"+"Quantity : 1"+"\n"+"\n"+"My delivery address:"+"\n"+"\n"+"My contact number:");

            spinner.setSelection(2);

        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView1, View view1, int position1, long id1) {
                // your code here
                if (parentView1.getChildAt(0) != null) {
                    ((TextView) parentView1.getChildAt(0)).setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                    getContactUsInformation(String.valueOf(id1+1));
                    subject = spinner.getSelectedItem().toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here

            }

        });



        contactUs_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (contactUsMessageET.getText().toString().length()==0){
                    new android.app.AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Message cannot be blank")
                            .setPositiveButton("OK", null)
                            .show();
                }
                else if (contactUs_ByEmailET.getText().toString().length()==0){
                    new android.app.AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Please enter your email")
                            .setPositiveButton("OK", null)
                            .show();


                }
                else if (contactUs_ByEmailET.getText().toString().length()==0 && contactUsMessageET.getText().toString().length()==0){
                    new android.app.AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage("Please enter your email and message")
                            .setPositiveButton("OK", null)
                            .show();


                }else{
                    sendEmail();
                }
            }
        });

        return view;
    }

    private void getContactUsInformation(String contactID){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="Infos/contactus/contact/"+contactID+"/lang/1/customer/"+UserID+"/shop/"+SelectedShopID+"?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    private void getOrderRef(){
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action="Orders/histories/customer/"+UserID+"/n/20/p/1?apikey="+apikey;
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);

    }

    private void sendEmail()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        //multishop
        String apikey =pref.getString("apikey","");
        String action;
        if (UserID.length() > 0) {
            action="SendEmail/mailto?apikey="+apikey+"&email="+contactUs_userEmailET.getText().toString()+"&subject="+subject+"&message="+contactUsMessageET.getText().toString()+"&id_order="+id_orderList.get(orderRef)+"&shop="+SelectedShopID+"";
        }else{
            action="SendEmail/mailto?apikey="+apikey+"&email="+contactUs_userEmailET.getText().toString()+"&subject="+subject+"&message="+contactUsMessageET.getText().toString()+"&id_order=&shop="+SelectedShopID+"";
        }
        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        if(result!=null){

            try {
                if (result.getBoolean("status")) {
                    System.out.println("hahaaaaaaab");
                    if (result.getString("action").equals("Infos_contactus"))
                    {
                        JSONObject data=result.getJSONObject("data");
                        String company_phone = data.getString("company_phone");
                        String company_email = data.getString("company_email");
                        String customer_email = data.getString("customer_email");
                        String contact_email = data.getString("contact_email");
                        String name = data.getString("name");
                        String description = data.getString("description");
//                        contactUs_ByPhoneET.setText(phone);
                        contactUs_ByEmailET.setText(contact_email);

                        if(contact_email.equals("") || contact_email.equals("null"))
                        {
                            contactUs_ByEmailET.setText("service@poplook.com");

                        }


                        if(UserID.length()>0) {
                            contactUs_userEmailET.setText(customer_email);
                            contactUs_userEmailET.setEnabled(false);
                            OrderRefLL.setVisibility(View.VISIBLE);
                            subjectDivider1.setVisibility(View.GONE);
                            getOrderRef();

                            if(fromInStore.equals("1"))
                            {
                                OrderRefLL.setVisibility(View.GONE);
                                spinner.setEnabled(false);
                                spinner.setClickable(false);

                            }

                        }else{
                            contactUs_userEmailET.setText("");
                            contactUs_userEmailET.setEnabled(true);
                            OrderRefLL.setVisibility(View.GONE);
                            subjectDivider1.setVisibility(View.VISIBLE);
                        }

                    }else if (result.getString("action").equals("SendEmail_mailto")) {
                        String message = result.getString("message");
                        Toast toast = Toast.makeText(getActivity(),
                                "Message has been sent", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();
                        contactUsMessageET.setText("");

                    }
                    else if (result.getString("action").equals("Orders_histories")) {
                        JSONObject data = result.getJSONObject("data");
                        JSONArray jsonArr = new JSONArray();
                        jsonArr = data.getJSONArray("order_histories");

                        List<String> list = new ArrayList<String>();
                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            JSONObject jObj = jsonArr.getJSONObject(i);

                            String id_order = jObj.getString("id_order");
                            String invoice_number = jObj.getString("invoice_number");
                            String invoice_date = jObj.getString("invoice_date");

                            String dateOnly=invoice_date.split(" ")[0];
                            list.add("IN"+invoice_number+" - "+dateOnly);
                            id_orderList.add(id_order);
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                R.layout.spinner_layout2, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        contactUs_orderRefSpinner.setAdapter(dataAdapter);

                        contactUs_orderRefSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView1, View view1, int position1, long id1) {
                                // your code here
                                if (parentView1.getChildAt(0) != null) {
                                    ((TextView) parentView1.getChildAt(0)).setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
                                    orderRef=position1;
                                    System.out.println("ORDERLIST INT "+orderRef);
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // your code here

                            }

                        });

                    }
                }else
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Message")
                            .setMessage(result.getString("message"))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }).show();

            }
            catch (Exception e){

            }



        }
        else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Message")
                    .setMessage("Please connect to the Internet")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();

        }
    }

}
