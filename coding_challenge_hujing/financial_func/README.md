# Excel 财务函数实现 #

### 问题 ###

* 尝试实现Excel中债券相关核心的YIELD YIELDMAT  DURATION  MDURATION等财务函数的计算， 实现服务端计算入库，不依赖Excel产出
* 目前找到的有IMSL和JMSL(JMSL™ Numerical Library)两个商业算法库，还有一个F#版本的 https://github.com/fsprojects/ExcelFinancialFunctions， 可考虑将现有F#版本相关算法移植到JVM上。
* 难点主要在，F#为一门.net平台上的函数式编程语言，其中有些闭包调用，一些函数式语言中的fold，aggregate等操作比较难以直接转换到java语言。
* 已建立将相关入口类及方法样板代码，单元测试相关的代码已经初步完成

### 要点 ###

* 完成YIELD YIELDMAT  DURATION  MDURATION四个Excel函数的功能开发，现有单元测试用例通过，补充其他用例，保证单元测试代码覆盖率；
* 注意编码规范，命名规范等；
* 考查Git提交规范性， 注意控制commit粒度， 方便从commit log考查候选人开发，重构及解决问题思路；
* 考查候选人综合解决特定问题的能力；
