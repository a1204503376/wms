<template>
    <el-autocomplete
        v-model="val"
        :clearable="true"
        :debounce="800"
        :fetch-suggestions="querySearchAsync"
        :hide-loading="true"
        :highlight-first-item="true"
        :trigger-on-focus="false"
        placeholder="请输入物品编码或名称"
        popper-class="popper-auto"
        size="mini"
        :disabled="disabled"
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
        selectVal: [Array, String, Object],
        // 单选多选切换，默认为false
        multiple: {type: Boolean, required: false, default: false},
        // 是否禁用 默认为 false不禁用
        disabled: {type: Boolean, required: false, default: () => false}
    },
    data() {
        return {
            val: '',
            isEdit: func.isNotEmpty(this.selectVal),
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
            if (this.isEdit) {
                return;
            }
            let currentSku = this.data.find(item => item.skuId === this.selectVal.skuId);
            if (func.isEmpty(currentSku)) {
                this.data.push(this.selectVal);
            }
            this.val = this.selectVal.skuCode;
        },
        // 输入值后失去焦点
        onChange(val) {
            if (!this.data.map((item) => item.skuCode).includes(this.val)) {
                this.val = '';
                this.$emit('selectValChange', {});
            }

            for (let i = 0; i < this.data.length; i++) {
                if (this.data[i].skuCode === val) {
                    this.$emit('selectValChange', this.data[i]);
                    return;
                }
            }
        },
        // 异步请求下拉数据
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
.popper-auto {
    width: auto!important ;
    li {
        line-height: normal!important;
        padding: 7px;
        .name {
            text-overflow: ellipsis;
            overflow: hidden;
        }
        .addr {
            font-size: 12px;
            color: #b4b4b4;
        }
        .highlighted .addr {
            color: #ddd;
        }
    }
}
</style>
