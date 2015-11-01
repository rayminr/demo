package com.banshi.model.dao;


import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Dao基类
 */
public abstract class BaseDao {

    @Resource
    protected SqlSessionTemplate sqlSessionTemplate;

    private SqlMapClientTemplate dalTemplate;

    public Object queryOne(String sqlId, Object object) {
        return sqlSessionTemplate.selectOne(sqlId, object);
    }

    public Object queryOne(String sqlId, Map<String, Object> _params) {
        return sqlSessionTemplate.selectOne(sqlId, _params);
    }

    public <T> List<T> queryList(String sqlId, Map<String, Object> _params) {
        return sqlSessionTemplate.selectList(sqlId, _params);
    }

    public <T> List<T> queryList(String sqlId, Object _params) {
        return sqlSessionTemplate.selectList(sqlId, _params);
    }

    public <T> int insert(String sqlId, T obj) {
        return sqlSessionTemplate.insert(sqlId, obj);
    }

    public <T> int update(String sqlId, T obj) {
        return sqlSessionTemplate.update(sqlId, obj);
    }

}
