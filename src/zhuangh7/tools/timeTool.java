package zhuangh7.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class timeTool {
	    /** 
	     * ʱ���ʽ������ 
	     */  
	    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");  
	    public static String getDate(){  
	        return df.format(new Date());  
	    }    
}
