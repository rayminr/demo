package com.banshi.service;


import com.banshi.model.dao.BaseDaoTest;
import org.unitils.spring.annotation.SpringBeanByType;

public abstract class BaseServiceTest extends BaseDaoTest {

    @SpringBeanByType
    protected UserService userService;

    @SpringBeanByType
    protected SessionService sessionService;
}
