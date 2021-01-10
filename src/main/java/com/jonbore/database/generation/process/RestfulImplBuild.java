package com.jonbore.database.generation.process;

import com.jonbore.database.generation.entity.Configuration;
import com.jonbore.database.generation.entity.Table;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bo.zhou
 * @date 2021/1/4 下午2:39
 */
public class RestfulImplBuild {
    public static void write(Table table, Configuration configuration) {
        baseImpl(configuration);
        File entity = new File(configuration.getHome() + "/restful/impl/" + table.getUpperCamelCaseName() + "RestServer.java");
        if (entity.exists()) {
            entity.delete();
        }
        entity.getParentFile().mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".restful.impl;\n\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.").append(table.getUpperCamelCaseName()).append(";\n")
                .append("import ").append(configuration.getParentPackage()).append(".service.").append(table.getUpperCamelCaseName()).append("Service;\n")
                .append("import ").append(configuration.getParentPackage()).append(".service.BaseService;\n\n")
                .append("import org.springframework.stereotype.Controller;\n" +
                        "import lombok.extern.slf4j.Slf4j;\n" +
                        "import org.springframework.web.bind.annotation.ResponseBody;\n" +
                        "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                        "\n" +
                        "import javax.annotation.Resource;\n\n")
                .append("/**\n" +
                        " * @Description Web API层基础服务接口实现\n" +
                        " * @Author " + System.getProperty("user.name") + "\n" +
                        " * @Date " + dateFormat.format(new Date()) + "\n" +
                        " */\n")
                .append(
                                "@Slf4j\n" +
                                "@Controller\n" +
                                "@ResponseBody\n" +
                                "@RequestMapping(\"" + table.getLowerCamelCaseName() + "\")\n" +
                                "public class " + table.getUpperCamelCaseName() + "RestServer extends BaseRestfulImpl<" + table.getUpperCamelCaseName() + "> {\n" +
                                "    private static " + table.getUpperCamelCaseName() + "Service " + table.getLowerCamelCaseName() + "Service;\n" +
                                "\n" +
                                "    @Resource\n" +
                                "    public void set" + table.getUpperCamelCaseName() + "Service(" + table.getUpperCamelCaseName() + "Service " + table.getLowerCamelCaseName() + "Service) {\n" +
                                "        " + table.getUpperCamelCaseName() + "RestServer." + table.getLowerCamelCaseName() + "Service = " + table.getLowerCamelCaseName() + "Service;\n" +
                                "    }\n" +
                                "\n" +
                                "    @Override\n" +
                                "    public BaseService<" + table.getUpperCamelCaseName() + "> getBaseService() {\n" +
                                "        return " + table.getLowerCamelCaseName() + "Service;\n" +
                                "    }\n" +
                                "}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void baseImpl(Configuration configuration) {
        File entity = new File(configuration.getHome() + "/restful/impl/BaseRestfulImpl.java");
        if (entity.exists()) {
            return;
        }
        entity.getParentFile().mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".restful.impl;\n\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.TrackableEntity;\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.Page;\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.JsonViewObject;\n")
                .append("import ").append(configuration.getParentPackage()).append(".exception.ServiceException;\n")
                .append("import ").append(configuration.getParentPackage()).append(".mapper.BaseInterfaceMapper;\n")
                .append("import ").append(configuration.getParentPackage()).append(".restful.BaseRestful;\n")
                .append("import ").append(configuration.getParentPackage()).append(".service.BaseService;\n\n")
                .append("import com.alibaba.fastjson.JSON;\n" +
                        "import com.alibaba.fastjson.JSONObject;\n" +
                        "import com.google.common.collect.Maps;\n" +
                        "import lombok.extern.slf4j.Slf4j;\n" +
                        "import org.springframework.web.bind.annotation.PathVariable;\n" +
                        "import org.springframework.web.bind.annotation.RequestBody;\n\n" +
                        "import java.util.List;\n" +
                        "import java.util.Map;\n\n")
                .append("/**\n" +
                        " * @Description Web API层基础服务接口实现\n" +
                        " * @Author " + System.getProperty("user.name") + "\n" +
                        " * @Date " + dateFormat.format(new Date()) + "\n" +
                        " */\n")
                .append("@Slf4j\n" +
                        "public abstract class BaseRestfulImpl<Entity extends TrackableEntity> implements BaseRestful<Entity> {\n" +
                        "    /**\n" +
                        "     * 抽象方法，子类需要实现，得到基础服务接口\n" +
                        "     *\n" +
                        "     * @return 各子类相对应的Service接口\n" +
                        "     */\n" +
                        "    public abstract BaseService<Entity> getBaseService();\n" +
                        "\n" +
                        "    /**\n" +
                        "     * {\n" +
                        "     * \"content\": \"{\"endRowNum\":10,\"objCondition\":null,\"pageNumber\":1,\"pageSize\":10,\"startRowNum\":0,\"total\":2,\"totalPageNum\":1,\n" +
                        "     * \"rows\":[\n" +
                        "     * {\"createDate\":\"2015-09-29 15:30:42\",\"groupName\":\"test1\",\"id\":\"C8879FFFB2064E66BB7A7D4ED052C9DE\",\"username\":null,\"webPort\":\"5052\"},\n" +
                        "     * {\"createDate\":\"2015-09-29 14:51:46\",\"groupName\":\"test2\",\"id\":\"122830C1DC334517BC7BCE79F62D4FD5\",\"username\":null,\"webPort\":\"5052\"}\n" +
                        "     * ]}\",\n" +
                        "     * \"message\": \"\",\n" +
                        "     * \"status\": \"success\"\n" +
                        "     * }\n" +
                        "     *\n" +
                        "     * @param page\n" +
                        "     * @return\n" +
                        "     */\n" +
                        "    public JsonViewObject getPage(@RequestBody Page<Entity> page) {\n" +
                        "\n" +
                        "        JsonViewObject jsonView = JsonViewObject.newInstance();\n" +
                        "        String jsonStr = JSON.toJSONString(page);\n" +
                        "        try {\n" +
                        "            Map<String, Object> mapBean = Maps.newHashMap();\n" +
                        "            if (page != null) {\n" +
                        "                if (page.getCondition() != null && page.getCondition() instanceof Map) {\n" +
                        "                    mapBean = (Map<String, Object>) page.getCondition();\n" +
                        "                }\n" +
                        "            }\n" +
                        "            page = this.getBaseService().findByPage(page, mapBean);\n" +
                        "            jsonView.success(page);\n" +
                        "        } catch (ServiceException e) {\n" +
                        "            jsonView.fail(e);\n" +
                        "            log.error(\"{} getPage error, jsonStr:{}\", this.getClass().getSimpleName(), jsonStr, e);\n" +
                        "        }\n" +
                        "        return jsonView;\n" +
                        "    }\n" +
                        "\n" +
                        "    /**\n" +
                        "     * {\n" +
                        "     * \"content\": \"[\n" +
                        "     * {\"createDate\":\"2015-09-29 15:30:42\",\"id\":\"C8879FFFB2064E66BB7A7D4ED052C9DE\",\"name\":\"test1\",\"status\":\"0\"},\n" +
                        "     * {\"createDate\":\"2015-09-29 14:51:46\",\"id\":\"122830C1DC334517BC7BCE79F62D4FD5\",\"name\":\"test2\",\"status\":\"0\"}\n" +
                        "     * ]\",\n" +
                        "     * \"message\": \"\",\n" +
                        "     * \"status\": \"success\"\n" +
                        "     * }\n" +
                        "     *\n" +
                        "     * @return\n" +
                        "     */\n" +
                        "    public JsonViewObject getAll() {\n" +
                        "        JsonViewObject jsonView = JsonViewObject.newInstance();\n" +
                        "        try {\n" +
                        "            List<Entity> list = this.getBaseService().findAll();\n" +
                        "            jsonView.success(list);\n" +
                        "        } catch (Exception e) {\n" +
                        "            jsonView.fail(e);\n" +
                        "            log.error(\"{} getAll error\", this.getClass().getSimpleName(), e);\n" +
                        "        }\n" +
                        "        return jsonView;\n" +
                        "    }\n" +
                        "\n" +
                        "    /**\n" +
                        "     * {\n" +
                        "     * \"content\": \"[\n" +
                        "     * {\"createDate\":\"2015-09-29 15:30:42\",\"id\":\"C8879FFFB2064E66BB7A7D4ED052C9DE\",\"name\":\"test1\",\"status\":\"0\"},\n" +
                        "     * {\"createDate\":\"2015-09-29 14:51:46\",\"id\":\"122830C1DC334517BC7BCE79F62D4FD5\",\"name\":\"test2\",\"status\":\"0\"}\n" +
                        "     * ]\",\n" +
                        "     * \"message\": \"\",\n" +
                        "     * \"status\": \"success\"\n" +
                        "     * }\n" +
                        "     *\n" +
                        "     * @param entity\n" +
                        "     * @return\n" +
                        "     */\n" +
                        "    public JsonViewObject getByWhere(@RequestBody Entity entity) {\n" +
                        "        JsonViewObject jsonView = JsonViewObject.newInstance();\n" +
                        "        String jsonStr = JSON.toJSONString(entity);\n" +
                        "        try {\n" +
                        "            //参数校验过程中修改，兼容实现，restful入参不用map，便于进行参数逐个校验\n" +
                        "            Map params = JSONObject.parseObject(JSONObject.toJSONString(entity), Map.class);\n" +
                        "            List list = this.getBaseService().findByMap(params);\n" +
                        "            jsonView.success(list);\n" +
                        "        } catch (Exception e) {\n" +
                        "            jsonView.fail(e);\n" +
                        "            log.error(\"{} getByWhere error，jsonStr:{}\", this.getClass().getSimpleName(), jsonStr, e);\n" +
                        "        }\n" +
                        "        return jsonView;\n" +
                        "    }\n" +
                        "\n" +
                        "    /**\n" +
                        "     * {\n" +
                        "     * \"content\": \"{\"groupId\":\"D54FAB067D6F47A99136210ED640368E\",\"id\":\"C8879FFFB2064E66BB7A7D4ED052C9DE\",\"status\":\"0\",\"webPort\":\"5052\"}\",\n" +
                        "     * \"message\": \"\",\n" +
                        "     * \"status\": \"success\"\n" +
                        "     * }\n" +
                        "     *\n" +
                        "     * @param id\n" +
                        "     * @return JsonViewObject\n" +
                        "     */\n" +
                        "    public JsonViewObject getById(@PathVariable String id) {\n" +
                        "        JsonViewObject jsonView = JsonViewObject.newInstance();\n" +
                        "        try {\n" +
                        "            Entity entity = this.getBaseService().findById(id);\n" +
                        "            jsonView.success(entity);\n" +
                        "        } catch (Exception e) {\n" +
                        "            jsonView.fail(e);\n" +
                        "            log.error(\"BaseRestfulImpl getById error, id:{}\", id, e);\n" +
                        "        }\n" +
                        "        return jsonView;\n" +
                        "    }\n" +
                        "\n" +
                        "    public JsonViewObject deleteByIds(String ids) {\n" +
                        "        JsonViewObject jsonView = JsonViewObject.newInstance();\n" +
                        "        String[] idArray = ids.split(\",\");\n" +
                        "        try {\n" +
                        "            if (idArray.length > 0) {\n" +
                        "                for (String id : idArray) {\n" +
                        "                    jsonView = this.getBaseService().deleteById(id);\n" +
                        "                }\n" +
                        "            }\n" +
                        "        } catch (ServiceException e) {\n" +
                        "            jsonView.fail(e);\n" +
                        "            log.error(\"BaseRestfulImpl deleteByIds error, id:{}\", ids, e);\n" +
                        "        }\n" +
                        "        return jsonView;\n" +
                        "    }\n" +
                        "\n" +
                        "    public JsonViewObject save(@RequestBody Entity entity) {\n" +
                        "        JsonViewObject jsonView = JsonViewObject.newInstance();\n" +
                        "        try {\n" +
                        "            if (entity != null) {\n" +
                        "                jsonView = this.getBaseService().save(entity);\n" +
                        "            }\n" +
                        "        } catch (ServiceException e) {\n" +
                        "            jsonView.fail(e);\n" +
                        "            log.error(\"BaseRestfulImpl save error, jsonStr:{}\", JSON.toJSONString(entity), e);\n" +
                        "        }\n" +
                        "        return jsonView;\n" +
                        "    }\n" +
                        "\n" +
                        "    public JsonViewObject update(@RequestBody Entity entity) {\n" +
                        "        JsonViewObject jsonView = JsonViewObject.newInstance();\n" +
                        "        try {\n" +
                        "            if (entity != null) {\n" +
                        "                jsonView = this.getBaseService().update(entity);\n" +
                        "            }\n" +
                        "        } catch (ServiceException e) {\n" +
                        "            jsonView.fail(e);\n" +
                        "            log.error(\"BaseRestfulImpl update error, jsonStr:{}\", JSON.toJSONString(entity), e);\n" +
                        "        }\n" +
                        "        return jsonView;\n" +
                        "    }\n" +
                        "}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
