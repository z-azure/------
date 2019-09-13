

首先是在centos上按照老师给的教程安装golang的相关内容，安装成功后进行后面的操作。
首先是创建了一个hello.go的文件，然后执行结果如下，可知安装基本正确。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190913172139622.png)
#### 然后编写第一个库
首先创建包路径
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190913172623572.png)
然后创建名为reverse.go的文件
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190913173233394.png)
然后将其编译
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019091318155192.png)
然后再修改刚才创建的hello.go文件
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190913181900966.png)
然后将其安装并执行
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190913182209132.png)

#### 测试
编写测试文件
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190913182801982.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NvZGlmZmVyZW50,size_16,color_FFFFFF,t_70)
然后运行测试
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019091318324790.png)
可以知道结果正确，至此包的测试完毕。
