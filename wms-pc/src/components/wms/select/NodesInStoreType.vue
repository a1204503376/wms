<template>
    <nodes-select
        v-model="val"
        :data-source="dataSource"
        :clearable="true"
        v-bind="$attrs"
    >
    </nodes-select>
</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {getInStoreType} from "@/api/core/dict";

export default {
    name: "NodesInStoreType",
    components: {NodesSelect},
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, Number, String]
    },
    data() {
        return {
            val: this.selectVal,
            dataSource: []
        }
    },
    watch: {
        val(newVal) {
            this.$emit('selectValChange', newVal);
        }
    },
    created() {
        this.getDataSource();
    },
    methods: {
        getDataSource() {
            getInStoreType().then((res) => {
                let dataSource = [];
                let data = res.data.data;
                for (let item of data) {
                    dataSource.push({
                        label: item['dictValue'],
                        value: item['dictKey']
                    })
                }
                this.dataSource = dataSource;
            })
        }
    }
}
</script>

<style scoped>

</style>
