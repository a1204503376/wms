<template>
  <el-dialog
    :title="titleText"
    :visible.sync="showDialogs"
    width="40%"
    v-dialogDrag
    @close="Oncloses"
    @open="openDialog"
    append-to-body
  >
    <el-form
      :model="paramForm"
      :rules="rules"
      ref="paramForm"
      label-width="110px"
    >
      <div class="dialog__body">
        <el-row :gutter="20">
          <el-col>
            <el-form-item label="单据编码" prop="countBillNo">
              <el-input
                style="width: 90%"
                type="text"
                placeholder="单据编码"
                v-model="paramForm.countBillNo"
                maxlength="50"
                disabled
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col>
            <el-form-item label="总库位数量" prop="countLocation">
              <el-input
                v-model="paramForm.countLocation"
                style="width: 90%"
                placeholder="总库位数量"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col>
            <el-form-item label="任务分配数量" prop="countTask">
              <el-input-number
                v-model="paramForm.countTask"
                placeholder="库位数量"
                :min="1"
                style="width: 90%"
                onkeyup="this.value = this.value.replace(/[^\d.]/g,'');"
              ></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="childAdds">确 认</el-button>
      <el-button @click="childCancels">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getDetail, countTask } from "@/api/wms/count/countheader.js";

export default {
  name: "paramDialog",
  props: {
    isShowDialogs: {
      type: Boolean,
      default: true,
    },
    titleText: {
      type: String,
      default: "分派任务",
    },
    dataSourcetask: {
      type: Object,
      default: {},
    },
  },
  data() {
    return {
      showDialogs: this.isShowDialogs,
      paramForm: {
        countBillNo: "",
        countLocation: "",
        countTask: "",
      },
      rules: {
        countTask: [
          { required: true, message: "任务分配数量", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    openDialog() {
      this.defaultgetData();
    },
    defaultgetData() {
      debugger
      getDetail(this.dataSourcetask.countBillId).then((res) => {
        if (res.data.code === 200) {
          debugger
          this.paramForm.countBillId = this.dataSourcetask.countBillId;
          this.paramForm.countBillNo = res.data.data.countBillNo;
          this.paramForm.countLocation = res.data.data.countDetailVOList.length;
        }
      });
    },
    //保存 和 修改
    childAdds() {
      this.$refs["paramForm"].validate((valid) => {
        if (valid) {
          
          countTask(this.paramForm).then(
            () => {
              this.$message({
                type: "success",
                message: "操作成功!",
              });
              this.visible = false;
              const resultData = {
                visible: this.visible,
              };
              this.$emit("randomSuccess", resultData);
              return true;
            },
            (error) => {
              console.log(error);
            }
          );
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
      this.$refs.paramForm.resetFields();
      //   this.paramForm.id = "";
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
  margin-top: 20px;
  overflow-x: hidden;
}
</style>