package com.exam.support.util;

import freemarker.template.Template;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Map;

/**
 * Created by lijun7 on 2016/4/27.
 */
public class FtlConvertHtmlUtil {

    /**
     * 通过模板构造邮件内容
     */
    public static String getHtmlText(Template tpl, Map map){
        String htmlText="";
        try {
            //解析模板并替换动态数据
            htmlText= FreeMarkerTemplateUtils.processTemplateIntoString(tpl,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return htmlText;
    }
}
