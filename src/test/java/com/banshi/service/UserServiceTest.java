package com.banshi.service;


import com.banshi.model.dto.UserDTO;
import com.banshi.support.XlsDataSetBeanFactory;
import org.junit.Test;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.reflectionassert.ReflectionAssert;



public class UserServiceTest extends BaseServiceTest {

    @Test
    @DataSet("/testdata/service/UserServiceTest/insert_user.xls")
    public void test() throws Exception {
        UserDTO user = userService.getUserById(1L);
        UserDTO expected_user = XlsDataSetBeanFactory.createBean("/testdata/service/UserServiceTest/insert_user.xls", "T_USER", UserDTO.class, UserDTO.class);
        ReflectionAssert.assertReflectionEquals(expected_user, user);
    }
}
