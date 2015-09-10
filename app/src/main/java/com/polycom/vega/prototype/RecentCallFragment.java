package com.polycom.vega.prototype;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;

import java.util.ArrayList;

/**
 * Created by Michael on 9/10/2015.
 */
public class RecentCallFragment extends Fragment implements Thread.UncaughtExceptionHandler, IActivity, IDataBind, AdapterView.OnItemClickListener {
    private View fragment;
    private Context context;
    private ListView listView;
    private ArrayList<String> calls;
    private RecentCallAdapter recentCallAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_recentcall, container, false);
        context = fragment.getContext();

        initComponent();
        initComponentState();
        initAnimation();
        dataBind();

        return fragment;
    }

    @Override
    public void initComponent() {
        listView = (ListView) fragment.findViewById(R.id.fragment_recentcall_listView);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

    }

    @Override
    public void dataBind() {
        calls = new ArrayList<String>();
        calls.add("172.21.97.153");
        calls.add("172.21.97.151");
        calls.add("172.21.97.190");

        recentCallAdapter = new RecentCallAdapter(context, calls);

        listView.setAdapter(recentCallAdapter);
        listView.setOnItemClickListener(this);
    }
}
