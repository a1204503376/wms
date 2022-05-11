<template>
    <nodes-select
        v-model="val"
        :data-source="dataSource"
        :is-custom-template="true"
        label-name="whName"
        value-key="whCode"
        value-name="whCode">
    </nodes-select>


</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {getWarehouseSelectResponseList} from "@/api/wms/warehouse/warehouse";
import func from "@/util/func";

export default {
    name: "NodesWarehouse",
    components: {NodesSelect},
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, String],
        multiple: {type: Boolean, required: false, default: false}
    },
    data() {
        return {
            val: {},
            dataSource: []
        }
    },
    watch: {
        val(newVal) {
            let result = newVal;
            if (func.isArray(newVal)) {
                result = newVal.map(d => d.whId);
            } else if (func.isObject(newVal)) {
                result = newVal.whId
            }
            this.$emit('selectValChange', result);
        }
    },
    created() {
        this.getDataSource();
    },
    methods: {
        async getDataSource() {
            const response = await getWarehouseSelectResponseList();
            this.dataSource = response.data.data;
        }
    }
}
</script>

<style scoped>

</style>
