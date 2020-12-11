package cn.ruleengine.web.util;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

import java.io.File;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/8/13
 * @since 1.0.0
 */
public class GeneratorMybatisPlus {

    @Test
    public void executor() {
        String packageName = "cn.ruleengine.web.store";
        String dbUrl = "";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig
                .setUrl(dbUrl)
                .setUsername("")
                .setTypeConvert(new MySqlTypeConvert())
                .setPassword("")
                .setDriverName("com.mysql.jdbc.Driver");
        generateByTables(false, dataSourceConfig, packageName,
                "rule_engine_workspace");

    }

    /**
     * @param serviceNameStartWithI UserService, 设置成true: user -> IUserService
     * @param dataSourceConfig      dataSourceConfig
     * @param packageName           packageName
     * @param tableNames            tableNames
     */
    private void generateByTables(boolean serviceNameStartWithI, DataSourceConfig dataSourceConfig, String packageName, String... tableNames) {
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setEntityBooleanColumnRemoveIsPrefix(true)
                .setCapitalMode(true)
                .setEntityLombokModel(true)
                .setLogicDeleteFieldName("deleted")
                .setNaming(NamingStrategy.underline_to_camel)
                //.setTableFillList(Arrays.asList(createTime,updateTime,deleted))
                //修改替换成你需要的表名，多个表名传数组
                .setInclude(tableNames);

        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(false)
                //.setDateType(DateType.ONLY_DATE)
                .setAuthor("dqw")
                .setOutputDir(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "java")
                .setFileOverride(true)
                /*.setBaseColumnList(true).setBaseResultMap(true)*/
                .setOpen(false);
        if (!serviceNameStartWithI) {
            config.setServiceName("%sManager");
            config.setServiceImplName("%sManagerImpl");
        }
        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setMapper("mapper")
                                .setXml("mapper")
                                .setEntity("entity")
                                .setService("manager")
                                .setServiceImpl("manager.impl")
                                .setController("controller.genertor")
                ).execute();
    }
}
