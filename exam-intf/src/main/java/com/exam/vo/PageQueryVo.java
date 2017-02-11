package com.exam.vo;

public class PageQueryVo {
    /** 当前页 */
    protected int currentPage = 1;

    /** 每页记录数 */
    protected int pageSize = 15;

    /** 录数 */
    protected int totalCount;

    /** 总页数 */
    protected int page;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
