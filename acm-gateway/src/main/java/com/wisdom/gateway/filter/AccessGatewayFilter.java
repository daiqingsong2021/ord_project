package com.wisdom.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.wisdom.base.common.authority.PermissionInfo;
import com.wisdom.base.common.log.LogInfo;
import com.wisdom.auth.client.config.ServiceAuthConfig;
import com.wisdom.auth.client.config.UserAuthConfig;
import com.wisdom.auth.client.jwt.ServiceAuthUtil;
import com.wisdom.auth.client.jwt.UserAuthUtil;
import com.wisdom.auth.server.common.util.jwt.IJWTInfo;
import com.wisdom.base.common.context.BaseContextHandler;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.auth.TokenForbiddenResponse;
import com.wisdom.gateway.feign.ILogService;
import com.wisdom.gateway.feign.IUserService;
import com.wisdom.gateway.utils.DBLog;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 */
@Configuration
@Slf4j
public class AccessGatewayFilter implements GlobalFilter, Ordered {

    public static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10101;

    @Autowired
    @Lazy
    private IUserService userService;

    @Autowired
    @Lazy
    private ILogService logService;

    @Value("${gate.ignore.startWith}")
    private String startWith;

    @Value("${gate.ignore.endWith}")
    private String endWith;

    @Value("${gate.ignore.contain}")
    private String contain;

    //    @Value("${zuul.prefix}")
//    private String zuulPrefix;

    private static final String GATE_WAY_PREFIX = "/api";
    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Autowired
    private UserAuthConfig userAuthConfig;

    @Autowired
    private ServiceAuthUtil serviceAuthUtil;

    @Override
    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("check token and user permission....");
        LinkedHashSet requiredAttribute = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        ServerHttpRequest request = exchange.getRequest();
        String requestUri = request.getPath().pathWithinApplication().value();
        if (requiredAttribute != null) {
            Iterator<URI> iterator = requiredAttribute.iterator();
            while (iterator.hasNext()) {
                URI next = iterator.next();
                if (next.getPath().startsWith(GATE_WAY_PREFIX)) {
                    requestUri = next.getPath().substring(GATE_WAY_PREFIX.length());
                }
            }
        }
        final String method = request.getMethod().toString();
        BaseContextHandler.setToken(null);
        ServerHttpRequest.Builder mutate = request.mutate();
        // 不进行拦截的地址
        if (this.isStartWith(requestUri) || this.isEndsWith(requestUri) || this.isContains(requestUri)) {
            ServerHttpRequest build = mutate.build();
            return chain.filter(exchange.mutate().request(build).build());
        }
        IJWTInfo user = null;
        try {
            user = getJWTUser(request, mutate);
        } catch (Exception e) {
            log.error("用户Token过期异常", e);
            return getVoidMono(exchange, new TokenForbiddenResponse("User Token Forbidden or Expired!"));
        }
//        List<PermissionInfo> permissionIfs = userService.getAllPermissionInfo();
        // 判断资源是否启用权限约束
//        Stream<PermissionInfo> stream = getPermissionIfs(requestUri, method, permissionIfs);
//        List<PermissionInfo> result = stream.collect(Collectors.toList());
//        PermissionInfo[] permissions = result.toArray(new PermissionInfo[]{});
//        if (permissions.length > 0) {
//            if (checkUserPermission(permissions, serverWebExchange, user)) {
//                return getVoidMono(serverWebExchange, new TokenForbiddenResponse("User Forbidden!Does not has Permission!"));
//            }
//        }
        // 申请客户端密钥头
        mutate.header(serviceAuthConfig.getTokenHeader(), serviceAuthUtil.getClientToken());
        HttpHeaders headers = request.getHeaders();
        mutate.header("gateway_host", headers.get("host").get(0));//前端请求Host
        ServerHttpRequest build = mutate.build();
        return chain.filter(exchange.mutate().request(build).build());

    }

    protected DataBuffer stringBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

    /**
     * 网关抛异常
     *
     * @param body
     */
    @NotNull
    private Mono<Void> getVoidMono(ServerWebExchange serverWebExchange, ApiResult body) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        byte[] bytes = JSONObject.toJSONString(body).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }


    /**
     * 获取目标权限资源
     *
     * @param requestUri
     * @param method
     * @param serviceInfo
     * @return
     */
    private Stream<PermissionInfo> getPermissionIfs(final String requestUri, final String method, List<PermissionInfo> serviceInfo) {
        return serviceInfo.parallelStream().filter(new Predicate<PermissionInfo>() {
            @Override
            public boolean test(PermissionInfo permissionInfo) {
                String uri = permissionInfo.getUri();
                if (uri.indexOf("{") > 0) {
                    uri = uri.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
                }
                String regEx = "^" + uri + "$";
                return (Pattern.compile(regEx).matcher(requestUri).find())
                        && method.equals(permissionInfo.getMethod());
            }
        });
    }

    private void setCurrentUserInfoAndLog(ServerWebExchange serverWebExchange, IJWTInfo user, PermissionInfo pm) {
        String host = serverWebExchange.getRequest().getRemoteAddress().toString();
        LogInfo logInfo = new LogInfo(pm.getMenu(), pm.getName(), pm.getUri(), new Date(), user.getId(), user.getName(), host);
        DBLog.getInstance().setLogService(logService).offerQueue(logInfo);
    }

    /**
     * 返回session中的用户信息
     *
     * @param request
     * @param ctx
     * @return
     */
    private IJWTInfo getJWTUser(ServerHttpRequest request, ServerHttpRequest.Builder ctx) throws Exception {
        List<String> strings = request.getHeaders().get(userAuthConfig.getTokenHeader());
        String authToken = null;
        if (strings != null) {
            authToken = strings.get(0);
        }
        if (StringUtils.isBlank(authToken)) {
            strings = request.getQueryParams().get("token");
            if (strings != null) {
                authToken = strings.get(0);
            }
        }
        ctx.header(userAuthConfig.getTokenHeader(), authToken);
        BaseContextHandler.setToken(authToken);
        IJWTInfo infoFromToken = userAuthUtil.getInfoFromToken(authToken);
        ctx.header("userName",URLEncoder.encode(infoFromToken.getUniqueName(), "UTF-8"));
        ctx.header("userId", infoFromToken.getId());
        ctx.header("actuName",URLEncoder.encode(infoFromToken.getActuName(), "UTF-8"));
        ctx.header("userType",infoFromToken.getUserType());
        String hostString = getClientIp(request);
        if (StringUtils.isBlank(hostString) || hostString.equals("127.0.0.1") || hostString.equals("0:0:0:0:0:0:1")) {
            ctx.header("userHost", null);
        } else {
            ctx.header("userHost", hostString);
        }
        return infoFromToken;
    }


    public static String getClientIp(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = "";
        if(headers.containsKey("x-forwarder-for"))
        {
            ip = headers.get("x-forwarder-for").get(0);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if(headers.containsKey("x-forwarded-for"))
            {
                ip = headers.get("x-forwarded-for").get(0);
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if(headers.containsKey("Proxy-Client-IP"))
            {
                ip = headers.get("Proxy-Client-IP").get(0);
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if(headers.containsKey("WL-Proxy-Client-IP"))
            {
                ip = headers.get("WL-Proxy-Client-IP").get(0);
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getHostString();
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    private boolean checkUserPermission(PermissionInfo[] permissions, ServerWebExchange ctx, IJWTInfo user) {
        List<PermissionInfo> permissionInfos = userService.getPermissionByUsername(user.getUniqueName());
        PermissionInfo current = null;
        for (PermissionInfo info : permissions) {
            boolean anyMatch = permissionInfos.parallelStream().anyMatch(new Predicate<PermissionInfo>() {
                @Override
                public boolean test(PermissionInfo permissionInfo) {
                    return permissionInfo.getCode().equals(info.getCode());
                }
            });
            if (anyMatch) {
                current = info;
                break;
            }
        }
        if (current == null) {
            return true;
        } else {
            if (!RequestMethod.GET.toString().equals(current.getMethod())) {
                setCurrentUserInfoAndLog(ctx, user, current);
            }
            return false;
        }
    }


    /**
     * URI是否以什么打头
     *
     * @param requestUri
     * @return
     */
    private boolean isStartWith(String requestUri) {
        boolean flag = false;
        if(!ObjectUtils.isEmpty(this.startWith)) {
            for (String s : this.startWith.split(",")) {
                if (requestUri.startsWith(s)) {
                    return true;
                }
            }
        }
        return flag;
    }

    /**
     * URI是否以什么结束
     *
     * @param requestUri
     * @return
     */
    private boolean isEndsWith(String requestUri) {
        boolean flag = false;
        if(!ObjectUtils.isEmpty(this.endWith)) {
            for (String s : this.endWith.split(",")) {
                if (requestUri.endsWith(s)) {
                    return true;
                }
            }
        }
        return flag;
    }

    /**
     * URI是否包含
     *
     * @param requestUri
     * @return
     */
    private boolean isContains(String requestUri) {
        boolean flag = false;
        if(!ObjectUtils.isEmpty(this.contain)) {
            for (String s : this.contain.split(",")) {
                if (requestUri.contains(s)) {
                    return true;
                }
            }
        }
        return flag;
    }

    /**
     * 网关抛异常
     *
     * @param body
     * @param code
     */
    private Mono<Void> setFailedRequest(ServerWebExchange serverWebExchange, String body, int code) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        return serverWebExchange.getResponse().setComplete();
    }

}
