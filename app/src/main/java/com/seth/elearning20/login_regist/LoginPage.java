package com.seth.elearning20.login_regist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seth.elearning20.FrontPage;
import com.seth.elearning20.Login;
import com.seth.elearning20.R;

/**
 * Created by Seth on 2017/4/29.
 */

public class LoginPage extends Fragment{
    private Button loginBtn;
    private RelativeLayout findPassword;
    private RelativeLayout newRegist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        loginBtn = (Button) view.findViewById(R.id.login_btn);
        findPassword = (RelativeLayout) view.findViewById(R.id.fetchPassword);
        newRegist = (RelativeLayout) view.findViewById(R.id.regist);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FrontPage.class);
                startActivity(intent);
            }
        });

        findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ForgetPassPage();
                //Fragment fragment1 = Login.getContentFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_login,fragment);
                //transaction.hide(fragment1).add(R.id.activity_login, fragment);
                Login.setContentFragment(fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        newRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new RegistPage();
                //Fragment fragment1 = Login.getContentFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_login,fragment);
                //transaction.hide(fragment1).add(R.id.activity_login, fragment);
                Login.setContentFragment(fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
}


