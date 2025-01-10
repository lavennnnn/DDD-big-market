package cn.hush.infrastructure.gateway.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Hush
 * @description
 * @create 2025-01-10 下午2:59
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable {

    private String code;
    private String info;
    private T data;

}
