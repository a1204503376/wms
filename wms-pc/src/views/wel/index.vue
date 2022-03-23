<template>
  <div class="homePage">
    <el-row :gutter="10" class="detailed">
      <el-col :span="18">
        <div class="left_center">
          <div class="header">
            <p>周转分析</p>
          </div>
          <div class="main">
            <ul class="main_ul">
              <li>出库单量</li>
              <el-tooltip class="item" effect="dark" :enterable="false"
                          content="当日出库订单总量"
                          placement="top-start">
                <li>{{ this.reportSoBillCount.billCountToday }}</li>
              </el-tooltip>

              <template v-if="reportSoBillCount.rate < 0">
                <el-tooltip class="item" effect="dark" :enterable="false"
                            :content="['当日对比昨日单量下降 '+(0-reportSoBillCount.rate)]+'%'"
                            placement="top-start">
                  <li style="color:red;">
                    <i class="el-icon-caret-bottom"></i>
                    {{ 0 - reportSoBillCount.rate }}%
                  </li>
                </el-tooltip>

              </template>
              <template v-else>
                <el-tooltip class="item" effect="dark" :enterable="false"
                            :content="['当日对比昨日单量上涨 '+reportSoBillCount.rate]+'%'"
                            placement="top-start">
                  <li style="color:green;">
                    <i class="el-icon-caret-top"></i>
                    {{ reportSoBillCount.rate }}%
                  </li>
                </el-tooltip>
              </template>
              <el-tooltip class="item" effect="dark" :enterable="false"
                          content="当日出库订单包含物品数量"
                          placement="top-start">
                <li>
                  <p>sku数量 {{ reportSoBillCount.skuCountToday }}</p>
                </li>
              </el-tooltip>
            </ul>
            <ul class="main_ul">
              <li>入库单量</li>
              <el-tooltip class="item" effect="dark" :enterable="false"
                          content="当日入库订单总量"
                          placement="top-start">
                <li>{{ this.reportAsnBillCount.billCountToday }}</li>
              </el-tooltip>
              <template v-if="reportAsnBillCount.rate < 0">
                <el-tooltip class="item" effect="dark" :enterable="false"
                            :content="['当日对比昨日单量下降 '+(0-reportAsnBillCount.rate)]+'%'"
                            placement="top-start">
                  <li style="color:red;">
                    <i class="el-icon-caret-bottom"></i>
                    {{ 0 - reportAsnBillCount.rate }}%
                  </li>
                </el-tooltip>
              </template>
              <template v-else>
                <el-tooltip class="item" effect="dark" :enterable="false"
                            :content="['当日对比昨日单量上涨 '+reportAsnBillCount.rate]+'%'"
                            placement="top-start">
                  <li style="color:green;">
                    <i class="el-icon-caret-top"></i>
                    {{ reportAsnBillCount.rate }}%
                  </li>
                </el-tooltip>
              </template>
              <el-tooltip class="item" effect="dark" :enterable="false" content="当日入库订单包含物品数量"
                          placement="top-start">
                <li>
                  <p>sku数量 {{ reportAsnBillCount.skuCountToday }}<i class="el-icon-star-on"></i></p>
                </li>
              </el-tooltip>
            </ul>
            <ul class="main_ul">
              <li>缺货分析</li>
              <el-tooltip class="item" effect="dark" :enterable="false" content="当前订单量"
                          placement="top-start">
                <li style="color:red;">{{ this.skuCount.count }}</li>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" :enterable="false" content="当前订单缺货量"
                          placement="top-start">
                <li style="color:green;">{{ this.skuCount.qty }}</li>
              </el-tooltip>
            </ul>
            <ul class="main_ul mouse_over" @click="dullnessDialog()">
              <li>呆滞物品</li>
              <el-tooltip class="item" effect="dark" :enterable="false" content="呆滞一周物品数量"
                          placement="top-start">
                <li>{{ this.idleSkuRlt.currSkuCount }}&nbsp;</li>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" :enterable="false" content="同上一周相比"
                          placement="top-start">
                <template v-if="idleSkuRlt.rate < 0">
                  <li style="color:red;">
                    <i class="el-icon-caret-bottom"></i>
                    {{ 0 - idleSkuRlt.rate }}%
                  </li>
                </template>
                <template v-else>
                  <li style="color:green;">
                    <i class="el-icon-caret-top"></i>
                    {{ idleSkuRlt.rate }}%
                  </li>
                </template>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" :enterable="false" content="呆滞物品占用库位数量"
                          placement="top-start">
                <li>
                  <p>占用库位 {{ idleSkuRlt.occupyLocCount }}<i class="el-icon-star-on"></i></p>
                </li>
              </el-tooltip>
            </ul>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="right_center">
          <div class="header">
            <p>库存分析</p>
          </div>
          <el-row>
            <el-col :span="12">
              <ul class="main_ul">
                <li>库存物品总数</li>
                <el-tooltip class="item" effect="dark" :enterable="false" content="当月库存物品数量"
                            placement="top-start">
                  <li>{{ stockSkuCount.currentSkuCount }}</li>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" :enterable="false" content="同比上个月"
                            placement="top-start">
                  <template v-if="stockSkuCount.rate < 0">
                    <li style="color:red;">
                      <i class="el-icon-caret-bottom"></i>
                      {{ 0 - stockSkuCount.rate }}%
                    </li>
                  </template>
                  <template v-else>
                    <li style="color:green;">
                      <i class="el-icon-caret-top"></i>
                      {{ stockSkuCount.rate }}%
                    </li>
                  </template>
                </el-tooltip>
              </ul>
            </el-col>
            <el-col :span="12">
              <ul class="main_ul">
                <li>库位占用</li>
                <el-tooltip class="item" effect="dark" :enterable="false" content="当月占用库位数量"
                            placement="top-start">
                  <li>{{ locOccupy.currOccupyCount }}</li>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" :enterable="false" content="同比上个月"
                            placement="top-start">
                  <template v-if="locOccupy.rate < 0">
                    <li style="color:red;">
                      <i class="el-icon-caret-bottom"></i>
                      {{ 0 - locOccupy.rate }}%
                    </li>
                  </template>
                  <template v-else>
                    <li style="color:green;">
                      <i class="el-icon-caret-top"></i>
                      {{ locOccupy.rate }}%
                    </li>
                  </template>
                </el-tooltip>
              </ul>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="12">
        <div id="barOutstockSku" class="eharts-box"></div>
      </el-col>
      <el-col :span="12">
        <div id="pieUnExecTaskType" class="eharts-box"></div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="12">
        <div class="left_security">
          <span class="title">安全库存预警</span>
          <el-table ref="table1" :data="tableOption.tableData1" style="width: 100%">
            <el-table-column prop="whName" label="库房" width="180" show-overflow-tooltip></el-table-column>
            <el-table-column prop="skuName" label="物品名称" width="180" show-overflow-tooltip></el-table-column>
            <el-table-column prop="stockQty" label="库存量" show-overflow-tooltip></el-table-column>
            <el-table-column prop="lowStorage" label="库存低储" show-overflow-tooltip></el-table-column>
            <el-table-column prop="highStorage" label="库存高储" show-overflow-tooltip></el-table-column>
          </el-table>
        </div>
        <!--                <div id="heatmapLoc" class="eharts-box" :style="echarts_box_style"></div>-->
      </el-col>
      <el-col :span="12">
        <div class="right_storage">
          <span class="title">物品出库量</span>
          <el-table ref="table2" :data="tableOption.tableData2" style="width: 100%">
            <el-table-column prop="whName" label="库房" width="180" show-overflow-tooltip></el-table-column>
            <el-table-column prop="skuName" label="物品名称" width="180" show-overflow-tooltip>
              <template slot-scope="scope"> {{ scope.row.skuName }}</template>
            </el-table-column>
            <el-table-column prop="qty" label="出库总量" show-overflow-tooltip></el-table-column>
            <el-table-column prop="wsuName" label="计量单位" show-overflow-tooltip></el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
    <!--呆滞物品 弹框-->
<!--    <dullGoods :visible="dialogdull.visible" @callback="callbackJuris"></dullGoods>-->
  </div>
</template>


<script>
import {
  getSoBillCount,
  getAsnBillCount,
  getStockoutSkuCount,
  getIdleSkuInfo,
  getBillTrait,
  getSkuStockInfo,
  getLocOccupyInfo,
  getOutstockRate,
  getUnExecTaskInfo,
  getUnsafeStockSkuList,
  getSkuOutstockSummary
} from "@/api/statistics/statistics";
// import dullGoods from './components/dullGoods'

let goodsData = require("./echarts.json");

function convert(source, target, basePath) {
  for (let key in source) {
    let path = basePath ? basePath + "." + key : key;
    if (!key.match(/^\$/)) {
      target.children = target.children || [];
      let child = {
        name: path
      };
      target.children.push(child);
      convert(source[key], child, path);
    }
  }

  if (!target.children) {
    target.value = source.$count || 1;
  } else {
    target.children.push({
      name: basePath,
      value: source.$count
    });
  }
}

let data = [];
convert(goodsData, data, "");

export default {
  name: "homePage",
  data() {
    return {
      dialogdull: {
        visible: false,
      },
      inited: false,
      timer: {
        table1: null,
        table2: null
      },
      my_interval: null,
      body_wrapper: null,
      tableOption: {
        maxHeight: 0,
        tableData1: [],
        tableData2: [],
        height: 200
      },
      refreshToken: {},
      echarts_box_height: "100px",
      barOutstockSku: null,
      barSafeStock: null,
      heatmapLoc: null,
      pieUnExecTaskType: null,
      reportSoBillCount: {
        billCountToday: 0,
        billCountYesterday: 0,
        rate: 0,
        skuCountToday: 0
      },
      reportAsnBillCount: {
        billCountToday: 0,
        billCountYesterday: 0,
        rate: 0,
        skuCountToday: 0
      },
      skuCount: {
        count: 0,
        qty: 0
      },
      idleSkuRlt: {
        currentWeekCount: 0,
        lastWeekCount: 0,
        rate: 0,
        occupyLocCount: 0
      },
      billTrait: {
        snCount: 0,
        notSnCount: 0
      },
      stockSkuCount: {
        currentSkuCount: 0,
        lastSkuCount: 0,
        rate: 0
      },
      locOccupy: {
        currOccupyCount: 0,
        lastOccupyCount: 0,
        rate: 0
      }
    };
  },
  components: {
    // dullGoods
  },
  deactivated() {
    window.removeEventListener("resize", this.windowOnsesize, false);
  },
  mounted() {
    this.body_wrapper = document.querySelectorAll('.el-table__body');

    this.initEchartsItemHeight();
    let self = this;
    this.$set(this.tableOption, 'maxHeight', 500);
    setTimeout(() => {
      // 基于准备好的dom，初始化echarts实例
      this.barOutstockSku = this.$echarts.init(document.getElementById('barOutstockSku'));
      // this.barSafeStock = this.$echarts.init(document.getElementById('barSafeStock'));
      // this.heatmapLoc = this.$echarts.init(document.getElementById('heatmapLoc'));
      this.pieUnExecTaskType = this.$echarts.init(document.getElementById('pieUnExecTaskType'));
      self.drawLine();
      self.inited = true;
    }, 0);
    this.my_interval = setInterval(function () {
      if (!self.inited) {
        return;
      }
      if (!window.sessionStorage.getItem('access_token')) {
        clearInterval(self.my_interval);
      }
    }, 1000 * 60);
    this.refreshToken = JSON.parse(window.sessionStorage.getItem('refresh_token'));
  },
  computed: {
    // echarts_box_style() {
    //   return {height: this.echarts_box_height + "px"};
    // }
  },
  created() {
    window.addEventListener("resize", this.windowOnsesize, false);
  },
  destroyed() {
    //清空定时任务
    if (this.my_interval) {
      clearTimeout(this.my_interval);
      this.my_interval = null;
    }
    var lastTimeoutId = setTimeout(";");
    for (var i = 0; i <= lastTimeoutId._id; i++) {
      clearTimeout(i);
    }
  },
  beforeDestroy() {
    //window.removeEventListener('resize', this.my_interval);
    clearInterval(this.timer.table1);
    clearInterval(this.timer.table2);
  },
  beforeRouteLeave(to, from, next) {
    next();
    if (this.my_interval) {
      clearInterval(this.my_interval);
      this.my_interval = null;
    }
  },
  methods: {
    //呆滞物品
    callbackJuris(res) {
      this.dialogdull.visible = res.visible;
    },
    dullnessDialog() {
      return;
      this.dialogdull.visible = true;
    },
    tableScroll() {
      let tr_h = 29; //tr行高
      let scrollNum = 1; //每一次滚动的距离。
      let offset_1 = 0, offset_2 = 0;// 偏移量

      this.timer.table1 = setInterval(() => {
        let dataLength = this.tableOption.tableData1.length;
        if (dataLength * tr_h <= this.tableOption.height) {
          this.body_wrapper[0].style['margin-top'] = '0px';
          return;
        }
        if (0 - offset_1 >= tr_h + scrollNum) {
          offset_1 = 0;
          let first_row = Object.assign({}, this.tableOption.tableData1[0]);
          this.tableOption.tableData1.splice(0, 1);
          this.tableOption.tableData1.push(first_row);
          clearInterval(this.timer.table1);
        } else {
          offset_1 -= scrollNum;
        }
        this.body_wrapper[0].style['margin-top'] = offset_1 + 'px';

      }, 100);
      this.timer.table2 = setInterval(() => {
        let dataLength = this.tableOption.tableData2.length;
        if (dataLength * tr_h <= this.tableOption.height) {
          this.body_wrapper[1].style['margin-top'] = '0px';
          return;
        }
        if (0 - offset_2 >= tr_h + scrollNum) {
          offset_2 = 0;
          let first_row = Object.assign({}, this.tableOption.tableData2[0]);
          this.tableOption.tableData2.splice(0, 1);
          this.tableOption.tableData2.push(first_row);
          clearInterval(this.timer.table2);
        } else {
          offset_2 -= scrollNum;
        }
        this.body_wrapper[1].style['margin-top'] = offset_2 + 'px';
      }, 100);
    },

    windowOnsesize(e) {
      this.initEchartsItemHeight();
      this.barOutstockSku.resize();
      // this.barSafeStock.resize();
      // this.heatmapLoc.resize();
      this.pieUnExecTaskType.resize();
    },
    initEchartsItemHeight() {
      const window_height = window.innerHeight;
      const rightTopNav_height = 0;
      let detailed = document.querySelector(".detailed");
      const el_center = document.querySelector(".center");
      const echarts_box_marginTopNum = 20;
      this.tableOption.height = this.echarts_box_height = Math.floor(
        (window_height -
          rightTopNav_height -
          (detailed ? detailed.offsetHeight : 0) -
          0 -
          echarts_box_marginTopNum) /
        2
      );
      this.$nextTick(()=>{
          if (this.$refs.table1) {
              this.$refs.table1.doLayout();
          }
      });
    },
    drawLine() {
      // 调用接口获取数据
      getSoBillCount().then(res => {
        this.reportSoBillCount = res.data.data;
      });
      getAsnBillCount().then(res => {
        this.reportAsnBillCount = res.data.data;
      });
      getStockoutSkuCount().then(res => {
        this.skuCount = res.data.data;
      });
      getIdleSkuInfo(null, null).then(res => {
        this.idleSkuRlt = res.data.data;
      });
      getBillTrait().then(res => {
        this.billTrait = res.data.data;
      });
      getSkuStockInfo().then(res => {
        this.stockSkuCount = res.data.data;
      });
      getLocOccupyInfo().then(res => {
        this.locOccupy = res.data.data;
      });
      // 安全库存预警
      getUnsafeStockSkuList().then(res => {
        this.tableOption.tableData1 = res.data.data;
        this.tableScroll();
      });
      // 出库统计
      getSkuOutstockSummary().then(res => {
        this.tableOption.tableData2 = res.data.data;
        this.tableScroll();
      })

      // 柱状图：一周内发货频率最高的前十个SKU
      getOutstockRate().then(res => {
        let data = [];//['曲轴', '调速弹簧', '上壳体', '活塞销', '连杆总成', '中间轴', '挺柱', '进气门', '飞轮', '链条'];
        let value = [];//[1500, 1749, 2938, 4737, 5013, 5380, 7530, 7739, 7839, 8374];
        res.data.data.forEach(item => {
          data.push(item.skuName);
          value.push(item.rate);
        });
        this.barOutstockSku.setOption({
          title: {
            top: 10,
            text: '一周发货数量前10 物品',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{b} : {c}'
          },
          xAxis: {
            type: 'category',
            data: data
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            data: value,
            type: 'bar',
            showBackground: true,
            backgroundStyle: {
              color: 'rgba(220, 220, 220, 0.8)'
            }
          }]
        });
      });
      // 柱状图：SKU安全库存

      // 热力图：一周内库位热力图

      // 饼状图：获取未执行任务类型
      getUnExecTaskInfo().then(res => {
        let taskList = res.data.data;
        let taskTypeList = [];
        let data = [];
        taskList.forEach(task => {
          data.push({
            value: task.taskQty,
            name: task.taskTypeDesc
          });
          taskTypeList.push(task.taskTypeDesc);
        });
        this.pieUnExecTaskType.setOption({
          title: {
            text: '待执行任务',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{b} : {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: taskTypeList
          },
          series: [
            {
              name: '访问来源',
              type: 'pie',
              radius: '55%',
              //center: ['50%', '60%'],
              data: data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        });
      });
      // setTimeout(this.drawLine, 1000 * 2);
    }
  }
};
</script>
<style lang="css">
*{margin:0px;padding:0px;}
.left_center, .right_center{
  min-height:220px;
}

.left_center, .right_center, .left_security, .right_storage {
  background:#fff;
}

.left_center .header, .right_center .header, .left_security .title, .right_storage .title{
  font-size:16px;
  height:50px;
  line-height:50px;
  margin-left:15px;
}
.left_center .main_ul {
  width:20%;
  float: left;
  margin-left:5%;
  border-right:none !important;
}

.right_center .main_ul {
  width:60%;
  float: left;
  margin-left:20%;
  border-right:none !important;
}

.main_ul li{
  font-size:14px;
  font-weight:700;
  list-style: none;
  padding:10px 0;
  line-height:17px;
}

.main_ul li:last-child {
  color:#1e9fff;
}
.eharts-box {
    background: #fff;
    margin-top: 5px;
    height: 250px;
}

</style>
