package com.banshi.model.dao;


import org.unitils.UnitilsJUnit4;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

@Transactional(value = TransactionMode.ROLLBACK)
@SpringApplicationContext({"classpath:applicationContext.xml", "classpath:testDataSource.xml"})
public abstract class BaseDaoTest extends UnitilsJUnit4 {

    @SpringBeanByType
    protected SessionDao sessionDao;

    @SpringBeanByType
    protected UserDao userDao;

    @SpringBeanByType
    protected LoginLogDao loginLogDao;


}
