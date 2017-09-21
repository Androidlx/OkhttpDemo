package com.example.lixin.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okHttpClient = MyApplication.okHttpClient();

    }
    //同步的get
    public void get(View view) {
        //request设置url
        final Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();

        new Thread() {

            @Override
            public void run() {

                //通过newCall方法将request转换成call,如果用 execute() 是同步
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        //通过response.body().string() 拿到服务器给我们的json
                        String json = response.body().string();
                        //通过response.body().byteStream 拿到服务器给我们的输入流  主要用在大文件
                        InputStream inputStream = response.body().byteStream();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,  "get成功", Toast.LENGTH_SHORT).show();
                            }
                        });


                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "get失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "get失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }.start();
    }
    //异步,所以的回调方法里面都是分线程.不能更新ui
    public void getAsync(View view) {
        //request设置url
        final Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //提示用户
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "get失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String json = response.body().string();
                    //用json解析

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "get成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    //提示用户
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "get失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
    public void post(View view) {
        //request设置url
        FormBody formBody = new FormBody.Builder()
                .add("act", "login")
                .add("username", "lixin666")
                .add("password", "123456")
                .add("client", "android")
                .build();
        final Request request = new Request.Builder()
                .url("http://169.254.60.203/mobile/index.php")
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {



                //提示用户
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求失败"+e, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    final String json = response.body().string();
                    //用json解析

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "请求成功"+json, Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    //提示用户
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "get失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void postAsync(View view) {
    }
}
