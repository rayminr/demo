package com.banshi.model.dao;


import com.banshi.model.dto.UserDTO;
import com.banshi.utils.MapUtil;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserDao extends BaseDao {

    /**
     * 根据dto插入一条记录，返回自动生成主键
     *
     * @param userDTO
     * @return
     */
    public int insert(UserDTO userDTO) {
        return insert("User.insert", userDTO);
    }

    /**
     * 根据dto更新一条记录，更新条件为id=dto.id
     *
     * @param userDTO
     * @return
     */
    public int update(UserDTO userDTO) {
        return update("User.update", userDTO);
    }

    /**
     * 根据id(主键)查询，返回唯一一条记录
     *
     * @param userId
     * @return
     */
    public UserDTO selectById(Long userId) {
        return (UserDTO) queryOne("User.selectById", userId);
    }

    /**
     * 根据唯一索引字段查询，返回唯一一条记录
     *
     * @param userDTO
     * @return
     */
    public UserDTO selectByUniqueKey(UserDTO userDTO) {
        return (UserDTO) queryOne("User.selectByUniqueKey", userDTO);
    }

    /**
     * 根据指定条件查询，返回记录集
     *
     * @param conditions
     * @return
     */
    public Integer countByCnd(Object... conditions) {
        Map<String, Object> cndMap = MapUtil.buildMap(conditions);
        return (Integer) queryOne("User.countByCnd", cndMap);
    }

    /**
     * 根据指定条件查询，返回记录集
     *
     * @param conditions
     * @return
     */
    public List<UserDTO> selectByCnd(Object... conditions) {
        Map<String, Object> cndMap = MapUtil.buildMap(conditions);
        return queryList("User.selectByCnd", cndMap);
    }

}
