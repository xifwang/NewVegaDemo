package com.polycom.vega.prototype;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.polycom.vega.fundamental.IActivity;

import java.util.ArrayList;

/**
 * Created by xwcheng on 9/9/2015.
 */
public class KeypadFragment extends Fragment implements IActivity, AdapterView.OnItemClickListener, Thread.UncaughtExceptionHandler {

    private LinearLayout fragment;
    private Context context;
    private ArrayList<String> keyList;
    private KeypadAdapter keypadAdapter;
    private GridView keypadGridView;
    TextView numberTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = (LinearLayout) inflater.inflate(R.layout.fragment_keypad, container, false);
        context = fragment.getContext();

        initComponent();
        initComponentState();
        initAnimation();

        return fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                numberTextView.setText(numberTextView.getText() + "1");
                break;
            case 1:
                numberTextView.setText(numberTextView.getText() + "2");
                break;
            case 2:
                numberTextView.setText(numberTextView.getText() + "3");
                break;
            case 3:
                numberTextView.setText(numberTextView.getText() + "4");
                break;
            case 4:
                numberTextView.setText(numberTextView.getText() + "5");
                break;
            case 5:
                numberTextView.setText(numberTextView.getText() + "6");
                break;
            case 6:
                numberTextView.setText(numberTextView.getText() + "7");
                break;
            case 7:
                numberTextView.setText(numberTextView.getText() + "8");
                break;
            case 8:
                numberTextView.setText(numberTextView.getText() + "9");
                break;
            case 9:
                numberTextView.setText(numberTextView.getText() + ".");
                break;
            case 10:
                numberTextView.setText(numberTextView.getText() + "0");
                break;
            case 11:
                numberTextView.setText(numberTextView.getText() + "/");
                break;
            case 13:

                break;
            case 14:
                String number = numberTextView.getText().toString();
                if (!TextUtils.isEmpty(number)) {
                    numberTextView.setText(number.substring(0, number.length() - 1));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void initComponent() {
        numberTextView = (TextView) fragment.findViewById(R.id.fragment_keypad_numberTextView);

        keyList = new ArrayList<String>();
        keyList.add("1");
        keyList.add("2");
        keyList.add("3");
        keyList.add("4");
        keyList.add("5");
        keyList.add("6");
        keyList.add("7");
        keyList.add("8");
        keyList.add("9");
        keyList.add("dot");
        keyList.add("0");
        keyList.add("slash");
        keyList.add("");
        keyList.add("call");
        keyList.add("back");

        keypadAdapter = new KeypadAdapter(context, keyList);

        keypadGridView = (GridView) fragment.findViewById(R.id.fragment_keypad_keypadGridView);
        keypadGridView.setAdapter(keypadAdapter);
        keypadGridView.setOnItemClickListener(this);
    }

    @Override
    public void initComponentState() {

    }

    @Override
    public void initAnimation() {

    }

    @Override
    public void registerNotification() {

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

    }
}
