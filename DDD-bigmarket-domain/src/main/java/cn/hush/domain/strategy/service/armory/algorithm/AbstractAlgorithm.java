package cn.hush.domain.strategy.service.armory.algorithm;

import cn.hush.domain.strategy.repository.IStrategyRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.security.SecureRandom;

/**
 * @author Hush
 * @description
 * @create 2024-12-31 下午5:16
 */

public abstract class AbstractAlgorithm implements IAlgorithm{

    @Resource
    protected IStrategyRepository repository;

    protected final SecureRandom secureRandom = new SecureRandom();

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum Algorithm{

        O1("o1Algorithm"),
        OLogN("oLogNAlgorithm");

        private String key;

    }

}
