package com.jonbore.database.generation.process;

import com.google.common.collect.Lists;
import com.jonbore.database.generation.entity.Column;
import com.jonbore.database.generation.entity.Configuration;
import com.jonbore.database.generation.entity.Table;
import com.jonbore.database.generation.entity.xml.KeyWordsFactory;
import com.jonbore.database.generation.entity.xml.Mapping;
import com.jonbore.database.generation.entity.xml.TypeMappingFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author bo.zhou
 * @date 2020/12/30 下午4:53
 */
public class ConnectionSelect {

    public static Map<String, Map<String, Mapping>> typeMapping = null;
    public static Map<String, List<String>> keyWordsMapping = null;
    public static Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static List<Table> getMySQLData(Configuration configuration) {
        try {
            Class.forName(configuration.getDriver());
            Connection connection = DriverManager.getConnection(configuration.getUrl(), configuration.getUsername(), configuration.getPassword());
            PreparedStatement statement = connection.prepareStatement("SELECT " +
                    "b.TABLE_NAME," +
                    "b.TABLE_COMMENT," +
                    "c.COLUMN_NAME " +
                    "FROM " +
                    "information_schema.`TABLES` b " +
                    "left join information_schema.`KEY_COLUMN_USAGE` c on b.TABLE_NAME=c.TABLE_NAME and c.CONSTRAINT_NAME='PRIMARY' " +
                    "WHERE " +
                    "b.TABLE_SCHEMA = '" + configuration.getDatabase() + "'" +
                    (StringUtils.isBlank(configuration.getTableNameExp()) ? " " : " and b.TABLE_NAME like '" + configuration.getTableNameExp() + "' ") +
                    (StringUtils.isBlank(configuration.getTable()) ? " " : " and b.TABLE_NAME in ('" + configuration.getTable() + "') ") +
                    "ORDER BY b.TABLE_NAME");
            ResultSet resultSet = statement.executeQuery();
            List<Table> tableList = Lists.newArrayList();
            while (resultSet.next()) {
                Table tb = new Table();
                tb.setTablename(resultSet.getString("TABLE_NAME"));
                tb.setTableComment(resultSet.getString("TABLE_COMMENT"));
                tb.setPrimaryKey(resultSet.getString("COLUMN_NAME"));
                PreparedStatement colst = connection.prepareStatement(
                        "SELECT " +
                                "a.COLUMN_NAME," +
                                "a.COLUMN_COMMENT," +
                                "a.DATA_TYPE " +
                                "FROM " +
                                "information_schema.`COLUMNS` a " +
                                "WHERE " +
                                "a.TABLE_SCHEMA = '" + configuration.getDatabase() + "' " +
                                "AND a.TABLE_NAME = '" + tb.getTablename() + "' " +
                                "ORDER BY " +
                                "a.ORDINAL_POSITION"
                );
                ResultSet colrs = colst.executeQuery();
                List<Column> columnList = Lists.newArrayList();
                while (colrs.next()) {
                    Column column = new Column();
                    column.setColumnName(colrs.getString("COLUMN_NAME"));
                    column.setDataType(colrs.getString("DATA_TYPE"));
                    column.setColumnComment(colrs.getString("COLUMN_COMMENT"));
                    columnList.add(column);
                }
                tb.setColumns(columnList);
                tableList.add(tb);
            }
            return tableList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void build(Configuration configuration) {
        typeMapping = TypeMappingFactory.getMapping();
        keyWordsMapping = KeyWordsFactory.getMapping();
        List<Table> tableList = getMySQLData(configuration);
        if (configuration.getCleanHome()) {
            File home = new File(configuration.getHome());
            if (home.exists()) {
                for (File file : home.listFiles()) {
                    if (file.isDirectory() && ("entity".equals(file.getName()) || "mapper".equals(file.getName()) || "exception".equals(file.getName()))) {
                        removeFolder(file);
                    }
                }
            }
            File resources = new File(configuration.getHome() + "/resources/configs/mapper");
            if (resources.exists()) {
                removeFolder(resources);
            }
        }
        for (Table tableInfo : Objects.requireNonNull(tableList)) {
            EntityBuild.write(tableInfo, configuration);
            MapperInterfaceBuild.write(tableInfo, configuration);
            MapperXmlBuild.write(tableInfo, configuration);
            ServiceBuild.write(tableInfo, configuration);
            ServiceImplBuild.write(tableInfo, configuration);
            RestfulBuild.write(tableInfo, configuration);
            RestfulImplBuild.write(tableInfo, configuration);
        }
    }

    public static void removeFolder(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    removeFolder(file1);
                }
            }
            file.delete();
        }
    }

    public static String getJavaAuthor(Table table) {
        String author = "/**\n" +
                " * @Description " + table.getTableComment() + "\n" +
                " * @Author " + System.getProperty("user.name") + "\n" +
                " * @Date " + dateFormat.format(new Date()) + "\n" +
                " */\n";
        return author;
    }

    public static String getXmlAuthor(Table table) {
        String author =
                "<!--\n" +
                        "   Description " + table.getTableComment() + "\n" +
                        "   Author " + System.getProperty("user.name") + "\n" +
                        "   Date " + dateFormat.format(new Date()) + "\n" +
                        "-->\n";
        return author;
    }

    public static boolean write(String content, File file) throws Exception {
        boolean flag = false;
        file.getParentFile().mkdirs();
        InputStream in = new ByteArrayInputStream(content.getBytes("UTF-8"));
        try (OutputStream os = new FileOutputStream(file)) {
            byte[] buf = new byte[2048];
            int len = -1;
            while ((len = in.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            os.flush();
            os.close();
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return flag;
    }

}
