package com.seth.elearning20.frontfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seth.elearning20.Login;
import com.seth.elearning20.R;
import com.seth.elearning20.sqlite.SqlDao;

/**
 * Created by Seth on 2017/4/26.
 */

public class MinePage extends Fragment {
    private TextView quit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);

        quit = (TextView) view.findViewById(R.id.quit);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SqlDao(getContext()).delUsr();
                startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish();
            }
        });
        return view;

    }
}
