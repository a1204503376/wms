<template>
    <span>
      <el-dialog :title="title"
                 width="40%"
                 top="3vh"
                 append-to-body
                 :close-on-click-modal="false"
                 @closed="dialogClosed"
                 :visible.sync="visible">

      <div class="el-dialog__body changeUserPanel">
        <el-form ref="form" :model="form" :rules="rules"
                 label-width="100px" style="padding-top: 20px;">
              <el-form-item label="订单编号" prop="taskIdList">
                <el-select
                        v-model="form.taskIdList"
                        multiple
                        collapse-tags
                        placeholder="请选择"
                        style="width:90%;">
                  <el-option
                          v-for="item in dataSource"
                          :key="item.taskId"
                          :label="item.billNo"
                          :value="item.taskId">
                  </el-option>
                </el-select>
              </el-form-item>
             <el-form-item label="用户名称" prop="userId" style="width:92%;">
                  <!-- <select-user v-model="form.userId" size="small" ref="newForm"></select-user> -->
                  <selectUserTable v-model="form.userId" size="small" ref="newForm"></selectUserTable>
              </el-form-item>
        </el-form>
      </div>
      <span slot="footer"
            class="dialog-footer">
        <el-button type="primary" @click="submitForm('form')">保 存</el-button>
        <el-button @click="dialogClosed('form')">关 闭</el-button>
      </span>
    </el-dialog>
    </span>
</template>

<script>
    import selectUserTable from "@/components/nodes/selectUserTable";
    import {changeUser} from "@/api/wms/core/task";

    export default {
        name: "changeUser",
        components: {selectUserTable},
        props: ["visible", "dataSource"],
        data() {
            return {
                title: "人员变更",
                form: {
                    taskIdList: '',
                    userId: '',
                },
                dialogUser: {
                    visible: false
                },
                rules: {
                    userId: [
                        {required: true, message: '请选择用户', trigger: 'change'}
                    ],
                    taskIdList: [
                        {required: true, message: '请选择订单编号', trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {
            submitForm(form) {

                this.$refs[form].validate(valid => {
                    if (valid) {
                        // 提交数据
                        changeUser(this.form.taskIdList.join(',') + '', this.form.userId)
                            .then(() => {
                                this.$message({
                                    type: "success",
                                    message: "操作成功!"
                                });
                            })
                            .then(() => {
                                let dialogResult = {
                                    visible: false,
                                    result: true
                                };
                                this.$emit('dialogResult', dialogResult);
                                this.$refs.newForm.text = "";
                                if (this.$refs[form]) {
                                    this.$refs[form].resetFields();
                                }
                            });
                        return true;
                    } else {
                        return false;
                    }
                });
            },
            dialogClosed() {
                let dialogResult = {
                    visible: false
                };
                this.$emit("dialogResult", dialogResult);
                for (let field in this.form) {
                    this.form[field] = undefined;
                }
                if (this.$refs['form']) {
                    this.$refs['form'].resetFields();

                }
            },
        }
    }
</script>

<style scoped>
</style>
