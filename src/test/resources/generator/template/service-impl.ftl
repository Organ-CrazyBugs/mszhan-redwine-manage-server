package ${basePackage}.service.impl.${datasourceName};

import ${basePackage}.dao.${datasourceName}.${modelNameUpperCamel}Mapper;
import ${basePackage}.model.${datasourceName}.${modelNameUpperCamel};
import ${basePackage}.service.${datasourceName}.${modelNameUpperCamel}Service;
import ${basePackage}.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: ${date}
 */
@Service
@Transactional
public class ${modelNameUpperCamel}ServiceImpl extends AbstractService<${modelNameUpperCamel}> implements ${modelNameUpperCamel}Service {
    @Resource
    private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

}
