package zhuangh7.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class timeTool {
	    /** 
	     * 时间格式到毫秒 
	     */  
	    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");  
	    public static String getDate(){  
	        return df.format(new Date());  
	    }    
}
