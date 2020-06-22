package com.concamap.config.security;

import com.concamap.model.Users;
import com.concamap.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

public class UserDetailServiceImp implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public UserDetailServiceImp(UserService userService) {
        this.userService = userService;
    }

    //Toàn bộ Việc xác thực user với DB sẽ nằm trong phương thức này
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users userLogin = userService.findActiveUserByUsername(username);

        if (userLogin == null) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng");
        }
        //Nếu User không tồn tại, thì trả về Exception và kết thúc việc xác thực

        Set<GrantedAuthority> authorities = new HashSet<>();
        //Khai báo danh sách các quyền mà user có thể có
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        //Thêm quyền Admin vào danh sách của User. Ở đây anh fix cứng một giá trị ROLE_ADMIN
        //để tránh gây hoang mang. Phải có thêm một bảng Role trong DB để lấy các Role tương ứng với từng User

        return new User(
                userLogin.getUsername(),
                userLogin.getPassword(),
                authorities
        );
        //Hàm này sẽ tự so khớp username và password với DB và nếu khớp sẽ gán cho nó role là: ROLE_ADMIN
        //như anh fix cứng ở đoạn code trên. Việc set ROLE_ADMIN để làm cho user truy cập được vào trang chủ
        // theo như phân quyền quy định ở code phân quyền
    }
}
