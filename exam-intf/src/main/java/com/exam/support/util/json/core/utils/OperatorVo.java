package com.exam.support.util.json.core.utils;

/**
 * 此类描述的是：
 * 
 * @author xujinfei
 */

public class OperatorVo {
    // 操作人id
    private int userId;
    // 操作人姓名
    private String nickName = "";

    /**
     * userId
     * 
     * @return the userId
     */

    public int getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */

    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * nickName
     *
     * @return  the nickName	 
     */
    
    public String getNickName() {
        return nickName;
    }

    /**
     * @param nickName the nickName to set
     */
    
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


}
