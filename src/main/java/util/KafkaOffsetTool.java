package util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.cluster.Broker;
import kafka.common.TopicAndPartition;
import kafka.javaapi.OffsetRequest;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.consumer.SimpleConsumer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KafkaOffsetTool {

    private static KafkaOffsetTool instance;
    final int TIMEOUT = 100000;
    final int BUFFERSIZE = 64 * 1024;

    private KafkaOffsetTool() {
    }

    public static synchronized KafkaOffsetTool getInstance() {
        if (instance == null) {
            instance = new KafkaOffsetTool();
        }
        return instance;
    }

    public Map<TopicAndPartition, Long> getLastOffset(String brokerList, List<String> topics,
                                                      String groupId) {

        Map<TopicAndPartition, Long> topicAndPartitionLongMap = Maps.newHashMap();

        Map<TopicAndPartition, Broker> topicAndPartitionBrokerMap =
                KafkaOffsetTool.getInstance().findLeader(brokerList, topics);

        for (Map.Entry<TopicAndPartition, Broker> topicAndPartitionBrokerEntry : topicAndPartitionBrokerMap
                .entrySet()) {
            // get leader broker
            Broker leaderBroker = topicAndPartitionBrokerEntry.getValue();

            kafka.javaapi.consumer.SimpleConsumer simpleConsumer = /*new kafka.javaapi.consumer.SimpleConsumer(leaderBroker.host(), leaderBroker.port(),
                    TIMEOUT, BUFFERSIZE, groupId);*/ null;

            long readOffset = getTopicAndPartitionLastOffset(simpleConsumer,
                    topicAndPartitionBrokerEntry.getKey(), groupId);

            topicAndPartitionLongMap.put(topicAndPartitionBrokerEntry.getKey(), readOffset);

        }

        return topicAndPartitionLongMap;

    }

    /**
     * * @param brokerList * @param topics * @param groupId * @return
     */
    public Map<TopicAndPartition, Long> getEarliestOffset(String brokerList, List<String> topics,
                                                          String groupId) {

        Map<TopicAndPartition, Long> topicAndPartitionLongMap = Maps.newHashMap();

        Map<TopicAndPartition, Broker> topicAndPartitionBrokerMap =
                KafkaOffsetTool.getInstance().findLeader(brokerList, topics);

        for (Map.Entry<TopicAndPartition, Broker> topicAndPartitionBrokerEntry : topicAndPartitionBrokerMap
                .entrySet()) {
            // get leader broker
            Broker leaderBroker = topicAndPartitionBrokerEntry.getValue();

            SimpleConsumer simpleConsumer = /*new kafka.javaapi.consumer.SimpleConsumer(leaderBroker.host(), leaderBroker.port(),
                    TIMEOUT, BUFFERSIZE, groupId);*/null;

            long readOffset = getTopicAndPartitionEarliestOffset(simpleConsumer,
                    topicAndPartitionBrokerEntry.getKey(), groupId);

            topicAndPartitionLongMap.put(topicAndPartitionBrokerEntry.getKey(), readOffset);

        }

        return topicAndPartitionLongMap;

    }

    /**
     * 得到所有的 TopicAndPartition * * @param brokerList * @param topics * @return topicAndPartitions
     */
    private Map<TopicAndPartition, Broker> findLeader(String brokerList, List<String> topics) {
        // get broker's url array
        String[] brokerUrlArray = getBorkerUrlFromBrokerList(brokerList);
        // get broker's port map
        Map<String, Integer> brokerPortMap = getPortFromBrokerList(brokerList);

        // create array list of TopicAndPartition
        Map<TopicAndPartition, Broker> topicAndPartitionBrokerMap = Maps.newHashMap();

        for (String broker : brokerUrlArray) {

            kafka.javaapi.consumer.SimpleConsumer consumer = null;
            try {
                // new util of simple Consumer
                consumer = new kafka.javaapi.consumer.SimpleConsumer(broker, brokerPortMap.get(broker), TIMEOUT, BUFFERSIZE,
                        "leaderLookup" + new Date().getTime());

                kafka.javaapi.TopicMetadataRequest req = new kafka.javaapi.TopicMetadataRequest(topics);

                kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);

                List<kafka.javaapi.TopicMetadata> metaData = resp.topicsMetadata();

                for (kafka.javaapi.TopicMetadata item : metaData) {
                    for (PartitionMetadata part : item.partitionsMetadata()) {
                        TopicAndPartition topicAndPartition =
                                new TopicAndPartition(item.topic(), part.partitionId());
                        topicAndPartitionBrokerMap.put(topicAndPartition, /*part.leader()*/ null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (consumer != null)
                    consumer.close();
            }
        }
        return topicAndPartitionBrokerMap;
    }

    /**
     * get last offset * @param consumer * @param topicAndPartition * @param clientName * @return
     */
    private long getTopicAndPartitionLastOffset(kafka.javaapi.consumer.SimpleConsumer consumer,
                                                TopicAndPartition topicAndPartition, String clientName) {
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo =
                new HashMap<>();

        requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(
                kafka.api.OffsetRequest.LatestTime(), 1));

        OffsetRequest request = new OffsetRequest(
                requestInfo, kafka.api.OffsetRequest.CurrentVersion(),
                clientName);

        kafka.javaapi.OffsetResponse response = consumer.getOffsetsBefore(request);

        if (response.hasError()) {
            System.out.println("Error fetching data Offset Data the Broker. Reason: "
                    + response.errorCode(topicAndPartition.topic(), topicAndPartition.partition()));
            return 0;
        }
        long[] offsets = response.offsets(topicAndPartition.topic(), topicAndPartition.partition());
        return offsets[0];
    }

    /**
     * get earliest offset * @param consumer * @param topicAndPartition * @param clientName * @return
     */
    private long getTopicAndPartitionEarliestOffset(kafka.javaapi.consumer.SimpleConsumer consumer,
                                                    TopicAndPartition topicAndPartition, String clientName) {
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo =
                new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();

        requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(
                kafka.api.OffsetRequest.EarliestTime(), 1));

        OffsetRequest request = new OffsetRequest(
                requestInfo, kafka.api.OffsetRequest.CurrentVersion(),
                clientName);

        kafka.javaapi.OffsetResponse response = consumer.getOffsetsBefore(request);

        if (response.hasError()) {
            System.out
                    .println("Error fetching data Offset Data the Broker. Reason: "
                            + response.errorCode(topicAndPartition.topic(), topicAndPartition.partition()));
            return 0;
        }
        long[] offsets = response.offsets(topicAndPartition.topic(), topicAndPartition.partition());
        return offsets[0];
    }

    /**
     * 得到所有的broker url * * @param brokerlist * @return
     */
    private String[] getBorkerUrlFromBrokerList(String brokerlist) {
        String[] brokers = brokerlist.split(",");
        for (int i = 0; i < brokers.length; i++) {
            brokers[i] = brokers[i].split(":")[0];
        }
        return brokers;
    }

    /**
     * 得到broker url 与 其port 的映射关系 * * @param brokerlist * @return
     */
    private Map<String, Integer> getPortFromBrokerList(String brokerlist) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        String[] brokers = brokerlist.split(",");
        for (String item : brokers) {
            String[] itemArr = item.split(":");
            if (itemArr.length > 1) {
                map.put(itemArr[0], Integer.parseInt(itemArr[1]));
            }
        }
        return map;
    }

    public static void main(String[] args) {
        List<String> topics = Lists.newArrayList();
        topics.add("bsa_sys_tmp");
// topics.add("bugfix");
        Map<TopicAndPartition, Long> topicAndPartitionLongMap =
                KafkaOffsetTool.getInstance().getEarliestOffset("bsa142:9092,bsa143:9092", topics,
                        "com.nsfocus.bsa.setl");

        for (Map.Entry<TopicAndPartition, Long> entry : topicAndPartitionLongMap.entrySet()) {
            System.out.println(entry.getKey().topic() + "-" + entry.getKey().partition() + ":" + entry.getValue());
        }

    }
}
        
       