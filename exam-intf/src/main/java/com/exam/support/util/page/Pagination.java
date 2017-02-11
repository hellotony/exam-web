package com.exam.support.util.page;

import org.apache.ibatis.session.RowBounds;

/**
 * Created by lijun7 on 2016/10/14.
 */
public class Pagination extends RowBounds {
    private int currentPage;
    private int pageSize;
    private int count;
    private boolean needCount;

    public Pagination() {
        this(true);
    }

    public Pagination(boolean needCount) {
        this.currentPage = 1;
        this.pageSize = 15;
        this.count = 0;
        this.needCount = needCount;
    }

    public static Pagination getPagination(RowBounds rowBounds) {
        return getPagination(rowBounds, false);
    }

    public static Pagination getPagination(RowBounds rowBounds, boolean needCount) {
        Pagination pagination = new Pagination(needCount);
        pagination.setPageSize(rowBounds.getLimit());
        pagination.setCurrentPage(rowBounds.getOffset() / rowBounds.getLimit() + 1);
        return pagination;
    }

    public static boolean noRow(RowBounds rowBounds) {
        return rowBounds instanceof Pagination?false:rowBounds.getLimit() == 2147483647 && rowBounds.getOffset() == 0;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public Pagination setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public Pagination setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getCount() {
        return this.count;
    }

    public Pagination setCount(int count) {
        this.count = count;
        return this;
    }

    public int getStartIndex() {
        return this.pageSize * (this.currentPage - 1);
    }

    public int getLimit() {
        return this.pageSize;
    }

    public boolean isNeedCount() {
        return this.needCount && this.count <= 0;
    }

    public boolean hasNext() {
        return this.pageSize * this.currentPage < this.count;
    }

    public Pagination next() {
        ++this.currentPage;
        return this;
    }
}