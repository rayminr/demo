package com.banshi.service;


import com.banshi.model.dto.SessionDTO;
import com.banshi.support.XlsDataSetBeanFactory;
import com.banshi.utils.AppProperties;
import com.banshi.utils.DateUtil;
import com.banshi.utils.Logger;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;


public class SessionServiceTest extends BaseServiceTest {

    @Test
    public void testTime() throws Exception {
        Long expireTime = Long.parseLong(AppProperties.getKVStr(SessionService.SESSION_EXPIRE_INTERVAL_MS));
        Long maxExpireTime = Long.parseLong(AppProperties.getKVStr(SessionService.SESSION_MAX_EXPIRE_INTERVAL_MS));

        // ticket中第二部分为session产生时间，判断是否已过最大期有效时间
        Date currentTime = new Date(System.currentTimeMillis());
        Date oldTime = new Date(1446374902876l);
        Date newTime = new Date(1446374902876l + expireTime);
        Date newTime2 = new Date(1446374902876l + maxExpireTime);

        Logger.debug(this, String.format("expireTime(mis) = %s", expireTime/60/1000));
        Logger.debug(this, String.format("oldTime = %s", DateUtil.formatDate(oldTime, DateUtil.YYYY_MM_DD_HH_MI_SS)));
        Logger.debug(this, String.format("newTime = %s", DateUtil.formatDate(newTime, DateUtil.YYYY_MM_DD_HH_MI_SS)));
        Logger.debug(this, String.format("newTime2 = %s", DateUtil.formatDate(newTime2, DateUtil.YYYY_MM_DD_HH_MI_SS)));
        Logger.debug(this, String.format("currentTime = %s", DateUtil.formatDate(currentTime, DateUtil.YYYY_MM_DD_HH_MI_SS)));

        assertTrue(currentTime.after(newTime));
        //assertTrue(currentTime.after(newTime2));

    }

    @Test
    public void createSessionTicket() throws Exception {
        SessionDTO input_session = XlsDataSetBeanFactory.createBean("/testdata/service/SessionServiceTest/createSessionTicket_input.xls", "T_SESSION", SessionDTO.class, SessionDTO.class);
        String ticketId = sessionService.createSessionTicket(input_session);

        boolean keepSuccess = sessionService.keepSessionTicket(ticketId);
        assertTrue(keepSuccess);

        boolean isValid = sessionService.validateSessionTicket(ticketId);
        assertTrue(isValid);
    }
}
