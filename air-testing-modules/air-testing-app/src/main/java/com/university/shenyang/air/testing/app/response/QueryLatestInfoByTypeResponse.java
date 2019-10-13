package com.university.shenyang.air.testing.app.response;


import com.university.shenyang.air.testing.app.pojo.ReportTypeInfo;
import com.university.shenyang.air.testing.common.response.BaseResponse;

import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class QueryLatestInfoByTypeResponse extends AppBaseResponse {
    private List<ReportTypeInfo> data;

    public List<ReportTypeInfo> getData() {
        return data;
    }

    public void setData(List<ReportTypeInfo> data) {
        this.data = data;
    }
}
