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
	//"io/ioutil"
	"github.com/spf13/cobra"
	"log"
	"os"
	//"encoding/json"
)

/*type serv struct{
	Name string
	Passwd string
	Email string
}*/

// loginCmd represents the login command
var loginCmd = &cobra.Command{
	Use:   "login",
	Short: "A brief description of your command",
	Long: `A longer description that spans multiple lines and likely contains examples
and usage of using your command. For example:

Cobra is a CLI library for Go that empowers applications.
This application is a tool to generate the needed files
to quickly create a Cobra application.`,
	Run: func(cmd *cobra.Command, args []string) {
		//fmt.Println("login called")
		username, _:=cmd.Flags().GetString("username")
		passwd, _:=cmd.Flags().GetString("passwd")
		suc := checkpasswd(username, passwd)
		filename:="./entity/log.log"
		logfile, errr:=os.OpenFile(filename,os.O_RDWR|os.O_APPEND,7)
		if(errr!=nil){
			fmt.Println("openfile fail")
		}
		defer logfile.Close()
		debuglog:=log.New(logfile,"",log.LstdFlags)
		if suc==true {
			fmt.Println("login success")
			debuglog.Println("login: username: "+username+" login success")
		} else {
			fmt.Println("login fail")
			debuglog.Println("login: username: "+username+" login fail")
		}
	},
}

func checkpasswd(username string, passwd string) bool{
	//fmt.Println("name: "+username+" passwd: "+passwd)
	//fp, _:=os.OpenFile("./entity/data.json", os.O_RDONLY, 0755)
	//defer fp.Close()
	us := readinfo()
	//fmt.Println(us)
	for i:=0;i<len(us.Id);i++ {
		//fmt.Println(us.Id[i].Name+" "+us.Id[i].Passwd)
		if(us.Id[i].Name==username&&us.Id[i].Passwd==passwd){
			return true
		}
	}
	return false
}

func init() {
	rootCmd.AddCommand(loginCmd)

	loginCmd.Flags().StringP("username", "u", "Anonymous", "help message for username")
	loginCmd.Flags().StringP("passwd", "p", "123", "help message for password")
	// Here you will define your flags and configuration settings.

	// Cobra supports Persistent Flags which will work for this command
	// and all subcommands, e.g.:
	// loginCmd.PersistentFlags().String("foo", "", "A help for foo")

	// Cobra supports local flags which will only run when this command
	// is called directly, e.g.:
	// loginCmd.Flags().BoolP("toggle", "t", false, "Help message for toggle")
}
