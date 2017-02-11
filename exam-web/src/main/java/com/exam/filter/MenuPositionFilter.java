package com.exam.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class MenuPositionFilter extends HttpServlet implements Filter {

    private Logger logger = LoggerFactory.getLogger(MenuPositionFilter.class);

    private static final long serialVersionUID = 6718669867510762779L;

    private String[] noFilter = null;
    private PathMatcher matcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        noFilter = filterConfig.getInitParameter("noFilter") != null ?
                filterConfig.getInitParameter("noFilter").split(",") : null;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response  =(HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(true);
        List<String> visaMenuList = (List<String>)session.getAttribute("menuList");//
        String url = request.getRequestURI();
        boolean isMatched = false;
        if (noFilter != null) {
            for (String urlPattern : noFilter) {
                if (matcher.match(urlPattern, url)){
                    isMatched = true;
                    break;
                }
            }
        }
        //不在白名单内 && 菜单权限不为空(已登录) && url不在菜单列表内
        if (!isMatched && visaMenuList != null && !visaMenuList.contains(url) && matcher.match("**/html/**", url)) {
            logger.info(url + "无访问权限或不在过滤名单内，重定向到403页面");
            response.setStatus(403);
            response.sendRedirect(request.getContextPath() + "/view/error/forbidden.html");
        } else {
            if (url.indexOf("/html") > -1) { //url返回页面的话就带上以下参数
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
