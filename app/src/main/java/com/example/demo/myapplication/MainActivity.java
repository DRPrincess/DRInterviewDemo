package com.example.demo.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.demo.myapplication.bean.DataBean;
import com.example.demo.myapplication.bean.MemberBean;
import com.example.demo.myapplication.utils.AssetsHelper;
import com.example.demo.myapplication.utils.DividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    MemberAdapter mMemberAdapter;

    List<MemberBean> mMemberBeanList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();

        getMembers();

    }


    private void initView(){
        mRecyclerView = findViewById(R.id.recyclerView);

        //设施布局适配器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                MainActivity.this, DividerItemDecoration.VERTICAL_LIST));

    }


    /**
     * 显示数据
     * @param memberBeans
     */
    private void showMembers(final List<MemberBean> memberBeans){

        if(mMemberBeanList == null){
            mMemberBeanList = new ArrayList<>() ;
        }
        mMemberBeanList.addAll(memberBeans);
        mMemberAdapter = new MemberAdapter(MainActivity.this,mMemberBeanList);
        mMemberAdapter.setOnItemClickLitener(new MemberAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {


                MemberBean bean = memberBeans.get(position);

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("条目：")
                        .append(position)
                        .append("姓名:")
                        .append(bean.username)
                        .append("电话号码:")
                        .append(bean.userphone);

                Toast.makeText(MainActivity.this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mRecyclerView.setAdapter(mMemberAdapter);
    }


    /**
     * 获取数据
     */
    private  void getMembers(){

        String fileName = "data";
        Observable.just(fileName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(new Function<String, DataBean>() {
                    @Override
                    public DataBean apply(String s) throws Exception {


                        DataBean dataBean = getDataForJsonFile(MainActivity.this,s,DataBean.class);
                        return dataBean;
                    }
                })
                .map(new Function<DataBean, List<MemberBean>>() {
                    @Override
                    public List<MemberBean> apply(DataBean dataBean) throws Exception {
                        return dataBean.users;
                    }
                })
                .subscribe(new Observer<List<MemberBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<MemberBean> memberBeans) {


                        showMembers(memberBeans);
                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "数据获取错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    /**
     * 解析本地 Assents内的 Json 文件,转换为实体类格式
     *
     * @param context
     * @param fileName
     * @return
     */
    private <T> T getDataForJsonFile(Context context, String fileName, Class<T> cls) {
        InputStream inputStream = AssetsHelper.getInstance().openAssets(context, fileName);
        if (inputStream == null) {
            return null;
        }
        T entry = null;
        JsonReader jsonReader = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            jsonReader = new JsonReader(inputStreamReader);
            entry = new Gson().fromJson(jsonReader, cls);
        } finally {
            if (jsonReader != null) {
                try {
                    jsonReader.close();
                } catch (IOException e) {
                    throw Exceptions.propagate(e);
                }
            }
        }
        return entry;
    }

}
