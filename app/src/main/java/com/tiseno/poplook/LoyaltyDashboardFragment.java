package com.tiseno.poplook;

import android.animation.ValueAnimator;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.tiseno.poplook.functions.FontUtil;
import com.tiseno.poplook.webservice.AsyncTaskCompleteListener;
import com.tiseno.poplook.webservice.WebServiceAccessGet;
import com.tiseno.poplook.webservice.WebServiceAccessPost;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class LoyaltyDashboardFragment  extends Fragment implements AsyncTaskCompleteListener<JSONObject> {

    TextView topLabel, belowDetailLabel, detailLabel, textProgress,towardsLbl,  spendlbl;
    TextView topLabelBenefits, secondLblBenefit, thirdLblBenefit, fourLblBenefit, fifthLblBenefit, sixLblBenefit;

    TextView topLabelDetailBenefits, secondLblDetailBenefit, thirdLblDetailBenefit, fourLblDetailBenefit, fifthLblDetailBenefit;

    RelativeLayout inviteRL, mysteryRL, early1rl, early2rl, leveluprl;

    ImageView dashboard ,dboard3, dboard2;

    Button benefitBtn, detailsBtn, submitBtn, levelUnlockBtn;

    CircularProgressBar progressBar;

    RelativeLayout benefitLy, detailLayout;

    String netAmount, getTier, getPercentage;

    LinearLayout bottomLL , LowLLSilver, LowLLBronze , mysteryLL, inLL;

    ScrollView scrollView;

    float finalVal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.loyalty_dashboard_layout, container, false);
//        rootView.setTag(TAG);


        progressBar = (CircularProgressBar)rootView.findViewById(R.id.progressBarr);
        textProgress = (TextView)rootView.findViewById(R.id.txtProgress);


        topLabel = (TextView) rootView.findViewById(R.id.topLabel);
        topLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_BLACK_FONT));
        detailLabel = (TextView) rootView.findViewById(R.id.labelOne);
        detailLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        belowDetailLabel = (TextView) rootView.findViewById(R.id.labelTwo);
        belowDetailLabel.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        towardsLbl = (TextView) rootView.findViewById(R.id.towardsLabel);
        towardsLbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        spendlbl = (TextView) rootView.findViewById(R.id.spendLabel);
        spendlbl.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


        topLabelBenefits = (TextView) rootView.findViewById(R.id.labelOne1);
        topLabelBenefits.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        secondLblBenefit = (TextView) rootView.findViewById(R.id.labelOne2);
        secondLblBenefit.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        thirdLblBenefit = (TextView) rootView.findViewById(R.id.labelOne3);
        thirdLblBenefit.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        fourLblBenefit = (TextView) rootView.findViewById(R.id.labelOne4);
        fourLblBenefit.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_MEDIUM_FONT));
        fifthLblBenefit = (TextView) rootView.findViewById(R.id.labelOne5);
        fifthLblBenefit.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        sixLblBenefit = (TextView) rootView.findViewById(R.id.labelOne6);
        sixLblBenefit.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        topLabelDetailBenefits = (TextView) rootView.findViewById(R.id.labelOne5_detail);
        topLabelDetailBenefits.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        secondLblDetailBenefit = (TextView) rootView.findViewById(R.id.labelOne5_detail2);
        secondLblDetailBenefit.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        thirdLblDetailBenefit = (TextView) rootView.findViewById(R.id.labelOne5_detail3);
        thirdLblDetailBenefit.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        fourLblDetailBenefit = (TextView) rootView.findViewById(R.id.labelOne5_detail4);
        fourLblDetailBenefit.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));
        fifthLblDetailBenefit = (TextView) rootView.findViewById(R.id.labelOne5_detail5);
        fifthLblDetailBenefit.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


        benefitBtn = (Button) rootView.findViewById(R.id.beneButn);
        benefitBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        detailsBtn = (Button) rootView.findViewById(R.id.detailBtn);
        detailsBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        submitBtn = (Button) rootView.findViewById(R.id.submitBtnn);
        submitBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));

        levelUnlockBtn = (Button) rootView.findViewById(R.id.levelUnlockBtn);
        levelUnlockBtn.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FontType.AVENIR_ROMAN_FONT));


        benefitLy = (RelativeLayout) rootView.findViewById(R.id.BenefitsLayout);
        detailLayout = (RelativeLayout) rootView.findViewById(R.id.DetailLayout);
        inviteRL = (RelativeLayout) rootView.findViewById(R.id.inviteExcRL);
        mysteryRL = (RelativeLayout) rootView.findViewById(R.id.mysteryMoneyRL);
        early1rl = (RelativeLayout) rootView.findViewById(R.id.earlyAccessRL);
        early2rl = (RelativeLayout) rootView.findViewById(R.id.earlyAccessRayaRL);
        leveluprl = (RelativeLayout) rootView.findViewById(R.id.levelUpRL);

        bottomLL = (LinearLayout) rootView.findViewById(R.id.bottomLayout);
        LowLLBronze = (LinearLayout) rootView.findViewById(R.id.lowerLLForBronze);
        LowLLSilver = (LinearLayout) rootView.findViewById(R.id.lowerLLForSilver);


        mysteryLL = (LinearLayout) rootView.findViewById(R.id.mmLL);
        inLL = (LinearLayout) rootView.findViewById(R.id.inviteLL);


        dashboard = (ImageView) rootView.findViewById(R.id.dashboard_image);

        dboard3 = (ImageView) rootView.findViewById(R.id.dashboard_image3);
        dboard2 = (ImageView) rootView.findViewById(R.id.dashboard_image2);


        scrollView = (ScrollView) rootView.findViewById(R.id.scroll);

        getLoyalty();




        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scrollView.fullScroll(ScrollView.FOCUS_UP);

                benefitLy.setVisibility(View.INVISIBLE);
                detailLayout.setVisibility(View.VISIBLE);

                benefitBtn.setBackgroundColor(Color.WHITE);
                benefitBtn.setTextColor(Color.BLACK);

                detailsBtn.setBackgroundColor(Color.BLACK);
                detailsBtn.setTextColor(Color.WHITE);

//                int i = Math.round(finalVal);


                ValueAnimator valueAnimator = ValueAnimator.ofInt(0, Integer.parseInt(getPercentage));
                valueAnimator.setDuration(1500);

                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {


                        textProgress.setText("" + valueAnimator.getAnimatedValue().toString() + "%");

                    }
                });
                valueAnimator.start();

                int animationDuration = 1500; // 2500ms = 2,5s
                progressBar.setProgressWithAnimation(Integer.parseInt(getPercentage), animationDuration); // Default duration = 1500ms


            }
        });

        benefitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scrollView.fullScroll(ScrollView.FOCUS_UP);

                benefitLy.setVisibility(View.VISIBLE);
                detailLayout.setVisibility(View.INVISIBLE);

                benefitBtn.setBackgroundColor(Color.BLACK);
                benefitBtn.setTextColor(Color.WHITE);

                detailsBtn.setBackgroundColor(Color.WHITE);
                detailsBtn.setTextColor(Color.BLACK);

                int animationDuration = 1500; // 2500ms = 2,5s
                progressBar.setProgressWithAnimation(0, animationDuration); // Default duration = 1500ms
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
//                SharedPreferences.Editor editor = pref.edit();
//
//                editor.putString("loyalty_id", "0");
//
//                editor.apply();

                Fragment fragment = new LoyaltyMainPageFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        return rootView;

    }

    @Override
    public void onTaskComplete(JSONObject result) {

        System.out.println("dapatttt " + result);

        if(result!=null){

            try {

                if (result.getBoolean("status")) {

                    //                    list.clear();
                    if (result.getString("action").equals("Customers_loyalty")) {

                        JSONObject data = result.getJSONObject("data");

                        netAmount = data.getString("spendmore");
                        getTier = data.getString("tier");
                        getPercentage = data.getString("percentage");


                        spendlbl.setText("Spend RM " + netAmount + " to level up");

                    }
                }


                if(getTier.equals("1"))
                {

                    towardsLbl.setText("TOWARDS SILVER");

                    dashboard.setImageResource(R.drawable.dashboard_bronze_1);

                    Spannable WordtoSpan = new SpannableString("WELCOME TO BRONZE");
                    WordtoSpan.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.bronze)), 11,17,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    fourLblBenefit.setText(WordtoSpan);

                    inviteRL.setVisibility(View.GONE);
                    mysteryRL.setVisibility(View.GONE);

                    inLL.setVisibility(View.GONE);
                    mysteryLL.setVisibility(View.GONE);

                    LowLLSilver.setVisibility(View.GONE);

                    LowLLBronze.setVisibility(View.VISIBLE);

                    sixLblBenefit.setText("Exclusive Promotions");
                    dboard3.setImageResource(R.drawable.dashboard_icon_exc);

                    fifthLblBenefit.setText("Birthday Discount");
                    dboard2.setImageResource(R.drawable.dashboard_icon_gift);


                }

               else if(getTier.equals("2"))
                {

                    towardsLbl.setText("TOWARDS GOLD");

                    dashboard.setImageResource(R.drawable.dashboard_silver_1);


                    Spannable WordtoSpan = new SpannableString("WELCOME TO SILVER");
                    WordtoSpan.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.silver)), 11,17,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    fourLblBenefit.setText(WordtoSpan);

                    early1rl.setVisibility(View.GONE);
                    early2rl.setVisibility(View.GONE);
                    leveluprl.setVisibility(View.GONE);

                    mysteryLL.setVisibility(View.GONE);
                    inLL.setVisibility(View.GONE);

                    LowLLBronze.setVisibility(View.VISIBLE);
                    LowLLSilver.setVisibility(View.VISIBLE);



                }
                else
                {

                    towardsLbl.setText("CONGRATULATIONS! You are now POPLOOK VVIP");


                    dashboard.setImageResource(R.drawable.dashboard_gold_2);

                    bottomLL.setVisibility(View.GONE);

                    LowLLSilver.setVisibility(View.VISIBLE);
                    LowLLBronze.setVisibility(View.VISIBLE);

                    Spannable WordtoSpan = new SpannableString("WELCOME TO GOLD");
                    WordtoSpan.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.gold)), 11,15,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    fourLblBenefit.setText(WordtoSpan);

                }


            }

            catch (Exception e){

            }


        }


    }

    private void getLoyalty()
    {

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0);
        String UserID = pref.getString("UserID", "");

        String SelectedShopID  = pref.getString("SelectedShopID", "1");

        String apikey =pref.getString("apikey","");

        String action="Customers/loyalty/id/"+UserID+"?shop_id="+SelectedShopID+"&api_version=desktop&apikey="+apikey;

//        String action="Customers/loyalty/id/"+UserID+"?currency=2&amount=500&api_version=desktop&apikey=PL:@KrAk!fA9RpGDcnIfDKzljGkEqW48yU4M6Y2GckgawSVbEg62FHKHBU7awnidFZ4wxVxUdcTAvkxT1GrlVhuZ1dKlqzl9zlsedD66G";


        WebServiceAccessGet callws = new WebServiceAccessGet(getActivity(), this);
        callws.execute(action);



    }

}
