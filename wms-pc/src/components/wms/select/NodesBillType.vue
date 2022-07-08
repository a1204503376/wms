<template>
    <el-select
        v-model="val"
        :collapse-tags="true"
        :io-type="ioType"
        :multiple="multiple"
        filterable
        placeholder="请选择"
        :size="size"
        value-key="billTypeId"
        @change="onChange">
        <el-option
            v-for="item in options"
            :key="item.billTypeCd"
            :label="item.billTypeName"
            :value="item.billTypeCd">
            <span style="float: left">{{ item.billTypeCd }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.billTypeName }}</span>
        </el-option>
    </el-select>
</template>

<script>
import {getBillTypeSelectResponseList} from "@/api/wms/basics/billType";

export default {
    name: "NodesBillType",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Object, String, Array, Number],
        // 是否多选 true:多选 默认为单选
        multiple: {type: Boolean, required: false, default: () => false},
        // 查询的单据类型，"":查询所有, "I":查询入库单据类型, "O":查询出库单据类型
        ioType: {type: String, required: false, default: () => ''},
        // 组件大小，默认为mini, 支持 medium/small/mini
        size: {type: String, required: false, default: () => "mini"}
    },
    data() {
        return {
            options: [this.selectVal],
            val: this.selectVal,
            ioTypeVal: this.ioType,
        }
    },
    watch: {
        selectVal(newVal) {
            this.val = newVal;
        }
    },
    created() {
        this.getDataSource();
    },
    methods: {
        async getDataSource() {
            let billTypeSelectQuery = {
                ioType: this.ioTypeVal
            };
            let {data: {data}} = await getBillTypeSelectResponseList(billTypeSelectQuery);
            this.options = data;
            this.loading = false;
        },
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
