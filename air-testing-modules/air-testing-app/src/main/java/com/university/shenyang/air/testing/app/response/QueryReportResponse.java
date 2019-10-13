package com.university.shenyang.air.testing.app.response;


import com.university.shenyang.air.testing.app.pojo.UserReportInfo;

public class QueryReportResponse extends AppBaseResponse {
    private UserReportInfo data;

    public UserReportInfo getData() {
        return data;
    }

    public void setData(UserReportInfo data) {
        this.data = data;
    }
}
