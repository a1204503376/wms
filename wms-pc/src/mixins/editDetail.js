import {editMixin} from "@/mixins/edit";
import Schema from "async-validator";
import func from "@/util/func";

export const editDetailMixin = {
    mixins: [editMixin],
    data() {
        return {
            table: {
                data: [],
                postData: []
            }
        }
    },
    created() {
        if (!this.isEdit) {
            this.batchAddToRows();
        } else {
            this.initTableData();
        }
    },
    watch: {
        $route(to) {
            if (to.matched && func.isArray(to.matched)
                && to.matched.length >= 2
                && to.matched[1].name === 'demoEdit') {
                if (this.isEdit) {
                    this.initTableData();
                }
            }
        }
    },
    methods: {
        async submitFormParams() {
            console.log('提交表单数据', this.form.params);
            console.log('提交表格数据', this.table.postData);
            this.$message.success('提交成功');
        },
        checkDetails() {
            debugger
            let tableData = this.table.data.filter(d => this.filterBlankRow(d));
            if (tableData.length === 0) {
                this.$message.warning("至少填写一条明细数据");
                return;
            }
            const descriptor = this.getDescriptor();
            const validator = new Schema(descriptor);
            for (const row of tableData) {
                let checkResult = this.checkRow(row, validator);
                if (!checkResult) {
                    continue;
                }
                this.$message.warning(`行号：${row.lineNumber}，${checkResult.errors[0].message}`,);
                return false;
            }
            this.table.postData = tableData;
            return true;
        },
        checkRow(row, validator) {
            let result;
            validator.validate(row, (errors, fields) => {
                if (errors) {
                    result = {errors, fields};
                }
            });
            return result;
        },
        checkGlobal() {
            return this.checkForm() && this.checkDetails();
        },
        onAddBatchRow() {
            this.batchAddToRows();
        },
        batchAddToRows() {
            Array(10).fill(1).forEach((item, index) => {
                this.table.data.splice(this.table.data.length + (index + 1), 0, this.createRowObj());
            });
        },
        initTableData() {
            this.form.params = {
                asnBillNo: '123',
                inStorageMode: 10,
                billTypeCd: 10,
            };
            let tableData = [];
            for (let i = 0, l = 11; i < l; i++) {
                let obj = {
                    sku: {
                        skuId: '1234567890',
                        skuCode: '00' + (i + 1),
                        skuName: '物品名称'
                    },
                    planQty: i + 1,
                    skuLot1: this.id,
                }
                tableData.push(obj);
            }

            this.table.data = tableData;
        }
    }
}
