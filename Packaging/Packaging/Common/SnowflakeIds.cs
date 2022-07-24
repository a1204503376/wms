using System;

namespace Packaging.Common
{
    /// <summary>
    /// 雪花算法改进，使用未来时间弥补NTP回拨导致的异常
    /// <para>目前缺陷是lastTimestamp数据，如果程序关闭后发生NTP回拨，程序仍然会产生重复id</para>
    /// </summary>
    public class SnowflakeIds
    {
        private static readonly Lazy<SnowflakeIds> Lazy = new Lazy<SnowflakeIds>(() => new SnowflakeIds(1, 1));

        public static SnowflakeIds Instance => Lazy.Value;

        //基准时间2019-12-19
        private static long StartStmp = 1576684800000L;

        //private const long START_STMP = 1480166465631L;
        /*每一部分占用的位数*/
        //机器标识位数
        const int MachineIdBits = 5;

        //数据标志位数
        const int DatacenterIdBits = 5;

        //序列号识位数
        const int SequenceBits = 12;

        /* 每一部分的最大值*/
        //机器ID最大值
        const long MaxMachineNum = -1L ^ (-1L << MachineIdBits);

        //数据标志ID最大值
        const long MaxDatacenterNum = -1L ^ (-1L << DatacenterIdBits);

        //序列号ID最大值
        private const long MaxSequenceNum = -1L ^ (-1L << SequenceBits);

        /*每一部分向左的位移*/
        //机器ID偏左移12位
        private const int MachineShift = SequenceBits;

        //数据ID偏左移17位
        private const int DatacenterIdShift = SequenceBits + MachineIdBits;

        //时间毫秒左移22位
        private const int TimestampLeftShift = SequenceBits + MachineIdBits + DatacenterIdBits;


        private long sequence; //序列号
        private long lastTimestamp = -1L; //上一次时间戳

        /// <summary>
        /// 机器标识
        /// </summary>
        public long MachineId { get; protected set; }

        /// <summary>
        /// 数据中心
        /// </summary>
        public long DatacenterId { get; protected set; }

        private readonly DateTime jan1St1970 = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
        private readonly object _lock = new object();

        /// <summary>
        /// 雪花算法改进，使用未来时间弥补NTP回拨导致的异常
        /// </summary>
        /// <param name="datacenterId">数据中心id，0-31</param>
        /// <param name="machineId">机器id，0-31</param>
        public SnowflakeIds(long datacenterId, long machineId)
        {
            // 如果超出范围就抛出异常
            if (datacenterId > MaxDatacenterNum || datacenterId < 0)
            {
                throw new ArgumentException($"datacenterId 取值范围：0~{MaxDatacenterNum}");
            }

            if (machineId > MaxMachineNum || machineId < 0)
            {
                throw new ArgumentException($"machineId 取值范围：0~{MaxMachineNum}");
            }

            //先检验再赋值
            MachineId = machineId;
            DatacenterId = datacenterId;
        }

        /// <summary>
        /// 获取id
        /// </summary>
        /// <returns></returns>
        public long NextId()
        {
            lock (_lock)
            {
                var timestamp = TimeGen();
                if (timestamp < lastTimestamp)
                {
                    return NextNtpId();
                    //throw new Exception($"时间戳必须大于上一次生成ID的时间戳.  拒绝为{lastTimestamp - timestamp}毫秒生成id");
                }

                //如果上次生成时间和当前时间相同,在同一毫秒内
                if (timestamp == lastTimestamp)
                {
                    //sequence自增，和sequenceMask相与一下，去掉高位
                    sequence = (sequence + 1) & MaxSequenceNum;
                    //判断是否溢出,也就是每毫秒内超过4095，当为4095时，与MaxSequenceNum相与，sequence就等于0
                    if (sequence == 0L)
                    {
                        //等待到下一毫秒
                        timestamp = TilNextMillis(lastTimestamp);
                    }
                }
                else
                {
                    //如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加,
                    //为了保证尾数随机性更大一些,最后一位可以设置一个随机数
                    sequence = 0L; //new Random().Next(10);
                }

                lastTimestamp = timestamp;
                return ((timestamp - StartStmp) << TimestampLeftShift) |
                       (DatacenterId << DatacenterIdShift) |
                       (MachineId << MachineShift) |
                       sequence;
            }
        }

        /// <summary>
        /// 产生NTP回拨id
        /// </summary>
        /// <returns></returns>
        private long NextNtpId()
        {
            //sequence自增，和sequenceMask相与一下，去掉高位
            sequence = (sequence + 1) & MaxSequenceNum;
            //判断是否溢出,也就是每毫秒内超过4095，当为4095时，与MaxSequenceNum相与，sequence就等于0
            if (sequence == 0L)
            {
                //自动+1毫秒，用未来时间做弥补，直至补齐
                ++lastTimestamp;
            }

            return ((lastTimestamp - StartStmp) << TimestampLeftShift) |
                   (DatacenterId << DatacenterIdShift) |
                   (MachineId << MachineShift) |
                   sequence;
        }

        /// <summary>
        /// 防止产生的时间比之前的时间还要小（由于NTP回拨等问题）,保持增量的趋势.
        /// </summary>
        /// <param name="timestamp"></param>
        /// <returns></returns>
        private long TilNextMillis(long timestamp)
        {
            var tmp = TimeGen();
            while (tmp <= timestamp)
            {
                timestamp = TimeGen();
            }

            return tmp;
        }

        /// <summary>
        /// 获取当前的时间戳
        /// </summary>
        /// <returns></returns>
        private long TimeGen()
        {
            return (long)(DateTime.UtcNow - jan1St1970).TotalMilliseconds;
        }
    }
}