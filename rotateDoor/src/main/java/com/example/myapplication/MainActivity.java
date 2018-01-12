package com.example.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/*public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}*/
public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    private final String TAG="Test3DRotateActivity";
    private ImageView image;
    private Button start ,stop;
    private Rotate3dAnimation rotation;
//    private StartNextRotate startNext;
    private RelativeLayout rl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl= (RelativeLayout) findViewById(R.id.rl);
        image = (ImageView) findViewById(R.id.image);
        start=(Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        start.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                //进行360度的旋转
//                startRotationRl(0,-90);
                startRotationRl(0,90);
            }
        });

        /*stop.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.e(TAG, "onClick: "+"click it" );
                image.clearAnimation();
                rl.clearAnimation();
            }
        });*/


    }

    /***
     * method_1
     * @param start
     * @param end
     */
    private void startRotationRl(float start, float end) {
        // 计算中心点
        final float centerX = rl.getWidth() ;
        //Z轴的缩放为0
        rotation =new Rotate3dAnimation(start, end, 0, rl.getHeight()/2, 0f, true);
        rotation.setDuration(1500);
//        rotation.setFillAfter(true);
        //rotation.setInterpolator(new AccelerateInterpolator());
        //匀速旋转
        rotation.setInterpolator(new LinearInterpolator());
        //设置监听
//        startNext = new StartNextRotate();
//        rotation.setAnimationListener(startNext);
        rl.startAnimation(rotation);
    }

    /*private void startRotation(float start, float end) {
        // 计算中心点
//        final float centerX = image.getWidth() / 2.0f;
//        final float centerY = image.getHeight() / 2.0f;
        final float centerX = image.getWidth() ;
        final float centerY = image.getWidth() ;
        Log.d(TAG, "centerX="+centerX+", centerY="+centerY);
        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        //final Rotate3dAnimation rotation =new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        //Z轴的缩放为0
        rotation =new Rotate3dAnimation(start, end, centerX, centerY, 0f, true);
        rotation.setDuration(2000);
        rotation.setFillAfter(true);
        //rotation.setInterpolator(new AccelerateInterpolator());
        //匀速旋转
        rotation.setInterpolator(new LinearInterpolator());
        //设置监听
//        startNext = new StartNextRotate();
//        rotation.setAnimationListener(startNext);
        image.startAnimation(rotation);
    }*/

    /*private class StartNextRotate implements Animation.AnimationListener {

        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub
            Log.d(TAG, "onAnimationEnd......");
//            image.startAnimation(rotation);
        }

        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

    }*/
}
