package top.coolbreeze4j.mybatisbatch.mapper;

import top.coolbreeze4j.mybatisbatch.entity.DemoData;

import java.util.List;

/**
 * @author CoolBreeze
 * @date 2023/7/24 11:52.
 */
public interface DemoMapper {
    int batchSave(List<DemoData> demoDataList);

    int save(DemoData demoData);
}
