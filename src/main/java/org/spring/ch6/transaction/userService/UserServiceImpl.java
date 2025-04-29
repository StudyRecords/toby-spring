package org.spring.ch6.transaction.userService;

import lombok.RequiredArgsConstructor;
import org.spring.ch6.transaction.Level;
import org.spring.ch6.transaction.User;
import org.spring.ch6.transaction.UserDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final int MIN_LOGIN_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    @Value("${spring.mail.username}")
    private String username;

    protected final UserDao userDao;
    protected final MailSender mailSender;

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    protected boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        return switch (level) {
            case BASIC -> user.getLogin() >= 50;
            case SILVER -> user.getRecommend() >= 30;
            case GOLD -> false;
            default -> throw new IllegalArgumentException("[UserService.canUpgradeLevel] 존재하지 않는 Level입니다.");
        };
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeMail(user);
    }

    public void add(User user) {
        userDao.add(user);
    }

    private void sendUpgradeMail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom(username);
        message.setSubject("Upgrade 안내");
        message.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다!");

        mailSender.send(message);
    }
}
