package com.soulocean.Diary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.soulocean.Diary.adapter.BeanAdapter;
import com.soulocean.Diary.database.BeanDao;
import com.soulocean.Diary.entity.Bean;

import java.util.ArrayList;

/**
 * @author soulo
 */
public class MainActivity extends AppCompatActivity {
    private BeanDao beanDao;
    private RecyclerView recyclerView;
    private BeanAdapter beanAdapter;
    private ArrayList<Bean> beanArrayList;
    private SharedPreferences shp;
    private SharedPreferences.Editor editor;
    private EditText userSnoEditText;
    private Boolean listStyle = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(1200);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String pattern = newText.trim();
                ArrayList<Bean> arrayList = beanDao.fuzzySearchByPattern(pattern);
                beanArrayList = arrayList;
                beanAdapter.setBeanList(beanArrayList);
                beanAdapter.notifyDataSetChanged();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_lock:
                userSnoEditText = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请设置密码");
                builder.setView(userSnoEditText);
                builder.setPositiveButton("确定", (dialog, which) -> {
                    if (userSnoEditText.getText().toString().trim().isEmpty()) {
                        Toast.makeText(this, "密码格式不合法", Toast.LENGTH_SHORT).show();
                    } else {
                        shp = getSharedPreferences("info", Context.MODE_PRIVATE);
                        editor = shp.edit();
                        editor.putString("password", userSnoEditText.getText().toString().trim());
                        editor.apply();
                        Toast.makeText(this, "密码设置成功", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create();
                builder.show();

                break;
            case R.id.app_bar_top:
                recyclerView.scrollToPosition(2);
                recyclerView.smoothScrollBy(0, -780);
                break;
            case R.id.app_bar_listStyle:
                if (listStyle) {
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
                    listStyle = false;
                } else {
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    listStyle = true;
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        beanDao = BeanDao.getDatabase(this);
     /*   beanDao.insert(new Bean("111","soul","0"));
        beanDao.insert(new Bean("222","soul","1"));
        beanDao.insert(new Bean("333","soul","2"));
        beanDao.insert(new Bean("444","soul","3"));*/

        FloatingActionButton addFloatButton_main = findViewById(R.id.addFloatButton_main);
        addFloatButton_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beanDao.insert(new Bean(null, null, "1"));
                beanArrayList = beanDao.listBean();
                beanAdapter.setBeanList(beanArrayList);
                beanAdapter.notifyItemInserted(beanArrayList.size() + 1);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("beanId", beanArrayList.get(beanArrayList.size() - 1).getId());
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView_main);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        beanArrayList = beanDao.listBean();
        beanAdapter = new BeanAdapter(beanArrayList);
        recyclerView.setAdapter(beanAdapter);
        beanAdapter.setOnItemClickListener(new BeanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("beanId", beanArrayList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        BeanAdapter beanAdapter = (BeanAdapter) recyclerView.getAdapter();
        assert beanAdapter != null;
        beanArrayList.clear();
        beanArrayList = beanDao.listBean();
        beanAdapter.setBeanList(beanArrayList);
        beanAdapter.notifyDataSetChanged();
        super.onResume();
    }
}