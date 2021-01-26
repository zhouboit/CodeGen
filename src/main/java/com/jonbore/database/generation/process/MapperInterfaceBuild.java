package com.jonbore.database.generation.process;


import com.jonbore.database.generation.entity.Configuration;
import com.jonbore.database.generation.entity.Table;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bo.zhou
 * @date 2021/01/01 下午7:03
 */
public class MapperInterfaceBuild {

    public static void write(Table table, Configuration configuration) {
        baseInterfaceMapper(configuration);
        File entity = new File(configuration.getHome() + "/mapper/" + table.getUpperCamelCaseName() + "Mapper.java");
        if (entity.exists()) {
            entity.delete();
        }
        entity.getParentFile().mkdirs();
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".mapper;\n\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.").append(table.getUpperCamelCaseName()).append(";\n\n")
                .append("import org.springframework.stereotype.Component;\n\n")
                .append(ConnectionSelect.getJavaAuthor(table))
                .append("@Component\n")
                .append("public interface ").append(table.getUpperCamelCaseName()).append("Mapper extends BaseInterfaceMapper<").append(table.getUpperCamelCaseName()).append("> {\n\n")
                .append("}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void baseInterfaceMapper(Configuration configuration) {
        File entity = new File(configuration.getHome() + "/mapper/BaseInterfaceMapper.java");
        if (entity.exists()) {
            return;
        }
        entity.getParentFile().mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".mapper;\n\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.TrackableEntity;\n\n")
                .append("import java.util.List;\n")
                .append("import java.util.Map;\n")
                .append("/**\n" +
                        " * @Description Mybatis数据库接口基类\n" +
                        " * @Author " + System.getProperty("user.name") + "\n" +
                        " * @Date " + dateFormat.format(new Date()) + "\n" +
                        " */\n")
                .append("public interface BaseInterfaceMapper<Entity extends TrackableEntity> {\n\n")
                .append("   void save(Entity entity);\n\n")
                .append("   Integer update(Entity entity);\n\n")
                .append("   Integer deleteById(String id);\n\n")
                .append("   Entity findById(String id);\n\n")
                .append("   List<Entity> findAll();\n\n")
                .append("   List<Entity> findByMap(Map<String, Object> map);\n\n")
                .append("   Integer getCount(Map<String, Object> map);\n\n")
                .append("   List<Entity> findByPage(Map<String, Object> map);\n\n");
        stringBuffer.append("}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
