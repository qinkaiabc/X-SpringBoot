package com.suke.czx.datasources.aspect;

import com.suke.czx.datasources.DataSourceNames;
import com.suke.czx.datasources.DynamicDataSource;
import com.suke.czx.datasources.annotation.DataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 多数据源，切面处理类
 * @author czx
 * @email object_czx@163.com
 * @date 2017/9/16 22:20
 */
@Aspect
@Component
public class DataSourceAspect implements Ordered {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.suke.czx.datasources.annotation.DataSource)")
    public void dataSourcePointCut() {

    }

    @Before("dataSourcePointCut()")
    public void around(JoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DataSource ds = method.getAnnotation(DataSource.class);
        if(ds == null){
            DynamicDataSource.setDataSource(DataSourceNames.FIRST);
            logger.debug("set datasource is " + DataSourceNames.FIRST);
        }else {
            DynamicDataSource.setDataSource(ds.name());
            logger.debug("set datasource is " + ds.name());
        }
    }

    @After("dataSourcePointCut()")
    public void after(){
        DynamicDataSource.clearDataSource();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
