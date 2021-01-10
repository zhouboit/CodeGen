package com.jonbore.database.generation.entity;


import com.jonbore.database.generation.process.ConnectionSelect;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;

import static com.jonbore.database.generation.process.ConnectionSelect.keyWordsMapping;

/**
 * @author bo.zhou
 * @date 2020/12/30 下午4:39
 */
public class Column {
    private String columnName;
    private String dataType;
    private String columnComment;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getLowerCamelCaseName() {
        String col = null;
        if (!columnName.contains("_")) {
            col = columnName.toLowerCase();
        } else {
            StringBuilder stringBuffer = new StringBuilder();
            String[] words = columnName.toLowerCase().split("_");
            for (int i = 1; i < words.length; i++) {
                if (i == 1) {
                    stringBuffer.append(words[i]);
                } else {
                    stringBuffer.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1));
                }
            }
            col = stringBuffer.toString();
        }
        if (!keyWordsMapping.get("java").contains(col)) {
            return col;
        }
        return col + "J";
    }

    public String getExpression() {
        if ("DT_CREATE_DATE".equals(columnName) || "DT_UPDATE_DATE".equals(columnName)) {
            return "now()";
        }
        return "#{" + getLowerCamelCaseName() + "}";
    }

    public String getObjectType() {
        return ConnectionSelect.typeMapping.get("mysql").get(dataType).getJava();
    }

    public String getDataType() {
        return dataType;
    }

    public String getJdbcType() {
        return ConnectionSelect.typeMapping.get("mysql").get(dataType).getMybatis();
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnComment() {
        if (StringUtils.isBlank(columnComment)) {
            return columnName;
        }
        Matcher matcher = ConnectionSelect.pattern.matcher(columnComment);
        return matcher.replaceAll("");
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }
}
