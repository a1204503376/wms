<template>
    <nodes-select
        v-model="val"
        :data-source="dataSource"
        value-key="woId"
        value-name="ownerCode"
        label-name="ownerName"
        :is-custom-template="true"
        :multiple="multiple"
       >

    </nodes-select>
</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {getOwnerSelectResponseList} from "@/api/wms/basics/owner";
import func from "@/util/func";

export default {
    name: "NodesOwner",
    components: {NodesSelect},
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        // 是否多选 true:多选 默认为单选
       multiple: {type: Boolean, required: false, default:()=>false}
    },
    data() {
        return {
            val: '',
            dataSource: []
        }
    },
    watch: {
        val(newVal) {
            debugger;
            let result = newVal;
            if (func.isArray(newVal)) {
                result = newVal.map(d => d.woId);
            } else if (func.isObject(newVal)) {
                result = newVal.woId
            }
            this.$emit('selectValChange', result);
        }
    },
    created() {
        this.getDataSource();
    },
    methods: {
        async getDataSource() {
            const response = await getOwnerSelectResponseList();
            this.dataSource = response.data.data;
        },
    }
}
</script>

<style scoped>

</style>
