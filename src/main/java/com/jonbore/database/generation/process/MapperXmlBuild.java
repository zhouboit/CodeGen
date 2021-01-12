package com.jonbore.database.generation.process;


import com.jonbore.database.generation.entity.Column;
import com.jonbore.database.generation.entity.Configuration;
import com.jonbore.database.generation.entity.Table;

import java.io.File;
import java.util.stream.Collectors;

/**
 * @author bo.zhou
 * @date 2021/1/1 下午8:05
 */
public class MapperXmlBuild {
    public static void write(Table table, Configuration configuration) {
        File entity = new File(configuration.getResourceHome() + "/resources/configs/mapper/" + table.getUpperCamelCaseName() + "Mapper.xml");
        if (entity.exists()) {
            entity.delete();
        }
        entity.getParentFile().mkdirs();
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n")
                .append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n")
                .append(ConnectionSelect.getXmlAuthor(table))
                .append("<mapper namespace=\"").append(configuration.getParentPackage()).append(".mapper.").append(table.getUpperCamelCaseName()).append("Mapper\">\n\n")
                .append("   <resultMap id=\"baseResultMap\" type=\"").append(configuration.getParentPackage()).append(".entity.").append(table.getUpperCamelCaseName()).append("\">\n");
        for (Column column : table.getColumns()) {
            stringBuffer.append("       <result column=\"").append(column.getColumnName()).append("\" property=\"").append(column.getLowerCamelCaseName()).append("\" jdbcType=\"").append(column.getJdbcType().toUpperCase()).append("\"/>\n");
        }
        stringBuffer.append("   </resultMap>\n\n");
        stringBuffer
                .append("   <sql id=\"Table_Name\">\n")
                .append("       ").append(table.getTablename()).append("\n")
                .append("   </sql>\n\n")
                .append("   <sql id=\"Base_Column_List\">\n")
                .append("       ").append(table.getColumns().stream().map(Column::getColumnName).collect(Collectors.joining(","))).append("\n")
                .append("   </sql>\n\n")
                .append("   <insert id=\"save\" parameterType=\"").append(configuration.getParentPackage()).append(".entity.").append(table.getUpperCamelCaseName()).append("\">\n")
                .append("       INSERT INTO\n")
                .append("       <include refid=\"Table_Name\"/>\n")
                .append("       (\n")
                .append("           <include refid=\"Base_Column_List\"/>\n")
                .append("       )\n")
                .append("       VALUES\n")
                .append("       (\n")
                .append("           ").append(table.getColumns().stream().map(Column::getExpression).collect(Collectors.joining(","))).append("\n")
                .append("       )\n")
                .append("   </insert>\n\n")
                .append("   <delete id=\"deleteById\" parameterType=\"String\">\n")
                .append("       DELETE FROM\n")
                .append("       <include refid=\"Table_Name\"/>\n")
                .append("       WHERE ").append(table.getPrimaryKey()).append(" = ").append(table.getExpressionKey()).append("\n")
                .append("   </delete>\n\n")
                .append("   <update id=\"update\" parameterType=\"").append(configuration.getParentPackage()).append(".entity.").append(table.getUpperCamelCaseName()).append("\">\n")
                .append("       UPDATE\n")
                .append("       <include refid=\"Table_Name\"/>\n")
                .append("       <include refid=\"sql_update\"/>\n")
                .append("       WHERE ").append(table.getPrimaryKey()).append(" = ").append(table.getExpressionKey()).append("\n")
                .append("   </update>\n\n")
                .append("   <select id=\"findById\" resultMap=\"baseResultMap\" parameterType=\"String\">\n")
                .append("       SELECT\n")
                .append("       <include refid=\"Base_Column_List\"/>\n")
                .append("       FROM\n")
                .append("       <include refid=\"Table_Name\"/>\n")
                .append("       WHERE ").append(table.getPrimaryKey()).append("=").append(table.getExpressionKey()).append("\n")
                .append("   </select>\n\n")
                .append("   <select id=\"findAll\" resultMap=\"baseResultMap\">\n")
                .append("       SELECT\n")
                .append("       <include refid=\"Base_Column_List\"/>\n")
                .append("       FROM\n")
                .append("       <include refid=\"Table_Name\"/>\n")
                .append("   </select>\n\n")
                .append("   <select id=\"findByMap\" parameterType=\"java.util.Map\" resultMap=\"baseResultMap\">\n")
                .append("       SELECT\n")
                .append("       <include refid=\"Base_Column_List\"/>\n")
                .append("       FROM\n")
                .append("       <include refid=\"Table_Name\"/>\n")
                .append("       <include refid=\"sql_query\"/>\n")
                .append("   </select>\n\n")
                .append("   <select id=\"getCount\" parameterType=\"java.util.Map\" resultType=\"int\">\n")
                .append("       SELECT count(1) FROM\n")
                .append("       <include refid=\"Table_Name\"/>\n")
                .append("       <include refid=\"sql_query\"/>\n")
                .append("   </select>\n\n")
                .append("   <select id=\"findByPage\" parameterType=\"java.util.Map\" resultMap=\"baseResultMap\">\n")
                .append("       SELECT\n")
                .append("       <include refid=\"Base_Column_List\"/>\n")
                .append("       FROM\n")
                .append("       <include refid=\"Table_Name\"/>\n")
                .append("       <include refid=\"sql_query\"/>\n")
                .append("       LIMIT #{startRowNum}, #{pageSize}\n")
                .append("   </select>\n\n");

        stringBuffer
                .append("   <sql id=\"sql_query\">\n")
                .append("       <where>\n");
        for (Column column : table.getColumns()) {
            if ("DT_CREATE_DATE".equals(column.getColumnName()) || "DT_UPDATE_DATE".equals(column.getColumnName())) {
                continue;
            }
            if ("Date".equals(column.getObjectType())) {
                stringBuffer
                        .append("           <if test=\"").append(column.getLowerCamelCaseName()).append(" != null \">\n")
                        .append("               <![CDATA[\n")
                        .append("                   ").append(column.getColumnName()).append(" = ").append(column.getExpression()).append(",\n")
                        .append("               ]]>\n")
                        .append("           </if>\n");
            } else {
                stringBuffer
                        .append("           <if test=\"").append(column.getLowerCamelCaseName()).append(" != null and ").append(column.getLowerCamelCaseName()).append(" != '' \">\n")
                        .append("               <![CDATA[\n")
                        .append("                   AND ").append(column.getColumnName()).append(" = ").append(column.getExpression()).append("\n")
                        .append("               ]]>\n")
                        .append("           </if>\n");
            }
        }
        stringBuffer.append("       </where>\n")
                .append("   </sql>\n\n")
                .append("   <sql id=\"sql_update\">\n")
                .append("       <set>\n");
        for (Column column : table.getColumns()) {
            if ("Date".equals(column.getObjectType())) {
                stringBuffer
                        .append("           <if test=\"").append(column.getLowerCamelCaseName()).append(" != null \">\n")
                        .append("               <![CDATA[\n")
                        .append("                   ").append(column.getColumnName()).append(" = ").append(column.getExpression()).append(",\n")
                        .append("               ]]>\n")
                        .append("           </if>\n");
            } else {
                stringBuffer
                        .append("           <if test=\"").append(column.getLowerCamelCaseName()).append(" != null and ").append(column.getLowerCamelCaseName()).append(" != '' \">\n")
                        .append("               <![CDATA[\n")
                        .append("                   ").append(column.getColumnName()).append(" = ").append(column.getExpression()).append(",\n")
                        .append("               ]]>\n")
                        .append("           </if>\n");
            }
        }
        stringBuffer.append("       </set>\n")
                .append("   </sql>\n\n")
                .append("</mapper>");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
