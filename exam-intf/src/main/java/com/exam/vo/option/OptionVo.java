package com.exam.vo.option;

/**
 * Created by lijun on 2017/2/7.
 */
public class OptionVo {
    private Integer id;
    private Integer titleId;
    private String content;
    private Integer optionOrder;
    private Integer nextTitleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getOptionOrder() {
        return optionOrder;
    }

    public void setOptionOrder(Integer optionOrder) {
        this.optionOrder = optionOrder;
    }

    public Integer getNextTitleId() {
        return nextTitleId;
    }

    public void setNextTitleId(Integer nextTitleId) {
        this.nextTitleId = nextTitleId;
    }
}
