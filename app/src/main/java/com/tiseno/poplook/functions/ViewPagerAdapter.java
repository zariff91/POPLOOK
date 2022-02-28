package com.tiseno.poplook.functions;


/**
 * Created by rahn on 9/1/15.
 */

    import android.app.Fragment;
    import android.app.FragmentManager;
    import android.app.FragmentTransaction;
    import android.content.Context;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.Color;
    import android.net.Uri;
    import androidx.viewpager.widget.PagerAdapter;
    import androidx.viewpager.widget.ViewPager;
    import androidx.appcompat.app.AppCompatActivity;

    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.webkit.WebSettings;
    import android.webkit.WebView;
    import android.webkit.WebViewClient;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.ProgressBar;


    import com.bumptech.glide.Glide;
    import com.nostra13.universalimageloader.core.DisplayImageOptions;
    import com.nostra13.universalimageloader.core.ImageLoader;
    import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
    import com.nostra13.universalimageloader.core.assist.FailReason;
    import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
    import com.squareup.picasso.Picasso;
    import com.tiseno.poplook.LoginFragment;
    import com.tiseno.poplook.MainActivity;
    import com.tiseno.poplook.ProductInfoVideoFragment;
    import com.tiseno.poplook.R;

    import uk.co.senab.photoview.PhotoViewAttacher;


public class ViewPagerAdapter extends PagerAdapter {
        ImageLoader imageLoader= ImageLoader.getInstance();
        Context context;
        Uri imagesList[];
        WebView imageView;
        ImageView videoIcon;
        WebView vidView;
        ProgressBar ImageProgressBar;
        ImageButton playButton;
        String vidUrl;
        Boolean IsVideoAvailable;
        PhotoViewAttacher mAttacher;

    public ViewPagerAdapter(Context context,Uri[] shopImageList, boolean IsVideoAvailable) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.imagesList = shopImageList;
            this.IsVideoAvailable = IsVideoAvailable;

        }

        @Override
        public int getCount() {

            return imagesList.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

                return view == ((View) object);

        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View page1 = inflater.inflate(R.layout.view_pager_image, null);
            imageView = (WebView) page1.findViewById(R.id.imageViewProductPager);
            imageView.setBackgroundColor(Color.WHITE);

            imageView.getSettings().setSupportZoom(true);


            ImageProgressBar = (ProgressBar) page1.findViewById(R.id.loadingImagePager);
            vidView = (WebView) page1.findViewById(R.id.videoView1);
            videoIcon = (ImageView)page1.findViewById(R.id.video_image_icon);
//            imageLoader.init(ImageLoaderConfiguration.createDefault(context.getApplicationContext()));
//            whitebg= (ImageView)page1.findViewById(R.id.whitebg);
            vidUrl = container.getTag().toString();


//            whitebg.setVisibility(View.VISIBLE);
            vidView.setVisibility(View.GONE);
            if(position==(imagesList.length-1)){


                if(IsVideoAvailable)
                {
                    ImageProgressBar.setVisibility(View.GONE);
                    videoIcon.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    videoIcon.setImageResource(R.drawable.default_video_2);

                    videoIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            MainActivity activity = (MainActivity) context;


                            ProductInfoVideoFragment fragment = new ProductInfoVideoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("videoURL", vidUrl);
                            fragment.setArguments(bundle);

                            FragmentManager fragmentManager = activity.getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
//
//                            Intent i = new Intent(activity, TestHTML5WebView.class);
//                            i.putExtra("video link", vidUrl);
//                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            activity.startActivity(i);
//                            activity.overridePendingTransition(R.anim.fadeoutanim,R.anim.fadeinanim);

                        }

                    });

                }else {

                    String imageUri= imagesList[position].toString();

                    if(imageUri.contains("drawable")){

                        imageView.setVisibility(View.GONE);
                        videoIcon.setVisibility(View.VISIBLE);

                        display(videoIcon,imageUri,ImageProgressBar);

                    }
                    else {
                        imageView.setVisibility(View.VISIBLE);
                        videoIcon.setVisibility(View.GONE);
                        String html = "<html><body><img src=\"" + imageUri + "\" width=\"100%\" height=\"100%\"\"/></body></html>";
                        imageView.loadData(html, "text/html", null);
//                        imageView.loadUrl(imageUri);

                    }
                }

            }
            else {

                String imageUri= imagesList[position].toString();

                if(imageUri.contains("drawable")){
                    imageView.setVisibility(View.GONE);
                    videoIcon.setVisibility(View.VISIBLE);

                    display(videoIcon,imageUri,ImageProgressBar);
                }
                else {
                    imageView.setVisibility(View.VISIBLE);
                    videoIcon.setVisibility(View.GONE);
//                    imageView.loadUrl(imageUri);

                    String html = "<html><body><img src=\"" + imageUri + "\" width=\"100%\" height=\"100%\"\"/></body></html>";
                    imageView.loadData(html, "text/html", null);
                }

            }
            ((ViewPager) container).addView(page1, 0);
            return page1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            Log.d("main", "garbaging imageview at: " + position);
            View view = (View) object;
            ((ViewPager) container).removeView(view);
        }

    public void display(final ImageView img, String url, final ProgressBar spinner)
    {
        imageLoader.displayImage(url, img, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                img.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE); // set the spinner visible
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                img.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE); // set the spinenr visibility to gone


            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                img.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE); //  loading completed set the spinenr visibility to gone
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                spinner.setVisibility(View.GONE); //  loading completed set the spinenr visibility to gone


            }

        });
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    }


