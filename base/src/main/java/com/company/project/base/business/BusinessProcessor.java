package com.company.project.base.business;

import cn.hutool.core.util.ReflectUtil;
import com.company.project.base.common.entity.BusinessWrap;
import com.company.project.base.common.entity.Request;
import com.company.project.base.common.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: laoliangliang
 * @description:
 * @create: 2020/4/24 17:15
 **/
@Component
public class BusinessProcessor implements IBusinessProcessor {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void before(Request request) {
        List<BusinessWrap> businessClass = request.getBusinessClass();
        if (businessClass == null) {
            return;
        }
        for (BusinessWrap businessWrap : businessClass) {
            Class clazz = businessWrap.getClazz();
            String action = businessWrap.getAction();
            Object extendObj = businessWrap.getExtendObj();
            Object bean = applicationContext.getBean(clazz);
            if (bean instanceof IBusinessBefore) {
                if (action == null) {
                    ((IBusinessBefore) bean).run(request, null, extendObj);
                } else {
                    ReflectUtil.invoke(bean, action, request, null, extendObj);
                }
            }
        }
    }

    @Override
    public void after(Request request, Result result) {
        List<BusinessWrap> businessClass = result.getBusinessClass();
        if (businessClass == null) {
            return;
        }
        for (BusinessWrap businessWrap : businessClass) {
            Class clazz = businessWrap.getClazz();
            String action = businessWrap.getAction();
            Object extendObj = businessWrap.getExtendObj();
            Object bean = applicationContext.getBean(clazz);
            if (bean instanceof IBusinessAfter) {
                if (action == null) {
                    ((IBusinessAfter) bean).run(request, result, extendObj);
                } else {
                    ReflectUtil.invoke(bean, action, request, result, extendObj);
                }
            }
        }
    }
}
