package cn.hush.domain.activity.service;

import cn.hush.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

/**
 * @author Hush
 * @description 抽奖活动服务
 * @create 2024-11-16 上午3:50
 */
@Service
public class RaffleActivityService extends AbstractRaffleActivity{


    public RaffleActivityService(IActivityRepository activityRepository) {
        super(activityRepository);
    }


}
