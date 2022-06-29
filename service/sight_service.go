package service

import (
	"context"
	"fmt"
	"funtour/database"
	. "funtour/model"
	"funtour/query"
	"funtour/tool"
)

type SightService struct {
}

// getSightListByUser 获取某用户的景点列表
func (*SightService) GetSightListByUser(userId string, page int32, size int32) (*Result, error) {
	defer tool.CatchPanic()

	findSightList := "select * from ( " +
		"select *, 2 as sort from c_sight where sight_id in ( " +
		"select uniqueId from c_label where label in ( " +
		"select label from c_label where uniqueId = ? and type = '用户') " +
		"and type = '景点') and is_use = '1' " +
		"UNION " +
		"SELECT *, 1 as sort from c_sight where is_use = '1'" +
		"order by hot_level desc " +
		") a ORDER BY a.sort desc " +
		"limit ? offset ?"
	sightList := make([]Sight, 0)
	database.GetDb().Raw(findSightList, userId, size, (int(page)-1)*int(size)).Scan(&sightList)

	if len(sightList) > 0 {
		return ToDataWithName("sightList", sightList), nil
	} else {
		return Error(205, "没有景点"), nil
	}
}

// GetSightList 获取景点列表
func (*SightService) GetSightList(userId, key string, types, regionCode []string, page int32, size int32) (*Result, error) {
	defer tool.CatchPanic()

	const start = "select * from ( " +
		" select sight_id,`name`,`describe`,region_id,best_time,star_level,hot_level,cover_images,address, 2 as sort from c_sight where sight_id in ( " +
		"	select uniqueId from c_label where label in ( " +
		"		select label from c_label where uniqueId = ? and type = '用户') " +
		"	and type = '景点') and is_use='1' " +
		" UNION " +
		" SELECT sight_id,`name`,`describe`,region_id,best_time,star_level,hot_level,cover_images,address, 1 as sort from c_sight where is_use='1' " +
		" order by hot_level desc " +
		" ) a" +
		" left join c_label la on la.uniqueId = a.sight_id " +
		" left join c_dictionary d on d.key = la.label " +
		" where 1=1 "

	const end = " ORDER BY a.sort desc limit ? offset ?;"

	keyCondition := ""
	typeCondition := ""
	regionCondition := ""

	// sql参数
	params := make([]interface{}, 0)
	params = append(params, userId)

	if key != "" {
		keyCondition = " and `name` like concat('%',?,'%') "
		params = append(params, key)
	}
	if len(types) > 0 {
		typeCondition = " and d.value in (?) "
		params = append(params, types)
	}
	if len(regionCode) > 0 {
		regionCondition = " and region_id in (?) "
		params = append(params, regionCode)
	}

	params = append(params, size, (int(page)-1)*int(size))

	//拼接sql语句
	findSightList := fmt.Sprintf("%s%s%s%s%s", start, keyCondition, typeCondition, regionCondition, end)

	sightList := make([]Sight, 0)
	database.GetDb().Raw(findSightList, params...).Scan(&sightList)

	if len(sightList) > 0 {
		return ToDataWithName("sightList", sightList), nil
	} else {
		return Error(205, "没有景点"), nil
	}
}

// 获取景点详细信息
func (*SightService) GetSightInfo(sightId string) (*Result, error) {
	defer tool.CatchPanic()

	sightInfo, err := database.GetSightInfoCache(sightId)
	if err != nil {
		tool.Error("查询景点信息出错", err)
		return Error(206, "查询景点信息出错"), err
	}

	if sightInfo == nil {
		sight := query.Use(database.GetDb()).Sight
		db := sight.WithContext(context.TODO())
		sightInfo, err = db.Where(sight.SightID.Eq(sightId)).Where(sight.IsUse.Eq("1")).First()
		if err != nil {
			tool.Error("查询景点信息出错", err)
			return Error(206, "查询景点信息出错"), err
		}
	}

	return ToData(sightInfo), nil
}

func (*SightService) MethodMapper() map[string]string {
	return map[string]string{
		"GetSightListByUser": "getSightListByUser",
		"GetSightList":       "getSightList",
		"GetSightInfo":       "getSightInfo",
	}
}
