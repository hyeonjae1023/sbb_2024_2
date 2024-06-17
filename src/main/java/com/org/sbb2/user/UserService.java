package com.org.sbb2.user;

import com.org.sbb2.CommonUtil;
import com.org.sbb2.DataNotFoundException;
import com.org.sbb2.EmailException;
import com.org.sbb2.TempPasswordMail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommonUtil commonUtil;
    private final TempPasswordMail tempPasswordMail;

    public SiteUser join(String username, String password, String email, String nickname, String profileImgUrl) {
        SiteUser user = SiteUser
                .builder()
                .username(username)
                .email(email)
                .password(password)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .build();

        System.out.println("HI");

        return userRepository.save(user);
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if(siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    @Transactional
    public void modifyPassword(String email) throws EmailException {
        String tempPassword = commonUtil.createTempPassword();
        SiteUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("해당 이메일의 유저가 없습니다."));
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);
        tempPasswordMail.sendSimpleMessage(email, tempPassword);
    }

    @Transactional
    public SiteUser whenSocialLogin(String providerTypeCode, String username, String email, String nickname, String profileImgUrl) {
        Optional<SiteUser> opUser = findByUsername(username);

        if(opUser.isPresent()) return opUser.get();

        return join(username,"", email, nickname, profileImgUrl);
    }

    public Optional<SiteUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
