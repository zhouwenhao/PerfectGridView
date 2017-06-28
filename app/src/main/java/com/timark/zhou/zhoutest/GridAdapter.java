package com.timark.zhou.zhoutest;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZHOU on 2017/6/27.
 */

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mLimitedList;
    private GridView mGv;
    private int gvWidth = 0;

    List<Integer> heights = new ArrayList<>(0);

    public GridAdapter(Context context, List<String> mLimitedList, GridView gv) {
        this.mContext = context;
        this.mLimitedList = mLimitedList == null ? new ArrayList<String>(0) : mLimitedList;
        this.mGv = gv;

        gvWidth = 500;
    }

    @Override
    public int getCount() {
        return mLimitedList.size();
    }

    @Override
    public Object getItem(int position) {
        return mLimitedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_limited_list, parent, false);
        }
        ViewHolder holder = ViewHolder.getViewHolder(convertView);

        /**
         * 此段用于在绘制之后修改gv的高度
         */
        final TextView tv = holder.mLimitedTextView;
        final ViewTreeObserver vo = holder.mLimitedTextView.getViewTreeObserver();
        vo.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Log.i("===getview===", "  tv.heigh=" + tv.getHeight());
                int line = ((int)(Math.ceil((getCount() * 1.0f) / mGv.getNumColumns())));
                int gvHeight = 0;
                for (int h : heights){
                    gvHeight += h;
                }
                if (heights.size() >= line &&  mGv.getHeight() != gvHeight){
                    ViewGroup.LayoutParams params = mGv.getLayoutParams();
                    params.height = gvHeight;
                    mGv.setLayoutParams(params);
                }
                return true;
            }
        });

        if (heights.size() == 0) {
            TextView tmptv = holder.mLimitedTextView;
            /**
             * API要求小于16的，请使用此段代码
             */
            if (Build.VERSION.SDK_INT < 16) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tmptv.getLayoutParams();
                int columWidthNum = mGv.getNumColumns() > 1 ? (mGv.getNumColumns() - 1) : 0;
                int tvWidth = ((gvWidth - columWidthNum * getGvHorizontalSpacing(mGv) - mGv.getPaddingLeft()
                        - mGv.getPaddingRight()) / mGv.getNumColumns() - params.leftMargin - params.rightMargin);
                tmptv.setWidth(tvWidth);
                Log.i("===getview===", "tvwidth= " + tvWidth);
            }else {
                /**
                 * API要求大于等于16的，请使用此段代码
                 */
                tmptv.setWidth(mGv.getColumnWidth());
                Log.i("===getview===", "columeWidth= " + mGv.getColumnWidth());
            }

            int line = ((int)(Math.ceil((getCount() * 1.0f) / mGv.getNumColumns())));
            for (int i = 0; i < line; i++) {
                int tmpHeight = 0;
                String str = "";
                //获取一行中数据最长一个字符串
                for (int j = 0; j < mGv.getNumColumns() && (i * mGv.getNumColumns() + j) < getCount(); j++) {
                    String tmpStr = mLimitedList.get(i*mGv.getNumColumns() + j);
                    if (tmpStr.length() > str.length()){
                        str = tmpStr;
                    }
                }
                //手动获取计算后的高度
                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                tmptv.setText(str);
                tmptv.measure(w, h);
                int heightxx = tmptv.getMeasuredHeight();
//                int width = tmptv.getMeasuredWidth();
                Log.i("===getview===", "heightxx= " + heightxx);
//                Log.i("===getview===", "width= " + width);
                if (heightxx > tmpHeight) {
                    tmpHeight = heightxx;
                }

                if (heights.size() <= i){
                    heights.add(tmpHeight);
                }else {
                    heights.set(i, tmpHeight);
                }
            }
        }

        if (mGv.getHeight() != 0){
            holder.mLimitedTextView.setHeight(heights.get(position/mGv.getNumColumns()));
        }
        Log.i("===getview===", "position=" + position + "  GV.heigh=" + mGv.getHeight());

        holder.mLimitedTextView.setText(mLimitedList.get(position));
        return convertView;
    }

    private static class ViewHolder {
        private TextView mLimitedTextView;

        private ViewHolder(View convertView) {
            mLimitedTextView = (TextView) convertView.findViewById(R.id.item_shop_limited_textView);
        }

        public static ViewHolder getViewHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

    private int getGvHorizontalSpacing(GridView gv){
        int horizontalSpace = 0;

        if (gv != null){
            //得到类对象
            Class gvClass = (Class) gv.getClass();
            /**
            * 得到类中的所有属性集合
            */
            Field[] fs = gvClass.getDeclaredFields();
            for (int i = 0; i < fs.length; i++){
                Field f = fs[i];
                f.setAccessible(true); //设置属性可访问
                try {
                    Object val = f.get(gv);
                    Log.i("===getview===", "name=" + f.getName() + "  val=" + val);
                    if (f.getName().equals("mHorizontalSpacing")){                       //切记切记，该属性名不能被混淆，否则是取不到该属性的
                        horizontalSpace = (int)val;
                        break;
                    }
                }catch (Exception e){
                    Log.i("===getview===", "exception = " + e.getMessage());
                }
            }
        }

        return horizontalSpace;
    }

}

