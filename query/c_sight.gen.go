// Code generated by gorm.io/gen. DO NOT EDIT.
// Code generated by gorm.io/gen. DO NOT EDIT.
// Code generated by gorm.io/gen. DO NOT EDIT.

package query

import (
	"context"

	"gorm.io/gorm"
	"gorm.io/gorm/clause"
	"gorm.io/gorm/schema"

	"gorm.io/gen"
	"gorm.io/gen/field"

	"funtour/model"
)

func newSight(db *gorm.DB) sight {
	_sight := sight{}

	_sight.sightDo.UseDB(db)
	_sight.sightDo.UseModel(&model.Sight{})

	tableName := _sight.sightDo.TableName()
	_sight.ALL = field.NewField(tableName, "*")
	_sight.ID = field.NewInt64(tableName, "id")
	_sight.SightID = field.NewString(tableName, "sight_id")
	_sight.Name = field.NewString(tableName, "name")
	_sight.Describe = field.NewString(tableName, "describe")
	_sight.RegionID = field.NewString(tableName, "region_id")
	_sight.BestTime = field.NewString(tableName, "best_time")
	_sight.StarLevel = field.NewString(tableName, "star_level")
	_sight.HotLevel = field.NewString(tableName, "hot_level")
	_sight.Unique = field.NewString(tableName, "unique")
	_sight.CoverImages = field.NewString(tableName, "cover_images")
	_sight.SignImages = field.NewString(tableName, "sign_images")
	_sight.ExtraImages = field.NewString(tableName, "extra_images")
	_sight.Extra = field.NewString(tableName, "extra")
	_sight.Type = field.NewString(tableName, "type")
	_sight.Address = field.NewString(tableName, "address")
	_sight.CreateTime = field.NewTime(tableName, "create_time")
	_sight.UpdateTime = field.NewTime(tableName, "update_time")
	_sight.IsUse = field.NewString(tableName, "is_use")

	_sight.fillFieldMap()

	return _sight
}

type sight struct {
	sightDo sightDo

	ALL         field.Field
	ID          field.Int64
	SightID     field.String
	Name        field.String
	Describe    field.String
	RegionID    field.String
	BestTime    field.String
	StarLevel   field.String
	HotLevel    field.String
	Unique      field.String
	CoverImages field.String
	SignImages  field.String
	ExtraImages field.String
	Extra       field.String
	Type        field.String
	Address     field.String
	CreateTime  field.Time
	UpdateTime  field.Time
	IsUse       field.String

	fieldMap map[string]field.Expr
}

func (s sight) Table(newTableName string) *sight {
	s.sightDo.UseTable(newTableName)
	return s.updateTableName(newTableName)
}

func (s sight) As(alias string) *sight {
	s.sightDo.DO = *(s.sightDo.As(alias).(*gen.DO))
	return s.updateTableName(alias)
}

func (s *sight) updateTableName(table string) *sight {
	s.ALL = field.NewField(table, "*")
	s.ID = field.NewInt64(table, "id")
	s.SightID = field.NewString(table, "sight_id")
	s.Name = field.NewString(table, "name")
	s.Describe = field.NewString(table, "describe")
	s.RegionID = field.NewString(table, "region_id")
	s.BestTime = field.NewString(table, "best_time")
	s.StarLevel = field.NewString(table, "star_level")
	s.HotLevel = field.NewString(table, "hot_level")
	s.Unique = field.NewString(table, "unique")
	s.CoverImages = field.NewString(table, "cover_images")
	s.SignImages = field.NewString(table, "sign_images")
	s.ExtraImages = field.NewString(table, "extra_images")
	s.Extra = field.NewString(table, "extra")
	s.Type = field.NewString(table, "type")
	s.Address = field.NewString(table, "address")
	s.CreateTime = field.NewTime(table, "create_time")
	s.UpdateTime = field.NewTime(table, "update_time")
	s.IsUse = field.NewString(table, "is_use")

	s.fillFieldMap()

	return s
}

func (s *sight) WithContext(ctx context.Context) *sightDo { return s.sightDo.WithContext(ctx) }

func (s sight) TableName() string { return s.sightDo.TableName() }

func (s sight) Alias() string { return s.sightDo.Alias() }

func (s *sight) GetFieldByName(fieldName string) (field.OrderExpr, bool) {
	_f, ok := s.fieldMap[fieldName]
	if !ok || _f == nil {
		return nil, false
	}
	_oe, ok := _f.(field.OrderExpr)
	return _oe, ok
}

func (s *sight) fillFieldMap() {
	s.fieldMap = make(map[string]field.Expr, 18)
	s.fieldMap["id"] = s.ID
	s.fieldMap["sight_id"] = s.SightID
	s.fieldMap["name"] = s.Name
	s.fieldMap["describe"] = s.Describe
	s.fieldMap["region_id"] = s.RegionID
	s.fieldMap["best_time"] = s.BestTime
	s.fieldMap["star_level"] = s.StarLevel
	s.fieldMap["hot_level"] = s.HotLevel
	s.fieldMap["unique"] = s.Unique
	s.fieldMap["cover_images"] = s.CoverImages
	s.fieldMap["sign_images"] = s.SignImages
	s.fieldMap["extra_images"] = s.ExtraImages
	s.fieldMap["extra"] = s.Extra
	s.fieldMap["type"] = s.Type
	s.fieldMap["address"] = s.Address
	s.fieldMap["create_time"] = s.CreateTime
	s.fieldMap["update_time"] = s.UpdateTime
	s.fieldMap["is_use"] = s.IsUse
}

func (s sight) clone(db *gorm.DB) sight {
	s.sightDo.ReplaceDB(db)
	return s
}

type sightDo struct{ gen.DO }

func (s sightDo) Debug() *sightDo {
	return s.withDO(s.DO.Debug())
}

func (s sightDo) WithContext(ctx context.Context) *sightDo {
	return s.withDO(s.DO.WithContext(ctx))
}

func (s sightDo) Clauses(conds ...clause.Expression) *sightDo {
	return s.withDO(s.DO.Clauses(conds...))
}

func (s sightDo) Returning(value interface{}, columns ...string) *sightDo {
	return s.withDO(s.DO.Returning(value, columns...))
}

func (s sightDo) Not(conds ...gen.Condition) *sightDo {
	return s.withDO(s.DO.Not(conds...))
}

func (s sightDo) Or(conds ...gen.Condition) *sightDo {
	return s.withDO(s.DO.Or(conds...))
}

func (s sightDo) Select(conds ...field.Expr) *sightDo {
	return s.withDO(s.DO.Select(conds...))
}

func (s sightDo) Where(conds ...gen.Condition) *sightDo {
	return s.withDO(s.DO.Where(conds...))
}

func (s sightDo) Exists(subquery interface{ UnderlyingDB() *gorm.DB }) *sightDo {
	return s.Where(field.CompareSubQuery(field.ExistsOp, nil, subquery.UnderlyingDB()))
}

func (s sightDo) Order(conds ...field.Expr) *sightDo {
	return s.withDO(s.DO.Order(conds...))
}

func (s sightDo) Distinct(cols ...field.Expr) *sightDo {
	return s.withDO(s.DO.Distinct(cols...))
}

func (s sightDo) Omit(cols ...field.Expr) *sightDo {
	return s.withDO(s.DO.Omit(cols...))
}

func (s sightDo) Join(table schema.Tabler, on ...field.Expr) *sightDo {
	return s.withDO(s.DO.Join(table, on...))
}

func (s sightDo) LeftJoin(table schema.Tabler, on ...field.Expr) *sightDo {
	return s.withDO(s.DO.LeftJoin(table, on...))
}

func (s sightDo) RightJoin(table schema.Tabler, on ...field.Expr) *sightDo {
	return s.withDO(s.DO.RightJoin(table, on...))
}

func (s sightDo) Group(cols ...field.Expr) *sightDo {
	return s.withDO(s.DO.Group(cols...))
}

func (s sightDo) Having(conds ...gen.Condition) *sightDo {
	return s.withDO(s.DO.Having(conds...))
}

func (s sightDo) Limit(limit int) *sightDo {
	return s.withDO(s.DO.Limit(limit))
}

func (s sightDo) Offset(offset int) *sightDo {
	return s.withDO(s.DO.Offset(offset))
}

func (s sightDo) Scopes(funcs ...func(gen.Dao) gen.Dao) *sightDo {
	return s.withDO(s.DO.Scopes(funcs...))
}

func (s sightDo) Unscoped() *sightDo {
	return s.withDO(s.DO.Unscoped())
}

func (s sightDo) Create(values ...*model.Sight) error {
	if len(values) == 0 {
		return nil
	}
	return s.DO.Create(values)
}

func (s sightDo) CreateInBatches(values []*model.Sight, batchSize int) error {
	return s.DO.CreateInBatches(values, batchSize)
}

// Save : !!! underlying implementation is different with GORM
// The method is equivalent to executing the statement: db.Clauses(clause.OnConflict{UpdateAll: true}).Create(values)
func (s sightDo) Save(values ...*model.Sight) error {
	if len(values) == 0 {
		return nil
	}
	return s.DO.Save(values)
}

func (s sightDo) First() (*model.Sight, error) {
	if result, err := s.DO.First(); err != nil {
		return nil, err
	} else {
		return result.(*model.Sight), nil
	}
}

func (s sightDo) Take() (*model.Sight, error) {
	if result, err := s.DO.Take(); err != nil {
		return nil, err
	} else {
		return result.(*model.Sight), nil
	}
}

func (s sightDo) Last() (*model.Sight, error) {
	if result, err := s.DO.Last(); err != nil {
		return nil, err
	} else {
		return result.(*model.Sight), nil
	}
}

func (s sightDo) Find() ([]*model.Sight, error) {
	result, err := s.DO.Find()
	return result.([]*model.Sight), err
}

func (s sightDo) FindInBatch(batchSize int, fc func(tx gen.Dao, batch int) error) (results []*model.Sight, err error) {
	buf := make([]*model.Sight, 0, batchSize)
	err = s.DO.FindInBatches(&buf, batchSize, func(tx gen.Dao, batch int) error {
		defer func() { results = append(results, buf...) }()
		return fc(tx, batch)
	})
	return results, err
}

func (s sightDo) FindInBatches(result *[]*model.Sight, batchSize int, fc func(tx gen.Dao, batch int) error) error {
	return s.DO.FindInBatches(result, batchSize, fc)
}

func (s sightDo) Attrs(attrs ...field.AssignExpr) *sightDo {
	return s.withDO(s.DO.Attrs(attrs...))
}

func (s sightDo) Assign(attrs ...field.AssignExpr) *sightDo {
	return s.withDO(s.DO.Assign(attrs...))
}

func (s sightDo) Joins(fields ...field.RelationField) *sightDo {
	for _, _f := range fields {
		s = *s.withDO(s.DO.Joins(_f))
	}
	return &s
}

func (s sightDo) Preload(fields ...field.RelationField) *sightDo {
	for _, _f := range fields {
		s = *s.withDO(s.DO.Preload(_f))
	}
	return &s
}

func (s sightDo) FirstOrInit() (*model.Sight, error) {
	if result, err := s.DO.FirstOrInit(); err != nil {
		return nil, err
	} else {
		return result.(*model.Sight), nil
	}
}

func (s sightDo) FirstOrCreate() (*model.Sight, error) {
	if result, err := s.DO.FirstOrCreate(); err != nil {
		return nil, err
	} else {
		return result.(*model.Sight), nil
	}
}

func (s sightDo) FindByPage(offset int, limit int) (result []*model.Sight, count int64, err error) {
	result, err = s.Offset(offset).Limit(limit).Find()
	if err != nil {
		return
	}

	if size := len(result); 0 < limit && 0 < size && size < limit {
		count = int64(size + offset)
		return
	}

	count, err = s.Offset(-1).Limit(-1).Count()
	return
}

func (s sightDo) ScanByPage(result interface{}, offset int, limit int) (count int64, err error) {
	count, err = s.Count()
	if err != nil {
		return
	}

	err = s.Offset(offset).Limit(limit).Scan(result)
	return
}

func (s *sightDo) withDO(do gen.Dao) *sightDo {
	s.DO = *do.(*gen.DO)
	return s
}