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

func newParam(db *gorm.DB) param {
	_param := param{}

	_param.paramDo.UseDB(db)
	_param.paramDo.UseModel(&model.Param{})

	tableName := _param.paramDo.TableName()
	_param.ALL = field.NewField(tableName, "*")
	_param.ID = field.NewInt32(tableName, "id")
	_param.Key = field.NewString(tableName, "key")
	_param.Value = field.NewString(tableName, "value")
	_param.CreateTime = field.NewTime(tableName, "create_time")
	_param.UpdateTime = field.NewTime(tableName, "update_time")

	_param.fillFieldMap()

	return _param
}

type param struct {
	paramDo paramDo

	ALL        field.Field
	ID         field.Int32
	Key        field.String
	Value      field.String
	CreateTime field.Time
	UpdateTime field.Time

	fieldMap map[string]field.Expr
}

func (p param) Table(newTableName string) *param {
	p.paramDo.UseTable(newTableName)
	return p.updateTableName(newTableName)
}

func (p param) As(alias string) *param {
	p.paramDo.DO = *(p.paramDo.As(alias).(*gen.DO))
	return p.updateTableName(alias)
}

func (p *param) updateTableName(table string) *param {
	p.ALL = field.NewField(table, "*")
	p.ID = field.NewInt32(table, "id")
	p.Key = field.NewString(table, "key")
	p.Value = field.NewString(table, "value")
	p.CreateTime = field.NewTime(table, "create_time")
	p.UpdateTime = field.NewTime(table, "update_time")

	p.fillFieldMap()

	return p
}

func (p *param) WithContext(ctx context.Context) *paramDo { return p.paramDo.WithContext(ctx) }

func (p param) TableName() string { return p.paramDo.TableName() }

func (p param) Alias() string { return p.paramDo.Alias() }

func (p *param) GetFieldByName(fieldName string) (field.OrderExpr, bool) {
	_f, ok := p.fieldMap[fieldName]
	if !ok || _f == nil {
		return nil, false
	}
	_oe, ok := _f.(field.OrderExpr)
	return _oe, ok
}

func (p *param) fillFieldMap() {
	p.fieldMap = make(map[string]field.Expr, 5)
	p.fieldMap["id"] = p.ID
	p.fieldMap["key"] = p.Key
	p.fieldMap["value"] = p.Value
	p.fieldMap["create_time"] = p.CreateTime
	p.fieldMap["update_time"] = p.UpdateTime
}

func (p param) clone(db *gorm.DB) param {
	p.paramDo.ReplaceDB(db)
	return p
}

type paramDo struct{ gen.DO }

func (p paramDo) Debug() *paramDo {
	return p.withDO(p.DO.Debug())
}

func (p paramDo) WithContext(ctx context.Context) *paramDo {
	return p.withDO(p.DO.WithContext(ctx))
}

func (p paramDo) Clauses(conds ...clause.Expression) *paramDo {
	return p.withDO(p.DO.Clauses(conds...))
}

func (p paramDo) Returning(value interface{}, columns ...string) *paramDo {
	return p.withDO(p.DO.Returning(value, columns...))
}

func (p paramDo) Not(conds ...gen.Condition) *paramDo {
	return p.withDO(p.DO.Not(conds...))
}

func (p paramDo) Or(conds ...gen.Condition) *paramDo {
	return p.withDO(p.DO.Or(conds...))
}

func (p paramDo) Select(conds ...field.Expr) *paramDo {
	return p.withDO(p.DO.Select(conds...))
}

func (p paramDo) Where(conds ...gen.Condition) *paramDo {
	return p.withDO(p.DO.Where(conds...))
}

func (p paramDo) Exists(subquery interface{ UnderlyingDB() *gorm.DB }) *paramDo {
	return p.Where(field.CompareSubQuery(field.ExistsOp, nil, subquery.UnderlyingDB()))
}

func (p paramDo) Order(conds ...field.Expr) *paramDo {
	return p.withDO(p.DO.Order(conds...))
}

func (p paramDo) Distinct(cols ...field.Expr) *paramDo {
	return p.withDO(p.DO.Distinct(cols...))
}

func (p paramDo) Omit(cols ...field.Expr) *paramDo {
	return p.withDO(p.DO.Omit(cols...))
}

func (p paramDo) Join(table schema.Tabler, on ...field.Expr) *paramDo {
	return p.withDO(p.DO.Join(table, on...))
}

func (p paramDo) LeftJoin(table schema.Tabler, on ...field.Expr) *paramDo {
	return p.withDO(p.DO.LeftJoin(table, on...))
}

func (p paramDo) RightJoin(table schema.Tabler, on ...field.Expr) *paramDo {
	return p.withDO(p.DO.RightJoin(table, on...))
}

func (p paramDo) Group(cols ...field.Expr) *paramDo {
	return p.withDO(p.DO.Group(cols...))
}

func (p paramDo) Having(conds ...gen.Condition) *paramDo {
	return p.withDO(p.DO.Having(conds...))
}

func (p paramDo) Limit(limit int) *paramDo {
	return p.withDO(p.DO.Limit(limit))
}

func (p paramDo) Offset(offset int) *paramDo {
	return p.withDO(p.DO.Offset(offset))
}

func (p paramDo) Scopes(funcs ...func(gen.Dao) gen.Dao) *paramDo {
	return p.withDO(p.DO.Scopes(funcs...))
}

func (p paramDo) Unscoped() *paramDo {
	return p.withDO(p.DO.Unscoped())
}

func (p paramDo) Create(values ...*model.Param) error {
	if len(values) == 0 {
		return nil
	}
	return p.DO.Create(values)
}

func (p paramDo) CreateInBatches(values []*model.Param, batchSize int) error {
	return p.DO.CreateInBatches(values, batchSize)
}

// Save : !!! underlying implementation is different with GORM
// The method is equivalent to executing the statement: db.Clauses(clause.OnConflict{UpdateAll: true}).Create(values)
func (p paramDo) Save(values ...*model.Param) error {
	if len(values) == 0 {
		return nil
	}
	return p.DO.Save(values)
}

func (p paramDo) First() (*model.Param, error) {
	if result, err := p.DO.First(); err != nil {
		return nil, err
	} else {
		return result.(*model.Param), nil
	}
}

func (p paramDo) Take() (*model.Param, error) {
	if result, err := p.DO.Take(); err != nil {
		return nil, err
	} else {
		return result.(*model.Param), nil
	}
}

func (p paramDo) Last() (*model.Param, error) {
	if result, err := p.DO.Last(); err != nil {
		return nil, err
	} else {
		return result.(*model.Param), nil
	}
}

func (p paramDo) Find() ([]*model.Param, error) {
	result, err := p.DO.Find()
	return result.([]*model.Param), err
}

func (p paramDo) FindInBatch(batchSize int, fc func(tx gen.Dao, batch int) error) (results []*model.Param, err error) {
	buf := make([]*model.Param, 0, batchSize)
	err = p.DO.FindInBatches(&buf, batchSize, func(tx gen.Dao, batch int) error {
		defer func() { results = append(results, buf...) }()
		return fc(tx, batch)
	})
	return results, err
}

func (p paramDo) FindInBatches(result *[]*model.Param, batchSize int, fc func(tx gen.Dao, batch int) error) error {
	return p.DO.FindInBatches(result, batchSize, fc)
}

func (p paramDo) Attrs(attrs ...field.AssignExpr) *paramDo {
	return p.withDO(p.DO.Attrs(attrs...))
}

func (p paramDo) Assign(attrs ...field.AssignExpr) *paramDo {
	return p.withDO(p.DO.Assign(attrs...))
}

func (p paramDo) Joins(fields ...field.RelationField) *paramDo {
	for _, _f := range fields {
		p = *p.withDO(p.DO.Joins(_f))
	}
	return &p
}

func (p paramDo) Preload(fields ...field.RelationField) *paramDo {
	for _, _f := range fields {
		p = *p.withDO(p.DO.Preload(_f))
	}
	return &p
}

func (p paramDo) FirstOrInit() (*model.Param, error) {
	if result, err := p.DO.FirstOrInit(); err != nil {
		return nil, err
	} else {
		return result.(*model.Param), nil
	}
}

func (p paramDo) FirstOrCreate() (*model.Param, error) {
	if result, err := p.DO.FirstOrCreate(); err != nil {
		return nil, err
	} else {
		return result.(*model.Param), nil
	}
}

func (p paramDo) FindByPage(offset int, limit int) (result []*model.Param, count int64, err error) {
	result, err = p.Offset(offset).Limit(limit).Find()
	if err != nil {
		return
	}

	if size := len(result); 0 < limit && 0 < size && size < limit {
		count = int64(size + offset)
		return
	}

	count, err = p.Offset(-1).Limit(-1).Count()
	return
}

func (p paramDo) ScanByPage(result interface{}, offset int, limit int) (count int64, err error) {
	count, err = p.Count()
	if err != nil {
		return
	}

	err = p.Offset(offset).Limit(limit).Scan(result)
	return
}

func (p *paramDo) withDO(do gen.Dao) *paramDo {
	p.DO = *do.(*gen.DO)
	return p
}
