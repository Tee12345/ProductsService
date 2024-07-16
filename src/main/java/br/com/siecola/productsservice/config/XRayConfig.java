package br.com.siecola.productsservice.config;

import com.amazonaws.xray.*;
import com.amazonaws.xray.jakarta.servlet.*;
import com.amazonaws.xray.strategy.sampling.*;
import jakarta.servlet.*;
import org.slf4j.*;
import org.springframework.context.annotation.*;
import org.springframework.util.*;

import java.io.*;
import java.net.*;

@Configuration
public class XRayConfig {

    private static final Logger LOG = LoggerFactory.getLogger(XRayConfig.class);

    public XRayConfig() {

        try{
            URL ruleFile = ResourceUtils.getURL("classpath:xray/xray-sampling-rules.json");
            AWSXRayRecorder awsxRayRecorder = AWSXRayRecorderBuilder.standard()
                    .withDefaultPlugins()
                    .withSamplingStrategy(new CentralizedSamplingStrategy(ruleFile))
                    .build();

            AWSXRay.setGlobalRecorder(awsxRayRecorder);
        } catch(FileNotFoundException e) {
            LOG.error("XRay config file not found");
        }
    }

    @Bean
    public Filter TracingFilter() {
        return new AWSXRayServletFilter("[productsservice");
    }

}
