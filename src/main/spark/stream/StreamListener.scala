package sparkdemo.streaming

import org.apache.spark.streaming.scheduler._

/*
* 流的监听器 以后可以用来做做spark 任务的监控
* 将这个做成可以上报的那种
* */
class StreamListener extends StreamingListener {
    override def onStreamingStarted(streamingStarted: StreamingListenerStreamingStarted): Unit = {
        println("流处理开始了:" + streamingStarted.time)
    }

    override def onReceiverStarted(receiverStarted: StreamingListenerReceiverStarted): Unit = {
        println("接收开始了:" + receiverStarted.receiverInfo)
    }

    override def onReceiverError(receiverError: StreamingListenerReceiverError): Unit = {
        println("接收出错:" + receiverError.receiverInfo)
        println("接收出错:" + receiverError.receiverInfo.lastError)
        println("接收出错:" + receiverError.receiverInfo.lastErrorMessage)
        println("接收出错:" + receiverError.receiverInfo.name)
    }

    override def onReceiverStopped(receiverStopped: StreamingListenerReceiverStopped): Unit = {
        println("接收停止了:" + receiverStopped.receiverInfo)
    }

    override def onBatchSubmitted(batchSubmitted: StreamingListenerBatchSubmitted): Unit = {
        println("batch提交了：" + batchSubmitted.batchInfo)
        println("batch提交了：处理数据条数" + batchSubmitted.batchInfo.numRecords)
    }

    override def onBatchStarted(batchStarted: StreamingListenerBatchStarted): Unit = {
        println("batch 开始了" + batchStarted.batchInfo)
        println("batch 开始了：处理数据条数：" + batchStarted.batchInfo.numRecords)
        println("batch 开始了：处理数据条数：" + batchStarted.batchInfo.batchTime)
    }

    override def onBatchCompleted(batchCompleted: StreamingListenerBatchCompleted): Unit = {
        println("batch 结束了：" + batchCompleted.batchInfo)
        println("batch 结束了：结束时间:" + batchCompleted.batchInfo.processingEndTime)
        println("batch 结束了：开始时间:" + batchCompleted.batchInfo.processingStartTime)
        println("batch 结束了：延迟时间:" + batchCompleted.batchInfo.processingDelay)
        println("batch 结束了：batchtime:" + batchCompleted.batchInfo.batchTime)
        println("batch 结束了：调度延时:" + batchCompleted.batchInfo.schedulingDelay)
        println("batch 结束了：处理数据条数:" + batchCompleted.batchInfo.numRecords)
        println("batch 结束了：输出信息:" + batchCompleted.batchInfo.outputOperationInfos)
    }

    override def onOutputOperationStarted(outputOperationStarted: StreamingListenerOutputOperationStarted): Unit = {
        println("输出操作开始了:" + outputOperationStarted.outputOperationInfo)
    }

    override def onOutputOperationCompleted(outputOperationCompleted: StreamingListenerOutputOperationCompleted): Unit = {
        println("输出操作结束了：" + outputOperationCompleted.outputOperationInfo)
    }
}
