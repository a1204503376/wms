<template>
  <el-dialog
    :title="titleText"
    :visible.sync="showDialogs"
    width="80%"
    v-dialogDrag="true"
    @close="Oncloses"
    @open="openDialog"
    top="3vh"
    style="height: 800px"
    overflow="auto"
    append-to-body
  >
    <el-form :model="skupackage" ref="skupackage" label-width="90px">
      <div class="dialog__body">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="库房" prop="whId">
              <el-select
                v-model="skupackage.whId"
                placeholder="请选择"
                @change="getZoneId"
                size="medium"
                style="width: 100%"
              >
                <el-option
                  v-for="item in zoneList"
                  :key="item.whId"
                  :label="item.whName"
                  :value="item.whId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="库区" prop="zoneId">
                <el-select
                    v-model="skupackage.zoneId[0]"
                    placeholder="请选择"
                    size="medium"
                    @change="getlocId"
                    style="width: 100%"
                >
                    <el-option
                        v-for="item in zoneIdList"
                        :key="item.zoneId"
                        :label="item.zoneName"
                        :value="item.zoneId"
                    />
                </el-select>
            </el-form-item>
          </el-col>
           <el-col :span="6">
            <el-form-item label="库位" prop="locId">
                <el-select
                    v-model="skupackage.locId"
                    placeholder="请选择"
                    size="medium"
                    style="width: 100%"
                >
                    <el-option
                        v-for="item in locIdList"
                        :key="item.locId"
                        :label="item.locCode"
                        :value="item.locId"
                    />
                </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="容器编码" prop="lpnCode">
              <el-input
                v-model="skupackage.lpnCode"
                size="medium"
                style="width: 100%"
              ></el-input>
            </el-form-item>
          </el-col>
          
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="物品名称" prop="skuName">
              <el-input
                v-model="skupackage.skuName"
                size="medium"
                style="width: 100%"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="盘点时间" prop="startTimeArray">
              <el-date-picker
                v-model="skupackage.startTimeArray"
                placeholder="请选择盘点时间"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
                size="medium"
                :default-time="['00:00:00', '23:59:59']"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-button
              type="primary"
              class="searchBtn"
              size="small"
              @click="searchBtn(skupackage)"
              >查 询
            </el-button>
            <el-button
              type="warning"
              plain
              class="searchBtn"
              size="small"
              @click="resetForm"
              >重 置</el-button
            >
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-table
              :data="gridData"
              border
              height="265px"
              :header-cell-style="{ 'background-color': '#fafafa' }"
              overflow="auto"
              @selection-change="handleCurrentChange"
              v-loading="dataLoading"
              element-loading-text="数据正在加载中"
              element-loading-spinner="el-icon-loading"
            >
              <el-table-column type="selection" width="60"></el-table-column>
              <el-table-column
                prop="serialNumber"
                label="序列号"
                align="center"
                width="130"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="locCode"
                label="库位"
                align="center"
                width="130"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="lpnCode"
                label="容器编码"
                align="center"
                width="120"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="skuCode"
                label="物品编码"
                align="center"
                width="120"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="skuName"
                label="物品名称"
                align="center"
                width="120"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="countQty"
                label="盘点数量"
                align="center"
                width="120"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="wsuName"
                label="计量单位"
                align="center"
                width="120"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="lotNumber"
                label="批次号"
                align="center"
                width="170"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="userName"
                label="盘点人"
                align="center"
                width="100"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="procTimeDesc"
                label="盘点时间"
                align="center"
                width="200"
                show-overflow-tooltip
              ></el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </div>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="warning" @click="toVoid">作废</el-button>
      <el-button type="primary" @click="childAdds" :loading="saveLoading"
        >生成报告</el-button
      >
      <el-button @click="childCancels">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getZone, getSelectList } from "@/api/wms/warehouse/zone.js";
import {
  searchSku,
  randomCountDifferenceReport,
  abolishRandomCount,
} from "@/api/wms/count/countheader.js";
import selectSku from "@/components/nodes/selectSku.vue";
import request from "@/router/axios";

export default {
  name: "paramDialog",
  components: {
    selectSku,
  },
  props: {
    isShowDialogs: {
      type: Boolean,
      default: true,
    },
    titleText: {
      type: String,
      default: "随机盘点",
    },
    dataSourcetask: {
      type: Object,
      default: {},
    },
  },
  data() {
    return {
      dataLoading: false,
      saveLoading: false,
      gridData: [],
      dialogCommGoods: false,
      showDialogs: this.isShowDialogs,
      skupackage: {
        whId: "",
        zoneId: [],
        locId: "",
        locCode: "",
        lpnCode: "",
        skuName: "",
        locIdEnd: "",
        lpnCode: "",
        skuName: "",
        skuId: "",
        startTimeArray: [],
        countRecordVOList: [],
      },
      zoneList: [], //库房下拉数据
      zoneIdList: [], //库区
      locIdList: [],
    };
  },
  computed: {
    ids() {
      let ids = [];
      this.skupackage.countRecordVOList.forEach((ele) => {
        ids.push(ele.wcrId);
      });
      return ids.join(",");
    },
  },
  methods: {
    //作废
    toVoid() {
      if (this.skupackage.countRecordVOList.length === 0) {
        this.$message.warning("至少选择一条数据！");
        return;
      }
      this.$confirm("确定作废当前数据？", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return abolishRandomCount(this.ids);
        })
        .then(() => {
          this.$message({
            type: "success",
            message: "操作成功!",
          });
          this.searchBtn(this.skupackage);
          this.$emit("randomSuccess");
        })
        .finally(() => {
          this.saveLoading = false;
        });
      //  abolishRandomCount(this.ids).then(
      //             () => {
      //                 this.$message({
      //                     type: "success",
      //                     message: "操作成功!"
      //                 });
      //                 this.searchBtn(this.skupackage);
      //                 this.$emit("randomSuccess");
      //             },
      //         ).finally(() => {
      //             this.saveLoading = false;
      //         });
    },
    //模糊搜索
    searchBtn(val) {
      this.dataLoading = true;
      request({
        url: "/api/wms/log/countRecord/randomList",
        method: "post",
        data: val,
      })
        .then((res) => {
          this.gridData = res.data.data;
        })
        .finally(() => {
          this.dataLoading = false;
        });
    },
    resetForm() {
      if (this.$refs.skupackage) {
        this.$refs.skupackage.resetFields();
      }
      this.gridData = [];
    },
    //获取选中用户的数
    handleCurrentChange(val) {
      this.skupackage.countRecordVOList = val;
    },
    //获取库房
    defaultgetZone() {
      getZone().then((res) => {
        this.zoneList = res.data.data;
      });
    },
    //选择库区 获取起始库位和结束库位数据源
    getlocId(zoneId) {
      request({
        url: "/api/wms/warehouse/location/list?zoneId=" + zoneId,
        method: "get",
      }).then((res) => {
        this.locIdList = res.data.data;
      });
    },
    //选择库房 获取库区数据源
    getZoneId(val) {
      // this.skupackage.skuId = val;
      getSelectList(val).then((res) => {
        this.zoneIdList = res.data.data;
      });
    },
    //打开回调
    openDialog() {
      this.defaultgetZone();
    },
    //生成报告
    childAdds() {
      this.$refs["skupackage"].validate((valid) => {
        if (valid) {
          this.saveLoading = true;
          this.skupackage.locId = "";
          randomCountDifferenceReport(this.skupackage)
            .then(() => {
              this.$message({
                type: "success",
                message: "操作成功!",
              });
              this.searchBtn(this.skupackage);
              this.$emit("randomSuccess");
            })
            .finally(() => {
              this.saveLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    //取消
    childCancels(paramForm) {
      this.$emit("randomCancel");
    },
    Oncloses() {
      this.resetForm();
    },
  },
  watch: {
    isShowDialogs(val) {
      this.showDialogs = val; //②监听外部对props属性result的变更，并同步到组件内的data属性myResult中
    },
    showDialogs(val) {
      this.$emit("randomchange", val); //③组件内对myResult变更后向外部发送事件通知
    },
  },
};
</script>

<style lang="scss" scoped>
.dialog__body {
  padding: 3px 20px;
  // margin-top: 20px;
  overflow-x: hidden;
}
</style>
