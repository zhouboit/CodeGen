package com.jonbore.database.generation.entity;

import com.jonbore.database.generation.process.ConnectionSelect;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;

/**
 * @author bo.zhou
 * @date 2020/12/30 下午4:39
 */
public class Table {
    private String tablename;
    private String tableComment;
    private List<Column> columns;
    private String primaryKey;

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getUpperCamelCaseName() {
        StringBuilder stringBuffer = new StringBuilder();
        String[] words = tablename.toLowerCase().split("_");
        for (int i = 2; i < words.length; i++) {
            stringBuffer.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1));
        }
        return stringBuffer.toString();
    }

    public String getLowerCamelCaseName() {
        StringBuilder stringBuffer = new StringBuilder();
        String[] words = tablename.toLowerCase().split("_");
        for (int i = 2; i < words.length; i++) {
            if (StringUtils.isBlank(stringBuffer.toString())) {
                stringBuffer.append(words[i]);
            } else {
                stringBuffer.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1));
            }
        }
        return stringBuffer.toString();
    }


    public String getTableComment() {
        Matcher matcher = ConnectionSelect.pattern.matcher(tableComment);
        return matcher.replaceAll("");
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getPrimaryKey() {
        if (StringUtils.isBlank(primaryKey)) {
            return "#{id}";
        }
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getLowerCamelCaseKey() {
        StringBuilder stringBuffer = new StringBuilder();
        String[] words = primaryKey.toLowerCase().split("_");
        for (int i = 1; i < words.length; i++) {
            if (i == 1) {
                stringBuffer.append(words[i]);
            } else {
                stringBuffer.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1));
            }
        }
        return stringBuffer.toString();
    }

    public String getExpressionKey() {
        if (StringUtils.isBlank(primaryKey)) {
            return "#{id}";
        }
        return "#{" + getLowerCamelCaseKey() + "}";
    }
}
