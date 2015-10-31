package com.banshi.frame.interceptor;


import com.banshi.service.SessionService;
import com.banshi.controller.vo.AjaxResult;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 异步请求必须登录拦截器
 */
public class AjaxMustLoginInterceptor implements HandlerInterceptor {

    @Resource
    private SessionService sessionService;

    /**
     * 在业务处理器处理请求之前被调用
     * 如果返回false：从当前的拦截器往回执行所有拦截器的afterCompletion()，再退出拦截器链<p>
     * 如果返回true ：执行下一个拦截器，直到所有的拦截器都执行完毕
     *               再执行被拦截的Controller
     *               然后进入拦截器链，从最后一个拦截器往回执行所有的postHandle()
     *               接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!sessionService.isLogin(request, response)) {
            PrintWriter out = response.getWriter();
            AjaxResult result = new AjaxResult(AjaxResult.RET_CODE_CHECK_ERROR, "need to login");
            out.print(result.toJson());
            out.close();
            return false;
        }
        return true;
    }

    /**
     * 在业务处理器处理请求执行完成后，生成视图之前执行的动作
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用<p>
     * 当有拦截器抛出异常时，会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub
    }
}