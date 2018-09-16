# 关于offset维护
- 方案一 kafkamanager  
        主要借助kafkautils和 kc 相关的方法实现了有则用,没有则根据kafkaparams中的参数从最新消费还是最旧消费  
        缺陷:
            不能报错，关于每一步应该给出相应的警告
- 方案二 
        借助zkclient完成offset的存取（前提是理解方案一的一些步骤）
- 方案三  
        checkpoint的维护方式