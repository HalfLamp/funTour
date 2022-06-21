package main

import (
	"gorm.io/driver/mysql"
	"gorm.io/gen"
	"gorm.io/gorm"
)

func main() {
	g := gen.NewGenerator(gen.Config{
		OutPath: "./query",
	})
	db, _ := gorm.Open(mysql.Open("czy:Mysql@826525@tcp(czytgc.com:3307)/amusingtourdb"))
	g.UseDB(db)
	g.ApplyBasic(g.GenerateModelAs("sys_user", "User"))
	g.ApplyBasic(g.GenerateModelAs("sys_params", "Param"))
	g.ApplyBasic(g.GenerateModelAs("c_sight", "Sight"))
	g.ApplyBasic(g.GenerateModelAs("c_label", "Label"))
	g.Execute()

	//tool.GetToken("")
}
