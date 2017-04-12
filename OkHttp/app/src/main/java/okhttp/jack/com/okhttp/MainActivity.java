package okhttp.jack.com.okhttp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.GenericsCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView mTv;
    private ImageView imageView;
    private Bean bean;
    private OkHttpClient mOkHttpClient;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.imageView);
        mProgressBar = (ProgressBar) findViewById(R.id.id_progress);
        mProgressBar.setMax(100);
        downloadFile(mTv);
    }

    //get传集合
    public void getHtmlMap(View view) {
        String url = "";
        url = "http://127.0.0.1/mytest.php";
        Map<String, String> data = new HashMap<>();
        data.put("name", "张四");
        data.put("age", "30");
        data.put("sex", "男");
        OkHttpUtils
                .get()
                .url(url)
                .params(data)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    //get传参
    public void getHtmlData(View view) {
        String url = "";
        url = "http://127.0.0.1/mytest.php";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("name", "张力文")
                .addParams("age", "25")
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    //post传参
    public void getUser(View view) {
        String url = "http://127.0.0.1/mytest.php";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", "zhangliwen")
                .addParams("password", "123456")
                .build()
                .execute(new GenericsCallback<User>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mTv.setText("onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(User response, int id) {
                        mTv.setText("onResponse:" + response.username + "   " + response.password);
                    }
                });
    }

    //post传集合
    public void getUsers(View view) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", "张力文");
        params.put("pwd", "123456");
        String url = "http://127.0.0.1/mytest.php";
        OkHttpUtils
                .post()
                .url(url)
                .params(params)
                .build()
                .execute(new MyStringCallback());
    }

    //https
    public void getHttpsHtml(View view) {
        String url = "https://kyfw.12306.cn/otn/";
        OkHttpUtils
                .get()
                .url(url)
                .id(101)
                .build()
                .execute(new MyStringCallback());
    }

    //请求图片得到bitmap
    public void getImage(View view) {
        mTv.setText("");
        String url = "http://images.csdn.net/20150817/1.jpg";
        OkHttpUtils
                .get()//
                .url(url)//
                .tag(this)//
                .build()//
                .connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mTv.setText("onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id) {
                        Log.e("TAG", "onResponse：complete");
                        imageView.setImageBitmap(bitmap);
                    }
                });
    }

    //上传图片
    public void uploadFile(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.jpg");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "张力文");
        params.put("password", "123456");

        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");

        String url = "http://127.0.0.1/fileupload.php";

        OkHttpUtils.post()
                .addFile("mFile", "messenger_01.jpg", file)
                .url(url)
                .params(params)
                .headers(headers)
                .build()
                .execute(new MyStringCallback());
    }

    //多文件上传,现在后台的fileupload.php文件只支持单文件上传
    public void multiFileUpload(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.jpg");
        File file2 = new File(Environment.getExternalStorageDirectory(), "test1.txt");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "jpg文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!file2.exists()) {
            Toast.makeText(MainActivity.this, "txt文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "张力文");
        params.put("password", "123456");

        String url = "http://127.0.0.1/fileupload.php";
        OkHttpUtils.post()//
                .addFile("mFile", "messenger_01.jpg", file)
                .addFile("mFile", "test1.txt", file2)
                .url(url)
                .params(params)
                .build()
                .execute(new MyStringCallback());
    }
    //文件下载
    public void downloadFile(View view)
    {
        String url = "http://127.0.0.1/upload/messenger_01.jpg";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "messenger_01.jpg")//
                {

                    @Override
                    public void onBefore(Request request, int id)
                    {
                    }

                    @Override
                    public void inProgress(float progress, long total, int id)
                    {
                        mProgressBar.setProgress((int) (100 * progress));
                        Log.e(TAG, "inProgress :" + (int) (100 * progress));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        Log.e(TAG, "onError :" + e.getMessage());
                    }

                    @Override
                    public void onResponse(File file, int id)
                    {
                        Log.e(TAG, "onResponse :" + file.getAbsolutePath());
                    }
                });
    }
    public void postString(View view) {
        String url = "http://127.0.0.1/mytest.php";
        OkHttpUtils
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(new Gson().toJson(new User("zhangliwen", "123456")))
                .build()
                .execute(new MyStringCallback());
    }


    public class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            setTitle("loading...");
        }

        @Override
        public void onAfter(int id) {
            setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
            mTv.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e(TAG, "onResponse：complete");
            mTv.setText("onResponse:" + response);
            System.out.println("sdasdsadas:" + response);
            //Glide
            Glide.with(MainActivity.this)
                    .load(response)
                    .into(imageView);


            //使用Gson解析
            //            Gson gson = new Gson();
            //            bean = gson.fromJson(response,
            //                    new TypeToken<Bean>() {
            //                    }.getType());
            //            mTv.setText(bean.getName() + "   " + bean.getAge() + "   " + bean.getSex());

            switch (id) {
                case 100:
                    Toast.makeText(MainActivity.this, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(MainActivity.this, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            Log.e(TAG, "inProgress:" + progress);
            mProgressBar.setProgress((int) (100 * progress));
        }

    }

}
