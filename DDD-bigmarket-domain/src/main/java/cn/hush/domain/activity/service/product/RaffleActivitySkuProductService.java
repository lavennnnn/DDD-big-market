package cn.hush.domain.activity.service.product;

import cn.hush.domain.activity.model.entity.SkuProductEntity;
import cn.hush.domain.activity.repository.IActivityRepository;
import cn.hush.domain.activity.service.IRaffleActivitySkuProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author Hush
 * @description
 * @create 2024-12-17 下午11:15
 */
@Service
public class RaffleActivitySkuProductService implements IRaffleActivitySkuProductService {

    @Resource
    private IActivityRepository repository;


    @Override
    public List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId) {
        return repository.querySkuProductEntityListByActivityId(activityId);
    }
}
