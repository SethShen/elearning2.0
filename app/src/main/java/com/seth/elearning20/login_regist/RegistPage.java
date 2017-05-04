package com.seth.elearning20.login_regist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seth.elearning20.Login;
import com.seth.elearning20.R;

/**
 * Created by Seth on 2017/4/29.
 */

public class RegistPage extends Fragment implements View.OnClickListener{
    private Button checkBtn;
    private EditText usrPhone;
    private EditText checkNum;
    private TextView sendMsg;
    private SmsSend smsSend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.regist_fragment, container, false);

        FindViewById(view);
        smsSend = new SmsSend(getActivity(), sendMsg);
        smsSend.InitSms();

        checkNum.addTextChangedListener(new EditChangedListener());
        return view;

    }
    public class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkBtn.setEnabled(true);
            checkBtn.setBackgroundResource(R.drawable.regist_btn2);
            if(s.length() == 0){
                checkBtn.setEnabled(false);
                checkBtn.setBackgroundResource(R.drawable.regist_btn1);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_msg:
                String phone = usrPhone.getText().toString().trim().replaceAll("/s","");
                smsSend.sendMsg(phone);
                break;
            case R.id.new_regist:
                //获得用户输入的验证码
                String code = checkNum.getText().toString().replaceAll("/s","");
               // if(smsSend.CheckInput(code)){
               if(true){        //跳过短信
                    Fragment fragment = new CompleteInfoPage();
                    //Fragment fragment1 = Login.getContentFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.activity_login,fragment);
                    //transaction.hide(fragment1).add(R.id.activity_login, fragment);
                    Login.setContentFragment(fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                break;
        }
    }

    private void FindViewById(View view) {
        checkBtn = (Button) view.findViewById(R.id.new_regist);
        usrPhone = (EditText) view.findViewById(R.id.phone_num);
        checkNum = (EditText) view.findViewById(R.id.check_num);
        sendMsg = (TextView) view.findViewById(R.id.send_msg);

        sendMsg.setOnClickListener(this);
        checkBtn.setOnClickListener(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        smsSend.releaseSmS();
    }
}
