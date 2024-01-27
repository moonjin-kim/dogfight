package com.dogfight.dogfight.common.trace.logtrace;


import com.dogfight.dogfight.common.trace.TraceStatus;

public interface LogTrace {

    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);
}
