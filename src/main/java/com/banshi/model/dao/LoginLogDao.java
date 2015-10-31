package com.banshi.model.dao;


import com.banshi.model.dto.LoginLogDTO;
import org.springframework.stereotype.Repository;

@Repository
public class LoginLogDao extends BaseDao {

    /**
     * 根据dto插入一条记录，返回自动生成主键
     *
     * @param loginLogDTO
     * @return
     */
    public int insert(LoginLogDTO loginLogDTO) {
        return insert("LoginLog.insert", loginLogDTO);
    }

}
