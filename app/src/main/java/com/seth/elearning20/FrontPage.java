package com.seth.elearning20;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.seth.elearning20.frontfragment.FirstPage;
import com.seth.elearning20.frontfragment.MinePage;
import com.seth.elearning20.frontfragment.SearchBar;
import com.seth.elearning20.frontfragment.SecondPage;
import com.seth.elearning20.frontfragment.SpokenPage;

/**
 * Created by Seth on 2017/4/24.
 */

public class FrontPage extends FragmentActivity {
    private Fragment contentFragment;
    private Fragment firstPage;
    private Fragment minePage;
    private Fragment searchBar;
    private Fragment spokenPage;
    private Fragment listenPage;

    private FragmentManager fm;
    private RelativeLayout firstblock;
    private RelativeLayout thirdblock;
    private RelativeLayout forthblock;
    private RelativeLayout secondblock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_front);

        /*底部tab标签*/
        firstblock = (RelativeLayout) findViewById(R.id.recommond);
        forthblock = (RelativeLayout) findViewById(R.id.mine);
        thirdblock = (RelativeLayout) findViewById(R.id.spoken);
        secondblock = (RelativeLayout) findViewById(R.id.listen);

        /*各fragment注册*/
        fm = getSupportFragmentManager();
        firstPage = fm.findFragmentById(R.id.page_content);
        searchBar = fm.findFragmentById(R.id.page_title);
        minePage = fm.findFragmentById(R.id.page_content);
        spokenPage = fm.findFragmentById(R.id.page_content);
        listenPage = fm.findFragmentById(R.id.page_content);

        if(firstPage == null ){
            firstPage = new FirstPage();
            fm.beginTransaction().add(R.id.page_content, firstPage).commit();
            contentFragment = firstPage;
        }

        if (searchBar == null){
            searchBar = new SearchBar();
            fm.beginTransaction().add(R.id.page_title, searchBar).commit();
        }


    }
    //fragment替换方法
    private void addOrShowFragment(FragmentTransaction transaction, Fragment fragment){
        if(contentFragment == fragment){
            return ;
        }
        if(!fragment.isAdded()){    //如果当前fragment为被添加，则添加到Fragment管理器中
            transaction.hide(contentFragment).add(R.id.page_content, fragment).commit();
        }else{
            transaction.hide(contentFragment).show(fragment).commit();
        }
        contentFragment = fragment;
    }

    public void ToMinePage(View view){

        if (minePage == null){
            minePage = new MinePage();
        }
        addOrShowFragment(fm.beginTransaction(), minePage);

    }
    public void ToFirstPage(View view){

        if (firstPage == null){
            firstPage = new FirstPage();
        }
        addOrShowFragment(fm.beginTransaction(), firstPage);

    }

    public void ToThirdPage(View view){
        if(spokenPage == null){
            spokenPage = new SpokenPage();
        }
        addOrShowFragment(fm.beginTransaction(), spokenPage);
    }
    public void ToSecondPage(View view){
        if(listenPage == null){
            listenPage = new SecondPage();
        }
        addOrShowFragment(fm.beginTransaction(), listenPage);
    }
}
