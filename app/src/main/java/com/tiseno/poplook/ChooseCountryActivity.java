package com.tiseno.poplook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tiseno.poplook.functions.FontUtil;

public class ChooseCountryActivity extends AppCompatActivity {
    TextView chooseTV;
    TextView chooseET;
    Button chooseButton,choose_continue_btn;
    String SelectedShopID="",SelectedCountryName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here

            Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(this.getResources().getColor(R.color.lightgrey));


            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

// set an exit transition
            window.setExitTransition(new Explode());
            window.setEnterTransition(new Explode());

        }
        setContentView(R.layout.activity_choose_country);
        chooseTV =  (TextView) findViewById(R.id.chooseTV);
        chooseET =  (TextView) findViewById(R.id.chooseET);
        chooseButton =  (Button) findViewById(R.id.chooseButton);
        choose_continue_btn =  (Button) findViewById(R.id.choose_continue_btn);


        chooseTV.setTypeface(FontUtil.getTypeface(this, FontUtil.FontType.AVENIR_MEDIUM_FONT));
        chooseET.setTypeface(FontUtil.getTypeface(this, FontUtil.FontType.AVENIR_MEDIUM_FONT));
        choose_continue_btn.setTypeface(FontUtil.getTypeface(this, FontUtil.FontType.AVENIR_MEDIUM_FONT));


        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ChooseCountryActivity.this, ChooseCountryActivity2.class);
                ChooseCountryActivity.this.startActivityForResult(i, 1);
                overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);


            }

        });

        chooseET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ChooseCountryActivity.this, ChooseCountryActivity2.class);
                ChooseCountryActivity.this.startActivityForResult(i, 1);
                overridePendingTransition(R.anim.fadeoutanim, R.anim.fadeinanim);


            }

        });
        choose_continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedCountryName.equals("")){
                    SharedPreferences pref = ChooseCountryActivity.this.getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("SelectedShopID", "1");
                    editor.putString("SelectedCountryIsoCode", "MY");
                    editor.putString("SelectedCountryCurrency", "RM");
                    editor.putString("SelectedCountryName", "Malaysia (RM)");
                    editor.apply();
//                    new AlertDialog.Builder(ChooseCountryActivity.this)
//                            .setTitle("Message")
//                            .setMessage("Please choose shipping country")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    Intent i = new Intent(ChooseCountryActivity.this, ChooseCountryActivity2.class);
//                                    ChooseCountryActivity.this.startActivityForResult(i, 1);
//                                    overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);
//
//                                }
//                            }).show();
                    Intent intent = new Intent(ChooseCountryActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);
                }else {
                    Intent intent = new Intent(ChooseCountryActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);

                }

            }

        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        System.out.println("Hello OnKitkat");
        if(resultCode == RESULT_OK)
        {System.out.println("Hello OnKitkat"+RESULT_OK);
            if(data != null)
            {
                SelectedShopID = data.getStringExtra("SelectedShopID");
                SelectedCountryName = data.getStringExtra("SelectedCountryName");

                chooseET.setText(SelectedCountryName);
                chooseET.setTextColor(getResources().getColor(R.color.black));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }
}
