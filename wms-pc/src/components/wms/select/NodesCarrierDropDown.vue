<template>
    <el-select
        filterable
        v-model="val"
        collapse-tags
        placeholder="请选择"
        :multiple="multiple"
        size="mini"
        style="width:100%;"
        @change="onChange">
        <el-option
            v-for="item in dataSource"
            :key="item.code"
            :label="item.name"
            :value="item.code">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.code }}</span>
        </el-option>
    </el-select>

</template>

<script>
    // eslint-disable-next-line no-unused-vars
    import {carrierService} from "@/api/wms/basics/Carrier";


    export default {
        name: "carrierService",
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
              this.getDataSource();
            }
        },
        async created() {
            await this.getDataSource();
        },
        methods: {
            async getDataSource() {
                this.dataSource =await carrierService.getDropDown();
            },
            onChange(val) {
                this.$emit('selectValChange', val);
            }
        }
    }
</script>

<style scoped>

</style>
