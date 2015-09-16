package com.polycom.vega.prototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;
import com.polycom.vega.fundamental.VegaApplication;

/**
 * Created by xifwang on 9/16/2015.
 */
public class LocalGsControlFragment extends Fragment implements IActivity, IDataBind, Thread.UncaughtExceptionHandler {

    private View fragment;

    private Button muteButton;
    private Button cameraButton;
    private SeekBar volSeekBar;
    private boolean isEndpointMuted;
    private boolean isCameraOff;
    private int progressVolume;


    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragment = inflater.inflate(R.layout.fragment_local_gs_control, container, false);
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

    @Override
    public void dataBind() {

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

    }
}
