# OkHttp

>OkHttp现在已经得到Google官方认可，大量的app都采用OkHttp做网络请求。

##Gradle引入
compile compile 'com.squareup.okhttp3:okhttp:3.6.0'
    compile 'com.github.franmontiel:PersistentCookieJar:v0.9.3'
##Jar
Download [the latest okhttp JAR][1]
##上传单文件的php代码
Download [php file of php][2]

Maven:

###GitHub（square）
https://github.com/square/okhttp

>本文所有前后端联调数据都是基于PHP的本地后台数据

>本文用到一个第三方库是鸿洋大神封装的类库

##Code
```
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

php-code:
$data = $_REQUEST;
print_r(json_encode($data));
```
```
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
php-code:
$name = $_REQUEST['name'];
$age = $_REQUEST['age'];
$arr = array('name'=>$name,'age'=>$age,'sex'=>'男');
print_r(json_encode($arr));
```
```
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
                        mTv.setText("onResponse:" + response.username+"   "+response.password);
                    }
                });
    }

php-code:
$username = $_REQUEST['username'];
$password = $_REQUEST['password'];
$arr = array('username'=>$username,'password'=>$password);
print_r(json_encode($arr));
```
```
//post传集合
    public void getUsers(View view)
    {
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
php-code:
// $username = $_REQUEST['name'];
// $password = $_REQUEST['pwd'];
// $arr = array('username'=>$username,'password'=>$password);
// print_r(json_encode($arr));
$params = $_REQUEST;
print_r(json_encode($params));
```
```
//https
    public void getHttpsHtml(View view)
    {
        String url = "https://kyfw.12306.cn/otn/";
        OkHttpUtils
                .get()
                .url(url)
                .id(101)
                .build()
                .execute(new MyStringCallback());
    }
```
```
 //请求图片得到bitmap
    public void getImage(View view)
    {
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
                .execute(new BitmapCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        mTv.setText("onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id)
                    {
                        Log.e("TAG", "onResponse：complete");
                        imageView.setImageBitmap(bitmap);
                    }
                });
    }
```
```
//上传图片
    public void uploadFile(View view)
    {
        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.jpg");
        if (!file.exists())
        {
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

        OkHttpUtils.post()//
                .addFile("mFile", "messenger_01.jpg", file)//
                .url(url)
                .params(params)
                .headers(headers)
                .build()
                .execute(new MyStringCallback());
    }

php-code:
$files = $_FILES['mFile'];
print_r(json_encode($files));

php：
将文件传到服务，再返给本地

本地会接收到图片的name，size等信息

```
```
//多文件上传
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
                .addFile("mFile", "messenger_01.jpg", file)//
                .addFile("mFile", "test1.txt", file2)//
                .url(url)
                .params(params)//
                .build()//
                .execute(new MyStringCallback());
    }
```
```
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
```








[1]:https://repo1.maven.org/maven2/com/squareup/okhttp/okhttp/2.7.5/okhttp-2.7.5.jar
[2]:https://github.com/zhangliwen1101/okhttp/blob/master/php文件上传代码/fileupload.php
