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
    import android.net.Uri;
    import androidx.viewpager.widget.PagerAdapter;
    import androidx.viewpager.widget.ViewPager;
    import androidx.appcompat.app.AppCompatActivity;

    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.webkit.WebView;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.ProgressBar;


    import com.nostra13.universalimageloader.core.ImageLoader;
    import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
    import com.nostra13.universalimageloader.core.assist.FailReason;
    import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
    import com.tiseno.poplook.LoginFragment;
    import com.tiseno.poplook.MainActivity;
    import com.tiseno.poplook.ProductInfoVideoFragment;
    import com.tiseno.poplook.R;

    import uk.co.senab.photoview.PhotoViewAttacher;


public class ViewPagerAdapter extends PagerAdapter {
        ImageLoader imageLoader= ImageLoader.getInstance();
        Context context;
        Uri imagesList[];
        ImageView imageView,whitebg;
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
            imageView = (ImageView)page1.findViewById(R.id.imageViewProductPager);
            ImageProgressBar = (ProgressBar) page1.findViewById(R.id.loadingImagePager);
            vidView = (WebView) page1.findViewById(R.id.videoView1);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context.getApplicationContext()));
            whitebg= (ImageView)page1.findViewById(R.id.whitebg);
            vidUrl = container.getTag().toString();


            if(position==(imagesList.length-1)){

                whitebg.setVisibility(View.GONE);
                vidView.setVisibility(View.GONE);


                if(IsVideoAvailable)
                {
                    ImageProgressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.default_video_2);

                    imageView.setOnClickListener(new View.OnClickListener() {
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

                    String imageUri = imagesList[position].toString();

                    display(imageView, imageUri, ImageProgressBar);
                    mAttacher = new PhotoViewAttacher(imageView);
                }

            }
            else {
                whitebg.setVisibility(View.GONE);
                vidView.setVisibility(View.GONE);


               String imageUri= imagesList[position].toString();

                display(imageView, imageUri, ImageProgressBar);
                mAttacher = new PhotoViewAttacher(imageView);

            }
            ((ViewPager) container).addView(page1, 0);
            return page1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            if (position == (imagesList.length - 1)) {

            } else {
                ((ViewPager) container).removeView((View) object);
            }

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

            }

        });
    }


    }


