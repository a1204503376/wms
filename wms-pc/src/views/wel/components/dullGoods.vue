<template>
  <el-dialog
    title="呆滞物品"
    :visible.sync="visible"
    :close-on-click-modal="false"
    @open="dialogOpen"
    @opened="dialogOpened"
    :before-close="dialogClose"
    v-dialogDrag="true"
    width="65%"
    top="3vh"
    class="dialogs"
    append-to-body
    destroy-on-close
  >
    <div class="dialog-body">
      <el-row class="tabs-item">
        <el-col :span="3">
          <div style="width: 120px;">
            <ul id="tabList">
              <li :class="{active:tabIndex === 0}" @click="changeTab(0)">当前时间</li>
              <li :class="{active:tabIndex === 1}" @click="changeTab(1)">上次时间</li>
              <li :class="{active:tabIndex === 2}" @click="changeTab(2)">占用库位</li>
            </ul>
          </div>
        </el-col>
        <el-col :span="21">
          <div class="grid-content bg-purple-light">
            <div id="content">
              <div class="block">
                <el-form label-position="right" label-width="85px" size="medium">
                  <el-row :gutter="20">
                    <el-col :span="20">
                      <el-form-item label="时间范围">
                        <el-date-picker
                          type="daterange"
                          v-model="currTimeArea"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          size="small"
                          range-separator="至"
                          start-placeholder="开始时间"
                          end-placeholder="结束时间"
                          :clearable="false"
                        ></el-date-picker>
                      </el-form-item>
                    </el-col>
                    <el-col :span="4">
                      <el-button
                        type="primary"
                        size="small"
                        style="margin-top: 3px;"
                        @click="idleSkuSearch()"
                      >查 询
                      </el-button>
                    </el-col>
                  </el-row>
                </el-form>
              </div>
              <el-row :gutter="20" class="tabs-item">
                <el-col :span="24">
                  {{ data.currBeginTime }}至
                  {{ data.currEndTime }}呆滞物品
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="24">
                  <div class="tableDdata">
                    <el-table border :data="data.currSkuList">
                      <el-table-column property="skuCode" label="物品编码"></el-table-column>
                      <el-table-column property="skuName" label="物品名称"></el-table-column>
                      <el-table-column property="lastOutstockTime"
                                       label="上次出库时间"></el-table-column>
                    </el-table>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20" class="tabs-item">
                <el-col :span="24">
                  {{ data.lastBeginTime }}至
                  {{ data.lastEndTime }}呆滞物品
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="24">
                  <div class="tableDdata">
                    <el-table border :data="data.lastSkuList">
                      <el-table-column property="skuCode" label="物品编码"></el-table-column>
                      <el-table-column property="skuName" label="物品名称"></el-table-column>
                      <el-table-column property="lastOutstockTime"
                                       label="上次出库时间"></el-table-column>
                    </el-table>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20" class="tabs-item">
                <el-col :span="24">
                  {{ data.currBeginTime }}至
                  {{ data.currEndTime }}呆滞物品占用库位
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="24">
                  <div class="tableDdata">
                    <el-table border :data="data.occupyLocList">
                      <el-table-column property="locCode" label="库位编码"></el-table-column>
                    </el-table>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </el-dialog>
</template>
<script>
import {getIdleSkuInfo} from "@/api/statistics/statistics";

export default {
  name: "dullGoods",
  data() {
    return {
      tabIndex: 0,
      isHandelScroll: false, //区分是点击切换还是滚动
      tabOffsetTop: [],
      data: {
        currBeginTime: "",
        currEndTime: "",
        currSkuCount: 0,
        currSkuList: [],
        lastBeginTime: "",
        lastEndTime: "",
        lastSkuCount: 0,
        lastSkuList: [],
        occupyLocCount: 0,
        occupyLocList: []
      },
      currTimeArea: [], //时间范围
      currBeginTime: "", //开始时间
      currEndTime: "", //结束时间
      gridData: [],
      callback: {
        visible: false,
        result: false
      },
    };
  },
  props: {
    visible: {type: Boolean, default: false}
    // dataSource: {type: String, default: ''},
  },
  methods: {
    dialogOpen() {
      this.idleSkuSearch();
    },
    dialogOpened() {
      // 记录tab位置
      let tabs = document.querySelectorAll("#content .tabs-item");
      if (tabs) {
        this.tabOffsetTop = [];
        tabs.forEach(item => {
          this.tabOffsetTop.push(item.offsetTop);
        });
      }
    },
    idleSkuSearch() {
      let beginTime = "",
        endTime = "";
      if (this.currTimeArea.length > 0) {
        beginTime = this.currTimeArea[0];
        endTime = this.currTimeArea[1];
      }
      getIdleSkuInfo(beginTime, endTime).then(res => {
        this.data = res.data.data;
        this.currTimeArea = [];
        this.currTimeArea.push(this.data.currBeginTime);
        this.currTimeArea.push(this.data.currEndTime);
      });
    },
    dialogClose() {
      this.callback.visible = false;
      this.callback.result = false;
      this.$emit("callback", this.callback);
    },
    // 监控滚动条
    handleScroll(vertical, horizontal, nativeEvent) {
      if (this.isHandelScroll) {
        return;
      }
      let num = 500; //滚动div的高度
      let scrollHeight = nativeEvent.target.scrollHeight;
      let scrollTop = vertical.scrollTop;
      let countTabOffsetTop = this.tabOffsetTop.map(v => {
        return Math.abs(v - scrollTop);
      });
      let countTabOffsetTop2 = this.tabOffsetTop.map(v => {
        return v - scrollTop;
      });
      let minNum = Math.min.apply(Math, countTabOffsetTop);
      let index = countTabOffsetTop.indexOf(minNum);

      if (countTabOffsetTop[index] < 50 && countTabOffsetTop2[index] >= 0) {
        this.tabIndex = index;
      }
      if (Math.abs(scrollTop - (scrollHeight - num)) <= 50) {
        //处理最后一个tab
        this.tabIndex = this.tabOffsetTop.length - 1;
      }
    },
    // 切换左侧tab
    changeTab(index) {
      this.isHandelScroll = true;
      this.tabIndex = index;
      this.$refs["vs"].scrollTo(
        {
          y: this.tabOffsetTop[index]
        },
        200
      );
      setTimeout(() => {
        this.isHandelScroll = false;
      }, 200);
    }
  }
};
</script>
<style lang="scss" scoped>
.multiple-height {
  height: 30px;
}

.el-dialog__body {
  padding: 0px !important;
}

.el-main {
  padding: 0px !important;
}

.dialog-body .el-row {
  margin: 5px !important;
}

.el-form-item--medium .el-form-item__content,
.el-form-item--medium .el-form-item__label {
  /* line-height: 18px; */
  position: relative;
}

#tabList {
  list-style: none;
  margin-top: 0;
  padding-top: 0;
  border-right: 1px solid #d2d6de;
}

#tabList li:after {
  content: "";
  display: inline-block;
  width: 100%;
}

#tabList li {
  padding: 5px 20px 5px 20px;
  cursor: pointer;
  -moz-text-align-last: justify; //兼容firefox
  text-justify: distribute-all-lines; // 这行必加，兼容ie浏览器
  text-align: justify;
  display: block;

  &.active {
    border-right: 2px solid #668eff;
    text-align-last: justify;
    -moz-text-align-last: justify; //兼容firefox
    text-justify: distribute-all-lines; // 这行必加，兼容ie浏览器
  }
}

#content {
  position: relative;
  padding-top: 10px;
  padding-right: 10px;
  font-size: 18px;
  max-height: 500px;
}

.dialogs {
  .el-form-item--medium .el-form-item__content,
  .el-form-item--medium .el-form-item__label {
    line-height: 40px;
  }
}

.el-dialog__wrapper {
  overflow: none;
  overflow-y: hidden;
}
</style>
