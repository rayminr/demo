package com.banshi.service;


import com.banshi.model.dao.BaseDaoTest;
import com.banshi.utils.AppProperties;
import org.unitils.UnitilsJUnit4;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

public abstract class BaseServiceTest extends BaseDaoTest {

    @SpringBeanByType
    protected UserService userService;

    @SpringBeanByType
    protected SessionService sessionService;
}
