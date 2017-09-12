## java版本要求
jdk1.8

## 解压
```
unzip src.zip
```

## 进入文件夹
```
cd src
```

## 编译
```
javac *.java
```

## 执行主类
```
java SiteBookSystem
```

## 输入测试用例1
```
abcdefghijklmnopqrst1234567890
> Error: the booking is invalid!
U001 2016-06-02 22:00~22:00 A
> Error: the booking is invalid!
U002 2017-08-01 19:00~22:00 A
> Success: the booking is accepted!
U003 2017-08-02 13:00~17:00 B
> Success: the booking is accepted!
U004 2017-08-03 15:00~16:00 C
> Success: the booking is accepted!
U005 2017-08-05 09:00~11:00 D
> Success: the booking is accepted!
> 收入汇总
> ---
> 场地:A
> 2017-08-01 19:00~22:00 200元
> ⼩计：200元
>
> 场地:B
> 2017-08-02 13:00~17:00 200元
> ⼩计：200元
>
> 场地:C
> 2017-08-03 15:00~16:00 50元
> ⼩计：50元
>
> 场地:D
> 2017-08-05 09:00~11:00 80元
> ⼩计：80元
> ---
> 总计：530元
```

## 关闭程序
```
> Ctrl + C
```

## 执行主类
```
java SiteBookSystem
```

## 输入测试用例2
```
U002 2017-08-01 19:00~22:00 A
> Success: the booking is accepted!
U003 2017-08-01 18:00~20:00 A
> Error: the booking conflicts with existing bookings!
U002 2017-08-01 19:00~22:00 A C
> Success: the booking is accepted!
U002 2017-08-01 19:00~22:00 A C
> Error: the booking being cancelled does not exist!
U003 2017-08-01 18:00~20:00 A
> Success: the booking is accepted!
U003 2017-08-02 13:00~17:00 B
> Success: the booking is accepted!
> 收入汇总
> ---
> 场地:A
> 2017-08-01 18:00~20:00 160元
> 2017-08-01 19:00~22:00 违约金 100元
> ⼩计：260元
>
> 场地:B
> 2017-08-02 13:00~17:00 200元
> ⼩计：200元
>
> 场地:C
> ⼩计：0元
>
> 场地:D
> ⼩计：0元
> ---
> 总计：460元
```