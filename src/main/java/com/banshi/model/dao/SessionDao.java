package com.banshi.model.dao;


import com.banshi.model.dto.SessionDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * 更新session到DB
     *
     * @param sessionDTO
     * @return
     */
    public int updateByTicketId(SessionDTO sessionDTO, Long maxExpireTime) {
        Map<String, Object> cndMap = new HashMap<String, Object>();
        cndMap.put("ticketId", sessionDTO.getTicketId());
        cndMap.put("tktAccessTime", sessionDTO.getTicketAccessTime());
        cndMap.put("maxExpireTime", maxExpireTime);
        return update("Session.updateSessionByTicketId", sessionDTO);
    }

    /**
     * 获取session
     *
     * @param ticketId
     * @return
     */
    public SessionDTO getSessionByTicketId(String ticketId, Long maxExpireTime) {
        Map<String, Object> cndMap = new HashMap<String, Object>();
        cndMap.put("ticketId", ticketId);
        cndMap.put("maxExpireTime", maxExpireTime);
        return (SessionDTO) queryOne("Session.getSessionByTicketId", cndMap);
    }

}
