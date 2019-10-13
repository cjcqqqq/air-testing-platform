package com.university.shenyang.air.testing.app.response;

import com.university.shenyang.air.testing.common.response.BaseResponse;
import com.university.shenyang.air.testing.model.ReportInfo;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class QueryLatestReportResponse extends AppBaseResponse {
    private ReportInfo data;

   public ReportInfo getData() {
        return data;
    }

    public void setData(ReportInfo data) {
        this.data = data;
    }
}
