package com.wisdom.acm.base.junit;

import com.wisdom.base.common.util.DateUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Generate Junit test.
 * Quick & dirty ;)
 */
public final class JunitTestGenerator {

    private int modulePort = 0;
    private static
    final Logger LOGGER = Logger.getLogger("aka.junitgenerator.JUnitGenerator.MediaInfoJavaGenerator");

    /**
     * Generate JUnit tests from classes presents in the given jar or classes directory.
     *
     * @param destinationDirectory          destination directory for generated classes.
     * @param absolutePath                  absolute path to the jar/directory classes.
     * @param dependentJarsListAbsolutePath list of mandatory additional jars.
     */
    public void generateJunitTestClasses(final String destinationDirectory, final String absolutePath, final List<String> dependentJarsListAbsolutePath) {
        try {
            Map<String, List<String>> classNameListByPackageNameMap = null;
            if (absolutePath.endsWith(".jar")) {
                classNameListByPackageNameMap = getAllObjectListFromJar(absolutePath);
            } else {
                classNameListByPackageNameMap = getAllObjectListFromDirectory(absolutePath);
            }
            final URL[] urls = new URL[1 + dependentJarsListAbsolutePath.size()];
            urls[0] = new File(absolutePath).toURI().toURL();
            int i = 1;
            for (final String dependentJarsAbsolutePath : dependentJarsListAbsolutePath) {
                urls[i] = new File(dependentJarsAbsolutePath).toURI().toURL();
                i++;
            }
            final URLClassLoader classLoaderForJar = new URLClassLoader(urls);
            for (final Entry<String, List<String>> currentEntryMap : classNameListByPackageNameMap.entrySet()) {
                final String packageName = currentEntryMap.getKey();
                final List<String> classNameList = currentEntryMap.getValue();

                for (final String className : classNameList) {
                    final String classNameResource = packageName + "." + className;
                    try {
                        final Class<?> crunchifyClass = classLoaderForJar.loadClass(classNameResource);
                        if (crunchifyClass != null) {
                            if (!Modifier.isAbstract(crunchifyClass.getModifiers()) && !Modifier.isInterface(crunchifyClass.getModifiers())) {
                                createFile(crunchifyClass, destinationDirectory);
                            }
                        }
                    } catch (final NoClassDefFoundError e) {
                        LOGGER.logp(Level.SEVERE, "JunitTestGenerator", "generateJunitTestClasses", e.getMessage(), e);
                    } catch (final ClassNotFoundException e) {
                        LOGGER.logp(Level.SEVERE, "JunitTestGenerator", "generateJunitTestClasses", e.getMessage(), e);
                    }
                }
            }
            classLoaderForJar.close();
        } catch (final IOException e) {
            LOGGER.logp(Level.SEVERE, "JunitTestGenerator", "generateJunitTestClasses", e.getMessage(), e);
        }
    }

    private void createFile(final Class<?> crunchifyClass, final String path) {
        try {
            String directoryPath = crunchifyClass.getPackage().getName();
            directoryPath = directoryPath.replace(".", "/");

            final File dir = new File(path + "/" + directoryPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            final Path file = Paths.get(path + "/" + directoryPath + "/" + crunchifyClass.getSimpleName() + "Test.java");

            final List<String> javaLines = new ArrayList<>();

            final Set<String> allImports = getAllImports(crunchifyClass);

            javaLines.add("package " + crunchifyClass.getPackage().getName() + ";");
            javaLines.add("");
            javaLines.add("import static org.junit.Assert.*;");
            javaLines.add("import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;");

            javaLines.add("import com.alibaba.fastjson.JSON;");
            javaLines.add("import com.alibaba.fastjson.JSONObject;");
            javaLines.add("import com.fasterxml.jackson.core.JsonProcessingException;");
            javaLines.add("import com.fasterxml.jackson.databind.ObjectMapper;");
            javaLines.add("import org.junit.*;");
            javaLines.add("import org.junit.runners.MethodSorters;");
            javaLines.add("import org.springframework.http.*;");
            javaLines.add("import org.springframework.test.web.servlet.MvcResult;");
            javaLines.add("import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;");
            javaLines.add("import org.springframework.util.LinkedMultiValueMap;");
            javaLines.add("import org.springframework.util.MultiValueMap;");
            javaLines.add("import org.springframework.web.client.RestClientException;");
            javaLines.add("import org.springframework.web.client.RestTemplate;");

            javaLines.add("import java.util.*;");


//            javaLines.add("import org.junit.Test;");
//            javaLines.add("");
//            for (final String imports : allImports) {
//                javaLines.add("import " + imports + ";");
//            }
            javaLines.add("");
            javaLines.add("/**");
            javaLines.add(" * " + crunchifyClass.getSimpleName() + " Tester.");
            javaLines.add(" *");
            javaLines.add(" * @author hejian");
            javaLines.add(" * @version 1.0");
            javaLines.add(" * @since <pre>" + DateUtil.getToday() + "</pre>");
            javaLines.add(" */");
            javaLines.add("@FixMethodOrder(MethodSorters.NAME_ASCENDING)");
            javaLines.add("public class " + crunchifyClass.getSimpleName() + "Test {");

            //
            boolean urlflag = false;
            Annotation[] annotations = crunchifyClass.getAnnotations();
            for (Annotation annotation : annotations) {
                String s = annotation.toString();
                if (s.startsWith("@org.springframework.web.bind.annotation.") && s.contains("value=[")) {
                    //@org.springframework.web.bind.annotation.PostMapping(path=[], headers=[], name=, produces=[], params=[], value=[/add], consumes=[])
                    String s1 = s.substring(s.indexOf("value=[") + "value=[".length());
                    String api = s1.substring(0, s1.indexOf("]"));
                    api = api.startsWith("/") ? api.substring(1) : api;
                    javaLines.add("    private String url = \"http://127.0.0.1:" + modulePort + "/" + api + "\";");
                    urlflag = true;
                }
            }
            if (!urlflag) {

                javaLines.add("    private String url = \"http://127.0.0.1:" + modulePort + "/\";");
            }


            javaLines.add("    private Integer id = 9999;");
            javaLines.add("    private RestTemplate restTemplate = null;");
            javaLines.add("");
            javaLines.add("    @Before");
            javaLines.add("    public void before() throws Exception {");
            javaLines.add("        //Can test all controllers");
            javaLines.add("        restTemplate = new RestTemplate();");
            javaLines.add("    }");
            javaLines.add("");
            javaLines.add("    @After");
            javaLines.add("    public void after() throws Exception {");
            javaLines.add("    }");
            javaLines.add("");

            final Constructor<?>[] declaredConstructors = crunchifyClass.getDeclaredConstructors();
            int i = 0;
            for (final Constructor<?> declaredConstructor : declaredConstructors) {
                if (declaredConstructor.isAccessible()) {
                    javaLines.add("   /**");
                    javaLines.add("    * " + crunchifyClass.getSimpleName() + ".");
                    final Class<?>[] params = declaredConstructor.getParameterTypes();
                    if (params != null && params.length > 0) {
                        javaLines.add("    * With params: ");
                        javaLines.add("    * <ul>");
                        for (final Class<?> class1 : params) {
                            javaLines.add("    *     <li>" + class1.getSimpleName() + "</li>");
                        }
                        javaLines.add("    * </ul>");
                    }
                    javaLines.add("    */");
                    javaLines.add("    @Test");
                    javaLines.add("    public void test" + upperCaseFirst(crunchifyClass.getSimpleName()) + i + "() {");
                    String concatenatedParms = "";
                    if (params != null && params.length > 0) {
                        int j = 0;
                        for (final Class<?> class1 : params) {
                            if (class1.isPrimitive()) {
                                javaLines.add("        " + class1.getSimpleName() + " param" + j + ";");
                            } else {
                                javaLines.add("        " + class1.getSimpleName() + " param" + j + " = null;");
                            }
                            concatenatedParms = concatenatedParms + "param" + j + " ";
                            j++;
                        }
                        concatenatedParms = concatenatedParms.substring(0, concatenatedParms.length() - 1);
                        concatenatedParms = concatenatedParms.replaceAll(" ", ", ");
                    }
                    javaLines.add("        " + crunchifyClass.getSimpleName() + " " + lowerCaseFirst(crunchifyClass.getSimpleName()) + " = new " + crunchifyClass.getName() + "(" + concatenatedParms + ");");
                    javaLines.add("        ");
                    javaLines.add("        // Add assertions");
                    javaLines.add("        ");
                    javaLines.add("    }");
                    javaLines.add("");
                    i++;
                }
            }

            final Method[] declaredMethods = crunchifyClass.getDeclaredMethods();
            for (final Method declaredMethod : declaredMethods) {
                if (Modifier.isPublic(declaredMethod.getModifiers())) {
                    javaLines.add("    /**");
                    //javaLines.add("    * " + declaredMethod.getName() + ".");
                    final Parameter[] parameters = declaredMethod.getParameters();
                    final Class<?>[] params = declaredMethod.getParameterTypes();
                    String concatenatedParams = "";
                    if (params != null && params.length > 0) {
                        //javaLines.add("    * With params: ");
                        //javaLines.add("    * <ul>");
                        for (final Class<?> class1 : params) {
                            //javaLines.add("    *     <li>" + class1.getSimpleName() + "</li>");
                            String paramName = class1.getSimpleName();
                            if (class1.isArray()) {
                                paramName = "Array";
                            }
                            concatenatedParams = concatenatedParams + "参数 " + paramName + " ";
                        }
                        //javaLines.add("    * </ul>");
                    }
                    javaLines.add("     *");
                    javaLines.add("     */");
                    javaLines.add("    @Test");
                    boolean addflag = false;
                    boolean updateflag = false;
                    boolean deleteflag = false;
                    boolean queryflag = false;
                    if (declaredMethod.getName().startsWith("add")) {
                        javaLines.add("    public void test1" + upperCaseFirst(declaredMethod.getName()) + "() {");
                        addflag = true;
                    } else if (declaredMethod.getName().startsWith("get") || declaredMethod.getName().startsWith("query")) {
                        javaLines.add("    public void test2" + upperCaseFirst(declaredMethod.getName()) + "() {");
                        queryflag = true;
                    } else if (declaredMethod.getName().startsWith("update")) {
                        javaLines.add("    public void test3" + upperCaseFirst(declaredMethod.getName()) + "() {");
                        updateflag = true;
                    } else if (declaredMethod.getName().startsWith("delete")) {
                        javaLines.add("    public void test4" + upperCaseFirst(declaredMethod.getName()) + "() {");
                        deleteflag = true;
                    } else {
                        javaLines.add("    public void test3" + upperCaseFirst(declaredMethod.getName()) + "() {");
                    }

                    //
                    String api = null;
                    annotations = declaredMethod.getAnnotations();
                    for (Annotation annotation : annotations) {
                        String s = annotation.toString();
                        if (s.startsWith("@org.springframework.web.bind.annotation.") && s.contains("value=[")) {
                            //@org.springframework.web.bind.annotation.PostMapping(path=[], headers=[], name=, produces=[], params=[], value=[/add], consumes=[])
                            String s1 = s.substring(s.indexOf("value=[") + "value=[".length());
                            api = s1.substring(0, s1.indexOf("]"));
                            if (api.contains("{") && api.contains("}")) {
                                javaLines.add("        //String api = \"" + api + "\";");
                                for (int m = 0; m < 20; m++) {
                                    if (api.contains("{") && api.contains("}")) {
                                        String rs = api.substring(api.indexOf("{") + 1, api.indexOf("}"));
                                        if (rs.toLowerCase().contains("id")) {
                                            api = api.replace("{" + rs + "}", "9999");
                                        } else if (rs.toLowerCase().contains("pageSize")) {
                                            api = api.replace("{" + rs + "}", "20");
                                        }  else if (rs.toLowerCase().contains("currentPageNum")) {
                                            api = api.replace("{" + rs + "}", "1");
                                        }  else if (rs.toLowerCase().contains("size")) {
                                            api = api.replace("{" + rs + "}", "20");
                                        } else if (rs.toLowerCase().contains("num")) {
                                            api = api.replace("{" + rs + "}", "1");
                                        } else {
                                            api = api.replace("{" + rs + "}", rs);
                                        }
                                    }
                                }
                                javaLines.add("        String api = \"" + api + "\";");
                                javaLines.add("");
                            } else {
                                javaLines.add("        String api = \"" + api + "\";");
                            }
                        }
                    }

//                    if (concatenatedParams == "") {
//                        javaLines.add("    public void test" + upperCaseFirst(declaredMethod.getName()) + "() {");
//                    } else {
//                        javaLines.add("    public void test" + upperCaseFirst(declaredMethod.getName()) + "With" + concatenatedParams + "() {");
//                    }
                    boolean mapflag = false;
                    String concatenatedParms = "";
                    String urlparam = "";
                    if (params != null && params.length > 0) {
                        int j = 0;
                        for (final Class<?> class1 : params) {
//                            if (class1.isPrimitive()) {
//                                javaLines.add("        " + class1.getSimpleName() + " param" + j + ";");
//                            } else
                            if (class1.getSimpleName().equals("int") || class1.getSimpleName().equals("Integer")) {
                                //todo

                                //MessageFormat.format(api, 9999);
                                //flag = true;
                                try {
                                    if (parameters[j].getType().getSimpleName().equals("int") && parameters[j + 1].getType().getSimpleName().equals("int")) {
                                        javaLines.add("        " + class1.getSimpleName() + " p" + j + " = 20;");
                                    } else {
                                        javaLines.add("        " + class1.getSimpleName() + " p" + j + " = 1;");
                                    }
                                } catch (Exception e) {
                                    javaLines.add("        " + class1.getSimpleName() + " p" + j + " = 9999;");
                                }


                            } else if (class1.getSimpleName().equals("String")) {
                                //todo
                                //MessageFormat.format(api, class1.getSimpleName() + "100");
                                //flag = true;
                                javaLines.add("        " + class1.getSimpleName() + " p" + j + " = \"" + class1.getSimpleName() + "100\";");
                            } else {
                                if (updateflag == true) {
                                    javaLines.add("        Integer id = 1010;");
                                }

                                if (deleteflag == true) {
                                    javaLines.add("        int[] ids = {9999, 9999};");
                                }

                                if (deleteflag == false) {
                                    javaLines.add("        ");
                                    javaLines.add("        Map map = new HashMap<String, Object>();");
                                }
                                if (updateflag == true) {
                                    javaLines.add("        map.put(\"id\", id);");
                                }
                                Field[] declaredFields = class1.getDeclaredFields();
                                for (Field field : declaredFields) {
                                    // 如果类型是String
                                    if (field.getGenericType().toString().equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                                        javaLines.add("        map.put(\"" + field.getName() + "\", \"" + field.getName() + "100\");");
                                        urlparam = urlparam + "&" + field.getName() + "=" + field.getName() + "100";
                                    } else if (field.getGenericType().toString().equals("class java.lang.Integer") || field.getGenericType().toString().equals("class java.lang.Double") || field.getGenericType().toString().equals("int") || field.getGenericType().toString().equals("java.lang.Class<java.lang.Integer>")) {
                                        if (updateflag == true && field.getName().equals("id")) {
                                            //
                                        } else {
                                            javaLines.add("        map.put(\"" + field.getName() + "\", 9999);");
                                        }

                                        urlparam = urlparam + "&" + field.getName() + "=9999";
                                    } else if (field.getGenericType().toString().equals("class java.lang.Boolean")) {
                                        javaLines.add("        map.put(\"" + field.getName() + "\", false);");
                                        urlparam = urlparam + "&" + field.getName() + "=false";
                                    } else if (field.getGenericType().toString().equals("boolean")) {
                                        javaLines.add("        map.put(\"" + field.getName() + "\", false);");
                                        urlparam = urlparam + "&" + field.getName() + "=false";
                                    } else if (field.getGenericType().toString().equals("class java.util.Date")) {
                                        javaLines.add("        map.put(\"" + field.getName() + "\", \"2019-12-12\");");
                                        urlparam = urlparam + "&" + field.getName() + "=2019-12-12";
//                                    } else if (field.getType().toString().equals("interface java.util.List")) {
//                                        javaLines.add("        // " + concatenatedParams);
//
//                                        javaLines.add("        List list = new ArrayList();");
//                                        javaLines.add("        Map m = new HashMap<String, Object>();");
//                                        javaLines.add("        m.put(\"id\", 9999);");
//                                        javaLines.add("        list.add(m);");
//                                        javaLines.add("        map.put(\"" + field.getName() + "\", list);");
                                    } else {
                                        javaLines.add("        // " + concatenatedParams);
                                        javaLines.add("        map.put(\"" + field.getName() + "\", \"" + field.getName() + "100\");");
                                    }
                                }
                                mapflag = true;
                                //System.out.println(class1);
                                //javaLines.add("        " + class1.getSimpleName() + " param" + j + " = null;");
                            }
                            concatenatedParms = concatenatedParms + "param" + j + " ";
                            j++;
                        }
                        concatenatedParms = concatenatedParms.substring(0, concatenatedParms.length() - 1);
                        concatenatedParms = concatenatedParms.replaceAll(" ", ", ");
                    }
                    urlparam = urlparam.length() > 0 ? "?" + urlparam.substring(1) : "";
                    final boolean isStatic = Modifier.isStatic(declaredMethod.getModifiers());
                    //if (!isStatic) {
                    //javaLines.add("        " + crunchifyClass.getSimpleName() + " " + lowerCaseFirst(crunchifyClass.getSimpleName()) + " = null;");
                    //}

                    final Class<?>[] declaredExceptionList = declaredMethod.getExceptionTypes();
                    String prefix = "        ";
//                    if (declaredExceptionList != null && declaredExceptionList.length > 0) {
//                        javaLines.add("        try {");
//                        prefix = prefix + "    ";
//                    }

//                    final Class<?> returnMethod = declaredMethod.getReturnType();
//                    if (returnMethod == null) {
//                        if (isStatic) {
//                            javaLines.add(prefix + crunchifyClass.getSimpleName() + "." + declaredMethod.getName() + "(" + concatenatedParms + ");");
//                        } else {
//                            javaLines.add(prefix + lowerCaseFirst(crunchifyClass.getSimpleName()) + "." + declaredMethod.getName() + "(" + concatenatedParms + ");");
//                        }
//                    } else {
//                        if (isStatic) {
//                            javaLines.add(prefix + returnMethod.getSimpleName() + " " + lowerCaseFirst(returnMethod.getSimpleName()) + "Result = " + crunchifyClass.getSimpleName() + "." + declaredMethod.getName() + "(" + concatenatedParms + ");");
//                        } else {
//                            javaLines.add(prefix + returnMethod.getSimpleName() + " " + lowerCaseFirst(returnMethod.getSimpleName()) + "Result = " + lowerCaseFirst(crunchifyClass.getSimpleName()) + "." + declaredMethod.getName() + "(" + concatenatedParms + ");");
//                        }
//                    }
//                    javaLines.add(prefix);
//                    javaLines.add(prefix + "// Add assertions");
//                    javaLines.add(prefix);
                    javaLines.add(prefix + "");
                    javaLines.add(prefix + "//");
                    javaLines.add(prefix + "HttpHeaders headers = new HttpHeaders();");
                    javaLines.add(prefix + "headers.setContentType(MediaType.APPLICATION_JSON_UTF8);");
                    if (params == null || params.length == 0 || mapflag == false) {
                        javaLines.add(prefix + "HttpEntity formEntity = new HttpEntity(headers);");
                    } else {
                        if (deleteflag == true) {
                            javaLines.add(prefix + "HttpEntity formEntity = new HttpEntity(ids, headers);");
                        } else if (queryflag == true) {
                            javaLines.add(prefix + "HttpEntity formEntity = new HttpEntity(headers);");
                        } else {
                            javaLines.add(prefix + "HttpEntity formEntity = new HttpEntity(map, headers);");
                        }

                    }

                    if (addflag) {
                        javaLines.add(prefix + "String contentAsString = restTemplate.postForObject(url + api, formEntity, String.class);");
                    } else if (updateflag) {
                        javaLines.add(prefix + "// 发送请求");
                        javaLines.add(prefix + "ResponseEntity<String> resultEntity = restTemplate.exchange(url + api, HttpMethod.PUT, formEntity, String.class);");
                        javaLines.add(prefix + "String contentAsString = resultEntity.getBody();");

                    } else if (deleteflag) {
                        javaLines.add(prefix + "// 发送请求");
                        javaLines.add(prefix + "ResponseEntity<String> resultEntity = restTemplate.exchange(url + api, HttpMethod.DELETE, formEntity, String.class);");
                        javaLines.add(prefix + "String contentAsString = resultEntity.getBody();");
                    } else if (queryflag) {
                        javaLines.add(prefix + "String contentAsString = restTemplate.getForObject(url + api" + (urlparam.length() > 0 ? " + \"" + urlparam + "\"" : "") + ", String.class, formEntity);");
                    } else {
                        javaLines.add(prefix + "String contentAsString = restTemplate.getForObject(url + api, String.class, formEntity);");
                    }
                    javaLines.add(prefix + "");
                    javaLines.add(prefix + "JSONObject jsonObject = JSON.parseObject(contentAsString);");
                    javaLines.add(prefix + "System.out.println(\"==>contentAsString: \" + contentAsString);");

                    if (addflag == true) {
                        javaLines.add(prefix + "id = Integer.valueOf(String.valueOf(JSON.parseObject(String.valueOf(jsonObject.get(\"data\"))).get(\"id\")));");
                        javaLines.add(prefix + "System.out.println(\"==>id=\" + id);");
                        javaLines.add(prefix + "Assert.assertNotNull(id);");
                    } else {
                        javaLines.add(prefix + "Assert.assertNotNull(contentAsString);");
                    }

//                    if (declaredExceptionList != null && declaredExceptionList.length > 0) {
//                        for (final Class<?> class1 : declaredExceptionList) {
//                            javaLines.add("        } catch (" + class1.getSimpleName() + " e) {");
//                            javaLines.add(prefix + " // Handle exception");
//                        }
//                        javaLines.add("        }");
//                    }
                    javaLines.add("    }");
                    javaLines.add("");
                }
            }

            javaLines.add("");

            javaLines.add("}");

            Files.write(file, javaLines, Charset.forName("UTF-8"));
        } catch (final IOException e) {
            LOGGER.logp(Level.SEVERE, "JunitTestGenerator", "createFile", e.getMessage(), e);
        }
    }

    private Set<String> getAllImports(final Class<?> crunchifyClass) {
        final Set<String> result = new HashSet<>();

        final Constructor<?>[] declaredConstructors = crunchifyClass.getDeclaredConstructors();
        for (final Constructor<?> declaredConstructor : declaredConstructors) {
            final Class<?>[] params = declaredConstructor.getParameterTypes();
            if (params != null && params.length > 0) {
                for (final Class<?> class1 : params) {
                    if (!class1.isPrimitive() && !class1.isArray()) {
                        result.add(class1.getName());
                    }
                }
            }
        }

        final Method[] declaredMethods = crunchifyClass.getDeclaredMethods();
        for (final Method declaredMethod : declaredMethods) {
            final Class<?>[] params = declaredMethod.getParameterTypes();
            if (params != null && params.length > 0) {
                for (final Class<?> class1 : params) {
                    if (!class1.isPrimitive() && !class1.isArray()) {
                        result.add(class1.getName());
                    }
                }
            }
            final Class<?>[] declaredExceptionList = declaredMethod.getExceptionTypes();
            if (declaredExceptionList != null && declaredExceptionList.length > 0) {
                for (final Class<?> class1 : declaredExceptionList) {
                    if (!class1.isPrimitive() && !class1.isArray()) {
                        result.add(class1.getName());
                    }
                }
            }

            final Class<?> returnMethod = declaredMethod.getReturnType();
            if (returnMethod != null) {
                if (!returnMethod.isPrimitive() && !returnMethod.isArray()) {
                    result.add(returnMethod.getName());
                }
            }
        }

        return result;
    }

    private String lowerCaseFirst(final String value) {
        // Convert String to char array.
        final char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toLowerCase(array[0]);
        // Return string.
        return new String(array);
    }

    private String upperCaseFirst(final String value) {
        // Convert String to char array.
        final char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);
        // Return string.
        return new String(array);
    }

    private Map<String, List<String>> getAllObjectListFromJar(final String jarAbsolutePath) {
        final Map<String, List<String>> result = new HashMap<>();
        try {
            final ZipInputStream zip = new ZipInputStream(new FileInputStream(jarAbsolutePath));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    // This ZipEntry represents a class. Now, what class does it represent?
                    String className = entry.getName().replaceAll("/", "."); // including ".class"
                    if (className != null) {
                        className = className.substring(0, className.length() - ".class".length());
                        final String packageName = className.substring(0, className.lastIndexOf("."));
                        List<String> classNameList = result.get(packageName);
                        if (classNameList == null) {
                            classNameList = new ArrayList<>();
                            result.put(packageName, classNameList);
                        }
                        classNameList.add(className.substring(className.lastIndexOf(".") + 1, className.length()));
                    }
                }
            }
            zip.close();
        } catch (final IOException e) {
            LOGGER.logp(Level.SEVERE, "JunitTestGenerator", "getAllObjectListFromJar", e.getMessage(), e);
        }

        return result;
    }

    private Map<String, List<String>> getAllObjectListFromDirectory(final String absolutePath) {
        final Map<String, List<String>> result = new HashMap<>();
        final List<String> fileList = getFilesInPath(absolutePath);
        for (final String fileName : fileList) {
            // This ZipEntry represents a class. Now, what class does it represent?
            String className = fileName.replaceAll("/", ".").replaceAll("\\\\", "."); // including ".class"
            if (className != null) {
                className = className.substring(0, className.length() - ".class".length());
                final String packageName = className.substring(0, className.lastIndexOf("."));
                List<String> classNameList = result.get(packageName);
                if (classNameList == null) {
                    classNameList = new ArrayList<>();
                    result.put(packageName, classNameList);
                }
                classNameList.add(className.substring(className.lastIndexOf(".") + 1, className.length()));
            }
        }

        return result;
    }


    private List<String> getFilesInPath(final String path) {

        final List<String> result = new ArrayList<>();
        try {
            final File directory = new File(path);
            if (directory.isDirectory()) {
                final List<File> files = (List<File>) FileUtils.listFiles(directory, null, true);
                for (final File currentFile : files) {
                    String fileName = currentFile.getAbsolutePath();
                    if (!fileName.contains("ControllerInterceptor") && fileName.contains("Controller")) {
                        if (fileName != null && fileName.endsWith(".class") && !fileName.endsWith("package-info.class")) {
                            fileName = fileName.replace(path, "");
                            if (fileName.startsWith("\\")) {
                                fileName = fileName.substring(1);
                            }
                            result.add(fileName);
                        }
                    }
                }
            }
        } catch (final IllegalArgumentException e) {
            LOGGER.logp(Level.SEVERE, "JunitTestGenerator", "getFilesInPath", e.getMessage(), e);
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        final JunitTestGenerator generator = new JunitTestGenerator();
        final List<String> dependentJarsListAbsolutePath = new ArrayList<>();
        String targetPath = Class.class.getClass().getResource("/").getPath();
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            targetPath = targetPath.substring(1, targetPath.length() - 1).replaceAll("/", "\\\\");
        }
        String srcPath = targetPath.replace("target", "src").replace("classes", "test/java");
        String appConfig = targetPath.replace("target", "src/main/resources").replace("classes", "application.yml");
        if (os.contains("Windows")) {
            appConfig = appConfig.replaceAll("/", "\\\\");
        }
        Yaml yaml = new Yaml();
        Map load = (Map) yaml.load(new FileInputStream(new File(appConfig)));
        generator.modulePort = Integer.valueOf(((Map) (load.get("server"))).get("port").toString()).intValue();
        generator.generateJunitTestClasses(srcPath, targetPath, dependentJarsListAbsolutePath);
    }

}