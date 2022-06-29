package database

import (
	"context"
	"encoding/json"
	"errors"
	"funtour/query"
	"funtour/tool"
	"strconv"
	"sync"
	"time"
)

// 该类在LRU的算法上做了修改，增加了使用频次以及随时间降权，用于判断并存储热点数据的key。
var mutex sync.Mutex

// LRU结构体
type LRU struct {
	// 设置是否启用定时任务
	time_task *time.Ticker
	// LRU缓存的最大值
	size int
	// LRU的热点数据集
	HotData map[string]string
	// 权重队列,存储key值
	weightQueue map[string]weight
}

// 权重
type weight struct {
	// 最近一次的修改时间
	time int64
	// 总权重
	weight int64
}

// 创建LRU对象，必须指定缓存大小。创建时会自动启动定时任务
func NewLRU(size int) *LRU {
	lru := &LRU{
		size:        size,
		HotData:     make(map[string]string),
		weightQueue: make(map[string]weight),
	}
	lru.setTimeTask(true)

	return lru
}

func newWeight() *weight {
	return &weight{}
}

// 定时更新任务
func updateHotKey(lru *LRU) {
	for {
		t := <-lru.time_task.C
		tool.Info("LRU热点数据定时任务调度：当前时间为:", t)
		sight := query.Use(GetDb()).Sight
		db := sight.WithContext(context.TODO())
		redisCon := GetConnect()

		hotData := lru.HotData
		for k := range hotData {
			sightInfo, err := db.Where(sight.SightID.Eq(k)).Where(sight.IsUse.Eq("1")).First()
			if err != nil {
				tool.Error("定时任务从mysql读取热点数据出错,sightId:", k)
				continue
			}
			info, _ := json.Marshal(&sightInfo)
			// 设置缓存，有效期11分钟
			redisCon.Do("setex", tool.CACHE_SIGHT+k, 660, info)
		}
	}
}

func (this *LRU) setTimeTask(flag bool) error {

	if flag {
		ticker := time.NewTicker(10 * time.Minute)
		this.time_task = ticker
		go updateHotKey(this)

	} else {
		if this.time_task == nil {
			tool.Error("定时任务未启动！")
			return errors.New("有效期11分")
		} else {
			this.time_task.Stop()
			this.time_task = nil
		}
	}
	this.time_task.Reset(10 * time.Minute)
	return nil
}

func (this *LRU) Get(key string) string {
	mutex.Lock()
	value, ok := this.HotData[key]
	if ok == false {
		this.put(key)
	}
	// 重新计算权重
	this.get(key)
	mutex.Unlock()

	return value
}

func (this *LRU) put(key string) {
	item := newWeight()
	weight, _ := strconv.ParseInt("1", 10, 64)
	item.setWeight(weight)
	item.setTime(time.Now().Unix())

	this.weightQueue[key] = *item
}

func (this *LRU) get(key string) {
	// 首先对该数据的权重进行+1
	weight := this.weightQueue[key]
	weight.weight += 1
	this.weightQueue[key] = weight

	// 如果热点数据没有达到上线，则直接添加为热点数据
	if len(this.HotData) < this.size {
		this.HotData[key] = ""
		return
	}

	weight.calcWeight(key)
	this.weightQueue[key] = weight
	// 判空,为空则移除
	if this.weightQueue[key] == *newWeight() {
		delete(this.weightQueue, key)
	}
	// 记录最小的
	small := key
	// 对热点数据进行降权运算
	for hotKey, _ := range this.HotData {
		weight = this.weightQueue[hotKey]
		weight.calcWeight(hotKey)
		this.weightQueue[key] = weight
		if this.weightQueue[hotKey].weight < this.weightQueue[small].weight {
			small = hotKey
		}
	}

	if key != small {
		delete(this.HotData, small)
		this.HotData[key] = ""
	}
}

func (this *weight) setWeight(value int64) {
	this.weight = value
}

func (this *weight) setTime(value int64) {
	this.time = value
}

// 根据时间降权
func (this *weight) calcWeight(key string) {
	updateTimeStamp := this.time
	nowTimeStamp := time.Now().Unix()
	// 一分钟一个权重
	diff := (nowTimeStamp - updateTimeStamp) / 60
	// 更新权重及时间
	this.time = nowTimeStamp
	this.weight = this.weight - diff
	// 权重小于零则清除
	if this.weight < 0 {
		this = nil
	}
}
