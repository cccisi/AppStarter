package com.zhaohui.starter;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener{

    public static void putInt(String key, int value, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("identity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("identity", Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }
    /*1为局级；2为局级*/
    private final String TAG = "MainActivity";
    private final String juji_identity = new String("152722199407245053");
    private final String suoji_identity = new String("429001197806098903");
//    private final String suoji_identity = new String("152722199407245053");

    /*局级or所级*/
    private static int FLAG = 0;
    public RadioButton juJiRb, suoJiBb;
    public ImageView startZhiCha;
    private RadioGroup mRadioGroup;

    private String appId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        juJiRb = (RadioButton) findViewById(R.id.juji_rb);
        suoJiBb = (RadioButton) findViewById(R.id.suoji_rb);
        startZhiCha = (ImageView) findViewById(R.id.start_ibtn);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg);
        mRadioGroup.setOnCheckedChangeListener(this);
        startZhiCha.setOnClickListener(this);
//        juJiRb.setChecked(true);
        if(getInt("flag",this) == 1){
            juJiRb.setChecked(true);
        }else if(getInt("flag",this) == 2){
            suoJiBb.setChecked(true);
        }else {
            putInt("flag" , 0,this);
        }

        Intent intent=getIntent();
        if(intent.getFlags() == 0){
            Bundle bundle=intent.getExtras();
            String data_str = bundle.getString("data");
            Log.i(TAG, data_str);
            appId = bundle.getString("appID");
            if(appId.equals("3zhicha_oauth")){
                if(getInt("flag",this) == 1){
                    Intent data = new Intent();
                    //参数1：要调用另一个APP的activity所在的包名
                    //参数2：要调用另一个APP的activity名字
                    data.setClassName("com.ctcc.zc", "com.ctcc.zc.view.activity.InitActivity");
                    data.putExtra("result", "{\"result\": true,\"code\": \"0\",\"subjectDN\":\"CN=张博士"+juji_identity+",OU=00,OU=00,O=00,L=06,L=01,ST=11,C=CN\",\"msg\": \"认证成功\"}");
                    setResult(100, data); //设置返回数据
                    this.finish(); //关闭当前Activity
                    Log.e(TAG, "回调的局数据");
                }else if (getInt("flag",this) == 2){
                    Intent data = new Intent();
                    //参数1：要调用另一个APP的activity所在的包名
                    //参数2：要调用另一个APP的activity名字
                    data.setClassName("com.ctcc.zc", "com.ctcc.zc.view.activity.InitActivity");
                    data.putExtra("result", "{\"result\": true,\"code\": \"0\",\"subjectDN\":\"CN=张博士"+suoji_identity+",OU=00,OU=00,O=00,L=06,L=01,ST=11,C=CN\",\"msg\": \"认证成功\"}");
                    setResult(100, data); //设置返回数据
                    this.finish(); //关闭当前Activity
                    Log.e(TAG, "回调的所数据");
                }else {
                    Toast.makeText(MainActivity.this,"身份无效",Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.juji_rb:
                FLAG = 1;
                putInt("flag" , 1,this);
                Log.e(TAG, "onCheckedChanged: FLAG = " + getInt("flag",this));
                break;
            case R.id.suoji_rb:
                FLAG = 2;
                putInt("flag" , 2,this);
                Log.e(TAG, "onCheckedChanged: FLAG = " + getInt("flag",this) );
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_ibtn:
                if (getInt("flag",this) == 0){//未选择身份
                    Toast.makeText(MainActivity.this,"请选择登录身份",Toast.LENGTH_LONG).show();
                    break;
                }else {//选择局级或所级身份
                    //打开目标app，包名
//                    Toast.makeText(MainActivity.this,"点击",Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onClick: dianji");
                    /*默认启动*/
//                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.ctcc.zc");
//                    startActivity(LaunchIntent);
                    /*启动Activity*/
                    Intent intent = new Intent();
                    //第一种方式
                    ComponentName cn = new ComponentName("com.ctcc.zc", "com.ctcc.zc.view.activity.InitActivity");
                    try {
                        intent.setComponent(cn);
                        //第二种方式
                        //intent.setClassName("com.example.fm", "com.example.fm.MainFragmentActivity");
                        intent.putExtra("test", "test");
                        startActivity(intent);
                    } catch (Exception e) {
                        //TODO  可以在这里提示用户没有安装应用或找不到指定Activity，或者是做其他的操作
                    }
                    break;
                }
        }
    }

}
