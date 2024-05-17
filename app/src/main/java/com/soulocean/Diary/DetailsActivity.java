package com.soulocean.Diary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.soulocean.Diary.database.BeanDao;
import com.soulocean.Diary.entity.Bean;
import com.soulocean.Diary.util.CameraActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

/**
 * @author soulo
 */
public class DetailsActivity extends AppCompatActivity {

    private int beanId;
    private BeanDao beanDao;
    private RelativeLayout relativeLayout_details;
    private EditText details_content;
    private EditText details_title;
    private TextView details_state_time;
    private TextView details_state_textSize;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 菜单栏选中
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_details_share:
                shareText(beanDao.getBeanById(beanId).getContent());
                break;
            case R.id.app_bar_details_background:

                takePhoto();
                Toast.makeText(this, "修改背景", Toast.LENGTH_SHORT).show();
                break;
            case R.id.app_bar_details_delete:
                beanDao.deleteBeanById(beanId);
                onBackPressed();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        beanId = intent.getIntExtra("beanId", 0);
        beanDao = BeanDao.getDatabase(this);

        relativeLayout_details = findViewById(R.id.relativeLayout_details);
        relativeLayout_details.setBackground(getDrawableByUri(getImageStreamFromExternal(beanDao.getBeanById(beanId).getBackgroundImage())));
        details_title = findViewById(R.id.details_title);
        details_content = findViewById(R.id.details_content);
        details_state_time =findViewById(R.id.details_state_time);
        details_state_textSize =findViewById(R.id.details_state_textSize);
        details_title.setText(beanDao.getBeanById(beanId).getName());
        details_content.setText(beanDao.getBeanById(beanId).getContent());
        details_state_time.setText("🕰"+beanDao.getBeanById(beanId).getUpdateTime());
        //details_state_textSize.setText(beanDao.getBeanById(beanId).getContent().length());

        details_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Bean bean = beanDao.getBeanById(beanId);
                bean.setName(details_title.getText().toString().trim());
                beanDao.updateBeanByBean(bean);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        details_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Bean bean = beanDao.getBeanById(beanId);
                bean.setContent(details_content.getText().toString().trim());
                beanDao.updateBeanByBean(bean);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 从相册选择图片
     */
    public void takePhoto() {
        startActivity(new Intent(DetailsActivity.this, CameraActivity.class).putExtra(CameraActivity.ExtraType, CameraActivity.PHOTO));
    }

    private void shareText(String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "日记分享到"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获得相册、相机返回的结果，并显示
        if (CameraActivity.LISTENING) {
            Log.e("TAG", "返回的Uri结果：" + CameraActivity.IMG_URI);
            Log.e("TAG", "返回的File结果：" + CameraActivity.IMG_File.getPath());
            CameraActivity.LISTENING = false;
            Drawable drawable = getDrawableByUri(CameraActivity.IMG_URI);
            relativeLayout_details.setBackground(drawable);
            Bean bean = beanDao.getBeanById(beanId);
            bean.setBackgroundImage(CameraActivity.IMG_File.toString());
            beanDao.updateBeanByBean(bean);
        }

        details_content.setFocusable(true);
        details_content.setFocusableInTouchMode(true);
        details_content.requestFocus();
        DetailsActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private Drawable getDrawableByUri(Uri uri) {
        if (uri == null) {
            return null;
        }
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),
                    null, null);
            return new BitmapDrawable(getResources(), bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Uri getImageStreamFromExternal(String imageName) {
        if (imageName == null) {
            return null;
        }

        File picPath = new File(imageName);
        Uri uri = null;
        if (picPath.exists()) {
            uri = Uri.fromFile(picPath);
        }

        return uri;
    }
}