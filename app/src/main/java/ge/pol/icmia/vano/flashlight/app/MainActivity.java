package ge.pol.icmia.vano.flashlight.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.security.Policy;


public class MainActivity extends ActionBarActivity {
    private Context context = null;
    private static boolean turnedOn = false;
    private Camera camera = null;
    private Button lightButton = null;
    private SurfaceHolder surfaceHolder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        context = getApplicationContext();
        SurfaceView surfaceView = new SurfaceView(context);
        if(surfaceView != null) {
            surfaceHolder = surfaceView.getHolder();
//            if(surfaceHolder != null) {
//                surfaceHolder.addCallback(this);
//            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public void toggle(View view){
        if(lightButton == null){
            lightButton = (Button) findViewById(R.id.light_button);
        }
        if(!isFlashLightAvailable()) {
            alert("Flash light is not available");
            return;
        }
        lightButton.setText("turn off");

        if(turnedOn){
            turnOff();
        }else {
            turnOn();
        }
    }

    private void turnOn(){
        try{
            camera = Camera.open();
        }catch (Exception e){
            alert("Camera is not accessible!");
            return;
        }

        Camera.Parameters p = camera.getParameters();
        System.out.println(p.getSupportedFlashModes());
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

        camera.setParameters(p);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        turnedOn = true;
        lightButton.setText("turn off");
        alert("light on");
    }
    private void turnOff(){
        camera.stopPreview();
        camera.release();
        turnedOn = false;
        lightButton.setText("turn on");
        alert("light off");
    }
    private boolean isFlashLightAvailable(){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
    private void alert(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        System.out.println(msg);
    }
}
