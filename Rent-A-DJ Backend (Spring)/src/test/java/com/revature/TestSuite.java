package com.revature;

import com.revature.services.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AuthServiceTest.class,
        UserServiceTest.class,
        DJServiceTest.class,
        ReservationServiceTest.class
})
public class TestSuite {
    // This will run all tests from the specified classes
}