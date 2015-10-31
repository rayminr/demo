package com.banshi.model.dao;


import com.banshi.model.dto.SessionDTO;
import org.springframework.stereotype.Repository;

@Repository
public class SessionDao extends BaseDao {

    /**
     * 保存session到DB
     *
     * @param sessionDTO
     * @return
     */
    public int insert(SessionDTO sessionDTO) {
        return insert("Session.insert", sessionDTO);
    }

}
