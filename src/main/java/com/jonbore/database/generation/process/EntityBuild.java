package com.jonbore.database.generation.process;


import com.jonbore.database.generation.entity.Column;
import com.jonbore.database.generation.entity.Configuration;
import com.jonbore.database.generation.entity.Table;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bo.zhou
 * @date 2020/12/30 下午7:03
 */
public class EntityBuild {
    public static void write(Table table, Configuration configuration) {
        trackableEntity(configuration);
        jsonViewEntity(configuration);
        pageEntity(configuration);
        constants(configuration);
        validationGroups(configuration);
        File entity = new File(configuration.getHome() + "/entity/" + table.getUpperCamelCaseName() + ".java");
        if (entity.exists()) {
            entity.delete();
        }
        entity.getParentFile().mkdirs();
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".entity;\n\n")
                .append("import io.swagger.annotations.ApiModel;\n")
                .append("import io.swagger.annotations.ApiModelProperty;\n")
                .append("import lombok.Setter;\n")
                .append("import lombok.Getter;\n\n");
        for (Column column : table.getColumns()) {
            if ("id".equals(column.getLowerCamelCaseName()) || "createDate".equals(column.getLowerCamelCaseName()) || "updateDate".equals(column.getLowerCamelCaseName())) {
                continue;
            }
            if ("Date".equals(column.getObjectType())) {
                stringBuffer.append("import java.util.Date;\n\n");
            }
        }
        stringBuffer
                .append(ConnectionSelect.getJavaAuthor(table))
                .append("@Setter\n" + "@Getter\n" + "@ApiModel(value = \"").append(table.getLowerCamelCaseName()).append("\", description = \"").append(table.getTableComment()).append("\")\n")
                .append("public class ").append(table.getUpperCamelCaseName()).append(" extends TrackableEntity {\n");
        for (Column column : table.getColumns()) {
            if (StringUtils.isBlank(column.getColumnName())) {
                continue;
            }
            if ("id".equals(column.getLowerCamelCaseName()) || "createDate".equals(column.getLowerCamelCaseName()) || "updateDate".equals(column.getLowerCamelCaseName())) {
                continue;
            }
            stringBuffer
                    .append("    @ApiModelProperty(value = \"").append(column.getColumnComment()).append("\")\n")
                    .append("    private ").append(column.getObjectType()).append(" ").append(column.getLowerCamelCaseName()).append(";\n");
        }
        stringBuffer.append("}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void trackableEntity(Configuration configuration) {
        File entity = new File(configuration.getHome() + "/entity/TrackableEntity.java");
        if (entity.exists()) {
            return;
        }
        entity.getParentFile().mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".entity;\n\n")
                .append("import java.io.Serializable;\n")
                .append("import io.swagger.annotations.ApiModel;\n")
                .append("import io.swagger.annotations.ApiModelProperty;\n")
                .append("import lombok.Setter;\n")
                .append("import lombok.Getter;\n\n")
                .append("import java.util.Date;\n\n")
                .append("/**\n" +
                        " * @Description 实体类基类\n" +
                        " * @Author " + System.getProperty("user.name") + "\n" +
                        " * @Date " + dateFormat.format(new Date()) + "\n" +
                        " */\n")
                .append("@Setter\n")
                .append("@Getter\n")
                .append("@ApiModel(value = \"TrackableEntity\", description = \"实体类基类\")\n")
                .append("public class  TrackableEntity  implements Serializable {\n")
                .append("    private static final long serialVersionUID = -4052705808523280313L;\n" +
                        "    @ApiModelProperty(value = \"主键ID\", required = true, example = \"H01D505B6066F48AF87D9580C34F5CE94\")\n" +
                        "    private String id;\n" +
                        "    @ApiModelProperty(value = \"数据创建时间\", required = true, example = \"" + dateFormat.format(new Date()) + "\")\n" +
                        "    private Date createDate;\n" +
                        "    @ApiModelProperty(value = \"数据更新时间\", required = true, example = \"" + dateFormat.format(new Date()) + "\")\n" +
                        "    private Date updateDate;");
        stringBuffer.append("}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void jsonViewEntity(Configuration configuration) {
        File entity = new File(configuration.getHome() + "/entity/JsonViewObject.java");
        if (entity.exists()) {
            return;
        }
        entity.getParentFile().mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".entity;\n\n")
                .append("import ").append(configuration.getParentPackage()).append(".config.Constants;\n")
                .append("import java.io.Serializable;\n")
                .append("import lombok.Setter;\n")
                .append("import lombok.Getter;\n\n")
                .append("/**\n" +
                        " * @Description 接口传输及页面返回VIEW对象实体\n" +
                        " * @Author " + System.getProperty("user.name") + "\n" +
                        " * @Date " + dateFormat.format(new Date()) + "\n" +
                        " */\n")
                .append("@Setter\n")
                .append("@Getter\n")
                .append("public class JsonViewObject implements Serializable {\n" +
                        "   private Object content;\n" +
                        "\n" +
                        "   private String message;\n" +
                        "\n" +
                        "   private String status;\n" +
                        "\n" +
                        "   private Integer code;\n" +
                        "\n" +
                        "   private Long timestamp;\n" +
                        "\n" +
                        "   private JsonViewObject() {\n" +
                        "   }\n" +
                        "\n" +
                        "   public static JsonViewObject newInstance() {\n" +
                        "      return new JsonViewObject();\n" +
                        "   }\n" +
                        "\n" +
                        "   public static JsonViewObject getNewInstance(Object content, String message, String status) {\n" +
                        "      JsonViewObject jsonViewObject = new JsonViewObject();\n" +
                        "      jsonViewObject.setContent(content);\n" +
                        "      jsonViewObject.setMessage(message);\n" +
                        "      jsonViewObject.setStatus(status);\n" +
                        "      return jsonViewObject;\n" +
                        "   }\n" +
                        "\n" +
                        "   public void success(Object content) {\n" +
                        "      this.content = content;\n" +
                        "      this.status = Constants.JsonView.STATUS_SUCCESS;\n" +
                        "   }\n" +
                        "\n" +
                        "   public void success(Object content, String message) {\n" +
                        "      this.content = content;\n" +
                        "      this.message = message;\n" +
                        "      this.status = Constants.JsonView.STATUS_SUCCESS;\n" +
                        "   }\n" +
                        "\n" +
                        "   public void fail(String message) {\n" +
                        "      this.message = message;\n" +
                        "      this.status = Constants.JsonView.STATUS_FAIL;\n" +
                        "   }\n" +
                        "\n" +
                        "   public void fail(Object content) {\n" +
                        "      this.content = content;\n" +
                        "      this.status = Constants.JsonView.STATUS_FAIL;\n" +
                        "   }\n" +
                        "\n" +
                        "   public void fail(Object content, String message) {\n" +
                        "      this.content = content;\n" +
                        "      this.message = message;\n" +
                        "      this.status = Constants.JsonView.STATUS_FAIL;\n" +
                        "   }\n" +
                        "}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void pageEntity(Configuration configuration) {
        File entity = new File(configuration.getHome() + "/entity/Page.java");
        if (entity.exists()) {
            return;
        }
        entity.getParentFile().mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".entity;\n\n")
                .append("import io.swagger.annotations.ApiModel;\n" +
                        "import io.swagger.annotations.ApiModelProperty;\n" +
                        "\n" +
                        "import javax.validation.constraints.Min;\n" +
                        "import java.io.Serializable;\n" +
                        "import java.util.List;\n\n")
                .append("/**\n" +
                        " * @Description 分页查询输入输出对象实体\n" +
                        " * @Author " + System.getProperty("user.name") + "\n" +
                        " * @Date " + dateFormat.format(new Date()) + "\n" +
                        " */\n")
                .append("@ApiModel(value = \"Page\")\n")
                .append("public class Page<T> implements Serializable {\n" +
                        "    private static final long serialVersionUID = 6603381333387697977L;\n" +
                        "    @ApiModelProperty(value = \"每页数据条数\")\n" +
                        "    @Min(value = 0, message = \"每页数据条数不能为负数\")\n" +
                        "    private int pageSize = 10;\n" +
                        "    @ApiModelProperty(value = \"页码\")\n" +
                        "    @Min(value = 0, message = \"页码不能为负数\")\n" +
                        "    private int pageNum = 1;\n" +
                        "    @ApiModelProperty(value = \"总的数据数\")\n" +
                        "    private int total = 0;\n" +
                        "    @ApiModelProperty(value = \"数据开始行号\")\n" +
                        "    private int startRowNum = 0;\n" +
                        "    @ApiModelProperty(value = \"查询条件对象\")\n" +
                        "    private Object condition;\n" +
                        "    @ApiModelProperty(value = \"分页数据列表\")\n" +
                        "    private List<T> rows;\n" +
                        "    @ApiModelProperty(value = \"排序字段\")\n" +
                        "    private String orderBy = \"\";\n" +
                        "    @ApiModelProperty(value = \"排序规则\")\n" +
                        "    private String desc = \"\";\n" +
                        "\n" +
                        "    public Page() {\n" +
                        "    }\n" +
                        "\n" +
                        "    public Page(int pageSize, int total, int pageNum) {\n" +
                        "        this.pageSize = pageSize;\n" +
                        "        this.total = total;\n" +
                        "        this.pageNum = pageNum;\n" +
                        "        this.startRowNum = pageSize * (pageNum - 1);\n" +
                        "    }\n" +
                        "\n" +
                        "    public static <T> Page<T> newInstance(int pageSize, int total, int pageNum) {\n" +
                        "        return new Page<T>(pageSize, total, pageNum);\n" +
                        "    }\n" +
                        "\n" +
                        "    public int getTotalPageNum() {\n" +
                        "        return (this.total + this.pageSize - 1) / this.pageSize;\n" +
                        "    }\n" +
                        "\n" +
                        "    public int getPageSize() {\n" +
                        "        return pageSize;\n" +
                        "    }\n" +
                        "\n" +
                        "    public void setPageSize(int pageSize) {\n" +
                        "        this.pageSize = pageSize;\n" +
                        "    }\n" +
                        "\n" +
                        "    public int getPageNum() {\n" +
                        "        return pageNum;\n" +
                        "    }\n" +
                        "\n" +
                        "    public void setPageNum(int pageNum) {\n" +
                        "        this.pageNum = pageNum;\n" +
                        "    }\n" +
                        "\n" +
                        "    public int getTotal() {\n" +
                        "        return total;\n" +
                        "    }\n" +
                        "\n" +
                        "    public void setTotal(int total) {\n" +
                        "        this.total = total;\n" +
                        "    }\n" +
                        "\n" +
                        "    public int getStartRowNum() {\n" +
                        "        return startRowNum;\n" +
                        "    }\n" +
                        "\n" +
                        "    public void setStartRowNum(int startRowNum) {\n" +
                        "        this.startRowNum = startRowNum;\n" +
                        "    }\n" +
                        "\n" +
                        "    public Object getCondition() {\n" +
                        "        return condition;\n" +
                        "    }\n" +
                        "\n" +
                        "    public void setCondition(Object condition) {\n" +
                        "        this.condition = condition;\n" +
                        "    }\n" +
                        "\n" +
                        "    public List getRows() {\n" +
                        "        return rows;\n" +
                        "    }\n" +
                        "\n" +
                        "    public void setRows(List rows) {\n" +
                        "        this.rows = rows;\n" +
                        "    }\n" +
                        "\n" +
                        "    public String getOrderBy() {\n" +
                        "        return orderBy;\n" +
                        "    }\n" +
                        "\n" +
                        "    public void setOrderBy(String orderBy) {\n" +
                        "        this.orderBy = orderBy;\n" +
                        "    }\n" +
                        "\n" +
                        "    public String getDesc() {\n" +
                        "        return desc;\n" +
                        "    }\n" +
                        "\n" +
                        "    public void setDesc(String desc) {\n" +
                        "        this.desc = desc;\n" +
                        "    }\n" +
                        "\n" +
                        "}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void constants(Configuration configuration) {
        File entity = new File(configuration.getHome() + "/config/Constants.java");
        if (entity.exists()) {
            return;
        }
        entity.getParentFile().mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".config;\n\n")
                .append("/**\n" +
                        " * @Description 常量类\n" +
                        " * @Author " + System.getProperty("user.name") + "\n" +
                        " * @Date " + dateFormat.format(new Date()) + "\n" +
                        " */\n")
                .append("public final class Constants {\n" +
                        "    public final class JsonView {\n" +
                        "        public static final String STATUS_SUCCESS = \"success\";\n" +
                        "        public static final String STATUS_FAIL = \"fail\";\n" +
                        "    }\n" +
                        "}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void validationGroups(Configuration configuration) {
        File entity = new File(configuration.getHome() + "/config/ValidationGroups.java");
        if (entity.exists()) {
            return;
        }
        entity.getParentFile().mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".config;\n\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.TrackableEntity;\n")
                .append("import ").append(configuration.getParentPackage()).append(".restful.BaseRestful;\n")
                .append("import javax.validation.groups.Default;\n\n")
                .append("/**\n" +
                        " * @Description restful参数校验通用定义\n" +
                        " * @Author " + System.getProperty("user.name") + "\n" +
                        " * @Date " + dateFormat.format(new Date()) + "\n" +
                        " */\n")
                .append("public class ValidationGroups {\n" +
                        "\n" +
                        "    /**\n" +
                        "     * @Description: 通用保存接口校验组，继承Default，否则每个属性都得加上分组属性\n" +
                        "     *\n" +
                        "     * @see BaseRestful#save\n" +
                        "     **/\n" +
                        "    public interface Create extends Default {}\n" +
                        "\n" +
                        "    /**\n" +
                        "     * @Description: 通用更新接口校验组，继承Default，否则每个属性都得加上分组属性\n" +
                        "     *\n" +
                        "     * @see BaseRestful#update\n" +
                        "     **/\n" +
                        "    public interface Update extends Default {}\n" +
                        "\n" +
                        "    /**\n" +
                        "     * @Description: 通用查询接口校验组，继承Default，否则每个属性都得加上分组属性\n" +
                        "     *\n" +
                        "     * @see BaseRestful#getByWhere(TrackableEntity)\n" +
                        "     **/\n" +
                        "    public interface Query extends Default {}\n" +
                        "}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
