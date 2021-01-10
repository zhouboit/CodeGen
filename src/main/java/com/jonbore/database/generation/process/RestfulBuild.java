package com.jonbore.database.generation.process;

import com.jonbore.database.generation.entity.Configuration;
import com.jonbore.database.generation.entity.Table;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bo.zhou
 * @date 2021/1/4 下午2:16
 */
public class RestfulBuild {
    public static void write(Table table, Configuration configuration) {
        baseInterfaceRestful(configuration);
    }

    private static void baseInterfaceRestful(Configuration configuration) {
        File entity = new File(configuration.getHome() + "/restful/BaseRestful.java");
        if (entity.exists()) {
            return;
        }
        entity.getParentFile().mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer
                .append("package ").append(configuration.getParentPackage()).append(".restful;\n\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.TrackableEntity;\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.JsonViewObject;\n")
                .append("import ").append(configuration.getParentPackage()).append(".entity.Page;\n")
                .append("import ").append(configuration.getParentPackage()).append(".config.ValidationGroups;\n")
                .append("import io.swagger.annotations.*;\n" +
                        "import org.springframework.http.MediaType;\n" +
                        "import org.springframework.validation.annotation.Validated;\n" +
                        "import org.springframework.web.bind.annotation.*;\n" +
                        "\n" +
                        "import javax.validation.constraints.NotBlank;\n\n")
                .append("/**\n" +
                        " * @Description Web API层基础服务接口\n" +
                        " * @Author " + System.getProperty("user.name") + "\n" +
                        " * @Date " + dateFormat.format(new Date()) + "\n" +
                        " */\n")
                .append("@Api(value = \"Web API层基础服务接口\", description = \"Web API层基础服务接口\")\n" +
                        "@Validated\n" +
                        "public interface BaseRestful<Entity extends TrackableEntity> {\n" +
                        "    /**\n" +
                        "     * 获取所有记录\n" +
                        "     *\n" +
                        "     * @return JsonViewObject\n" +
                        "     */\n" +
                        "    @ApiOperation(value = \"获取所有记录\", notes = \"获取所有记录\", response = JsonViewObject.class, produces = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    @GetMapping(value = \"/byAll\", produces = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    JsonViewObject getAll();\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 根据条件分页查询记录\n" +
                        "     *\n" +
                        "     * @param page\n" +
                        "     * @return JsonViewObject\n" +
                        "     */\n" +
                        "    @ApiOperation(value = \"根据条件分页查询记录\", notes = \"根据条件分页查询记录\", response = JsonViewObject.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    @PostMapping(value = \"/byPage\", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    @ResponseBody\n" +
                        "    JsonViewObject getPage(@ApiParam(value = \"查询条件和分页参数\", required = true, example = \"{\\\"pageSize\\\": 10, \\\"pageNum\\\": 1}\") @RequestBody @Validated Page<Entity> page);\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 根据条件查询记录\n" +
                        "     *\n" +
                        "     * @param entity 查询条件\n" +
                        "     * @return JsonViewObject\n" +
                        "     */\n" +
                        "    @ApiOperation(value = \"根据条件查询记录\", notes = \"根据条件查询记录\", response = JsonViewObject.class, consumes = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    @PostMapping(value = \"/byCondition\", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    JsonViewObject getByWhere(@ApiParam(value = \"查询条件\", required = true) @RequestBody @Validated({ValidationGroups.Query.class}) Entity entity);\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 根据id查询记录\n" +
                        "     *\n" +
                        "     * @param id\n" +
                        "     * @return JsonViewObject\n" +
                        "     */\n" +
                        "    @ApiOperation(value = \"根据id查询记录\", notes = \"根据id查询记录\", response = JsonViewObject.class, produces = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    @GetMapping(value = \"/{id}\", produces = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    JsonViewObject getById(@ApiParam(value = \"记录的id\", required = true, example = \"1\") @PathVariable(\"id\") @NotBlank(message = \"查询id不能为空\") String id);\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 新建记录\n" +
                        "     *\n" +
                        "     * @param entity\n" +
                        "     * @return JsonViewObject\n" +
                        "     */\n" +
                        "    @ApiOperation(value = \"新建记录\", notes = \"新建记录\", response = JsonViewObject.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    @PostMapping(value = \"/creating\", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    JsonViewObject save(@ApiParam(value = \"记录的JSON格式字符串\", required = true) @RequestBody @Validated({ValidationGroups.Create.class}) Entity entity);\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 根据id删除\n" +
                        "     *\n" +
                        "     * @param ids\n" +
                        "     * @return JsonViewObject\n" +
                        "     */\n" +
                        "    @ApiOperation(value = \"根据多个id删除记录\", notes = \"根据多个id删除记录\", response = JsonViewObject.class, produces = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    @ApiImplicitParams(value = {\n" +
                        "            @ApiImplicitParam(paramType = \"query\", name = \"ids\", dataType = \"String\", required = true, value = \"多个记录id，用逗号分隔\", example = \"1,2\")\n" +
                        "    })\n" +
                        "    @GetMapping(value = \"/deleting\", produces = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    JsonViewObject deleteByIds(@RequestParam(\"ids\") @NotBlank(message = \"删除ids不能为空\") String ids);\n" +
                        "\n" +
                        "    /**\n" +
                        "     * 修改记录\n" +
                        "     *\n" +
                        "     * @param entity\n" +
                        "     * @return\n" +
                        "     */\n" +
                        "    @ApiOperation(value = \"修改记录\", notes = \"修改记录\", response = JsonViewObject.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    @PostMapping(value = \"/updating\", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)\n" +
                        "    JsonViewObject update(@ApiParam(value = \"记录的JSON格式字符串\", required = true) @RequestBody @Validated({ValidationGroups.Update.class}) Entity entity);\n" +
                        "\n" +
                        "}");
        try {
            ConnectionSelect.write(stringBuffer.toString(), entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
