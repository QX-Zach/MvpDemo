package com.example.ozner.mvpdemo.SwipeMenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ozner.mvpdemo.R;
import com.example.ozner.mvpdemo.UIView.SwipeListLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SwipeMenu_1Activity extends AppCompatActivity {

    private List<String> list = new ArrayList<>();
    private Set<SwipeListLayout> sets = new HashSet<>();
    ListView lv_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_menu_1);
        lv_main = (ListView) findViewById(R.id.lv_main);
        initList();
        lv_main.setAdapter(new ListAdapter());
        lv_main.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (sets.size() > 0) {
                            for (SwipeListLayout s : sets) {
                                s.setStatus(SwipeListLayout.Status.Close, true);
                                sets.remove(s);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initList() {
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        list.add("G");
        list.add("H");
        list.add("I");
        list.add("J");
        list.add("K");
        list.add("L");
        list.add("M");
        list.add("N");
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(SwipeMenu_1Activity.this).inflate(R.layout.slip_list_item, null);
            }
            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_name.setText(list.get(position));
            final SwipeListLayout sll_main = (SwipeListLayout) convertView.findViewById(R.id.sll_root);
            TextView tv_top = (TextView) convertView.findViewById(R.id.tv_top);
            TextView tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            sll_main.setOnSwipeStatusListener(new MyOnSlipStatusListener(sll_main));
            tv_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    String str = list.get(position);
                    list.remove(position);
                    list.add(0, str);
                    notifyDataSetChanged();
                }
            });
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

    class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout)) {
                    sets.remove(slipListLayout);
                }
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }
    }
}
