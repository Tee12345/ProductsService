package br.com.siecola.productsservice.config;

import com.amazonaws.xray.entities.*;
import com.amazonaws.xray.spring.aop.*;
import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Aspect
@Component
public class XRayInspector extends BaseAbstractXRayInterceptor {

    @Override
    protected Map<String, Map<String, Object>> generateMetadata(
            ProceedingJoinPoint proceedingJoinPoint, Subsegment subsegment) {
            return super.generateMetadata(proceedingJoinPoint, subsegment);
    }

    @Override
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled)")
    protected void xrayEnabledClasses() {}
}
