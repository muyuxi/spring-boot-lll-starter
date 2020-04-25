## 简介
Spring Boot lll starter项目是个基于企业级实战抽象脱胎出来的，适用于管理平台后端和微服务快速搭建的基础架构模板，致力于大量减少
程序员业务代码的编写工作，少走弯路

## 特性和功能
- 提供了从Controller层到mapper层代码**一键生成功能**
- 统一请求和响应参数封装
- 统一异常处理
- 常用基础方法抽象封装
- 使用jackson进行序列表配置
- 集成tkMyBatis、通用Mapper插件、PageHelper分页插件，实现单表业务零SQL
- swagger2开发文档集成，启动后访问`localhost:8081/swagger-ui.html`
- [业务统一码](#1)

## 快速开发
### 第一步
项目目标是适用于管理平台及微服务，目前正在完善管理平台功能
若开发管理平台后台，则可删除`consumer`项目，保留`base`及`background`，然后请对background项目进行改名，但请保留基本格式(**否则自动代码生成不生效**)：
- 项目名
  - 项目名-service
    - com.公司名.模块名.项目名service
  - 项目名-service-impl
    - com.公司名.模块名.项目名serviceimpl

### 第二步
自动生成代码工具在background-service-impl的test包中，对自动代码生成工具的`ModifiedConstant`类进行修改，`ExecuteGenerate`该类负责自动生成框架

## 技术选型
1. SpringCloud Alibaba
2. tkMybatis+MyBatis PageHelper分页插件
3. Druid Spring Boot Starter
4. Nacos配置及注册中心
5. hutool工具集
6. lombok
7. feign
8. 其它技术，如Redis，ElasticSearch等请随业务需要自行添加

## 代码目录架构

- spring-boot-lll-starter
  - base 基础包，随项目升级自行进行迭代
    - aop 全局aop包 目前有业务码及日志打印的处理
    - business 业务处理器及接口
    - common 一些项目基石的基本类及公共工具
    - config 全项目配置文件
    - exception 自定义异常类及异常全局处理
    - mybatis mybatis的基石抽象类
  - background 后台项目
    - background-service 后台项目实体类及接口
      - pojo 多层次的实体类分类，包括请求、删除、返回结果等等
      - service 服务接口层
    - background-service-impl 后台项目实现主体
      - main
          - business 统一业务类
            - clazz 添加业务实现类
            - BusinessEnum 每添加一个业务实现类，在枚举中塞一个编码
          - config 本项目配置文件
          - controller 控制层
          - mapper Dao层
          - service 服务实现层
      - test
        - generate 代码生成文件源码
        - util 生成代码用到的工具
        - ModifiedConstant 代码生成配置，代码生成前请配置该文件
        - ExecuteGenerate 代码生成执行类，里面有几个生成例子
      
  - resources 模板文件，如有需要可以自定义


## 说明
1. 你会看到我的自动生成代码显得比较复杂，有那么多查询返回的自定义类，业务初期你可能一个简单的单表查询就能解决所有字段的查询，
但需求是一直在变的，很快你就要面对重新定义返回类，入参类等等，所以我决定在代码生成初期就尽可能的细分代码
2. 生成基本接口有：增、删、改、查单个及查列表，其中查单个和查列表根据企业实战很有可能随业务变动会变成不止查询一张表，所以我留了mapper层由用户自己实现
3. 之所以用jackson进行序列化配置，也是因为用fastjson的时候常遇到坑，如果你习惯使用fastjson这两者也不冲突，你可以在业务代码里使用fastjson
4. 推荐多用Precondition类来抛出异常，由全局异常处理判断如何处理
5. base包我不打算打成额外Jar包，我希望随业务增长，使用者可以把base包作为基础包不断自行迭代
6. <span id="1">根据企业级实战，我们业务后期常会遇到一种情况，好多接口的调用都可能会需要操作相同的业务，比如用户的很多行为，都可能改变用户状态，
我的业务码设计就是为了解决这种情况`Result.ok().addBusinessClass(Result.wrap(BusinessEnum.A001.getClazz())) `通过这样添加业务码统一去解决同一件事，本质用到了设计模式的策略模式，你可以处理的参数为入参和出参
</span>