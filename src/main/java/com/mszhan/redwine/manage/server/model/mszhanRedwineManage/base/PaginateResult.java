package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base;

import java.util.List;

/**
 * Created by pcq on 2017/8/8.
 */
public class PaginateResult<T> {
    public Long total;
    public List<T> rows;
    
    public PaginateResult() {
		super();
	}

	public PaginateResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public static <T> PaginateResult<T> newInstance(long total, List<T> rows){
        return new PaginateResult<>(total, rows);
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
