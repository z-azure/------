pragma solidity ^0.4.24;

contract Test_v1 {
    struct bill{
        int money;
        int qian_m;
        string from_user;
        bool valid;
    }
    
    mapping(string=>bill) bills;
    
    function Test_v1() {}
    
    function get_bills_money(string user) returns(int,int) {
        if(bills[user].valid==true) {
            if(bills[user].money<0) return (0,bills[user].qian_m);
            return (1,bills[user].money);
        }
        return (0,0);
    }
    
    function get_bills_from(string user) returns(string) {
        if(bills[user].valid==false) {
            return "";
        }
        return bills[user].from_user;
    }
    
    function tran_bills(string f_u,string t_u,int m) returns(bool){
        if(bills[f_u].valid==false) {
            bills[f_u].money-=m;
            bills[f_u].valid=true;
            bills[f_u].qian_m+=m;
            bills[t_u].valid=true;
            bills[t_u].money+=m;
            bills[t_u].from_user=f_u;
            return true;
        }
        if(bills[f_u].money<m) {
            if(bills[f_u].money>0){
                return false;
            }
            //return false;
        }
        if(bills[f_u].money<0){
            bills[f_u].qian_m+=m;
        }
        bills[f_u].money-=m;
        bills[t_u].valid=true;
        bills[t_u].money+=m;
        bills[t_u].from_user=bills[f_u].from_user;
        return true;
    }
    
    function huanqian(string com){
        bills[bills[com].from_user].qian_m-=bills[com].money;
        bills[bills[com].from_user].money+=bills[com].money;
        bills[com].money=0;
        bills[com].valid=false;
    }
}