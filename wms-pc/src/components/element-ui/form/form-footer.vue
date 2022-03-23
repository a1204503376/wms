<template>
    <div style="line-height:60px;text-align:right;">
        <template v-for="button in this.form.buttonGroup">
            <el-button v-show="!button.hide"
                       :type="button.type"
                       :loading="button.loading"
                       @click="buttonClick(button)">
                {{ button.label }}
            </el-button>
        </template>
        <el-button v-if="this.form.saveBtn === undefined || this.form.saveBtn"
                   type="primary"
                   @click="onSave"
                   :loading="this.form.loading.save"
                   v-show="!isView">保 存
        </el-button>
        <el-button @click="onClose">关 闭</el-button>
    </div>
</template>

<script>
export default {
    name: "form-footer",
    inject: ["form"],
    provide() {
        return {
            form: this.form,
            dynamic: this
        }
    },
    data(){
        return {
            isView:false
        }
    },
    methods:{
        // 操作栏自定义按钮点击事件
        buttonClick(button) {
            if (button && button.click) {
                this.$set(button, 'loading', true);
                button.click(this);
            }
        },
        onSave(){
            this.form.save();
        },
        onClose(){
            this.form.close();
        }
    }
}
</script>

<style scoped>

</style>
