package com.factweavers.netflixzuulapigateway;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
/**
 * Created by weavers on 09/08/18.
 */
@Component
public class ZuulLoggingFilter extends ZuulFilter{

    private Logger logger= LoggerFactory.getLogger(this.getClass());


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("In run");
        HttpServletRequest request= RequestContext.getCurrentContext().getRequest();
        logger.info("request -> {} request URI -> {} ", request, request.getRequestURL());
        return null;
    }
}
