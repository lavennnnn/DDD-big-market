package cn.hush.config;


import cn.hush.infrastructure.jwt.JwtConfigProperties;
import cn.hush.infrastructure.jwt.JwtService;
import cn.hush.types.common.Constants;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Hush
 * @description jwt令牌校验的拦截器
 * @create 2025-01-07 上午5:33
 */
@Component
@Slf4j
public class JwtEmployeeInterceptor implements HandlerInterceptor {

    @Resource
    private JwtConfigProperties jwtConfigProperties;
    @Resource
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        //判断当前拦截到的是Controller的方法还是其他资源
        if(!(handler instanceof HandlerMethod)){
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = req.getHeader(jwtConfigProperties.getAdminTokenName());

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = jwtService.parseJWT(jwtConfigProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(Long.valueOf(claims.get(Constants.JwtClaimsConstant.EMP_ID).toString()));
            log.info("当前员工id：", empId);
            //BaseContext.setCurrentId(empId);
            return true;
        }catch (RuntimeException e){
            log.error("登录信息失效，",e);
            res.setStatus(401);
            return false;

        }
    }

}
