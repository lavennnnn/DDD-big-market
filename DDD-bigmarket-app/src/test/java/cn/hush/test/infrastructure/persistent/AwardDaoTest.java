package cn.hush.test.infrastructure.persistent;

import cn.hush.infrastructure.dao.IAwardDao;
import cn.hush.infrastructure.dao.po.AwardPO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Hsuh
 * @description 奖品持久化单元测试
 * @create 2024-08-28 下午12:31
 */

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class AwardDaoTest {

    @Resource
    private IAwardDao awardDao;

    @Test
    public void test_queryAwardList() {
        List<AwardPO> awards = awardDao.queryAwardList();
        log.info("测试结果：{}", awards.toString());
    }

}
