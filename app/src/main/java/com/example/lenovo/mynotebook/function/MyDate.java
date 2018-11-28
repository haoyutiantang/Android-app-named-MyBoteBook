package com.example.lenovo.mynotebook.function;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

/*
返回以下格式的时间
2018年1月22日 16:36:55.
 */
public class MyDate {

    //创建一个日历实例，用的中国当地时间
    Calendar calendar = Calendar.getInstance(Locale.CHINA);

    public String getDate ()
    {
        //获取系统的年日期
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH)+1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String str = year+"年"+month+"月"+day+"日";        //将当前时间格式化

        return str;
    }

    //此方法用来首位补零
    public String addzero(int temp,int len)
    {
        StringBuffer add=new StringBuffer();
        add.append(temp);
        while(add.length()<len)
        {
            add.insert(0,0);
        }
        return add.toString();

    }

    public String getDateAndTime()
    {
        StringBuffer str=new StringBuffer();
        str.append(getDate()).append(" ");
        str.append(this.addzero((calendar.get(calendar.HOUR)+12)%24, 2)).append(":");
        str.append(this.addzero(calendar.get(calendar.MINUTE), 2)).append(":");
        str.append(this.addzero(calendar.get(calendar.SECOND), 2)).append(".");
        return str.toString();
    }
}
