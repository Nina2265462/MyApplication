package com.bawei.picture.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bawei.picture.R;
import com.bawei.picture.api.ApiService;
import com.bawei.picture.entity.HeadEntity;
import com.bawei.picture.net.HttpUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    private Unbinder bind;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.but1)
    Button but1;
    @BindView(R.id.but2)
    Button but2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(MainActivity.this)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .previewVideo(true)// 是否可预览视频 true or false
                        .enablePreviewAudio(true) // 是否可播放音频 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                        .enableCrop(true)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .withAspectRatio(16,9)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                        .isGif(true)// 是否显示gif图片 true or false
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                        .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code;

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    String compresspath = selectList.get(0).getCompressPath();
                    System.out.println("path==="+compresspath);

                    upload(compresspath);
                    break;
            }
        }
    }

    private void upload(String compresspath) {
        File file = new File(compresspath);
        HashMap<String,String> map = new HashMap<>();
        map.put("userid","5442");
        map.put("sessionid","15623963764715442");
        //文件转化成内存对象（请求体对象）
        RequestBody requestBody = MultipartBody.create(MediaType.parse("image/*"),file);

        MultipartBody.Part part = MultipartBody.Part.createFormData("image",file.getName(),requestBody);
        HttpUtil.getInstance().createService(ApiService.class).getHeadPic(map,part)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HeadEntity>() {
                    @Override
                    public void accept(HeadEntity headEntity) throws Exception {
                        Glide.with(MainActivity.this).load(headEntity.headPath).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(img);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
        bind = null;
    }
}
