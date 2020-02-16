package com.example.springboot.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PostTimeElapsedFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PostTimeElapsedFilter.class);

    @Override
    public String filterType() {        // Filter de tipo Post, despues de que se resuelva la ruta

        return "post"; // palabra clave
    }

    @Override
    public int filterOrder() {

        return 1;
    }

    @Override
    public boolean shouldFilter() { // validamos si ejecutamos o no el filtro

        return true;        // validamos si existe un parametro en la ruta (request Param) si existe retornamos true o false. si el user esta autenticado...
    }

    @Override
    public Object run() throws ZuulException {      // se resuelva la logica de nuestro filtro, debemos pasar datos al request

        RequestContext ctx = RequestContext.getCurrentContext();    // obtenemos el objeto http request
        HttpServletRequest request = ctx.getRequest();

        log.info("Entering post filter");

        Long startTime = (Long) request.getAttribute("startTime");
        Long finalTime = System.currentTimeMillis();
        Long timeElapsed = finalTime - startTime;

        log.info(String.format("Time elapsed in seconds: %s seg.", timeElapsed.doubleValue()/1000.00));
        log.info(String.format("Time elapsed in milliseconds: %s ms.", timeElapsed));

        return null;
    }
}
