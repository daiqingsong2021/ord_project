//package com.wisdom.acm.sys.config;
//
//import com.wisdom.base.common.mapper.ApiLogTotalMapper;
//import com.wisdom.base.common.mapper.ApiLoggerAuthMapper;
//import com.wisdom.base.common.util.EntityUtils;
//import com.wisdom.base.common.po.ApiLogTotal;
//import com.wisdom.base.common.po.ApiLoggerAuth;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
///**
// * 拦截器
// *
// * @author Administrator
// */
//@Aspect
//@Component
//public class ControllerInterceptor {
//    //定义一个日志的全局的静态
//    private static Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);
//
//    @Autowired
//    private ApiLogTotalMapper apiLogTotalMapper;
//
//    @Autowired
//    private ApiLoggerAuthMapper apiLoggerAuthMapper;
//
//    //ThreadLocal 维护变量 避免同步
//    //ThreadLocal为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
//    ThreadLocal<Long> startTime = new ThreadLocal<>();// 开始时间
//    //ThreadLocal 维护变量 避免同步 ThreadLocal是一个关于创建线程局部变量的类、
//    //通常情况下，我们创建的变量是可以被任何一个线程访问并且修改的。而使用ThreadLocal创建的变量只能被当前线程访问，其他线程无法访问和修改。
//    //ThreadLocal为每个使用该变量的线程提供独立的变量副本,所以每一个线程都可以独以地改变自己的副本,而不会影响其它线程所对应的副本
//    ThreadLocal<Long> startTimes = new ThreadLocal<Long>();//开始时间Local<>();//开始时间
//
//    /**
//     * map1存放方法被调用的次数O
//     */
//    ThreadLocal<Map<String, Long>> map1 = new ThreadLocal<>();
//
//    /**
//     * map2存放方法总耗时
//     */
//    ThreadLocal<Map<String, Long>> map2 = new ThreadLocal<>();
//
//
//    /**
//     * 定义一个切入点. 解释下：
//     * <p>
//     * ~ 第一个 * 代表任意修饰符及任意返回值. ~ 第二个 * 定义在web包或者子包 ~ 第三个 * 任意方法 ~ .. 匹配任意数量的参数.
//     */
//    /// static final String pCutStr = "execution(* com.appleyk.*..*(..))";
//    static final String pCutStr = "execution(* com.wisdom.acm..controller..*+.* (..))";
//    //static final String pCutStr = "execution(* com.wisdom.acm.sys.*..*(..))";
//    // "execution(* com.wisdom.acm..service.impl..*+.* (..))";
//
//    @Pointcut(value = pCutStr)
//    public void logPointcut() {
//    }
//
//    /**
//     * Aop：环绕通知 切整个包下面的所有涉及到调用的方法的信息
//     *
//     * @param joinPoint
//     * @return
//     * @throws Throwable
//     */
//    @Around("logPointcut()")
//    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
//
//
//        //初始化 一次
//        if (map1.get() == null) {
//            map1.set(new HashMap<>());
//
//        }
//
//        if (map2.get() == null) {
//            map2.set(new HashMap<>());
//        }
//
//        long start = System.currentTimeMillis();
//        try {
//            Object result = joinPoint.proceed();
//            if (result == null) {
//                //如果切到了 没有返回类型的void方法，这里直接返回
//                return null;
//            }
//            long end = System.currentTimeMillis();
//
//            logger.info("===================");
//            String tragetClassName = joinPoint.getSignature().getDeclaringTypeName();
//            String MethodName = joinPoint.getSignature().getName();
//
//            Object[] args = joinPoint.getArgs();// 参数
//            int argsSize = args.length;
//            String argsTypes = "";
//            String typeStr = joinPoint.getSignature().getDeclaringType().toString().split(" ")[0];
//            String returnType = joinPoint.getSignature().toString().split(" ")[0];
//            logger.info("类/接口:" + tragetClassName + "(" + typeStr + ")");
//            logger.info("方法:" + MethodName);
//            logger.info("参数个数:" + argsSize);
//            logger.info("返回类型:" + returnType);
//            if (argsSize > 0) {
//                // 拿到参数的类型
//                for (Object object : args) {
//                    argsTypes += object.getClass().getTypeName().toString() + " ";
//                }
//                logger.info("参数类型：" + argsTypes);
//            }
//
//            Long total = end - start;
//            logger.info("耗时: " + total + " ms!");
//
//            if (map1.get().containsKey(MethodName)) {
//                Long count = map1.get().get(MethodName);
//                map1.get().remove(MethodName);//先移除，在增加
//                map1.get().put(MethodName, count + 1);
//
//                count = map2.get().get(MethodName);
//                map2.get().remove(MethodName);
//                map2.get().put(MethodName, count + total);
//            } else {
//
//                map1.get().put(MethodName, 1L);
//                map2.get().put(MethodName, total);
//            }
//
//            final ApiLogTotal apiLogTotal = new ApiLogTotal();
//            apiLogTotal.setArgsSize(argsSize);
//            apiLogTotal.setArgsTypes(argsTypes);
//            apiLogTotal.setMethodName(MethodName);
//            apiLogTotal.setReturnType(returnType);
//            apiLogTotal.setTragetClassName(tragetClassName);
//            apiLogTotal.setTotalTime(total);
//
//            final ApiLoggerAuth apiLoggerAuth = new ApiLoggerAuth();
//            if (tragetClassName.contains("sys")){
//                apiLoggerAuth.setLoggerType("系统管理");
//            }
//            int indexC = tragetClassName.lastIndexOf("C");
//            int indexst = tragetClassName.lastIndexOf(".");
//            String moduleName = tragetClassName.substring(indexst+1,indexC);
//            apiLoggerAuth.setModuleName(moduleName);
//            if (MethodName.contains("query")){
//                apiLoggerAuth.setOperaName("查询");
//            }
//            if(MethodName.contains("add")){
//                apiLoggerAuth.setOperaName("增加");
//            }
//            if(MethodName.contains("update")){
//                apiLoggerAuth.setOperaName("修改");
//            }
//            if(MethodName.contains("del")){
//                apiLoggerAuth.setOperaName("删除");
//            }
//            executeAsyncTask(apiLogTotal,apiLoggerAuth);
//
//            return result;
//
//        } catch (Throwable e) {
//            long end = System.currentTimeMillis();
//            logger.info("====around " + joinPoint + "\tUse time : " + (end - start) + " ms with exception : "
//                    + e.getMessage());
//            throw e;
//        }
//
//    }
//
//    //对Controller下面的方法执行前进行切入，初始化开始时间
//    @Before(value = "execution(* com.wisdom.acm.sys.controller.*.*(..))")
//    public void beforMehhod(JoinPoint jp) {
//        startTime.set(System.currentTimeMillis());
//    }
//
//    //对Controller下面的方法执行后进行切入，统计方法执行的次数和耗时情况
//    //注意，这里的执行方法统计的数据不止包含Controller下面的方法，也包括环绕切入的所有方法的统计信息
//    @AfterReturning(value = "execution(* com.wisdom.acm.sys.controller.*.*(..))")
//    public void afterMehhod(JoinPoint jp) {
//        long end = System.currentTimeMillis();
//        long total = end - startTime.get();
//        String methodName = jp.getSignature().getName();
//        logger.info("连接点方法为：" + methodName + ",执行总耗时为：" + total + "ms");
//
//        //重新new一个map
//        Map<String, Long> map = new HashMap<>();
//
//        //从map2中将最后的 连接点方法给移除了，替换成最终的，避免连接点方法多次进行叠加计算
//        //由于map2受ThreadLocal的保护，这里不支持remove，因此，需要单开一个map进行数据交接
//        for (Map.Entry<String, Long> entry : map2.get().entrySet()) {
//            if (entry.getKey().equals(methodName)) {
//                map.put(methodName, total);
//
//            } else {
//                map.put(entry.getKey(), entry.getValue());
//            }
//        }
//
////        for (Map.Entry<String, Long> entry : map1.get().entrySet()) {
////            for (Map.Entry<String, Long> entry2 : map.entrySet()) {
////                if (entry.getKey().equals(entry2.getKey())) {
////                    System.err.println(entry.getKey() + ",被调用次数：" + entry.getValue() + ",综合耗时：" + entry2.getValue() + "ms");
////                }
////            }
////
////        }
//    }
//
//    @Async
//    // 通过@Async注解方法表明这个方法是一个异步方法，如果注解在类级别，则表名该类的所有方法都是异步的，
//    // 而这里的方法自动被注入使用ThreadPoolTaskExecutor作为TaskExecutor
//    public void executeAsyncTask(ApiLogTotal apiLogTotal,ApiLoggerAuth apiLoggerAuth) {
//        EntityUtils.setCreateInfo(apiLogTotal);
//        apiLogTotalMapper.insert(apiLogTotal);
//
//        if (!ObjectUtils.isEmpty(apiLoggerAuth)){
//            EntityUtils.setCreateInfo(apiLoggerAuth);
//            apiLoggerAuthMapper.insert(apiLoggerAuth);
//        }
//    }
//
//}
