package com.cai.funtour.api.pub;

import com.cai.funtour.pojo.Result;

import java.util.List;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/6/10 16:17
 * @description：景点接口
 */
public interface SightService {
    /**
     * 根据用户喜好获取景点列表
     * @return
     */
    default Result getSightListByUser(String userId, Integer page, Integer size){
        return Result.error(222, "系统无服务");
    }

    /**
     * 根据条件获取景点详情
     * @return
     */
    default Result getSightList(String userId, List types, List areas, Integer page, Integer size){
        return Result.error(222, "系统无服务");
    }

    /**
     * 根据类型获取景点列表
     * @param types
     * @return
     */
    default Result getSightListByType(List<String> types){
        return Result.error(222, "系统无服务");
    }

    /**
     * 根据热门程度获取经典列表
     * @return
     */
    default Result getSightListByHot(){
        return Result.error(222, "系统无服务");
    }

    /**
     * 根据地域获取景点列表
     * @param areas
     * @return
     */
    default Result getSightListByArea(List<String> areas){
        return Result.error(222, "系统无服务");
    }

    /**
     * 获取景点详细信息
     * @param sightId
     * @return
     */
    default Result getSightInfo(String sightId){
        return Result.error(222, "系统无服务");
    }

    /**
     * 根据关键词查询景点
     * @param keyWords
     * @return
     */
    default Result searchSights(String keyWords){
        return Result.error(222, "系统无服务");
    }

    /**
     * 根据一个景点推荐相似景点
     * @param sightId
     * @return
     */
    default Result similarSights(String sightId){
        return Result.error(222, "系统无服务");
    }

}
