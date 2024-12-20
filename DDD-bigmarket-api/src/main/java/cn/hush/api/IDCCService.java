package cn.hush.api;

import cn.hush.types.model.Response;

/**
 * @author Hush
 * @description DCC 动态配置中心管理接口
 * @create 2024-12-21 上午2:44
 */
public interface IDCCService {

    Response<Boolean> updateConfig(String key, String value);

}
