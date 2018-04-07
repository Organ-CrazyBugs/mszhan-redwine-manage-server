package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query;

/**
 * Created by god on 2017/11/9.
 */
public class PageQuery {
        private Integer limit = 20;
        private Integer offset = 0;
    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
