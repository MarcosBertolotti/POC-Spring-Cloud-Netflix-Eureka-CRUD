package com.example.springboot.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PreTimeElapsedFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PreTimeElapsedFilter.class);

    @Override
    public String filterType() {        // Filter de tipo Pre, antes de que se resuelva la ruta y antes de la comunicacion con el micro servicio

        return "pre"; // palabra clave
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

        log.info(String.format("%s request enrutado a %s", request.getMethod(), request.getRequestURL().toString()));

        Long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        return null;
    }
}
