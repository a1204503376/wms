<template>
    <el-autocomplete
        v-model="val"
        :clearable="true"
        :debounce="800"
        :disabled="disabled"
        :fetch-suggestions="querySearchAsync"
        :hide-loading="true"
        :highlight-first-item="true"
        :trigger-on-focus="false"
        placeholder="请输入物品编码或名称"
        popper-class="popper-auto"
        size="mini"
        @change="onChange"
        @select="handleSelect">
        <template v-slot="{ item }">
            <span style="float: left">{{ item.skuCode }}</span>&nbsp;&nbsp;
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.skuName }}</span>
        </template>
    </el-autocomplete>
</template>

<script>
import {getSkuSelectResponseTop10List} from "@/api/wms/basics/sku";
import func from "@/util/func";

export default {
    name: "NodesSku",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Object],
        // 单选多选切换，默认为false
        multiple: {type: Boolean, required: false, default: false},
        // 是否禁用 默认为 false不禁用
        disabled: {type: Boolean, required: false, default: () => false}
    },
    data() {
        return {
            val: '',
            isEdit: false,
            data: []
        }
    },
    watch: {
        selectVal() {
            this.setDefaultByProps();
        }
    },
    created() {
        this.setDefaultByProps();
    },
    methods: {
        setDefaultByProps() {
            this.isEdit = func.isNotEmpty(this.selectVal.skuId)
            if (!this.isEdit) {
                return;
            }
            let currentSku = this.data.find(item => item.skuId === this.selectVal.skuId);
            if (func.isEmpty(currentSku)) {
                this.data.push(this.selectVal);
            }
            this.val = this.selectVal.skuCode;
        },
        // 值变化后失去焦点触发
        onChange(val) {
            if (!this.data.map((item) => item.skuCode).includes(this.val)) {
                this.val = '';                                  // 清空物品编码
                this.$emit('selectValChange', {});  // 回传空对象，清空外面其他组件
                this.data = []                                 // 清空下拉数组，使其再次获得焦点后不会有下拉数据
            }
            for (let i = 0; i < this.data.length; i++) {
                if (this.data[i].skuCode === val) {
                    this.$emit('selectValChange', this.data[i]);
                    return;
                }
            }
        },
        // 输入建议数据准备好时通过 cb(data) 返回到 autocomplete 组件中
        async querySearchAsync(queryString, cb) {
            if (queryString === '') {
                cb([]);
                return;
            }
            let param = {
                key: queryString
            }
            let {data: {data}} = await getSkuSelectResponseTop10List(param);
            cb(data);
            this.data = data;
        },
        handleSelect(item) {
            this.val = item.skuCode;
            this.$emit('selectValChange', item);
        },
    },
}
</script>
<style>
</style>
