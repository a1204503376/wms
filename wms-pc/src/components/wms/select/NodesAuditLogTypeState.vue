<template>
    <el-select
        v-model="val"
        :clearable="true"
        collapse-tags
        placeholder="请选择"
        :multiple="multiple"
        size="mini"
        style="width:100%;"
        @change="onChange">
        <el-option
            v-for="item in dataSource"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            <span style="float: left">{{ item.label }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.label }}</span>
        </el-option>
    </el-select>

</template>

<script>
    import {auditLogTypeStateService} from "@/api/wms/log/AuditLogTypeState";


    export default {
        name: "lpnTypeStateService",
        model: {
            prop: 'selectVal',
            event: 'selectValChange'
        },
        props: {
            selectVal: [Array, String],
            // 单选多选切换，默认为false
            multiple: {type: Boolean, required: false, default: false}
        },
        data() {
            return {
                val:this.selectVal,
                dataSource: []
            }
        },
        watch: {
            selectVal(newVal) {
                this.val=newVal;
            }
        },
        async created() {
            await this.getDataSource();
        },
        methods: {
            async getDataSource() {
                this.dataSource = await auditLogTypeStateService.getLpnTypeState();
            },
            onChange(val) {
                this.$emit('selectValChange', val);
            }
        }
    }
</script>

<style scoped>

</style>
