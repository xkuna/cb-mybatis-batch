package top.coolbreeze4j.mybatisbatch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.coolbreeze4j.mybatisbatch.service.IDemoService;

/**
 * @author CoolBreeze
 * @date 2023/7/24 14:59.
 */
@SpringBootTest
public class DemoServiceImplTest {
    @Autowired
    IDemoService demoService;

    /**
     * 超大数据量 入库场景
     * 需要确保数据准确无误
     * 实现复用会话的同时，数据分片插入 提高插入效率
     */
    @Test
    public void mpBatchTest(){
        demoService.mpBatch();
    }

    /**
     * 数据导入场景且数据不是特别大
     * 因为数据导入的时候需要进行数据验证等相关问题
     * 下面实现复用会话的形式 减少性能开销，并对错误数据进行记录
     */
    @Test
    public void mpImportData(){
        demoService.mpImportData();
    }
}
