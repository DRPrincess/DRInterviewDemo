package com.example.demo.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.demo.myapplication.bean.MemberBean;

import java.util.List;

/**
 * 成员适配器
 *
 * @author DR
 * @date 2018/8/27
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {


    Context mContext;

    List<MemberBean>  mMemberBeans;

    private OnItemClickLitener mOnItemClickLitener;


    private SparseBooleanArray mCheckStates = new SparseBooleanArray();


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public MemberAdapter(Context context, List<MemberBean> memberBeans) {
        mContext = context;
        mMemberBeans = memberBeans;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MyViewHolder holder = null;
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_members,parent,false);
        holder = new MyViewHolder(convertView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        MemberBean memberBean = mMemberBeans.get(position);

        if(memberBean == null){
            return;
        }

        holder.tvMemberName.setText(memberBean.username);



        //防止复用布局导致Checkbox错乱
        holder.checkboxMember.setTag(position);
        holder.checkboxMember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                int pos = (int) view.getTag();
                if (isChecked) {
                    mCheckStates.put(pos, true);
                } else {
                    mCheckStates.delete(pos);
                }
            }
        });
        holder.checkboxMember.setChecked(mCheckStates.get(position, false));



        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(v, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    mOnItemClickLitener.onItemLongClick(v, position);
                    return false;
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mMemberBeans == null?0:mMemberBeans.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{


        TextView tvMemberName ;
        CheckBox checkboxMember;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
            checkboxMember = itemView.findViewById(R.id.checkboxMember);
        }
    }


    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }
}
