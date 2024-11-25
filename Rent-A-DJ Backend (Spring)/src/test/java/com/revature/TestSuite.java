package com.revature;

import com.revature.services.UserServiceTest;
import com.revature.services.AuthServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AuthServiceTest.class,
        UserServiceTest.class
})
public class TestSuite {
    // This will run all tests from the specified classes
}