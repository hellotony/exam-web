package com.exam.support.util.json.core.utils;

/**
 * 
 * Description: 用ThreadLocal提供一个存储线程内变量的地方.
 * <p/>
 * 客户端代码可以用静态方法存储和获取线程内变量,不需要依赖于HttpSession.
 * web层的Controller可通过此处向business层传入user_id之类的变量
 * 
 */
public class UserSession {
    /** * 保存变量的ThreadLocal，保持在同一线程中同步数据. */
    private static final ThreadLocal<OperatorVo> OPERATORVO_LOCAL = new ThreadLocal<OperatorVo>();

    /** * 工具类的protected构造方法. */
    protected UserSession() {

    }

    /**
     * 获得线程中保存的属性.
     * 
     * @return OperatorVo
     */
    public static OperatorVo get() {
        return null == OPERATORVO_LOCAL.get() ? new OperatorVo() : OPERATORVO_LOCAL.get();
    }

    /**
     * 设置制定属性名的值.
     * 
     * @param operatorVo
     *            属性名称
     */
    public static void set(OperatorVo operatorVo) {
        if (null == operatorVo) {
            operatorVo = new OperatorVo();
        }
        OPERATORVO_LOCAL.set(operatorVo);
    }
}
