package com.negotiation.common.feign;

import com.negotiation.common.pojo.User;
import com.negotiation.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user-service")
public interface UserFeignService {

    @GetMapping("/user/get/{userId}")
    R getById(@PathVariable Integer userId);

    @PutMapping("/user/update")
    R update(@RequestBody User user);
}
