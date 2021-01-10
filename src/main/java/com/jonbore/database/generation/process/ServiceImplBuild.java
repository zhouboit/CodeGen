package com.jonbore.database.generation.process;

import com.jonbore.database.generation.entity.Configuration;
import com.jonbore.database.generation.entity.Table;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bo.zhou
 * @date 2021/1/4 下午12:52
 */
public class ServiceImplBuild {
    public static void write(Table table, Configuration configuration) {
        baseImpl(configuration);
        File entity = new File(configuration.getHome() + "/service/impl/" + table.getUpperCamelCaseName() + "ServiceImpl.java");
        if (entity.exists()) {
            entity.delete();
        }
        entity.getParentFile().mkdirs();
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".service.impl;\n\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.").append(table.getUpperCamelCaseName()).append(";\n")
                .append("import ").append(configuration.getParentPackage()).append(".mapper.").append(table.getUpperCamelCaseName()).append("Mapper;\n")
                .append("import ").append(configuration.getParentPackage()).append(".mapper.BaseInterfaceMapper;\n")
                .append("import ").append(configuration.getParentPackage()).append(".service.").append(table.getUpperCamelCaseName()).append("Service;\n")
                .append(
                        "import lombok.extern.slf4j.Slf4j;\n" +
                        "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "import org.springframework.beans.factory.annotation.Qualifier;\n" +
                        "import org.springframework.stereotype.Service;\n\n")
                .append(ConnectionSelect.getJavaAuthor(table))
                .append("@Slf4j\n")
                .append("@Service(\"").append(table.getLowerCamelCaseName()).append("Service\")\n")
                .append("public class ").append(table.getUpperCamelCaseName()).append("ServiceImpl extends BaseServiceImpl<").append(table.getUpperCamelCaseName()).append(">  implements ").append(table.getUpperCamelCaseName()).append("Service {\n\n")
                .append("   @Autowired\n" +
                        "   @Qualifier(\""+table.getLowerCamelCaseName()+"Mapper\")\n" +
                        "   private "+table.getUpperCamelCaseName()+"Mapper "+table.getLowerCamelCaseName()+"Mapper;\n" +
                        "    \n" +
                        "   @Override\n" +
                        "   public BaseInterfaceMapper<"+table.getUpperCamelCaseName()+"> getBaseInterfaceMapper() {\n" +
                        "       return "+table.getLowerCamelCaseName()+"Mapper;\n" +
                        "   }")
                .append("}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void baseImpl(Configuration configuration) {
        File entity = new File(configuration.getHome() + "/service/impl/BaseServiceImpl.java");
        if (entity.exists()) {
            return;
        }
        entity.getParentFile().mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".service.impl;\n\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.TrackableEntity;\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.Page;\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.JsonViewObject;\n")
                .append("import ").append(configuration.getParentPackage()).append(".exception.ServiceException;\n")
                .append("import ").append(configuration.getParentPackage()).append(".mapper.BaseInterfaceMapper;\n")
                .append("import ").append(configuration.getParentPackage()).append(".service.BaseService;\n\n")
                .append("import org.apache.commons.lang3.StringUtils;\n" +
                        "import lombok.extern.slf4j.Slf4j;\n" +
                        "import org.springframework.transaction.annotation.Transactional;\n" +
                        "import java.util.Date;\n" +
                        "import java.util.List;\n" +
                        "import java.util.UUID;\n" +
                        "import java.util.Map;\n\n")
                .append("/**\n" +
                        " * @Description 逻辑处理层基础服务接口实现\n" +
                        " * @Author " + System.getProperty("user.name") + "\n" +
                        " * @Date " + dateFormat.format(new Date()) + "\n" +
                        " */\n")
                .append("@Transactional(rollbackFor = ServiceException.class)\n" +
                        "@Slf4j\n" +
                        "public abstract class BaseServiceImpl<Entity extends TrackableEntity> implements BaseService<Entity> {\n" +
                        "    /**\n" +
                        "     * 抽象方法需要实现，得到基础服务接口\n" +
                        "     *\n" +
                        "     * @return\n" +
                        "     */\n" +
                        "    public abstract BaseInterfaceMapper<Entity> getBaseInterfaceMapper();\n" +
                        "\n" +
                        "    public Page findByPage(Page page, Map<String, Object> map) throws ServiceException {\n" +
                        "        try {\n" +
                        "            if (map.get(\"keyword\") == null || StringUtils.isBlank((String) map.get(\"keyword\"))) {\n" +
                        "                map.put(\"keyword\", null);\n" +
                        "            }\n" +
                        "            page.setTotal(this.getBaseInterfaceMapper().getCount(map));\n" +
                        "            page = Page.newInstance(page.getPageSize(), page.getTotal(), page.getPageNum());\n" +
                        "            map.put(\"startRowNum\", page.getStartRowNum());\n" +
                        "            map.put(\"pageSize\", page.getPageSize());\n" +
                        "            page.setRows(this.getBaseInterfaceMapper().findByPage(map));\n" +
                        "        } catch (Exception e) {\n" +
                        "            log.error(\"BaseServiceImpl findByPage \", e);\n" +
                        "            throw new ServiceException(e.getMessage(), e);\n" +
                        "        }\n" +
                        "        return page;\n" +
                        "    }\n" +
                        "\n" +
                        "    public JsonViewObject save(Entity entity) throws ServiceException {\n" +
                        "        JsonViewObject jsonViewObject = JsonViewObject.newInstance();\n" +
                        "        try {\n" +
                        "            entity.setId(UUID.randomUUID().toString().replace(\"-\",\"\").toUpperCase());\n" +
                        "            entity.setCreateDate(new Date());\n" +
                        "            this.getBaseInterfaceMapper().save(entity);\n" +
                        "            jsonViewObject.success(entity.getId(), \"数据保存成功！\");\n" +
                        "        } catch (Exception e) {\n" +
                        "            jsonViewObject.fail(\"数据保存失败！\");\n" +
                        "            log.error(\"BaseServiceImpl save \", e);\n" +
                        "            throw new ServiceException(e.getMessage(), e);\n" +
                        "        }\n" +
                        "        return jsonViewObject;\n" +
                        "    }\n" +
                        "\n" +
                        "    public JsonViewObject update(Entity entity) throws ServiceException {\n" +
                        "        JsonViewObject jsonViewObject = JsonViewObject.newInstance();\n" +
                        "        try {\n" +
                        "            entity.setCreateDate(new Date());\n" +
                        "            Integer updateCount = this.getBaseInterfaceMapper().update(entity);\n" +
                        "            jsonViewObject.success(updateCount, \"数据修改成功！\");\n" +
                        "        } catch (Exception e) {\n" +
                        "            jsonViewObject.fail(\"数据修改失败！\");\n" +
                        "            log.error(\"BaseServiceImpl update \", e);\n" +
                        "            throw new ServiceException(e.getMessage(), e);\n" +
                        "        }\n" +
                        "        return jsonViewObject;\n" +
                        "    }\n" +
                        "\n" +
                        "    public JsonViewObject deleteById(String id) throws ServiceException {\n" +
                        "        JsonViewObject jsonViewObject = JsonViewObject.newInstance();\n" +
                        "        try {\n" +
                        "            Integer deleteCount = this.getBaseInterfaceMapper().deleteById(id);\n" +
                        "            if (deleteCount < 1) {\n" +
                        "                jsonViewObject.fail(deleteCount, \"删除数据不存在！\");\n" +
                        "            } else {\n" +
                        "                jsonViewObject.success(deleteCount, \"数据删除成功！\");\n" +
                        "            }\n" +
                        "        } catch (Exception e) {\n" +
                        "            jsonViewObject.fail(\"数据删除失败！\");\n" +
                        "            log.error(\"BaseServiceImpl deleteById \", e);\n" +
                        "            throw new ServiceException(e.getMessage(), e);\n" +
                        "        }\n" +
                        "        return jsonViewObject;\n" +
                        "    }\n" +
                        "\n" +
                        "\n" +
                        "    public Entity findById(String id) throws ServiceException {\n" +
                        "        Entity entity;\n" +
                        "        try {\n" +
                        "            entity = this.getBaseInterfaceMapper().findById(id);\n" +
                        "        } catch (Exception e) {\n" +
                        "            log.error(\"BaseServiceImpl findById \", e);\n" +
                        "            throw new ServiceException(e.getMessage(), e);\n" +
                        "        }\n" +
                        "        return entity;\n" +
                        "    }\n" +
                        "\n" +
                        "    public List<Entity> findAll() throws ServiceException {\n" +
                        "        List<Entity> list;\n" +
                        "        try {\n" +
                        "            list = this.getBaseInterfaceMapper().findAll();\n" +
                        "        } catch (Exception e) {\n" +
                        "            log.error(\"BaseServiceImpl findAll \", e);\n" +
                        "            throw new ServiceException(e.getMessage(), e);\n" +
                        "        }\n" +
                        "        return list;\n" +
                        "    }\n" +
                        "\n" +
                        "    public List<Entity> findByMap(Map<String, Object> map) throws ServiceException {\n" +
                        "        List<Entity> list;\n" +
                        "        try {\n" +
                        "            list = this.getBaseInterfaceMapper().findByMap(map);\n" +
                        "        } catch (Exception e) {\n" +
                        "            log.error(\"BaseServiceImpl findByMap\", e);\n" +
                        "            throw new ServiceException(e.getMessage(), e);\n" +
                        "        }\n" +
                        "        return list;\n" +
                        "    }\n" +
                        "}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
