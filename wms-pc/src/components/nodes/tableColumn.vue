<template>
  <div class="tableColumn">
    <el-drawer
      :before-close="tableColumnDialogClose"
      :visible.sync="tableColumnVisible"
      label="rtl"
      custom-class="demo-drawer"
      size="15%"
      ref="drawer"
      append-to-body
    >
      <el-checkbox-group v-model="tableColumnChecked">
        <el-checkbox
          v-for="item in tableColumnData"
          :checked="!item.hide"
          @change="tableColumnCheckboxChange"
          :label="item.prop"
          :key="item.prop"
          >{{ item.label }}
        </el-checkbox>
      </el-checkbox-group>
    </el-drawer>
  </div>
</template>
<script>
export default {
  name: "tableColumn",
  props: {
    tableColumnVisible: {
      type: Boolean,
      default: false,
    },
    tableColumnData: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      checked: true,
      tableColumnChecked: [],
    };
  },
  // computed: {
  //   visible() {
  //     return this.tableColumnVisible
  //   }
  // },
  methods: {
    //关闭弹出层Checkbox_dialogs
    tableColumnDialogClose() {
      this.$emit("tableColumnDialogClose");
    },
    tableColumnCheckboxChange(val) {
      this.$emit("tableColumnCheckboxChange", this.tableColumnChecked);
    },
  },
};
</script>
<style lang="scss" scoped>
.el-checkbox-group {
  padding-left: 20px;
  font-size: 30px;
}

.el-checkbox {
  width: 100%;
}
/deep/ .el-drawer__body {
  overflow: auto;
}
</style>
