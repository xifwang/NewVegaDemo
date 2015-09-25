package com.polycom.vega.prototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.fundamental.VegaFragment;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;

import static android.widget.ImageView.*;

/**
 * Created by xifwang on 9/16/2015.
 */
public class LocalGsControlFragment extends VegaFragment implements IActivity, IDataBind, Thread.UncaughtExceptionHandler {
    private Button muteButton;
    private Button cameraButton;
    private SeekBar volSeekBar;
    private ImageView cameraImageView;
    private boolean isEndpointMuted;
    private boolean isCameraOff;
    private int progressVolume;
    private float mPosX;
    private float mPosY;
    private float mCurPosX;
    private float mCurPosY;
    private float mFingerDistance;
    private float mCurFingerDistance;
    private boolean is2FingerPressed;
    private boolean isGestureTriggered;
    private CookieManager cookieManager;

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_local_gs_control, container, false);
        context = fragment.getContext();
        application = (VegaApplication) getActivity().getApplication();
        fragmentManager = getActivity().getSupportFragmentManager();
        cookieManager = new CookieManager();


        try {
            initComponent();
            initComponentState();
            initAnimation();
            dataBind();
        } catch (Exception ex) {
            Toast.makeText(getView().getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return fragment;
    }

    @Override
    public void initComponent() {
        //init the button status
        checkAudioMuteStatus();
        checkCameraOffStatus();
        checkVolumeStatus();

        muteButton = (Button) fragment.findViewById(R.id.fragment_local_gs_control_mutebutton);
        muteButton.setOnClickListener(muteButton_OnClickListener);

        cameraButton = (Button) fragment.findViewById(R.id.fragment_local_gs_control_camerabutton);
        cameraButton.setOnClickListener(careraButton_OnClickListener);

        volSeekBar = (SeekBar) fragment.findViewById(R.id.fragment_local_gs_control_volumebutton);
        volSeekBar.setOnSeekBarChangeListener(volume_ChangeListener);

        cameraImageView = (ImageView) fragment.findViewById(R.id.fragment_local_gs_control_cameraimageview);
        cameraImageView.setOnTouchListener(cameraView_OnTouchListener);
        mPosX = mPosY = mCurPosX = mCurPosY = mFingerDistance = mCurFingerDistance = 0;
        is2FingerPressed = isGestureTriggered = false;
    }

    private View.OnClickListener muteButton_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            muteAudio();
        }
    };

    private View.OnClickListener careraButton_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            disableCamera();
        }
    };

    private SeekBar.OnSeekBarChangeListener volume_ChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateVolumeSeekBar(progress);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    };

    private OnTouchListener cameraView_OnTouchListener = new OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    mPosX = event.getX();
                    mPosY = event.getY();
                    mFingerDistance = 0;
                    is2FingerPressed = false;
                    isGestureTriggered = false;
                    break;
                case MotionEvent.ACTION_UP:
                    //stop the camera control
                    controlCamera(6);
                    //Toast.makeText(getActivity().getApplicationContext(), "stop stop stop", Toast.LENGTH_SHORT).show();
                    mPosX = mPosY = mCurPosX = mCurPosY = mFingerDistance = mCurFingerDistance = 0;
                    is2FingerPressed = false;
                    isGestureTriggered = false;
                    break;

                case MotionEvent.ACTION_MOVE:

                    mCurPosX = event.getX();
                    mCurPosY = event.getY();

                    if(isGestureTriggered){
                        return true;
                    }

                    //if there are 2 fingers, actions are zoom in/out
                    if (event.getPointerCount() == 2){
                        is2FingerPressed = true;
                        float x = event.getX(0) - event.getX(1);
                        float y = event.getY(0) - event.getY(1);
                        float distance = (float) Math.sqrt(x * x + y * y);
                        //calculate the finger distance when the first time we press down
                        if(mFingerDistance == 0){
                            mFingerDistance = distance;
                        }
                        else{
                            if (distance - mFingerDistance >= 50){
                                //zoom in
                                isGestureTriggered = true;
                                controlCamera(4);
                                //Toast.makeText(getActivity().getApplicationContext(), "ZOOM IN IN IN", Toast.LENGTH_SHORT).show();
                            }
                            else if( distance - mFingerDistance <= -50){
                                //zoom out
                                isGestureTriggered = true;
                                controlCamera(5);
                                //Toast.makeText(getActivity().getApplicationContext(), "ZOOM OUT OUT OUT", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    //1 finger, up/down/left/right action
                    else if(!is2FingerPressed){
                        //Up and down
                        if((Math.abs(mCurPosY - mPosY) > Math.abs(mCurPosX - mPosX))
                                && (Math.abs(mCurPosY - mPosY) > 200)) {
                            if (mCurPosY - mPosY > 0){
                                //do something
                                isGestureTriggered = true;
                                controlCamera(2);
                                //Toast.makeText(getActivity().getApplicationContext(), "DOWN DOWN DOWN", Toast.LENGTH_SHORT).show();
                            }else{
                                //do something
                                isGestureTriggered = true;
                                controlCamera(0);
                                //Toast.makeText(getActivity().getApplicationContext(), "UP UP UP", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //left and right
                        else if((Math.abs(mCurPosX - mPosX) > Math.abs(mCurPosY - mPosY))
                                && (Math.abs(mCurPosX - mPosX) > 200)){
                            if (mCurPosX - mPosX > 0){
                                //do something
                                isGestureTriggered = true;
                                controlCamera(1);
                                //Toast.makeText(getActivity().getApplicationContext(), "RIGHT RIGHT RIGHT", Toast.LENGTH_SHORT).show();
                            }

                            else{
                                //do something
                                isGestureTriggered = true;
                                controlCamera(3);
                                //Toast.makeText(getActivity().getApplicationContext(), "LEFT LEFT LEFT", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                    break;
            }
            return true;
        }
    };

    @Override
    public void initComponentState() {

    }

    @Override
    public void initAnimation() {

    }

    @Override
    public void registerNotification() {

    }

    private void muteAudio() {

        //send REST to mute audio
        String url = ((VegaApplication) getActivity().getApplicationContext()).getServerUrl() + "/rest/audio/muted";

        try {

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //// TODO: 9/10/2015
                    isEndpointMuted = !isEndpointMuted;
                    updateMuteBottomImage(isEndpointMuted);
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            };
            //Request.Method.PUT
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, responseListener, errorListener) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    String isEPMutedStr = isEndpointMuted ? "false" : "true";

                    return isEPMutedStr.getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };


            CookieHandler.setDefault(cookieManager);

            Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkAudioMuteStatus() {

        //send REST to check the mute state
        String url = ((VegaApplication) getActivity().getApplicationContext()).getServerUrl() + "/rest/audio/muted";

        try {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("false"))
                        isEndpointMuted = false;
                    else
                        isEndpointMuted = true;
                    updateMuteBottomImage(isEndpointMuted);
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            };

            StringRequest stringRequest = new StringRequest(url, responseListener, errorListener);

            CookieHandler.setDefault(cookieManager);
            Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMuteBottomImage(boolean isEndpointMuted) {
        if (isEndpointMuted) {
            muteButton.setBackgroundResource(R.drawable.icon_mic_off);
            muteButton.setText(R.string.mute);
        } else {
            muteButton.setBackgroundResource(R.drawable.icon_mic);
            muteButton.setText(R.string.unmute);
        }
    }

    private void disableCamera() {

        //send REST to mute audio
        String url = ((VegaApplication) getActivity().getApplicationContext()).getServerUrl() + "/rest/video/local/mute";

        try {

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //// TODO: 9/10/2015
                    isCameraOff = !isCameraOff;
                    updateCameraBottomImage(isCameraOff);
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            };
            //Request.Method.PUT
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, responseListener, errorListener) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    String isCameraOffStr = isCameraOff ? "false" : "true";

                    return isCameraOffStr.getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };

            CookieHandler.setDefault(cookieManager);
            Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkCameraOffStatus() {

        //send REST to check the mute state
        String url = ((VegaApplication) getActivity().getApplicationContext()).getServerUrl() + "/rest/video/local/mute";

        try {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("false"))
                        isCameraOff = false;
                    else
                        isCameraOff = true;
                    updateCameraBottomImage(isCameraOff);
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            };

            StringRequest stringRequest = new StringRequest(url, responseListener, errorListener);

            CookieHandler.setDefault(cookieManager);

            Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkVolumeStatus() {

        //send REST to check the mute state
        String url = ((VegaApplication) getActivity().getApplicationContext()).getServerUrl() + "/rest/audio/volume";

        try {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressVolume = Integer.parseInt(response);
                    volSeekBar.setProgress(progressVolume);
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            };

            StringRequest stringRequest = new StringRequest(url, responseListener, errorListener);

            CookieHandler.setDefault(cookieManager);
            Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateVolumeSeekBar(final int currentVolume) {
        //send REST to mute audio
        String url = ((VegaApplication) getActivity().getApplicationContext()).getServerUrl() + "/rest/audio/volume";

        try {

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity().getApplicationContext(), error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                }
            };
            //Request.Method.PUT
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, responseListener, errorListener) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    String volProgress = Integer.toString(currentVolume);
                    return volProgress.getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };

            CookieHandler.setDefault(cookieManager);
            Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
            progressVolume = currentVolume;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCameraBottomImage(boolean isCameraOff) {
        if (isCameraOff) {
            cameraButton.setBackgroundResource(R.drawable.icon_video_off);
            cameraButton.setText(R.string.cameraoff);
        } else {
            cameraButton.setBackgroundResource(R.drawable.icon_video);
            cameraButton.setText(R.string.cameraon);
        }
    }


    private void controlCamera(int direction) {

        //send REST to mute audio
        String url = ((VegaApplication) getActivity().getApplicationContext()).getServerUrl() + "/rest/cameras/near/selectedpeople";
        String directionString[] = {"up","right","down","left","zoom in","zoom out"};


        try {
            JSONObject json;
            if(direction < 6) {
                json = new JSONObject("{\"action\":\"moveStart\",\"direction\":\"" + directionString[direction] + "\"}");
            }
            else {
                json = new JSONObject("{\"action\":\"moveStop\"}");
            }

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject  response) {

                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            };
            //Request.Method.POST
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(url, json, responseListener, errorListener);
            CookieHandler.setDefault(cookieManager);

            Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonArrayRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void dataBind() {

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

    }
}
