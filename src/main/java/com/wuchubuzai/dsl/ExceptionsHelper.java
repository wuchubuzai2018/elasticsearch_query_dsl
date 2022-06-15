package com.wuchubuzai.dsl;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionsHelper {

    public static String stackTrace(Throwable e) {
        StringWriter stackTraceStringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stackTraceStringWriter);
        e.printStackTrace(printWriter);
        return stackTraceStringWriter.toString();
    }
	
	
}
