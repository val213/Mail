/*雪花算法（Snowflake）:由Twitter开源的分布式唯一ID生成算法,
它可以在分布式系统中生成唯一的ID。每个ID是一个64位的长整型数字，它们是按时间顺序生成的。
在这里由于我们的ID是Integer类型，故一是可以选择类型的强转，但是这样无法保证有序性；二是对该算法进行调整使得其生成的是Integer类型的ID

雪花算法的优点：1、高效生成：雪花算法生成ID的速度非常快，可以达到数百万个ID每秒。
             2、分布式唯一性：通过设置不同的workerId和datacenterId，可以在分布式系统中保证ID的唯一性
             3、有序性：生成的ID是按时间顺序排序的，有利于数据库索引和查询的优化（但是目前我们是强转long为Integer，有序性这一点不满足）
             4、高可用性：不依赖中心节点，避免了单点故障的问题（针对分布式系统）
*/

package com.example.backend.algorithm;
public class SnowflakeIdGenerator {
    //纪元时间，用来计算当前时间戳的基准，一般设置为系统开始时间
    private final long twepoch = 1288834974657L;
    //工作节点所占的位数
    private final long workerIdBits = 5L;
    //数据中心ID所占的位数
    private final long datacenterIdBits = 5L;
    //计算出的最大工作节点ID
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    //计算出的最大数据中心ID
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    //序列号占用的位数
    private final long sequenceBits = 12L;
    //工作节点部分在生成ID时需要左移的位数
    private final long workerIdShift = sequenceBits;
    //数据中心部分在生成ID时需要左移的位数
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    //时间戳部分在生成ID时需要左移的位数
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    //序列号掩码，用于按位与操作，保证序列号不超过最大值
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);
    //当前节点的工作ID
    private long workerId;
    //当前节点的数据中心ID
    private long datacenterId;
    //序列号，用来记录同一毫秒内生成的ID数
    private long sequence = 0L;
    //上次生成ID的时间戳
    private long lastTimestamp = -1L;

    //构造函数，初始化workerId和datacenterId,并进行边界检查
    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    //生成Id
    public synchronized long nextId() {
        long timestamp = timeGen();  //获取当前时间戳

        //若当前时间小于上次生成ID的时间就抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {    //如果当前时间等于上次生成ID的时间，则增加序列号
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {  //如果序列号溢出，则等待到下一毫秒再生成ID
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {    //如果当前时间大于上次生成ID的时间，则重置序列号
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    //等待直到下一毫秒
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    //获取当前系统的时间戳
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}

