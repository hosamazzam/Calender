package com.hosazz.flightscalendar.adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hosazz.flightscalendar.R;
import com.hosazz.flightscalendar.view.AutoResizeTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by ismail.khan2 on 3/18/2016.
 */
public class MonthGridAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;

    List<String> mItemList = Collections.EMPTY_LIST;
    List<Date> mEventList = Collections.EMPTY_LIST;
    int mToday, mMonth, mYear, mDisplayMonth, mDisplayYear;
    int mCurrentDayTextColor, mDaysOfMonthTextColor, mDaysOfWeekTextColor, mMonthNameTextColor;
    boolean mIsMonthView = true;
    MyViewHolder mHolder;

    public MonthGridAdapter(Context context, int year, int month, int today) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItemList = new ArrayList<>();

        Calendar calendar = new GregorianCalendar(year, month, 1);
        mItemList = getItemList(calendar);

        mToday = today;
        mMonth = mDisplayMonth = month;
        mYear = mDisplayYear = year;

    }

    /**
     * This method generates the days of the month to be displayed
     *
     * @param calendar Species for which month the days should be generated
     * @return itemsList list of days for the month to be displayed
     */
    private List<String> getItemList(Calendar calendar) {
        List<String> itemList = new ArrayList<>();
        itemList.add("Su");
        itemList.add("Mo");
        itemList.add("Tu");
        itemList.add("We");
        itemList.add("Th");
        itemList.add("Fr");
        itemList.add("Sa");

        //getting first day of the month [sun-1; mon-2; tue-3; wed-4; thu-5; fri-6; sat-7;]
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        //total number of days in a month
        int numOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        //coz array starts with 0 and Month day starts with 1
        if (firstDayOfMonth > 0) {
            firstDayOfMonth = firstDayOfMonth - 1;
        }

        int day = 1;
        for (int i = 0; i < numOfDays + firstDayOfMonth; i++) {
            //checking for first day of the day match
            //if first day of month is Tuesday (i.e firstDayOfMonth = 2 [3-1]
            //then [0][1] values in the list will be empty
            if (i >= firstDayOfMonth) {
                itemList.add("" + day);
                day++;
            } else {
                //adding empty space to list until first day of month ('firstDayOfMonth') is reached
                itemList.add("");
            }
        }

        return itemList;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_calendar_month, parent, false);
            mHolder = new MyViewHolder(convertView, mIsMonthView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (MyViewHolder) convertView.getTag();
        }

        String item = mItemList.get(position);

        if (position < 7) {//position 0-6 are reserved for days of the weeks (weekdays & weekends)
            mHolder.tvCalendarMonthDay.setVisibility(View.GONE);
            mHolder.tvCalendarWeekDayName.setVisibility(View.VISIBLE);
            mHolder.tvCalendarWeekDayName.setText(item);
            mHolder.tvCalendarWeekDayName.setTextColor(mDaysOfWeekTextColor);

        } else { //positions >= 7 are reserved for days of the month (eg 1 to 31)

            mHolder.tvCalendarWeekDayName.setVisibility(View.GONE);
            mHolder.border.setVisibility(View.GONE);
            mHolder.tvCalendarMonthDay.setVisibility(View.VISIBLE);
            mHolder.tvCalendarMonthDay.setText(mItemList.get(position));

            if (!item.isEmpty()) {
                if (mToday == Integer.parseInt(item) && mDisplayMonth == mMonth && mDisplayYear == mYear) {
                    mHolder.tvCalendarMonthDay.setTextColor(mCurrentDayTextColor);
                } else {
                    Date date = getDate(mDisplayYear, mDisplayMonth, Integer.parseInt(item));
                    if (mDisplayYear <= mYear) {
                        if (mDisplayMonth <= mMonth) {
                            if (mDisplayMonth == mMonth) {
                                if (mToday < Integer.parseInt(item))
                                    mHolder.tvCalendarMonthDay.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                                else
                                    mHolder.tvCalendarMonthDay.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
                            } else
                                mHolder.tvCalendarMonthDay.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
                        } else {
                            mHolder.tvCalendarMonthDay.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                        }
                    } else {
                        mHolder.tvCalendarMonthDay.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    }
                    if (mEventList.contains(date)) {
                        mHolder.tvCalendarMonthDay.setBackgroundResource(R.drawable.textview_background_event);
                        mHolder.tvCalendarMonthDay.setTextColor(Color.CYAN);
                    } else {
                        mHolder.tvCalendarMonthDay.setBackgroundResource(R.drawable.textview_background_no_event);
                        //  mHolder.tvCalendarMonthDay.setTextColor(ContextCompat.getColor(mContext, mToday <= Integer.parseInt(item) ? R.color.gray : android.R.color.black));
                    }
                }
            }

        }
//        mHolder.tvCalendarMonthDay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new SimpleTooltip.Builder(mContext)
//                        .anchorView(v)
//                        .text("Texto do Tooltip")
//                        .gravity(Gravity.TOP)
//                        .animated(false)
//                        .backgroundColor(mContext.getResources().getColor(R.color.colorAccent))
//                        .transparentOverlay(true)
//                        .build()
//                        .show();
//                v.setBackgroundResource(R.drawable.textview_background_event);
//            }
//        });
//        mHolder.tvCalendarMonthDay.setOnHoverListener(new View.OnHoverListener() {
//            @Override
//            public boolean onHover(View v, MotionEvent event) {
//                v.setBackgroundResource(R.drawable.textview_background_event);
//                return false;
//            }
//        });
        return convertView;
    }

    /**
     * This method specifies if this is used for MonthView or YearView to change the text size
     *
     * @param isMonthView
     */
    public void setIsMonthView(boolean isMonthView) {
        mIsMonthView = isMonthView;
        if (!isMonthView) {
            notifyDataSetChanged();
        }
    }

    /**
     * This method updates the view based on the year and month passed
     *
     * @param year
     * @param month
     */
    public void updateCalendar(int year, int month) {
        mDisplayMonth = month;
        mDisplayYear = year;

        Calendar calendar = new GregorianCalendar(year, month, 1);
        mItemList = getItemList(calendar);
        notifyDataSetChanged();
    }

    /**
     * This method will highlight the event dates
     *
     * @param eventList List of dates
     */
    public void setEventList(List<Date> eventList) {
        mEventList = eventList;
        notifyDataSetChanged();
    }

    /**
     * @return mItemList - list fo days of a month
     */
    public List<String> getItemList() {
        return mItemList;
    }

    /**
     * This method takes in year, month & day integer values and generates a date object
     *
     * @param year
     * @param month
     * @param day
     * @return Date - a date object
     */
    private Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public void setCurrentDayTextColor(int color) {
        mCurrentDayTextColor = color;
        notifyDataSetChanged();
    }

    public void setDaysOfMonthTextColor(int color) {
        mDaysOfMonthTextColor = color;
        notifyDataSetChanged();
    }

    public void setDaysOfWeekTextColor(int color) {
        mDaysOfWeekTextColor = color;
        notifyDataSetChanged();
    }

    public void setMonthNameTextColor(int color) {
        mMonthNameTextColor = color;
        notifyDataSetChanged();
    }

    public void selection(int pos, View view) {
        if (pos >= 7 && !mItemList.get(pos).isEmpty())
            view.findViewById(R.id.row_cm_tv_day).setBackgroundResource(R.drawable.textview_background_event);
    }

    class MyViewHolder implements View.OnTouchListener {

        AutoResizeTextView tvCalendarMonthDay, tvCalendarWeekDayName;
        View border;

        public MyViewHolder(View view, boolean isMonthView) {
            tvCalendarWeekDayName = view.findViewById(R.id.row_cm_tv_week_day_name);
            border = view.findViewById(R.id.row_cm_border);
            tvCalendarMonthDay = view.findViewById(R.id.row_cm_tv_day);
            if (isMonthView) {
                tvCalendarMonthDay.setMinTextSize(35);
                tvCalendarWeekDayName.setMinTextSize(35);
            } else {
                tvCalendarMonthDay.setMinTextSize(25);
                tvCalendarWeekDayName.setMinTextSize(25);
            }
            // view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //  tvCalendarMonthDay.setBackgroundResource(R.drawable.textview_background_event);
            return true;
        }
    }
}