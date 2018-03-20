package com.my.bleoclock;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FragmentAnther extends Fragment {
    @BindView(R.id.fragment_anther_set_time)
    TextView tvSetTime;
    @BindView(R.id.fragment_anther_set_date)
    TextView tvSetDate;
    @BindView(R.id.fragment_anther_select_time_zone)
    TextView tvTimeZone;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    AlertDialog timeDialog;
    AlertDialog dateDialog;


    public FragmentAnther() {
        // Required empty public constructor
    }

    public static android.support.v4.app.Fragment newInstance(String param1, String param2) {
        FragmentAnther fragment = new FragmentAnther();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initDialog(){
        //其实对话框里面很多东西都是可以自定义的
        //  1.  图标
        //  2.  整个布局
        View layoutDialogTime = getLayoutInflater().inflate(R.layout.layout_dialog_time,null);

        timeDialog=new AlertDialog.Builder(getContext()) //先得到构造器
                .setTitle(getResources().getString(R.string.set_time))
                .setView(layoutDialogTime)
                .create();
//                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                })
//                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).create();
        timeDialog.getWindow().setGravity(Gravity.BOTTOM);


//        View layoutDialogDate = getLayoutInflater().inflate(R.layout.layout_dialog_date,null);
//        dateDialog=new AlertDialog.Builder(getContext()) //先得到构造器
//                .setTitle(getResources().getString(R.string.set_time))
//                .setView(layoutDialogDate)
//                .create();
//                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                })
//                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).create();

//        dateDialog.getWindow().setGravity(Gravity.BOTTOM);

        dateDialog = new AlertDialog.Builder(getContext()).create();
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anther, container, false);
        ButterKnife.bind(this,view);
        timeDialog = new AlertDialog.Builder(getContext()).create();
        dateDialog = new AlertDialog.Builder(getContext()).create();
        return view;
    }

    @OnClick({R.id.fragment_anther_set_time,R.id.fragment_anther_set_date})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.fragment_anther_set_time:
                timeDialogShow();
                break;
            case R.id.fragment_anther_set_date:
                dataDialogShow();
                break;
            default:
                break;
        }
    }

    private void timeDialogShow(){
        timeDialog.show();
        Window window = timeDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setContentView(R.layout.layout_dialog_time);
        window.findViewById(R.id.layout_dialog_time_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog.dismiss();
            }
        });
        window.findViewById(R.id.layout_dialog_time_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog.dismiss();
            }
        });
    }

    private void dataDialogShow(){
        dateDialog.show();
        Window window = dateDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setContentView(R.layout.layout_dialog_date);
        window.findViewById(R.id.layout_dialog_date_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog.dismiss();
            }
        });
        window.findViewById(R.id.layout_dialog_date_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog.dismiss();
            }
        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
