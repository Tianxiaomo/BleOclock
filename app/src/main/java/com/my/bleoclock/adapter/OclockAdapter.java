package com.my.bleoclock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.my.bleoclock.protocol.Alarm;
import com.my.bleoclock.ItemSwitchChangeListener;
import com.my.bleoclock.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by qkz on 2018/1/28.
 */

public class OclockAdapter extends RecyclerView.Adapter<OclockAdapter.ViewHolder> {


    List<Alarm> listOclockInfo;
    ClickItem mClickItem;
    ClickItem longClickItem;
    ItemSwitchChangeListener apiBackCall;
    private static Context context;
    private String[] items = new String[] {"一次","每天","工作日","周末","自定义"};
    private String[] weekItems = new String[] {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};

    public OclockAdapter(Context context,List<Alarm> listOclockInfo){
        this.context = context;
        this.listOclockInfo = listOclockInfo;
    }

    public void setClickItem(ClickItem clickItem){this.mClickItem = clickItem;}

    public void setOnLongClickListener(ClickItem clickItem){this.longClickItem = clickItem;}

    public void setSwitchVibration(ItemSwitchChangeListener apiBackCall){this.apiBackCall = apiBackCall;}

    public interface ClickItem{
        void clickItem(View view,int position);
    }

    public void setDataList(List<Alarm> oclockInfoList){
        listOclockInfo.clear();
        listOclockInfo.addAll(oclockInfoList);
        super.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_oclock,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Alarm oclockInfo = listOclockInfo.get(position);

        holder.tvOclockTime.setText(oclockInfo.getHour()+":"+oclockInfo.getMinute());
        //0一次，1每天，2法定工作日，3法定家假日，4周一至周五，5自定义
        holder.tvOclockDay.setText(loadRepeat(oclockInfo.getRepeat()));

        if(oclockInfo.isOnOff()){
            holder.tvOclockOnOff.setText(context.getString(R.string.on));
            holder.switchOclock.setChecked(true);
        }else {
            holder.tvOclockOnOff.setText(context.getString(R.string.off));
            holder.switchOclock.setChecked(false);
        }
        if(!oclockInfo.getLabel().equals(null)){
            holder.tvOclockRemark.setText(oclockInfo.getLabel());
        }else{
            holder.tvOclockRemark.setText(" ");
        }
        if(oclockInfo.isVibration()) {
            holder.tvRing.setText(oclockInfo.getRing() +"    " + "振动");
        }else{
            holder.tvRing.setText(oclockInfo.getRing());
        }

        holder.switchOclock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                   if (apiBackCall != null) {
                       apiBackCall.change(position, b);
                   }
               }
           });

        holder.llOclock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickItem != null) {
                    mClickItem.clickItem(v,position);
                }
            }
        });

        holder.llOclock.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(longClickItem != null){
                    longClickItem.clickItem(view,position);
                }
                return true;
            }
        });

    }

    private String loadRepeat(byte[] repeat){
        String temString = "";
        if(Arrays.equals(repeat,new byte[]{0,0,0,0,0,0,0})){
            temString = items[0];
        }else if(Arrays.equals(repeat,new byte[]{1,1,1,1,1,1,1})){
            temString = items[1];
        }else if(Arrays.equals(repeat,new byte[]{1,1,1,1,1,0,0})){
            temString = items[2];
        }else if(Arrays.equals(repeat , new byte[]{0,0,0,0,0,1,1})){
            temString = items[3];
        }else{
            for(int j=0;j<7;j++){
                if(repeat[j] == 1)
                    temString += weekItems[j];
            }
        }
        return temString;
    }

    @Override
    public int getItemCount() {return listOclockInfo == null ? 0 : listOclockInfo.size();}

     class ViewHolder extends RecyclerView.ViewHolder{
//        @BindView(R.id.tv_oclock_time)
        TextView tvOclockTime;
//        @BindView(R.id.tv_oclock_day)
        TextView tvOclockDay;
//        @BindView(R.id.tv_oclock_onoff)
        TextView tvOclockOnOff;
//        @BindView(R.id.switch_oclock)
        Switch switchOclock;

        TextView tvOclockRemark;
        TextView tvRing;
        LinearLayout llOclock;
        public ViewHolder(View itemview){
            super(itemview);
//            ButterKnife.bind(this,itemview);
            tvOclockTime = itemview.findViewById(R.id.tv_oclock_time);
            tvOclockDay = itemview.findViewById(R.id.tv_oclock_day);
            tvOclockOnOff = itemview.findViewById(R.id.tv_oclock_onoff);
            switchOclock = itemview.findViewById(R.id.switch_oclock);
            tvOclockRemark = itemview.findViewById(R.id.tv_oclock_remark);
            tvRing = itemview.findViewById(R.id.tv_oclock_bell_vibration);
            llOclock = itemview.findViewById(R.id.ll_olock);
        }
    }
}
