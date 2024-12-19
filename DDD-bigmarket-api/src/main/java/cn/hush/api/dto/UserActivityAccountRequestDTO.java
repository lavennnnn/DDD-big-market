package cn.hush.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Hush
 * @description 查詢用户活动额度 请求对象
 * @create 2024-12-10 上午3:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityAccountRequestDTO implements Serializable {

    private String userId;

    private Long activityId;

}
