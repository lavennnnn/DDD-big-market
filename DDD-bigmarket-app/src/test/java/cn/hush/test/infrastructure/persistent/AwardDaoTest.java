package cn.hush.test.infrastructure.persistent;

import cn.hush.infrastructure.persistent.dao.IAwardDao;
import cn.hush.infrastructure.persistent.po.AwardPO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Autowired
    private IAwardDao awardDao;

    @Test
    public void test_queryAwardList() {
        List<AwardPO> awards = awardDao.queryAwardList();
        log.info("测试结果：{}", awards.toString());
    }

}
