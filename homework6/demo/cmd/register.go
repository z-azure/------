/*
Copyright © 2019 NAME HERE <EMAIL ADDRESS>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package cmd

import (
	"fmt"
	"os"
	"io/ioutil"

	"github.com/spf13/cobra"

	"encoding/json"

	"log"
)

type user struct{
	Id []serv
}

type serv struct{
	Name string
	Passwd string
	Email string
}

type servslice struct{
	servs []serv
}

// registerCmd represents the register command
var registerCmd = &cobra.Command{
	Use:   "register",
	Short: "A brief description of your command",
	Long: `A longer description that spans multiple lines and likely contains examples
and usage of using your command. For example:

Cobra is a CLI library for Go that empowers applications.
This application is a tool to generate the needed files
to quickly create a Cobra application.`,
	Run: func(cmd *cobra.Command, args []string) {
		//fmt.Println("register called")
		//var s servslice
		//str := '{"servs":[{"name":"asd","passwd":"123"}]}'
		str := serv{
			Name: "",
			Passwd: "",
			Email: "",
		}
		
		username, _:=cmd.Flags().GetString("user")
		passwd, _:=cmd.Flags().GetString("passwd")
		email, _:=cmd.Flags().GetString("email")
		fmt.Println("register called by "+username)
		fmt.Println("password is "+passwd)
		fmt.Println("email is "+email)
		str.Name=username
		str.Passwd=passwd
		str.Email=email
		
		filename:="./entity/log.log"
		logfile, errr:=os.OpenFile(filename,os.O_RDWR|os.O_APPEND,7)
		//logfile, errr:=os.Create(filename);
		if(errr!=nil){
			fmt.Println("openfile fail")
		}
		defer logfile.Close()
		debuglog:=log.New(logfile,"",log.LstdFlags)

		if checkuser(username)==true {
			fmt.Println("username exist, create account fail")
			debuglog.Println("register: username "+username+" exist, create account fail")
		} else {
			debuglog.Println("register: username: "+username+" password: "+passwd+" email: "+email);
			savecuruser(str)
			//err:=json.Unmarshal([]byte(str),&s)
			us := readinfo()
			/*var te serv
			te.Name="asd"
			te.Passwd="234"
			te.Email="sedf@a"*/
			//var us user
			us.Id=append(us.Id,str)
			//us.Id=append(us.Id,te)
			data, _:= json.Marshal(us)
			//fmt.Println(us)
			saveinfo(data)
		}
	},
}

func savecuruser(s serv){
	fp, _:=os.OpenFile("./entity/curUser.txt", os.O_RDWR, 7)
	defer fp.Close()
	fp.WriteString("Usrname: "+s.Name+" Password: "+s.Passwd+" Email: "+s.Email)

}

func checkuser(username string) bool{
	//fmt.Println("name: "+username+" passwd: "+passwd)
	//fp, _:=os.OpenFile("./entity/data.json", os.O_RDONLY, 0755)
	//defer fp.Close()
	us := readinfo()
	//fmt.Println(us)
	for i:=0;i<len(us.Id);i++ {
		//fmt.Println(us.Id[i].Name+" "+us.Id[i].Passwd)
		if(us.Id[i].Name==username){
			return true
		}
	}
	return false
}

func readinfo() user{
	data, err := ioutil.ReadFile("./entity/data.json")
	if err != nil{
		//return false
	}
	var user1 user
	err = json.Unmarshal(data, &user1)
	if err!=nil {
		fmt.Println(err)
	}
	//fmt.Println("read from file")
	//fmt.Println(user1)
	return user1
}

func saveinfo(data []byte){
	fp, err := os.OpenFile("./entity/data.json",os.O_WRONLY,0755)//,os.O_RDWR,0755)o_append
	if err!=nil {
		fmt.Println(err)
	}
	
	_, err = fp.Write(data)
	if err!=nil {
		fmt.Println(err)
	}
	
	defer fp.Close()
}

func init() {
	rootCmd.AddCommand(registerCmd)
	registerCmd.Flags().StringP("user", "u", "Anonymous", "help message for username")
	registerCmd.Flags().StringP("passwd", "p", "123", "help message for password")
	registerCmd.Flags().StringP("email", "e", "123@xxx.com", "help message for email")
	// Here you will define your flags and configuration settings.

	// Cobra supports Persistent Flags which will work for this command
	// and all subcommands, e.g.:
	// registerCmd.PersistentFlags().String("foo", "", "A help for foo")

	// Cobra supports local flags which will only run when this command
	// is called directly, e.g.:
	// registerCmd.Flags().BoolP("toggle", "t", false, "Help message for toggle")
}
