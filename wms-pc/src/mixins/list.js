import {menuMixin} from "@/mixins/menu";
import func from "@/util/func";
import {nowDateFormat} from "@/util/date"
import {getCrudColumnResponseList} from "@/api/core/column";
import {deepClone} from "@/util/util";
import {mapGetters} from "vuex";
import {importFile} from "@/api/wms/basics/supplier";

export const listMixin = {
    mixins: [menuMixin],
    data() {
        return {
            form: {
                params: {},
                deepCloneParams: {},
                events: {
                    search: this.onSearch,
                    reset: this.onReset
                }
            },
            table: {
                columnList: [],
                data: []
            },
            page: {
                total: 0,
                size: 20,
                current: 1,
                ascs: "", //正序字段集合
                descs: "", //倒序字段集合
            },
            columnShowHide: {
                visible: false,
                dataSource: []
            },
            exportExcelName: '',
            exportExcelSheet:[
                {
                    tHeader:[],
                    table:[],
                    keys:[],
                    sheetName: "",
                    cellStyle: [],
                }
            ]
        }
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionObj() {
            return {
                search: true
            }
        }
    },
    created() {
        this.getCrudColumnList();
        this.copyInitialValue();
    },
    methods: {
        copyInitialValue() {
            this.form.deepCloneParams = deepClone(this.form.params);
        },
        getTableData() {
        },
        onSearch() {
            this.getTableData();
        },
        onReset() {
            this.restoreDefaults();
        },
        restoreDefaults() {
            func.recursionObject(deepClone(this.form.deepCloneParams), this, this.form.params);
        },
        onRefresh() {
            this.getTableData();
        },
        onSortChange(column) {
            let prop = column.prop;
            let order = column.order;
            console.log(column);
            if (order === "ascending") {
                this.page.ascs = prop;
                this.page.descs = "";
            } else if (order === "descending") {
                this.page.descs = prop;
                this.page.ascs = "";
            } else {
                this.page.ascs = "";
                this.page.descs = "";
            }
            this.getTableData();
        },
        handleSizeChange(val) {
            this.page.size = val;
            this.getTableData();
        },
        handleCurrentChange(val) {
            this.page.current = val;
            this.getTableData();
        },
        setColumnList(columnList, data, flag) {
            if (func.isEmpty(data)) {
                return;
            }
            let deepCloneColumnList = deepClone(columnList);
            // this.columnShowHide.dataSource = this.getColumnDataSource();
            deepCloneColumnList.forEach(d => {
                let find = data.find(m => m['prop'] === d['prop']);
                if (!find) {
                    return;
                }
                Object.assign(d, {
                    aliasName: find.aliasName,
                    width: func.toInt(find.width, 0),
                    hide: find.hide,
                    fixed: find.fixed,
                    align: find.align,
                    order: find.order
                });
            });
            // 1. 显隐列组件赋值的列名称已本地配置的label为主，优先处理
            if (flag === 'init') {
                this.columnShowHide.dataSource = deepClone(deepCloneColumnList);
            }
            // 2. 再处理本地label是否采用别名
            deepCloneColumnList.forEach(d => {
                let find = data.find(m => m['prop'] === d['prop']);
                if (!find) {
                    return;
                }
                Object.assign(d, {
                    label: func.strDefault(find.aliasName, d.label)
                });
            });

            func.recursionObject(deepCloneColumnList, this, this.table.columnList);
            this.table.columnList.sort((a, b) => {
                let x = a['order'], y = b['order'];
                return ((x < y) ? -1 : (x > y) ? 1 : 0);
            });
        },
        getColumnDataSource: function () {
            return this.table.columnList;
        },
        getCrudColumnList() {
            let menu = this.getMenu();
            getCrudColumnResponseList(menu.id)
                .then(({data: {data}}) => {
                    this.setColumnList(this.table.columnList, data, 'init');
                });
        },
        onColumnShowHide(column) {
            this.columnShowHide.visible = !this.columnShowHide.visible;
            !this.columnShowHide.visible && this.updateColumn(column);
        },
        updateColumn(columnObj) {
            if (!func.isObject(columnObj)
                || func.isEmpty(columnObj['columnList'])) {
                return;
            }
            this.setColumnList(this.table.columnList, columnObj.columnList);
            this.$nextTick(() => {
                this.$refs.table.doLayout();
            });
        },
        // 本地导出
        exportCurrentDataToExcel(sheetName, filename) {
            if (func.isEmpty(this.table.data)) {
                return;
            }
            this.exportExcelSheet[0].tHeader = this.table.columnList.map((item) => {
                return item.label;
            }).join(",").split(",");
            this.exportExcelSheet[0].table = this.table.data;
            this.exportExcelSheet[0].keys = this.table.columnList.map((item) => {
                return item.prop;
            }).join(",").split(",");
            this.exportExcelSheet[0].sheetName =  sheetName || "sheet1"
            let localStr = nowDateFormat("yyyyMMddhhmm")
            this.exportExcelName = filename + localStr || localStr;
            this.exportExcelSheet[0].tHeader.forEach((value, index) => {
                let s = String.fromCharCode("A".charCodeAt(0) + index);
                let cell = {
                    cell: s + 1,
                    font: {
                        name: '宋体',
                        sz: 14,
                        bold: true,
                    },
                    fill: {
                        fgColor: {rgb: "c0c0c0"},
                    }
                };
                this.exportExcelSheet[0].cellStyle.push(cell);
            })
        },
        onUpload() {
            this.fileUpload.visible = true;
        },
        getFormData(res){
            let file = res.data.localFile.file;
            let param = new FormData();
            param.append("file", file);
            return param;
        }
    }
}
