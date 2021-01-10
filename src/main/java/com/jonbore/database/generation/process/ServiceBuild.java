package com.jonbore.database.generation.process;

import com.jonbore.database.generation.entity.Configuration;
import com.jonbore.database.generation.entity.Table;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bo.zhou
 * @date 2021/1/2 下午6:01
 */
public class ServiceBuild {
    public static void write(Table table, Configuration configuration) {
        baseInterfaceMapper(configuration);
        baseException(configuration);
        File entity = new File(configuration.getHome() + "/service/" + table.getUpperCamelCaseName() + "Service.java");
        if (entity.exists()) {
            entity.delete();
        }
        entity.getParentFile().mkdirs();
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".service;\n\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.").append(table.getUpperCamelCaseName()).append(";\n\n")
//                .append("import ").append(configuration.getParentPackage()).append(".service.BaseService;\n\n")
                .append(ConnectionSelect.getJavaAuthor(table))
                .append("public interface ").append(table.getUpperCamelCaseName()).append("Service extends BaseService<").append(table.getUpperCamelCaseName()).append("> {\n\n")
                .append("}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void baseInterfaceMapper(Configuration configuration) {
        File entity = new File(configuration.getHome() + "/service/BaseService.java");
        if (entity.exists()) {
            return;
        }
        entity.getParentFile().mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".service;\n\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.TrackableEntity;\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.JsonViewObject;\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.Page;\n")
                .append("import ").append(configuration.getParentPackage()).append(".exception.ServiceException;\n")
                .append("import java.util.List;\n")
                .append("import java.util.Map;\n")
                .append("/**\n" +
                        " * @Description 逻辑处理层基础服务接口\n" +
                        " * @Author " + System.getProperty("user.name") + "\n" +
                        " * @Date " + dateFormat.format(new Date()) + "\n" +
                        " */\n")
                .append("public interface BaseService<Entity extends TrackableEntity> {\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 分页查询\n" +
                        "     * @param page\n" +
                        "     * @param map\n" +
                        "     * @return\n" +
                        "     * @throws ServiceException\n" +
                        "     */\n" +
                        "    Page findByPage(Page page, Map<String, Object> map) throws ServiceException;\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 持久化对象的信息\n" +
                        "     * @param entity\n" +
                        "     * @return\n" +
                        "     * @throws ServiceException\n" +
                        "     */\n" +
                        "    JsonViewObject save(Entity entity) throws ServiceException;\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 修改对象的信息\n" +
                        "     * @param entity\n" +
                        "     * @return\n" +
                        "     * @throws ServiceException\n" +
                        "     */\n" +
                        "    JsonViewObject update(Entity entity) throws ServiceException;\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 根据ID 删除指定的对象\n" +
                        "     * @param id\n" +
                        "     * @return\n" +
                        "     * @throws ServiceException\n" +
                        "     */\n" +
                        "    JsonViewObject deleteById(String id) throws ServiceException;\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 根据ID 查找指定的对象\n" +
                        "     * @param id\n" +
                        "     * @return\n" +
                        "     * @throws ServiceException\n" +
                        "     */\n" +
                        "    Entity findById(String id) throws ServiceException;\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 得到所有的对象\n" +
                        "     *\n" +
                        "     * @return\n" +
                        "     * @throws ServiceException\n" +
                        "     */\n" +
                        "    List<Entity> findAll() throws ServiceException;\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 用于多条件查询\n" +
                        "     *\n" +
                        "     * @param map\n" +
                        "     * @return\n" +
                        "     * @throws ServiceException\n" +
                        "     */\n" +
                        "    List<Entity> findByMap(Map<String, Object> map) throws ServiceException;\n" +
                        "}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void baseException(Configuration configuration) {
        File entity = new File(configuration.getHome() + "/exception/ServiceException.java");
        if (entity.exists()) {
            return;
        }
        entity.getParentFile().mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".exception;\n\n")
                .append("/**\n" +
                        " * @Description 逻辑处理层统一返回异常（用于事务回滚等）\n" +
                        " * @Author " + System.getProperty("user.name") + "\n" +
                        " * @Date " + dateFormat.format(new Date()) + "\n" +
                        " */\n")
                .append("public class ServiceException extends Exception {\n\n")
                .append("   private static final long serialVersionUID = 1L;\n\n")
                .append("   public ServiceException(Exception e) {\n")
                .append("       super(e);\n")
                .append("   }\n\n")
                .append("   public ServiceException(String msg) {\n")
                .append("       super(msg);\n")
                .append("   }\n\n")
                .append("   public ServiceException(String msg, Exception e) {\n")
                .append("       super(msg, e);\n")
                .append("   }\n\n")
                .append("}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
