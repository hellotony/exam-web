package com.exam.vo.title;

import com.exam.vo.option.OptionVo;

import java.util.List;

/**
 * Created by lijun on 2017/2/7.
 */
public class TitleVo {
    private Integer id;
    // 题目类型 （1 单选 2 多选）
    private Integer type;
    // 题目所属性别（0 共有 1 女 2 男）
    private Integer genderType;
    private String content;
    private Integer nextTitleId;
    private String no;
    private Integer abstractNo;

    // 选项
    private List<OptionVo> options;

    public List<OptionVo> getOptions() {
        return options;
    }

    public void setOptions(List<OptionVo> options) {
        this.options = options;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGenderType() {
        return genderType;
    }

    public void setGenderType(Integer genderType) {
        this.genderType = genderType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getNextTitleId() {
        return nextTitleId;
    }

    public void setNextTitleId(Integer nextTitleId) {
        this.nextTitleId = nextTitleId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getAbstractNo() {
        return abstractNo;
    }

    public void setAbstractNo(Integer abstractNo) {
        this.abstractNo = abstractNo;
    }
}
