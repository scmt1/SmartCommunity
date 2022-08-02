/**
 * yammii.com Inc.
 * Copyright (c) 2018-2018 All Rights Reserved.
 */
package me.zhengjie.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author yongyan.pu
 * @version $Id: DateTineUtils.java, v 0.1 2018年12月27日 上午9:53:47 yongyan.pu Exp $
 */
public class DateUtils {

    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT);
        return sdf.format(date);
    }

}
