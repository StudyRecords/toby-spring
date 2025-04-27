package org.spring.ch6.proxyFactoryBean;

import org.spring.ch6.transaction.User;
import org.spring.ch6.transaction.UserDao;
import org.spring.ch6.transaction.UserServiceTest;
import org.spring.ch6.transaction.userService.UserServiceImpl;
import org.springframework.mail.MailSender;

public class TestUserServiceImpl extends UserServiceImpl {
    private String id = "user4";

    public TestUserServiceImpl(UserDao userDao, MailSender mailSender) {
        super(userDao, mailSender);
    }

    @Override
    protected void upgradeLevel(User user) throws RuntimeException {
        if (user.getId().equals(id)) {
            throw new UserServiceTest.TestUserServiceException();
        }
        super.upgradeLevel(user);
    }
}
