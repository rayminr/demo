package com.banshi.service;

import com.banshi.frame.cache.CacheProxy;
import com.banshi.frame.exception.AppException;
import com.banshi.model.dao.SessionDao;
import com.banshi.model.dto.SessionDTO;
import com.banshi.utils.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SessionService {

    public static final String SESSION_MAC_SALT = "SESSION_MAC_SALT";
    public static final String SESSION_EXPIRE_INTERVAL_MS = "SESSION_EXPIRE_INTERVAL_MS";
    public static final String SESSION_MAX_EXPIRE_INTERVAL_MS = "SESSION_MAX_EXPIRE_INTERVAL_MS";
    public static final String SESSION_STORE_TYPE = "SESSION_STORE_TYPE";
    public static final String SESSION_STORE_TYPE_DB = "DB";
    public static final String SESSION_STORE_TYPE_CACHE = "CACHE";
    public static final String SESSION_STORE_TYPE_MIX = "MIX";

    public static final String SESSION_KEY_SPLIT = ",";

    @Resource
    private SessionDao sessionDao;


    /**
     * 登录后产生session级别的令牌，并按照session存储方式保存session信息
     *
     * @param sessionDTO
     * @return 返回ticketId，格式:UUID + 分割符"," + 创建时间 + 分割符"," +用HMAC加密UUID去除连接符"-"
     */
    public String createSessionTicket(SessionDTO sessionDTO) {

        // 格式：UUID + 分割符"," + 创建时间 + 分割符"," +用HMAC加密UUID去除连接符"-"
        String ticketId = null;
        try {

            Long createTime = System.currentTimeMillis();

            StringBuilder sbTicket = new StringBuilder();
            String UUID = MyString.getUUID();
            String UUIDWithoutHyphen = UUID.replaceAll("-", "");
            sbTicket.append(UUID).append(SESSION_KEY_SPLIT).append(createTime).append(SESSION_KEY_SPLIT);

            // salt一部分在代码中，另一半在配置中管理
            String saltKey = CipherUtil.MAC_SALT + AppProperties.getKVStr(SESSION_MAC_SALT);
            String encryptUUIDWithoutHyphen = CipherUtil.encryptHMAC(CipherUtil.MAC_SHA, saltKey.getBytes(Constants.CHARSET_UTF_8), UUIDWithoutHyphen.getBytes(Constants.CHARSET_UTF_8));
            sbTicket.append(encryptUUIDWithoutHyphen);
            ticketId = sbTicket.toString();
            Logger.debug(this, String.format("ticketId = %s", ticketId));

            sessionDTO.setTicketId(ticketId);
            sessionDTO.setTicketCreateTime(createTime);
            sessionDTO.setTicketAccessTime(createTime);
            Logger.debug(this, String.format("sessionDTO = %s", sessionDTO.toJson()));

            String sessionStoreType = AppProperties.getKVStr(SESSION_STORE_TYPE);
            if (SESSION_STORE_TYPE_DB.equals(sessionStoreType)) {
                //session只保存到DB
                sessionDao.insert(sessionDTO);

            } else if (SESSION_STORE_TYPE_CACHE.equals(sessionStoreType)) {
                //session只保存到CACHE
                CacheProxy.put(CacheProxy.CACHE_LOGIN_USER, ticketId, sessionDTO);

            } else if (SESSION_STORE_TYPE_MIX.equals(sessionStoreType)) {
                //session保存到DB和CACHE
                sessionDao.insert(sessionDTO);
                CacheProxy.put(CacheProxy.CACHE_LOGIN_USER, ticketId, sessionDTO);

            } else {

                throw new AppException();
            }

        } catch (Exception e) {
            Logger.error(this, "createSessionTicket fail", e);
            throw new AppException();
        }

        return ticketId;
    }

    /**
     * <p>
     * 校验session ticket有效
     * 1、校验ticket格式:UUID + 分割符"," + 创建时间 + 分割符"," +用HMAC加密UUID去除连接符"-"
     * 2、校验ticket有效期:距离上次session访问时间间隔<={SESSION_EXPIRE_TIME}，及距离session创建时间间隔<={SESSION_MAX_EXPIRE_TIME}
     * </p>
     *
     * @param ticketId
     * @return 正确且有效:true
     */
    public boolean validateSessionTicket(String ticketId) {

        return checkSessionTicket(ticketId) != null;
    }

    /**
     * <p>
     * 校验session ticket有效
     * 1、校验ticket格式:UUID + 分割符"," + 创建时间 + 分割符"," +用HMAC加密UUID去除连接符"-"
     * 2、校验ticket有效期:距离上次session访问时间间隔<={SESSION_EXPIRE_TIME}，及距离session创建时间间隔<={SESSION_MAX_EXPIRE_TIME}
     * 3、校验通过后更新访问时间
     * </p>
     *
     * @param ticketId
     * @return
     */
    public boolean keepSessionTicket(String ticketId) {

        boolean keepSuccess = false;
        try {
            SessionDTO sessionDTO = checkSessionTicket(ticketId);
            Logger.debug(this, String.format("sessionDTO = %s", sessionDTO.toJson()));

            if (sessionDTO != null) {
                String sessionStoreType = AppProperties.getKVStr(SESSION_STORE_TYPE);
                Long maxExpireTime = Long.parseLong(AppProperties.getKVStr(SESSION_MAX_EXPIRE_INTERVAL_MS));

                Logger.debug(this, String.format("sessionStoreType = %s", sessionStoreType));

                // 更新访问时间
                sessionDTO.setTicketAccessTime(System.currentTimeMillis());
                if (SESSION_STORE_TYPE_DB.equals(sessionStoreType)) {
                    //session只更新DB，maxExpireTime为判断分区使用
                    keepSuccess = sessionDao.updateByTicketId(sessionDTO, maxExpireTime) == 1 ? true : false;

                } else if (SESSION_STORE_TYPE_CACHE.equals(sessionStoreType)) {
                    //session只从CACHE获取
                    keepSuccess = CacheProxy.put(CacheProxy.CACHE_LOGIN_USER, ticketId, sessionDTO);

                } else if (SESSION_STORE_TYPE_MIX.equals(sessionStoreType)) {
                    //session先从CACHE获取，再从DB获取
                    int updateCnt = sessionDao.updateByTicketId(sessionDTO, maxExpireTime);
                    Logger.debug(this, String.format("updateCnt = %s", updateCnt));
                    if (updateCnt > 0) {
                        keepSuccess = CacheProxy.put(CacheProxy.CACHE_LOGIN_USER, ticketId, sessionDTO);
                    }
                }
                Logger.debug(this, String.format("keepSuccess = %s", keepSuccess));
            }

        } catch (Exception ex) {
            Logger.error(this, "keepSessionTicket fail", ex);
            throw new AppException();
        }

        return keepSuccess;
    }


    /**
     * <p>
     * 校验session ticket有效
     * 1、校验ticket格式:UUID + 分割符"," + 创建时间 + 分割符"," +用HMAC加密UUID去除连接符"-"
     * 2、校验ticket有效期:距离上次session更新时间间隔<={SESSION_EXPIRE_TIME}，及距离session创建时间间隔<={SESSION_MAX_EXPIRE_TIME}
     * </p>
     *
     * @param ticketId
     * @return
     */
    private SessionDTO checkSessionTicket(String ticketId) {

        Logger.debug(this, String.format("ticketId = %s", ticketId));

        SessionDTO retSessionDTO = null;

        try {
            //校验ticketId格式
            if (MyString.isNotBlank(ticketId)) {
                String ticketArray[] = ticketId.split(SESSION_KEY_SPLIT);
                if (ticketArray.length == 3) {
                    String UUID = ticketArray[0];
                    Logger.debug(this, String.format("UUID = %s", UUID));

                    // ticket中第一部分为UUID格式
                    if (!MyString.isUUID(UUID)) {
                        return null;
                    }

                    // ticket中第二部分为session产生时间，判断是否已过最大期有效时间
                    String createTimeStr = ticketArray[1];
                    if (MyString.isBlank(createTimeStr) || !MyString.isNumeric(createTimeStr)) {
                        return null;
                    }

                    Date currentTime = new Date(System.currentTimeMillis());
                    long expireInterval = Long.parseLong(AppProperties.getKVStr(SESSION_EXPIRE_INTERVAL_MS));
                    long maxExpireInterval = Long.parseLong(AppProperties.getKVStr(SESSION_MAX_EXPIRE_INTERVAL_MS));
                    Date maxExpireTime = new Date(Long.parseLong(createTimeStr) + maxExpireInterval);

                    Logger.debug(this, String.format("createTimeStr = %s", createTimeStr));
                    Logger.debug(this, String.format("expireInterval = %s, expireInterval(mis) = %s", expireInterval, expireInterval / 60 / 1000));
                    Logger.debug(this, String.format("maxExpireInterval = %s, maxExpireInterval(hour) = %s", maxExpireInterval, maxExpireInterval / 60 / 60 / 1000));
                    Logger.debug(this, String.format("currentTime = %s", DateUtil.formatDate(currentTime, DateUtil.YYYY_MM_DD_HH_MI_SS)));
                    Logger.debug(this, String.format("maxExpireTime = %s", DateUtil.formatDate(maxExpireTime, DateUtil.YYYY_MM_DD_HH_MI_SS)));

                    if (currentTime.after(maxExpireTime)) {
                        return null;
                    }

                    String encryptStr = ticketArray[2];
                    String UUIDWithoutHyphen = UUID.replaceAll("-", "");
                    String saltKey = CipherUtil.MAC_SALT + AppProperties.getKVStr(SESSION_MAC_SALT);
                    String encryptUUIDWithoutHyphen = CipherUtil.encryptHMAC(CipherUtil.MAC_SHA, saltKey.getBytes(Constants.CHARSET_UTF_8), UUIDWithoutHyphen.getBytes(Constants.CHARSET_UTF_8));

                    // 验证机密串的正确性
                    if (encryptStr != null && encryptStr.equals(encryptUUIDWithoutHyphen)) {
                        String sessionStoreType = AppProperties.getKVStr(SESSION_STORE_TYPE);
                        SessionDTO sessionDTO = null;
                        if (SESSION_STORE_TYPE_DB.equals(sessionStoreType)) {
                            //session只从DB获取，maxExpireTime为判断分区使用
                            sessionDTO = sessionDao.getSessionByTicketId(ticketId, maxExpireInterval / 60 / 60 / 1000);

                        } else if (SESSION_STORE_TYPE_CACHE.equals(sessionStoreType)) {
                            //session只从CACHE获取
                            sessionDTO = (SessionDTO) CacheProxy.get(CacheProxy.CACHE_LOGIN_USER, ticketId);

                        } else if (SESSION_STORE_TYPE_MIX.equals(sessionStoreType)) {
                            //session先从CACHE获取，再从DB获取
                            sessionDTO = (SessionDTO) CacheProxy.get(CacheProxy.CACHE_LOGIN_USER, ticketId);
                            if (sessionDTO == null) {
                                sessionDTO = sessionDao.getSessionByTicketId(ticketId, maxExpireInterval / 60 / 60 / 1000);
                            }
                        }
                        if (sessionDTO != null && sessionDTO.getTicketCreateTime() != null && sessionDTO.getTicketAccessTime() != null) {
                            long tktCreateTime = sessionDTO.getTicketCreateTime();
                            long tktAccessTime = sessionDTO.getTicketAccessTime();
                            Date expireTime = new Date(tktAccessTime + expireInterval);
                            maxExpireTime = new Date(tktCreateTime + maxExpireInterval);

                            Logger.debug(this, String.format("tktCreateTime = %s", tktCreateTime));
                            Logger.debug(this, String.format("tktAccessTime = %s", tktAccessTime));
                            Logger.debug(this, String.format("expireTime = %s", DateUtil.formatDate(expireTime, DateUtil.YYYY_MM_DD_HH_MI_SS)));
                            Logger.debug(this, String.format("maxExpireTime = %s", DateUtil.formatDate(maxExpireTime, DateUtil.YYYY_MM_DD_HH_MI_SS)));

                            // 判断是否已有效
                            if (currentTime.before(expireTime) && currentTime.before(maxExpireTime)) {
                                return sessionDTO;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.error(this, "checkSessionTicket fail", ex);
            throw new AppException();
        }

        return retSessionDTO;
    }

}
