package com.huzhframework.ssmdemo.sys.shiro.filter;

import com.huzhframework.ssmdemo.dao.UserMapperImpl;
import com.huzhframework.ssmdemo.sys.shiro.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-15
 * <p>Version: 1.0
 */
public class SysUserFilter extends PathMatchingFilter {

    @Autowired
    private UserMapperImpl userService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        String username = (String) SecurityUtils.getSubject().getPrincipal();
        request.setAttribute(Constants.CURRENT_USER, userService.selectByUsername(username));
        return true;
    }
}
