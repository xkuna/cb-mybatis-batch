package top.coolbreeze4j.mybatisbatch.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import top.coolbreeze4j.mybatisbatch.entity.DemoData;
import top.coolbreeze4j.mybatisbatch.mapper.DemoMapper;
import top.coolbreeze4j.mybatisbatch.service.IDemoService;

import java.util.ArrayList;
import java.util.List;


/**
 * @author CoolBreeze
 * @date 2023/7/24 11:51.
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class DemoServiceImpl implements IDemoService {
    SqlSessionFactory sqlSessionFactory;
    DemoMapper demoMapper;

    public DemoServiceImpl(DemoMapper demoMapper, SqlSessionFactory sqlSessionFactory) {
        this.demoMapper = demoMapper;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * 关于事务提交相关问题记录
     * 当提交一次commit时 如果出现错误并且没有catch掉的话，后面的commit将终止执行
     */



    /**
     * 超大数据量 入库场景
     * 需要确保数据准确无误
     * 实现复用会话的同时，数据分片插入 提高插入效率
     *
     * 如果数据非常小的话 可以不启用batch模式，直接调用 batchSave() 方法即可
     */
    @Override
    public void mpBatch() {
        List<DemoData> demoDataList = generateDataList();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false)){
            log.info("执行插入......");
            //一定要用这种方式，否则 sqlSession 不生效
            DemoMapper mapper = sqlSession.getMapper(DemoMapper.class);
            int n = 1;
            for (List<DemoData> partList : ListUtil.partition(demoDataList, 200)) {
                log.info("执行第{}次插入，本次共{}条", n, partList.size());
                mapper.batchSave(partList);
                n ++;
            }

            log.info("执行结束......");
            sqlSession.commit();
        }
    }

    /**
     * 数据导入场景且数据不是特别大
     * 因为数据导入的时候需要进行数据验证等相关问题
     * 下面实现复用会话的形式 减少性能开销，并对错误数据进行记录
     */
    @Override
    public void mpImportData() {
//        List<DemoData> demoDataList = generateImpList();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);){
            List<DemoData> demoDataList = generateImpList2();
            log.info("执行插入......");

            //一定要用这种方式，否则 sqlSession 不生效
            DemoMapper mapper = sqlSession.getMapper(DemoMapper.class);
            for (int i = 0; i < demoDataList.size(); i++) {
                try {
                    //数据验证......

                    //验证成功 进行插入数据
                    mapper.save(demoDataList.get(i));
                    sqlSession.commit();
                }catch (Exception e){
                    log.error("第{}行插入报错，信息如下:\n{}",i + 1,e.getMessage());
                    //失败 组装提示信息......
                }
            }
            log.info("执行结束......");

        }
    }


    public List<DemoData> generateDataList(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            DemoData demoData = new DemoData(IdUtil.getSnowflakeNextId(), RandomUtil.randomString(3), RandomUtil.randomInt(18, 40));
            list.add(demoData);
        }
        return list;
    }


    public List<DemoData> generateImpList(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            DemoData demoData = new DemoData(IdUtil.getSnowflakeNextId(), RandomUtil.randomString(3), RandomUtil.randomInt(18, 40));
            list.add(demoData);
        }
        return list;
    }

    public List<DemoData> generateImpList2(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            DemoData demoData = new DemoData(IdUtil.getSnowflakeNextId(), RandomUtil.randomString(3), RandomUtil.randomInt(18, 40));
            list.add(demoData);
        }
        DemoData d = new DemoData(IdUtil.getSnowflakeNextId(), RandomUtil.randomString(20), RandomUtil.randomInt(18, 40));
        list.add(d);
        for (int i = 0; i < 200; i++) {
            DemoData demoData = new DemoData(IdUtil.getSnowflakeNextId(), RandomUtil.randomString(3), RandomUtil.randomInt(18, 40));
            list.add(demoData);
        }
        return list;
    }

}













