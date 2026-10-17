package com.nexasupplyai.ai.config;
import jakarta.servlet.*;import jakarta.servlet.http.*;import org.slf4j.MDC;import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;import java.io.IOException;import java.util.UUID;
@Component public class CorrelationIdFilter extends OncePerRequestFilter {
 public static final String HEADER="X-Correlation-ID",MDC_KEY="correlationId";
 protected void doFilterInternal(HttpServletRequest q,HttpServletResponse s,FilterChain c)throws ServletException,IOException{
  String v=q.getHeader(HEADER);String id=v==null||v.isBlank()||v.length()>120?UUID.randomUUID().toString():v;
  MDC.put(MDC_KEY,id);s.setHeader(HEADER,id);try{c.doFilter(q,s);}finally{MDC.remove(MDC_KEY);}}
}
